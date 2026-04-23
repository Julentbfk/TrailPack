TrailPack

Proyecto de Fin de Grado — CFGS Desarrollo de Aplicaciones Multiplataforma
Autor: Julen Tabuyo Murillo · Tutor: Emilio Saurina · Convocatoria: 2S2526


¿Qué es TrailPack?
TrailPack es una aplicación Android de Social Discovery para senderistas. El núcleo del producto no es un catálogo de rutas, sino una capa social construida sobre él: cualquier usuario puede publicar una actividad —una salida concreta con fecha, hora, punto de encuentro, nivel requerido y plazas disponibles— y otros usuarios pueden unirse a ella.
Las aplicaciones de referencia del sector (Wikiloc, AllTrails, Strava, Komoot) están construidas sobre el registro retrospectivo: sirven para guardar dónde fuiste, con quién y qué métricas obtuviste. Ninguna tiene un flujo nativo para organizar una salida futura con personas que no conoces de antes. Los grupos de senderismo se coordinan por WhatsApp: sin filtros de nivel, sin herramienta integrada, sin estructura. TrailPack resuelve exactamente eso.
La idea de fondo tiene también una dimensión social más amplia. España tiene un problema de soledad no deseada documentado que afecta especialmente a jóvenes de entre 18 y 35 años —el perfil exacto de usuario de esta app—, y el senderismo es una actividad con características especialmente adecuadas para crear vínculos reales: no es competitivo, la comunicación fluye de forma natural durante horas sin pantallas, y los estudios publicados en revistas como Landscape and Urban Planning o BMC Public Health confirman que hacerlo en grupo amplifica significativamente los beneficios psicológicos respecto a la práctica individual.

Objetivos del proyecto
Alcanzados ✅

Catálogo de parques naturales y rutas consultables desde un mapa interactivo.
Creación y publicación de rutas propias, descubribles por otros usuarios.
Sistema de actividades en tiempo real al que otros usuarios pueden unirse.
Sistema de niveles de usuario visible en cada perfil, como medida de compatibilidad y seguridad activa en el grupo.
Perfil social completo: nivel, foto, rutas favoritas, actividades creadas y participadas.
Seguimiento entre usuarios y feed de actividades de la comunidad.

En desarrollo 🔄

Sistema de amistad bidireccional con control de privacidad en actividades.
Feed personalizado con actividades de usuarios seguidos y amigos.
Mensajería directa y grupos de chat para planificación de salidas.


Stack tecnológico
CapaTecnologíaJustificaciónLenguajeKotlinNull-safety, sintaxis concisa, 100% compatible con el ecosistema Java. Usado en toda la app y en los scripts de ingesta.UIJetpack Compose + Material Design 3Interfaz reactiva definida en Kotlin. Los composables se redibujan solos al cambiar el estado, sin código de pegamento entre XML y lógica.ArquitecturaMVVMSeparación clara entre datos, lógica de negocio y presentación. Estado global centralizado en MainViewModel.MapasGoogle Maps SDK para AndroidMapa en modo terreno, polylines de trazado, marcadores reactivos, creación de waypoints y soporte para lite mode en thumbnails.BackendFirebase (Auth + Firestore + Storage)Auth para registro y verificación de email. Firestore como base de datos NoSQL en tiempo real. Storage para fotos de perfil. Sin esquema rígido, facilita la evolución del modelo.ImágenesCoilImageLoader personalizado con interceptor OkHttp para manejar el User-Agent de Wikimedia.Ingesta de datosPython (Wikidata SPARQL + Overpass API)Scripts de carga única que llenan Firestore con parques nacionales y rutas reales. La app siempre lee de Firestore, nunca de APIs externas en runtime.

Arquitectura
El proyecto sigue el patrón MVVM con repositorios independientes por dominio.
app/
├── data/                     # Repositorios (fuente única de verdad por dominio)
│   ├── AuthRepository.kt
│   ├── UserRepository.kt
│   ├── MapsRepository.kt     # Caché local con HashMap para parques y rutas
│   └── ActividadesRepository.kt
│
├── modelos/                  # Data classes Kotlin (entidades de negocio)
│   ├── Usuario.kt
│   ├── Actividad.kt
│   ├── ActividadConRuta.kt   # Join en memoria (Firestore no tiene joins nativos)
│   ├── Ruta.kt
│   ├── Coordenada.kt         # Value object con lat, lng y altitud
│   ├── ParqueNatural.kt
│   └── ...
│
├── routing/                  # Navegación desacoplada de la UI
│   ├── AppNavegation.kt      # NavHost centralizado
│   └── Enrutador.kt          # Funciones de navegación tipadas
│
├── theme/                    # Colores, tipografía y tema Material 3
│
└── vistas/
    ├── componentes/          # Composables reutilizables
    │   ├── formulario/       # TextFields, selectores de fecha y hora, prefijo de país
    │   ├── mapa/             # MapaPrincipal, ThumbnailRuta, MapaRutaCard
    │   ├── usuario/          # UserAvatar, FotoPerfil, NivelUsuario, PopUpListaUsuarios
    │   └── actividades/      # StatItem, UserAvatar
    ├── login/
    ├── registro/
    ├── mapa/                 # VistaMapa, VistaCrearRuta, VistaRutaDetalladaMapa
    ├── actividadespublicadas/ # Feed, CardActividad, VistaDetalleActividad
    ├── perfilusuario/        # Perfil propio, perfil público, editar perfil
    ├── ajustes/
    └── marcogeneral/         # ScaffoldTrailPack, TopBar, BottomBar, MainViewModel
