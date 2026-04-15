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

### Fase 7: Feed Social y Actividades
- **Estructura Social:** Creación del paquete `vistas/actividadespublicadas` para gestionar el feed de la comunidad.
- **Lógica de Negocio (ViewModel):** 
    - `ActividadesViewModel`: Implementación de "Join" en memoria (Actividad + Ruta).
- **Refactorización y Arquitectura de UI:**
    - **Globalización de Notificaciones (Toasts):** Se ha movido el observador de `notificationMessage` a `AppNavegation.kt`.
    - **Estandarización de PopUps:** Se ha refactorizado `PopUpPublicarRuta` para que sea un componente `Dialog` de Compose.
    - **Fixes de Datos:** Corregida la recuperación de rutas individuales en `MapsRepository`.
    - **Sincronización de UI:** Unificada la lógica de los botones "Unirse/Salir" en `VistaDetalleActividad`.

### Sesión 2026-04-15 — Migración del sistema de agentes y aprendizaje sobre IA

- **Diagnóstico del sistema anterior:** Identificados GOD files desincronizados (campos faltantes en modelos, renombres no reflejados). El sistema era RAG manual con riesgo alto de deriva de contexto.
- **Archivado:** 12 GOD files movidos a `meta/GODagentes/ARCHIVO/`. La carpeta `meta/` queda limpia.
- **CLAUDE.md:** Creado en la raíz del proyecto. Bootstrap automático de sesión con stack, protocolos, secuencia de inicio/cierre y mapa de agentes.
- **AGENTES.md:** Redefinido con 4 agentes por capas MVVM (Arquitecto, Artesano UI, Lógico, Guardián de Flujo). Nuevo protocolo de activación: rol interno para tareas ≤3 archivos, sub-agente real (`Explore`/`Plan`) para tareas más amplias. Plantilla de briefing para sub-agentes (arrancan en frío).
- **PROYECTO_MASTER.md:** Limpiado de referencias a GOD files. Tabla de fases actualizada hasta Fase 10.
- **TASKS.md:** Sincronizado con la realidad (tarea unirse/salir marcada completada).
- **Skills:** Creados `/empezamos` y `/cerramos` en `.claude/skills/`. Activos tras reinicio de Claude Code.
- **Aprendizaje del usuario:** Tokens y ventana de contexto, diferencia entre roles internos y sub-agentes reales, Skills vs MCP, criterio para no usar skill de diseño ahora y reservarlo para fase de refactoring.

### Fase 8: Gestión de Actividades y Refinamiento
- **Modelo de Datos:** Ampliación de `Actividad` con campos específicos para `horasalida` y `puntoencuentro`.
- **UI de Entrada:** Implementación de `SelectorHoraMejorado` con `TimePicker` nativo de Compose.
- **Gestión de Autoría:** Implementación de permisos basados en `idcreador` para habilitar funciones de edición y borrado.
- **Edición Atómica:** Creación de `PopUpEditarActividad` para realizar actualizaciones masivas de datos en Firestore mediante un `Map<String, Any>`.
- **Eliminación:** Implementación de borrado físico de actividades en Firestore con retorno seguro a la vista anterior mediante el `Enrutador`.
- **Arquitectura de Navegación:** Desacoplamiento de vistas mediante callbacks (`onBack`) inyectados desde el `AppNavigation`.
