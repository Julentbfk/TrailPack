# [LOG DE PROGRESO - TRAILPACK]

## Cronología Técnica
- **Fase 1-4:** Estructura base, Auth, Navegación y Perfil.
- **Fase 5:** Seguridad (Cambio pass, Borrado de cuenta), refactorización de formularios.
- **Fase 6:** 
    - **Núcleo Funcional (MVP):** 
        - Implementación navegación completa entre tabs (Mapa, Social, Perfil).
        - Desarrollo de `VistaMapa` y repositorio `MapsRepository`.
        - Carga dinámica de rutas asociadas a parques vía `BottomSheet` (Firebase).
    - **Optimización & Refactorización:**
        - Centralización de estado global (`selectedTab`, Auth) en `MainViewModel`.
        - Integración de sistema de notificaciones global (Toasts centralizados).
        - Implementación lógica persistencia Actividades (guardado en Firestore).
        - Refactorización componentes formulario (SelectorFecha con Material3).
