import firebase_admin
from firebase_admin import credentials, firestore
import requests
import time
import math
import unicodedata
from datetime import datetime

# ─── CONFIGURACIÓN ───────────────────────────────────────────────────────────
RUTA_CREDENCIALES = r"C:\Users\Julen\TrailpackKeys\trailpack-5fe83-firebase-adminsdk-fbsvc-0d9b1d9d76.json"
OVERPASS_URL      = "https://overpass-api.de/api/interpreter"
MAX_RUTAS_POR_PARQUE = 8
RADIO_BUSQUEDA_M     = 15000  # 15 km alrededor del centroide del parque
DISTANCIA_MAX_KM     = 40     # rutas más largas son etapas o grandes recorridos
# ─────────────────────────────────────────────────────────────────────────────


def haversine(lat1, lng1, lat2, lng2):
    """Distancia en km entre dos puntos geográficos."""
    R = 6371.0
    dlat = math.radians(lat2 - lat1)
    dlng = math.radians(lng2 - lng1)
    a = (math.sin(dlat / 2) ** 2
         + math.cos(math.radians(lat1)) * math.cos(math.radians(lat2))
         * math.sin(dlng / 2) ** 2)
    return 2 * R * math.asin(math.sqrt(a))


def calcular_distancia(coords):
    """Suma de segmentos consecutivos en km."""
    total = 0.0
    for i in range(1, len(coords)):
        total += haversine(coords[i - 1][0], coords[i - 1][1],
                           coords[i][0],     coords[i][1])
    return round(total, 2)


def estimar_duracion(distancia_km):
    """Estimación simple a 3.5 km/h."""
    horas = distancia_km / 3.5
    h = int(horas)
    m = int((horas - h) * 60)
    if h == 0:
        return f"{m}min"
    elif m == 0:
        return f"{h}h"
    else:
        return f"{h}h {m}min"


def mapear_dificultad(tags):
    """Mapea sac_scale de OSM a nuestros niveles."""
    sac = tags.get("sac_scale", tags.get("difficulty", ""))
    mapping = {
        "hiking":                    "Fácil",
        "mountain_hiking":           "Moderada",
        "demanding_mountain_hiking": "Difícil",
        "alpine_hiking":             "Muy difícil",
        "demanding_alpine_hiking":   "Muy difícil",
        "difficult_alpine_hiking":   "Muy difícil",
    }
    return mapping.get(sac, "Moderada")


def obtener_rutas_overpass(lat, lng):
    """Consulta relaciones route=hiking en un radio alrededor del centroide.
    Reintenta hasta 3 veces con backoff exponencial ante 429/504.
    """
    query = f"""
[out:json][timeout:60];
(
  relation[route=hiking](around:{RADIO_BUSQUEDA_M},{lat},{lng});
);
out geom;
"""
    headers = {"User-Agent": "TrailPackIngesta/1.0 (julen.tabuyo@gmail.com)"}
    esperas = [10, 30, 60]  # segundos entre reintentos
    for intento, espera in enumerate(esperas, start=1):
        try:
            resp = requests.post(
                OVERPASS_URL,
                data={"data": query},
                headers=headers,
                timeout=90
            )
            resp.raise_for_status()
            return resp.json().get("elements", [])
        except requests.exceptions.HTTPError as e:
            codigo = e.response.status_code if e.response is not None else "?"
            if intento < len(esperas):
                print(f"    ⚠ HTTP {codigo} — reintento {intento} en {espera}s...")
                time.sleep(espera)
            else:
                print(f"    ✗ HTTP {codigo} — se agotan reintentos.")
        except Exception as e:
            print(f"    ✗ Error inesperado: {e}")
            break
    return []


def extraer_coordenadas(relation):
    """Aplana la geometría de todos los ways miembro en una lista de (lat, lng)."""
    coords = []
    for member in relation.get("members", []):
        if member.get("type") == "way" and "geometry" in member:
            for nodo in member["geometry"]:
                coords.append((nodo["lat"], nodo["lon"]))
    return coords


def main():
    cred = credentials.Certificate(RUTA_CREDENCIALES)
    firebase_admin.initialize_app(cred)
    db = firestore.client()

    # ── Cargamos parques desde Firestore ──────────────────────────────────────
    parques = [doc.to_dict() for doc in db.collection("parquesnaturales").stream()]
    print(f"Parques cargados desde Firestore: {len(parques)}\n")

    # ── Borramos rutas oficiales previas ──────────────────────────────────────
    rutas_col = db.collection("rutas")
    print("Borrando rutas oficiales existentes...")
    borradas = 0
    for doc in rutas_col.where("esOficial", "==", True).stream():
        doc.reference.delete()
        borradas += 1
    print(f"  {borradas} rutas borradas.\n")

    total_ingestadas = 0

    for parque in parques:
        id_parque     = parque["idparquenatural"]
        nombre_parque = parque["nombre"]
        lat           = parque.get("lat", 0.0)
        lng           = parque.get("lng", 0.0)
        region        = parque.get("region", "")

        if lat == 0.0 and lng == 0.0:
            print(f"  ⚠ {nombre_parque}: sin coordenadas, saltando.")
            continue

        print(f"[{nombre_parque}] Consultando Overpass ({lat:.4f}, {lng:.4f})...")
        relaciones = obtener_rutas_overpass(lat, lng)

        # Filtramos: nombre obligatorio + geometría + distancia razonable
        rutas_validas = []
        for rel in relaciones:
            tags   = rel.get("tags", {})
            nombre = tags.get("name", "").strip()
            if not nombre:
                continue
            coords = extraer_coordenadas(rel)
            if len(coords) < 2:
                continue
            distancia_previa = calcular_distancia(coords)
            if distancia_previa > DISTANCIA_MAX_KM:
                continue
            rutas_validas.append((rel, nombre, coords, tags, distancia_previa))

        rutas_validas = rutas_validas[:MAX_RUTAS_POR_PARQUE]
        print(f"  → {len(relaciones)} encontradas, {len(rutas_validas)} válidas con nombre.")

        for rel, nombre_ruta, coords, tags, distancia in rutas_validas:
            osm_id   = rel["id"]
            idruta   = f"{id_parque}_{osm_id}"
            duracion  = estimar_duracion(distancia)

            coordenadas_fs = [
                {"lat": c[0], "lng": c[1], "altitud": 0.0}
                for c in coords
            ]

            ruta = {
                "idruta":           idruta,
                "idparquenatural":  id_parque,
                "pais":             "España",
                "region":           region,
                "nombre":           nombre_ruta,
                "dificultad":       mapear_dificultad(tags),
                "distancia":        distancia,
                "duracion":         duracion,
                "desnivelacumulado": 0,
                "desnivelpositivo":  0,
                "desnivelnegativo":  0,
                "esOficial":        True,
                "creadorId":        "",
                "nombreCreador":    "TrailPack",
                "descripcion":      tags.get("description", tags.get("note", "")),
                "fotosRuta":        [],
                "coordenadas":      coordenadas_fs,
                "fechacreacion":    int(datetime.now().timestamp() * 1000)
            }

            rutas_col.document(idruta).set(ruta)
            print(f"    ✓ {nombre_ruta[:50]:50}  {distancia:.1f} km  {duracion}")
            total_ingestadas += 1

        time.sleep(8)  # cortesía a Overpass entre parques

    print(f"\nIngesta completada — {total_ingestadas} rutas oficiales cargadas en Firestore.")


if __name__ == "__main__":
    main()
