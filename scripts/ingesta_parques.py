import firebase_admin
from firebase_admin import credentials, firestore
import requests
import unicodedata
from urllib.parse import unquote

# ─── CONFIGURACIÓN ───────────────────────────────────────────────────────────
RUTA_CREDENCIALES = r"C:\Users\Julen\TrailpackKeys\trailpack-5fe83-firebase-adminsdk-fbsvc-0d9b1d9d76.json"
# ─────────────────────────────────────────────────────────────────────────────

WIKIDATA_URL = "https://query.wikidata.org/sparql"

# Consulta SPARQL: parques nacionales (Q46169) de España (Q29)
# Pedimos nombre, descripción en español, región, superficie, foto y coordenadas
QUERY = """
SELECT ?parque ?parqueLabel ?descripcion ?regionLabel ?superficie ?imagen ?coord WHERE {
  ?parque wdt:P31/wdt:P279* wd:Q46169 .
  ?parque wdt:P17 wd:Q29 .
  OPTIONAL {
    ?parque schema:description ?descripcion .
    FILTER(LANG(?descripcion) = "es")
  }
  OPTIONAL {
    ?parque wdt:P131+ ?region .
    ?region wdt:P31 wd:Q10742 .
  }
  OPTIONAL { ?parque wdt:P2046 ?superficie . }
  OPTIONAL { ?parque wdt:P18 ?imagen . }
  OPTIONAL { ?parque wdt:P625 ?coord . }
  SERVICE wikibase:label { bd:serviceParam wikibase:language "es,en" . }
}
ORDER BY ?parqueLabel
"""


def obtener_foto_wikimedia(imagen_raw):
    """Llama a la API de Wikimedia Commons para obtener la URL real del thumbnail.
    Evita construir la URL manualmente — Wikimedia bloquea tamaños no permitidos.
    """
    if not imagen_raw:
        return ""
    filename = unquote(imagen_raw.split("/")[-1]).replace(" ", "_")
    params = {
        "action": "query",
        "titles": f"File:{filename}",
        "prop": "imageinfo",
        "iiprop": "url",
        "iiurlwidth": 800,
        "format": "json"
    }
    headers = {"User-Agent": "TrailPackIngesta/1.0 (julen.tabuyo@gmail.com)"}
    try:
        respuesta = requests.get(
            "https://commons.wikimedia.org/w/api.php",
            params=params, headers=headers, timeout=10
        )
        paginas = respuesta.json()["query"]["pages"]
        pagina = next(iter(paginas.values()))
        return pagina["imageinfo"][0]["thumburl"]
    except Exception:
        return ""


def limpiar_nombre(nombre):
    """Elimina prefijos redundantes: 'Parque Nacional de Doñana' → 'Doñana'"""
    prefijos = [
        "Parque nacional marítimo-terrestre del ",
        "Parque Nacional marítimo-terrestre del ",
        "Parque nacional y natural de ",
        "Parque Nacional y natural de ",
        "Parque nacional del ",
        "Parque Nacional del ",
        "Parque nacional de las ",
        "Parque Nacional de las ",
        "Parque nacional de la ",
        "Parque Nacional de la ",
        "Parque nacional de ",
        "Parque Nacional de ",
    ]
    for prefijo in prefijos:
        if nombre.startswith(prefijo):
            return nombre[len(prefijo):]
    return nombre


def normalizar_nombre(nombre):
    """'Doñana' → 'donana'"""
    sin_acentos = unicodedata.normalize("NFD", nombre)
    sin_acentos = "".join(c for c in sin_acentos if unicodedata.category(c) != "Mn")
    return sin_acentos.lower().replace(" ", "_").replace("-", "_")



def parsear_coordenadas(coord_str):
    """Convierte el formato WKT de Wikidata 'Point(lng lat)' a (lat, lng).
    Wikidata devuelve longitud primero, latitud segundo.
    """
    if not coord_str:
        return 0.0, 0.0
    limpio = coord_str.replace("Point(", "").replace(")", "").strip()
    partes = limpio.split()
    if len(partes) == 2:
        lng = float(partes[0])
        lat = float(partes[1])
        return lat, lng
    return 0.0, 0.0


def obtener_parques_wikidata():
    headers = {
        "Accept": "application/json",
        "User-Agent": "TrailPackIngesta/1.0 (julen.tabuyo@gmail.com)"
    }
    respuesta = requests.get(
        WIKIDATA_URL,
        params={"query": QUERY, "format": "json"},
        headers=headers,
        timeout=30
    )
    respuesta.raise_for_status()
    return respuesta.json()["results"]["bindings"]


def main():
    # Conectamos con Firebase
    cred = credentials.Certificate(RUTA_CREDENCIALES)
    firebase_admin.initialize_app(cred)
    db = firestore.client()
    coleccion = db.collection("parquesnaturales")

    print("Borrando colección existente...")
    docs = coleccion.stream()
    for doc in docs:
        doc.reference.delete()
    print("Colección limpia.\n")

    print("Consultando Wikidata...")
    resultados = obtener_parques_wikidata()
    print(f"Parques encontrados: {len(resultados)}")

    # Deduplicamos por ID — Wikidata devuelve múltiples filas cuando
    # un campo opcional (foto, región) tiene varios valores en su BD
    unicos = {}
    for item in resultados:
        wid = item["parque"]["value"].split("/")[-1]
        if wid not in unicos:
            unicos[wid] = item
    resultados = list(unicos.values())
    print(f"Parques únicos tras deduplicar: {len(resultados)}\n")

    for item in resultados:
        nombre      = limpiar_nombre(item.get("parqueLabel", {}).get("value", ""))
        descripcion = item.get("descripcion", {}).get("value", "")
        region      = item.get("regionLabel", {}).get("value", "")
        coord_raw   = item.get("coord", {}).get("value", "")
        imagen_raw  = item.get("imagen", {}).get("value", "")

        superficie_raw = item.get("superficie", {}).get("value", "0")
        superficie = float(superficie_raw) if superficie_raw else 0.0

        lat, lng = parsear_coordenadas(coord_raw)

        fotos = [obtener_foto_wikimedia(imagen_raw)] if imagen_raw else []

        # ID legible igual al idparquenatural del modelo
        doc_id = normalizar_nombre(nombre)

        parque = {
            "idparquenatural": doc_id,
            "nombre":          nombre,
            "descripcion":     descripcion,
            "pais":            "España",
            "region":          region,
            "tipo":            "Nacional",
            "superficie":      superficie,
            "fotosparque":     fotos,
            "lat":             lat,
            "lng":             lng,
            "contorno":        []
        }

        coleccion.document(doc_id).set(parque)
        print(f"  ✓  {nombre:40} ({doc_id})")

    print(f"\nIngesta completada — {len(resultados)} parques cargados en Firestore.")


if __name__ == "__main__":
    main()
