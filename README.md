# TrailPack 🥾

**App Android de Social Discovery para senderistas.**  
Publica una salida con fecha, hora y plazas — que otros se unan.

> TFG · CFGS Desarrollo de Aplicaciones Multiplataforma · 2S2526  
> Autor: Julen Tabuyo Murillo · Tutor: Emilio Saurina

---

## El problema

Apps como Wikiloc o AllTrails son geniales para registrar dónde fuiste. Pero nadie publica una actividad a la que otros puedan unirse. Los grupos de senderismo se coordinan por WhatsApp: sin filtros de nivel, sin herramienta, sin estructura.

**TrailPack resuelve eso.**

---

## Funcionalidades

| Módulo | Descripción |
|---|---|
| 🗺️ Mapa interactivo | Explora parques naturales, accede a sus rutas oficiales y rutas de la comunidad |
| 🧭 Rutas | Trazado polyline, desnivel, dificultad. Crea tus propias rutas colocando waypoints en el mapa |
| 📅 Actividades | Publica salidas con fecha, hora, plazas y punto de encuentro. Únete con un toque |
| 👥 Feed social | Secciones de actividades publicadas, tuyas, en las que participas y caducadas |
| 🙋 Perfiles | Nivel de experiencia, historial, rutas favoritas, perfiles públicos |
| 🔔 Social | Seguir usuarios, contadores de seguidores/siguiendo, listas sociales |

---

## Stack

![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=flat&logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack_Compose-4285F4?style=flat&logo=android&logoColor=white)
![Firebase](https://img.shields.io/badge/Firebase-FFCA28?style=flat&logo=firebase&logoColor=black)
![Google Maps](https://img.shields.io/badge/Google_Maps_SDK-4285F4?style=flat&logo=googlemaps&logoColor=white)
![Android](https://img.shields.io/badge/Android_8.0+-3DDC84?style=flat&logo=android&logoColor=white)

- **Kotlin** + **Jetpack Compose** (Material 3) — UI reactiva, sin XMLs
- **MVVM** — Repositorios → ViewModels → Composables
- **Firebase** Auth · Firestore · Storage
- **Google Maps SDK** — modo terreno, polylines, waypoints
- **Python** — scripts de ingesta desde Wikidata (SPARQL) y OpenStreetMap (Overpass API)

---

## Arquitectura

```
MVVM  →  Repositorios independientes por dominio
          └─ AuthRepository / UserRepository / MapsRepository / ActividadesRepository

Estado global del usuario centralizado en MainViewModel
Join en memoria Actividad ↔ Ruta (Firestore no tiene joins nativos)
Caché local con HashMap en MapsRepository
NavHost centralizado en AppNavegation.kt · lógica de rutas en Enrutador.kt
```

---

## Instalación

### Requisitos
- Android Studio Hedgehog+
- Android 8.0+ (API 26+)
- Proyecto Firebase con Auth, Firestore y Storage habilitados
- API Key de Google Maps para Android

### Pasos

```bash
git clone https://github.com/JulenTBM/TrailPack.git
```

1. Añade `app/google-services.json` desde tu consola de Firebase.
2. Crea `secrets.properties` en la raíz:
   ```
   MAPS_API_KEY=TU_CLAVE_AQUI
   ```
3. Abre en Android Studio y ejecuta.

> ⚠️ Ambos archivos están en `.gitignore`. No los subas al repositorio.

### Cargar datos reales (opcional)

```bash
pip install firebase-admin requests
python scripts/ingesta_parques.py   # 16 parques nacionales desde Wikidata
python scripts/ingesta_rutas.py     # Rutas desde OpenStreetMap
```

---

## Estado

| Fase | Contenido | |
|---|---|---|
| 1–3 | Auth, perfil, scaffold y navegación | ✅ |
| 4–6 | Mapas, rutas, actividades | ✅ |
| 7–9 | Perfil social, ingesta real, seguimiento | ✅ |
| 10 | Feed personalizado + sistema de amistad | 🔄 |
| 11 | Crashlytics, reglas Firestore, APK de lanzamiento | ⏳ |

**Próximo:** sistema de amistad bidireccional con notificaciones in-app · mensajería · perfil de elevación · Mapbox

---

## Configuración de compilación

```
compileSdk   35
minSdk       26
Firebase BOM 32.7.0
Maps SDK     19.0.0
```

---

*Julen Tabuyo Murillo · [julen.tabuyo@gmail.com](mailto:julen.tabuyo@gmail.com)*
