# [LOG DE PROGRESO - TRAILPACK]

## Cronología Técnica
- **Fase 1-4:** Estructura base, Auth, Navegación y Perfil.
- **Fase 5:** Seguridad (Cambio pass, Borrado de cuenta), refactorización de formularios.
- **Fase 6:** 
    - **Núcleo Funcional (MVP):** 
        - Implementación navegación completa entre tabs (Mapa, Social, Perfil).
        - Desarrollo de `VistaMapa` y repositorio `MapsRepository`.
        - Carga dinámica de rutas asociadas a parques vía `BottomSheet` (Firebase).
    - **Optimización & Refactorización (Actual):**
        - Centralización de estado global (`selectedTab`, Auth) en `MainViewModel`.
        - Implementación de navegación dinámica hacia `VistaRutaDetalladaMapa`.
        - Integración de `PopUpPublicarRuta` en vistas de detalle.
        - Corrección de bugs de persistencia de navegación y bloqueo de `BottomBar`.
