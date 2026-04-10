# [PROYECTO_MASTER: TrailPack - Arquitectura y Metodología]

Este archivo es la fuente de verdad del proyecto. Cualquier cambio en la arquitectura o metodología debe actualizar este archivo obligatoriamente.

## 1. Stack Tecnológico
- **UI:** Jetpack Compose (Material3).
- **Arquitectura:** MVVM (Model-View-ViewModel).
- **Backend:** Firebase (Auth, Firestore, Storage).
- **Gestión de Datos:** Repositorios independientes por dominio (`MapsRepository`, `UserRepository`, `AuthRepository`).

## 2. Metodología de Desarrollo
- **Triple Agente:** Arquitecto (Diseño), Formador (Razonamiento), Programador (Implementación).
- **Gestión de Logs:**
    - `LOG_PROGRESO.md`: Cronología de alto nivel.
    - `TASKS.md`: Lista de tareas pendientes/completadas.
- **Protocolo de Edición:** 
    - Arquitecto y Formador: **PROHIBIDO** editar archivos del proyecto. Solo pueden proporcionar código de guía.
    - Programador: **ÚNICO** autorizado para editar código. **OBLIGADO** a actualizar `LOG_PROGRESO.md` y `TASKS.md` tras cada hito importante.

## 3. Patrones de Diseño & Estándares
- **Formularios:** Uso estricto de `FormModel` (inmutable) + `mutableStateOf` en ViewModel.
- **Mapas:** Uso de `GoogleMaps` + `maps-compose`. Estado preservado en `Box` para evitar parpadeos.
- **Navegación:** `Scaffold` con `BottomBar` gestionada por `selectedTab` (State Hoisting).
- **Seguridad:** Re-autenticación en operaciones críticas y limpieza de stack con `popUpTo(0)`.

## 4. Evolución del Proyecto (Fases)
- **Fases 1-5:** Auth, Perfil, Seguridad base.
- **Fase 6:** Core Funcional (Mapa y Rutas).
- **Fase 7:** Social Feed y Eventos (Pendiente).