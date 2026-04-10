# [LOG DE PROGRESO - TRAILPACK]

## Cronología Técnica
- **Fase 1-4:** Estructura base, Auth, Navegación y Perfil.
- **Fase 5:** Seguridad (Cambio pass, Borrado de cuenta), refactorización de formularios.
- **Fase 6 (Hoy):** 
    - **Núcleo Funcional (MVP):** 
        - Implementación navegación completa entre tabs (Mapa, Social, Perfil).
        - Desarrollo de `VistaMapa` y repositorio `MapsRepository` (lectura de parques).
        - Carga dinámica de rutas asociadas a parques vía `BottomSheet` (Firebase).
    - **Optimización UI:** Refactorización a arquitectura de componentes aislados (`MapaContent`, `ListaRutasParqueBottomSheet`) para eliminar parpadeos en recomposición.
    - **Estándares:** Consolidación de gestión de tipos de datos entre Kotlin (`Long`) y Firestore (`int64`).

## Notas Técnicas Recientes
- Se ha confirmado que el uso de `Box` con visibilidad condicional es superior al uso de `if` imperativo para mantener la estabilidad del `GoogleMap` en Compose.
- Se debe mantener consistencia estricta: `Long` para fechas en Kotlin corresponde a `number (int64)` en Firestore.