Decisiones arquitectónicas clave
Estado global del usuario. Al iniciar la app o después del login, el objeto Usuario se carga en MainViewModel.usuarioGlobal. Todas las pantallas lo consumen desde ahí sin peticiones redundantes a Firestore.
Join en memoria. Firestore no soporta joins nativos entre colecciones. ActividadesViewModel cruza los datos en el cliente: recupera actividades, extrae los IDs de ruta únicos, consulta esas rutas al MapsRepository (con caché) y combina los objetos en ActividadConRuta. Minimiza las lecturas de Firebase y mantiene la lógica reactiva.
Caché de repositorio. MapsRepository implementa una caché con HashMap en companion object para rutas por parque, rutas por IDs y rutas individuales. Las operaciones de escritura invalidan la caché afectada.
Ingesta offline. Los 16 parques nacionales de España se cargaron desde Wikidata mediante consultas SPARQL, y las rutas desde OpenStreetMap mediante la API Overpass. Ambos scripts (scripts/ingesta_parques.py y scripts/ingesta_rutas.py) se ejecutaron una única vez. La app no depende de ninguna API externa en tiempo de ejecución.

Funcionalidades implementadas
Autenticación y perfil

Registro con validación en tiempo real y verificación de correo electrónico.
Login con reenvío de email de verificación y redirección según estado del usuario.
Completar perfil obligatorio antes del primer acceso.
Edición de datos personales, foto de perfil (Firebase Storage), cambio de contraseña y eliminación de cuenta con re-autenticación.

Mapa y rutas

Mapa interactivo en modo terreno con marcadores de parques naturales.
BottomSheet por parque con dos secciones desplegables: rutas oficiales (OpenStreetMap) y rutas creadas por la comunidad.
Ficha de ruta con trazado polyline en mapa interactivo, distancia, desnivel, dificultad y galería de fotos.
Thumbnails de ruta con mapa en lite mode cuando no hay foto disponible.
Creación de rutas propias: mapa TERRAIN interactivo con waypoints en tiempo real, cálculo de distancia acumulada con fórmula Haversine y formulario con nombre, dificultad y descripción.
Guardar rutas como favoritas con toggle reactivo.

Actividades sociales

Publicar actividades vinculadas a una ruta: fecha, hora de salida (selector nativo), plazas, punto de encuentro, nivel requerido.
Unirse y salir de actividades con actualización instantánea de contadores.
Editar y eliminar actividades propias (permisos basados en idcreador).
Feed de actividades con cuatro secciones desplegables: publicadas, creadas por ti, en las que participas y caducadas.
Paginación de 10 en 10 por sección con botón "Ver más".

Perfiles y capa social

Perfil propio con nivel de experiencia, foto, y tabs de actividades creadas, participadas y rutas favoritas.
Perfiles públicos de otros usuarios con sus actividades y estadísticas.
Seguir y dejar de seguir con contadores reactivos en ambos perfiles (batch write atómico en Firestore).
Listas de seguidores y seguidos con carga lazy al pulsar los contadores.
Avatares de participantes navegables en el detalle de cada actividad.
Notificaciones in-app centralizadas vía MainViewModel.showNotification().


Modelo de datos
Las entidades principales y sus relaciones en Firestore:
Usuario
├── uid, nombre, email, telefono, descripcion
├── fotoPerfil (URL de Firebase Storage)
├── nivel (calculado por actividad)
├── rutasFavoritas: List<String>  (IDs de rutas)
├── listaseguidores / listasiguiendo / listaamigos: List<String>
└── seguidorescount / siguiendocount / amigoscount: Int

Ruta
├── idruta, nombre, idparque, descripcion, dificultad
├── distancia, desnivelTotal, desnivelPositivo, desnivelNegativo
├── altitudMaxima, altitudMinima
├── coordenadas: List<Coordenada>  ← lat + lng + altitud por punto
├── fotosRuta: List<String>
├── esOficial: Boolean
└── nombreCreador

