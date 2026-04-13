# [DIARIO DE DESARROLLO Y TAREAS]

## Realizado (Hoy)
- [COMPLETADO] Estructura del feed social en `vistas/actividadespublicadas`.
- [COMPLETADO] Implementación de `ActividadesRepository` con acceso a Firestore.
- [COMPLETADO] Lógica de "Join" en `ActividadesViewModel` para vincular Actividades con sus Rutas correspondientes.
- [COMPLETADO] UI de `CardActividad` con diseño moderno para el feed social.
- [COMPLETADO] Implementación de `VistaRutasPublicadas` con scroll reactivo y carga diferida.
- [COMPLETADO] Creación de `VistaDetalleActividad` y su respectivo ViewModel.
- [COMPLETADO] Centralización de utilidades de fecha y estado de usuario en `MainViewModel`.

## Realizado (Anterior)
- [COMPLETADO] Configuración de navegación entre tabs.
- [COMPLETADO] UI de Mapa, marcadores reactivos y `BottomSheet` detalle.
- [COMPLETADO] Integración de `PopUpPublicarRuta` con persistencia en Firestore.

## Pendientes Próximos (Prioridad Alta)
1. **Interacción Social:** Implementar lógica para "Unirse" y "Abandonar" actividades (actualizar array de participantes en Firestore).
2. **UI Detalle Actividad:** Refinar el diseño de la vista detallada (añadir mapa estático o miniatura de la ruta).
3. **Optimización:** Implementar caché local para las rutas para evitar llamadas excesivas a Firebase en el feed social.
4. **Perfil:** Mostrar lista de actividades creadas/unidas por el usuario en su perfil.

## Pendientes (Prioridad Media/Baja)
1. **Geolocalización:** Integrar Google Places API para búsqueda de puntos.
2. **Notificaciones Push:** Configurar Firebase Cloud Messaging para alertas de nuevas actividades.
