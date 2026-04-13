# [LOG DE PROGRESO - TRAILPACK]

## Cronología Técnica

### Fase 1-4: Cimientos
- Estructura base del proyecto, Autenticación Firebase, Navegación inicial y Perfil de usuario.

### Fase 5: Seguridad y Refactorización
- Implementación de cambio de contraseña, borrado de cuenta y validación de formularios.

### Fase 6: Núcleo Funcional (MVP - Mapas)
- **Navegación:** Implementación de navegación completa entre tabs (Mapa, Social, Perfil) con estado centralizado.
- **Mapas:** Desarrollo de `VistaMapa` con marcadores reactivos y `MapsRepository`.
- **Rutas:** Carga dinámica de rutas asociadas a parques vía `BottomSheet` y persistencia en Firestore.
- **Publicación:** Implementación de `PopUpPublicarRuta` y lógica de guardado de actividades vinculadas a rutas.

### Fase 7: Feed Social y Actividades (En curso - Hoy)
- **Estructura Social:** Creación del paquete `vistas/actividadespublicadas` para gestionar el feed de la comunidad.
- **Repositorio de Actividades:** Implementación de `ActividadesRepository` con soporte para recuperación de colecciones y documentos individuales.
- **Lógica de Negocio (ViewModel):** 
    - `ActividadesViewModel`: Implementación de una operación de "Join" en memoria que combina datos de `Actividad` (Firestore) con datos de `Ruta` (MapsRepository) para mostrar información completa.
    - Manejo de estados de carga (`isLoading`) y reactividad en el feed.
- **UI Social:**
    - `VistaRutasPublicadas`: Implementación de `LazyColumn` con recarga automática vía `LaunchedEffect`.
    - `CardActividad`: Diseño de tarjeta para mostrar el resumen de la actividad, incluyendo vinculación con el autor y la ruta.
    - `VistaDetalleActividad`: Creación de la vista profunda para explorar una actividad específica.
- **Refactorización y Fixes de UI (Sesión Actual):**
    - **Corrección en MapsRepository:** Se ha ajustado la recuperación de rutas individuales para que use el campo `idruta` como clave de búsqueda, asegurando la consistencia entre colecciones.
    - **Mejora en VistaDetalleActividad:** Sincronización completa de datos de ruta y actividad. Se ha implementado la lógica condicional para los botones de acción (Unirse/Salir) basada en el estado real de la lista de participantes.
- **Globalización de Estado:**
    - Integración de `usuarioGlobal` en `MainViewModel` para evitar consultas redundantes a Firebase.
    - Sistema de notificaciones `showNotification` centralizado en el `MainViewModel`.
    - Utilidades de formateo de fecha (`formatearFecha`) integradas en el core para consistencia visual.