Actividad
├── idactividad, idruta, idcreador, nombreCreador
├── fechasalida (Long), horasalida (Long)
├── plazas, puntoencuentro, nivelrequerido
└── listaparticipantesIds: List<String>

ParqueNatural
├── idparque, nombre, descripcion, pais, region, tipo
├── superficie: Double
├── coordenadas (centro), contorno: List<GeoPoint>
└── foto (URL Wikimedia via Commons API)

Coordenada  ← value object embebido en Ruta
└── lat, lng, altitud: Double

Requisitos del sistema

Android 8.0 (API 26) o superior
Conexión a internet activa
Cuenta de Google para autenticación Firebase


Instalación y configuración
1. Clonar el repositorio
bashgit clone https://github.com/JulenTBM/TrailPack.git
cd TrailPack
2. Configurar Firebase

Crea un proyecto en Firebase Console.
Activa Authentication (proveedor Email/Password), Firestore Database y Storage.
Descarga el archivo google-services.json desde la configuración de tu app Android y colócalo en app/google-services.json.


⚠️ Este archivo contiene tus claves privadas. Está en .gitignore y nunca debe subirse al repositorio.

3. Configurar Google Maps

Obtén una API Key para Android en Google Cloud Console con las APIs Maps SDK for Android y Places API habilitadas.
Crea el archivo secrets.properties en la raíz del proyecto:

propertiesMAPS_API_KEY=TU_CLAVE_AQUI

⚠️ Este archivo también está en .gitignore. No lo subas al repositorio.

4. Compilar y ejecutar
Abre el proyecto en Android Studio Hedgehog o superior y ejecuta en un emulador (API 26+) o dispositivo físico.
compileSdk     35
minSdk         26  (Android 8.0+)
Firebase BOM   32.7.0
Google Maps    19.0.0
5. Cargar datos reales (opcional)
Los scripts de ingesta están en scripts/. Requieren Python 3 y acceso a internet:
bashpip install firebase-admin requests
python scripts/ingesta_parques.py   # Carga 16 parques nacionales desde Wikidata
python scripts/ingesta_rutas.py     # Carga rutas desde OpenStreetMap (Overpass API)

Los scripts borran la colección antes de reingestar. Ejecútalos solo una vez o cuando quieras refrescar los datos base.


Estado del desarrollo
FaseContenidoEstado1Autenticación, verificación de email, redirecciones por estado✅ Completada2Gestión completa de perfil (edición, foto, contraseña, borrado de cuenta)✅ Completada3Scaffold principal, navegación, estado global, notificaciones in-app✅ Completada4Google Maps SDK, marcadores de parques, datos estáticos✅ Completada5Cards de parques y rutas, BottomSheet, formulario de actividades✅ Completada6Gestión completa de actividades, feed con secciones, edición y borrado✅ Completada7Perfil social, tabs de historial, rutas favoritas✅ Completada8Ingesta real desde Wikidata y OSM, polylines, creación de rutas propias✅ Completada9Perfiles públicos, seguir/siguiendo, listas sociales✅ Completada10Feed personalizado, sistema de amistad, notificaciones push🔄 En desarrollo11Seguridad Firestore para producción, Crashlytics, APK de lanzamiento⏳ Pendiente

Hoja de ruta
Sistema de amistad bidireccional. Conexión explícita con solicitud y aceptación mediante buzón de notificaciones in-app. El modelo de datos ya lo contempla desde el diseño inicial (listaamigos, amigoscount). Las actividades podrán ser públicas, solo para amigos o privadas por invitación.
Feed personalizado. Sección "De tus amigos" en el feed de actividades, filtrada por idcreador e listaparticipantesIds de usuarios seguidos.
Mensajería. Chat vinculado a actividades concretas para coordinar salidas sin depender de plataformas externas.
Perfil de elevación. Los datos de altitud ya están almacenados en cada Coordenada. Queda visualizarlos como gráfica bajo el mapa en VistaRutaDetalladaMapa.
Mejoras de infraestructura. Firebase Cloud Messaging para notificaciones push, Crashlytics para captura de errores en dispositivos de testers, migración potencial a Mapbox para terreno 3D con polylines que siguen el relieve, y revisión de reglas de seguridad de Firestore para producción.

Contexto académico
Este proyecto es el Trabajo de Fin de Grado del Ciclo Formativo de Grado Superior en Desarrollo de Aplicaciones Multiplataforma, desarrollado entre febrero y abril de 2026.
La memoria completa del proyecto documenta en detalle el análisis del mercado de aplicaciones de senderismo, el marco teórico sobre los beneficios psicológicos del senderismo en grupo, la metodología iterativa e incremental seguida durante el desarrollo, y las decisiones técnicas y de producto tomadas en cada fase.

Autor
Julen Tabuyo Murillo
