# [PROYECTO_MASTER: TrailPack - Arquitectura y Metodología]

Este archivo es la fuente de verdad del proyecto. Cualquier cambio en la arquitectura o metodología debe actualizar este archivo obligatoriamente.

## 0. INICIALIZACIÓN Y MANTENIMIENTO DEL CONTEXTO (BOOSTRAP)
**AL INICIAR CUALQUIER SESIÓN, EL ORQUESTADOR DEBE:**
1. Leer: `meta/AGENTES.md`, `meta/PROYECTO_MASTER.md`, `meta/TASKS.md`.
2. Analizar el contexto del proyecto mediante `meta/FILE_TREE.md`.
3. Esperar al comando "Empezamos".

**DURANTE SESIONES LARGAS:**
- Si la sesión supera los 50 mensajes, el Orquestador deberá realizar un "autochequeo" re-leyendo los archivos en `meta/` para garantizar que no ha habido deriva de instrucciones.

---

Este archivo es la fuente de verdad del proyecto. Cualquier cambio en la arquitectura o metodología debe actualizar este archivo obligatoriamente.

## 1. Stack Tecnológico
- **UI:** Jetpack Compose (Material3).
- **Arquitectura:** MVVM (Model-View-ViewModel).
- **Backend:** Firebase (Auth, Firestore, Storage).
- **Gestión de Datos:** Repositorios independientes (`MapsRepository`, `UserRepository`, `AuthRepository`).
- **Gestión de Estado Global:** `MainViewModel` centraliza la navegación, estado de sesión, loader global y pestaña activa.

## 2. Metodología de Desarrollo
- **Interacción:** El usuario se comunica exclusivamente con el **Orquestador** (Programador Senior).
- **Protocolo de Edición (Inviolable):**
  - Agentes: **PROHIBIDO** editar archivos del proyecto. Solo proporcionan código de guía en texto plano.
  - Usuario: **ÚNICO** autorizado para editar código fuente.
- **Protocolo de Memoria Continua:**
  - **Logging Atómico:** Cada tarea finalizada implica una actualización inmediata de `TASKS.md` y `DECISION_LOG.md`.
  - **Disparadores de Sesión:**
    - **"Empezamos":** Lectura obligatoria de logs y petición de tarea prioritaria.
    - **"Cerramos por hoy":** Resumen final, consolidación de archivos y marcado de tareas pendientes.
- **Gestión de Logs:** `LOG_PROGRESO.md`, `DECISION_LOG.md`, `TASKS.md`.

## 3. Patrones de Diseño & Estándares
- **Formularios:** `FormModel` (inmutable) + `mutableStateOf` en ViewModel.
- **Mapas:** `GoogleMaps` + `maps-compose`.
- **Navegación:** `NavHost` centralizado + `Scaffold` con `BottomBar` gestionada por `MainViewModel.selectedTab`.
- **Seguridad:** Re-autenticación en operaciones críticas.

## 4. Evolución del Proyecto (Fases)
- **Fases 1-5:** Auth, Perfil, Seguridad base.
- **Fase 6:** Core Funcional (Mapa y Rutas).
- **Fase 7:** Social Feed y Eventos (En curso).
