# [DIARIO DE DESARROLLO Y TAREAS]

## Realizado hoy (Fase 6 - Core Mapa)
- [COMPLETADO] Configuración de navegación entre tabs.
- [COMPLETADO] Implementación `MapsRepository` (Consulta de Parques y Rutas asociadas).
- [COMPLETADO] UI de Mapa con `GoogleMap` y marcadores reactivos.
- [COMPLETADO] `BottomSheet` de detalle dinámico con carga de datos desde Firebase.
- [COMPLETADO] Optimización de rendimiento: Separación de componentes para evitar recargas del mapa.
- [COMPLETADO] Corrección de deserialización de fechas (`Long` vs `Timestamp`).

## Pendientes Próximos (Prioridad Alta)
1. **Diseño Visual:** Crear componentes `RutaCard` y vista de detalle de ruta.
2. **UI Rutas:** Implementar `LazyColumn` en el `BottomSheet` para listar las rutas de forma visual.
3. **Acción Publicar:** Implementar lógica de publicación de eventos (`Actividad`) en Firebase (accesible desde `RutaCard` y detalle).
4. **Arquitectura:** Iniciar estructura del repositorio para la Tab 2 (Social Feed).

[PENDIENTE] Diseñar `RutaCard` y `DetalleRuta`. (Prioridad: Media)
[COMPLETADO] Implementar navegación principal. (Prioridad: Alta)
[COMPLETADO] Integrar mapa con datos reales (Firebase). (Prioridad: Alta)
[PENDIENTE] Integrar Geolocalización (Google Places API). (Prioridad: Baja)