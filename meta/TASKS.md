# [TASKS — TrailPack]

---

## En curso

_(ninguna)_

---

## Pendientes — Prioridad Alta (Fase 9)

- [ ] **Perfil social:** Mostrar lista de actividades creadas y actividades en las que el usuario participa en `VistaPerfilUsuario`.
- [ ] **Caché local de rutas:** Implementar caché en memoria en `MapsRepository` para evitar llamadas repetidas a Firestore al navegar entre tabs.
- [ ] **FILE_TREE actualizado:** `VistaRutasPublicadas.kt` está siendo renombrado a `VistaActividades.kt` — actualizar `FILE_TREE.md` al confirmar el rename.

---

## Pendientes — Prioridad Baja (Futuro)

- [ ] **Skill `/refactor-ui`:** Crear skill de auditoría Jetpack Compose con mejores prácticas (recomposiciones, hoist de estado, keys en LazyColumn, tema). Activar solo cuando el proyecto esté estable y haya una fase de refactoring dedicada.

---

## Pendientes — Prioridad Media (Fase 10)

- [ ] **Notificaciones push:** Configurar Firebase Cloud Messaging para alertas de nuevas actividades.
- [ ] **Google Places API:** Integrar búsqueda de puntos de encuentro en el formulario de publicación.

---

## Completado

- [x] Estructura del feed social (`vistas/actividadespublicadas`).
- [x] `ActividadesRepository` con acceso a Firestore.
- [x] Lógica de Join en memoria (`ActividadConRuta`) en `ActividadesViewModel`.
- [x] UI de `CardActividad` con diseño moderno.
- [x] `VistaDetalleActividad` y `DetalleActividadViewModel`.
- [x] Lógica de Unirse / Abandonar actividades (actualización de `listaparticipantesIds` en Firestore).
- [x] Centralización de notificaciones (Toasts) en `MainViewModel` + observador en `AppNavegation`.
- [x] Refactorización de PopUps a componentes `Dialog` de Compose.
- [x] Selector de hora nativo (`SelectorHoraMejorado` con `TimePicker`).
- [x] Gestión de autoría: permisos de edición/borrado basados en `idcreador`.
- [x] `PopUpEditarActividad` con actualización masiva en Firestore.
- [x] Eliminación de actividades con retorno seguro via `Enrutador`.
- [x] Migración del sistema de agentes: archivado GOD files, creación de `CLAUDE.md`, redefinición de `AGENTES.md` y `PROYECTO_MASTER.md`.
- [x] Creación de skills `/empezamos` y `/cerramos` en `.claude/skills/`. Activos tras reinicio de Claude Code.
