# [PROYECTO_MASTER: TrailPack - Arquitectura y Metodología]

Este archivo es la fuente de verdad del proyecto. Cualquier cambio en la arquitectura o metodología debe actualizar este archivo obligatoriamente.

## 1. Stack Tecnológico
- **UI:** Jetpack Compose (Material3).
- **Arquitectura:** MVVM (Model-View-ViewModel).
- **Backend:** Firebase (Auth, Firestore, Storage).
- **Gestión de Datos:** Repositorios independientes (`MapsRepository`, `UserRepository`, `AuthRepository`).
- **Gestión de Estado Global:** `MainViewModel` centraliza la navegación, estado de sesión, loader global y pestaña activa.

## 2. Metodología de Desarrollo
- **Interacción:** El usuario se comunica con el **Orquestador** (Programador Senior), quien delega internamente a los agentes (Arquitecto - Database Engineering, Diseñador - UX/UI) sin exponer la complejidad de la comunicación.
- **Protocolo de Edición (Inviolable):**
    - Agentes: **PROHIBIDO** editar archivos del proyecto. Solo proporcionan código de guía en texto plano.
    - Usuario: **ÚNICO** autorizado para editar código fuente.
- **Gestión de Logs:**
    - `LOG_PROGRESO.md`: Cronología de alto nivel.
    - `DECISION_LOG.md`: Diario de decisiones técnicas.
    - `TASKS.md`: Lista de tareas pendientes/completadas.
- **Protocolo de Cierre (End of Day):** Al comando "Hemos terminado por hoy", el Orquestador debe:
    1. Revisar los cambios realizados en el día.
    2. Consolidar el resumen en `LOG_PROGRESO.md`.
    3. Actualizar `PROYECTO_MASTER.md` si hubo cambios estructurales de peso.

## 3. Patrones de Diseño & Estándares
- **Formularios:** `FormModel` (inmutable) + `mutableStateOf` en ViewModel.
- **Mapas:** `GoogleMaps` + `maps-compose`.
- **Navegación:** `NavHost` centralizado + `Scaffold` con `BottomBar` gestionada por `MainViewModel.selectedTab`.
- **Seguridad:** Re-autenticación en operaciones críticas.

## 4. Evolución del Proyecto (Fases)
- **Fases 1-5:** Auth, Perfil, Seguridad base.
- **Fase 6:** Core Funcional (Mapa y Rutas).
- **Fase 7:** Social Feed y Eventos (En curso).
