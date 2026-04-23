TrailPack 🥾
App Android de Social Discovery para senderistas.
Publica una salida con fecha, hora y plazas — que otros se unan.

TFG · CFGS Desarrollo de Aplicaciones Multiplataforma · 2S2526
Autor: Julen Tabuyo Murillo · Tutor: Emilio Saurina


El problema
Apps como Wikiloc o AllTrails son geniales para registrar dónde fuiste. Pero nadie publica una actividad a la que otros puedan unirse. Los grupos de senderismo se coordinan por WhatsApp: sin filtros de nivel, sin herramienta, sin estructura.
TrailPack resuelve eso.

Funcionalidades
MóduloDescripción🗺️ Mapa interactivoExplora parques naturales, accede a sus rutas oficiales y rutas de la comunidad🧭 RutasTrazado polyline, desnivel, dificultad. Crea tus propias rutas colocando waypoints en el mapa📅 ActividadesPublica salidas con fecha, hora, plazas y punto de encuentro. Únete con un toque👥 Feed socialSecciones de actividades publicadas, tuyas, en las que participas y caducadas🙋 PerfilesNivel de experiencia, historial, rutas favoritas, perfiles públicos🔔 SocialSeguir usuarios, contadores de seguidores/siguiendo, listas sociales

Stack
Mostrar imagen
Mostrar imagen
Mostrar imagen
Mostrar imagen
Mostrar imagen

Kotlin + Jetpack Compose (Material 3) — UI reactiva, sin XMLs
MVVM — Repositorios → ViewModels → Composables
Firebase Auth · Firestore · Storage
Google Maps SDK — modo terreno, polylines, waypoints
Python — scripts de ingesta desde Wikidata (SPARQL) y OpenStreetMap (Overpass API)


Arquitectura
MVVM  →  Repositorios independientes por dominio
          └─ AuthRepository / UserRepository / MapsRepository / ActividadesRepository

Estado global del usuario centralizado en MainViewModel
Join en memoria Actividad ↔ Ruta (Firestore no tiene joins nativos)
Caché local con HashMap en MapsRepository
NavHost centralizado en AppNavegation.kt · lógica de rutas en Enrutador.kt

Instalación
Requisitos

Android Studio Hedgehog+
Android 8.0+ (API 26+)
Proyecto Firebase con Auth, Firestore y Storage habilitados
API Key de Google Maps para Android

Pasos
bashgit clone https://github.com/JulenTBM/TrailPack.git

Añade app/google-services.json desde tu consola de Firebase.
Crea secrets.properties en la raíz:

   MAPS_API_KEY=TU_CLAVE_AQUI

Abre en Android Studio y ejecuta.


⚠️ Ambos archivos están en .gitignore. No los subas al repositorio.

Cargar datos reales (opcional)
bashpip install firebase-admin requests
python scripts/ingesta_parques.py   # 16 parques nacionales desde Wikidata
python scripts/ingesta_rutas.py     # Rutas desde OpenStreetMap

Estado
FaseContenido1–3Auth, perfil, scaffold y navegación✅4–6Mapas, rutas, actividades✅7–9Perfil social, ingesta real, seguimiento✅10Feed personalizado + sistema de amistad🔄11Crashlytics, reglas Firestore, APK de lanzamiento⏳
Próximo: sistema de amistad bidireccional con notificaciones in-app · mensajería · perfil de elevación · Mapbox

Configuración de compilación
compileSdk   35
minSdk       26
Firebase BOM 32.7.0
Maps SDK     19.0.0

Julen Tabuyo Murillo 
