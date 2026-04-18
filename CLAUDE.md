# CLAUDE.md — TrailPack Bootstrap

> Este archivo se carga automáticamente en cada sesión. Es la fuente de verdad del protocolo de trabajo.

---

## 1. El Proyecto

**TrailPack** es una app Android de Social Discovery para senderistas. El núcleo del producto no es el catálogo de rutas, sino la capa social: publicar actividades (eventos de senderismo con fecha, hora y plazas) y que otros usuarios se unan.

- **Stack:** Kotlin · Jetpack Compose (Material3) · MVVM · Firebase (Auth, Firestore, Storage) · Google Maps
- **Arquitectura:** Repositorios independientes → ViewModels → Composables. Estado global centralizado en `MainViewModel`.
- **Navegación:** `NavHost` centralizado en `AppNavegation.kt`. Rutas gestionadas por `Enrutador.kt`.

---

## 2. Protocolo de Trabajo (Inviolable)

| Regla | Detalle |
|---|---|
| **No tocar código** | Prohibido editar `.kt`, `.xml`, `.gradle` sin autorización explícita del usuario. |
| **Pasos atómicos** | Cada tarea se divide en pasos numerados. No se entrega el Paso 2 hasta confirmar el Paso 1. |
| **Pedagógico** | Explicar siempre el "porqué" antes del "qué". El objetivo es que el usuario entienda, no solo que funcione. |
| **Logs automáticos** | Al cerrar cada tarea: actualizar `meta/TASKS.md` y expandir `meta/LOG_PROGRESO.md`. |
| **Código real** | Leer siempre los archivos `.kt` actuales antes de proponer cambios. Los archivos en `meta/GODagentes/ARCHIVO/` son históricos. |

---

## 3. Secuencia de Inicio Obligatoria

Al recibir **"Empezamos"**, ejecutar en orden:

1. Leer `meta/TASKS.md` → identificar tarea de mayor prioridad.
2. Leer `meta/LOG_PROGRESO.md` → entender el último estado real.
3. Leer `meta/DECISION_LOG.md` → tener presentes las decisiones arquitectónicas vigentes.
4. Presentar resumen ejecutivo: dónde nos quedamos + propuesta de siguiente acción.

Al recibir **"Cerramos"**:

1. Actualizar `meta/TASKS.md` con el estado real de cada tarea trabajada.
2. Expandir `meta/LOG_PROGRESO.md` con el hito de la sesión.
3. Añadir a `meta/DECISION_LOG.md` si se tomó alguna decisión arquitectónica relevante.

---

## 4. Mapa de Agentes Internos

El Orquestador invoca estos roles internamente para analizar cada tarea desde su ángulo. No requieren que el usuario pegue nada: leen los archivos reales del proyecto.

| Agente | Capa MVVM | Archivos que lee |
|---|---|---|
| **Arquitecto** | Datos | `modelos/*.kt` · `data/*.kt` |
| **Artesano UI** | Presentación | `vistas/componentes/**` · `theme/**` |
| **Lógico** | ViewModels / Negocio | `vistas/**/*ViewModel.kt` |
| **Guardián de Flujo** | Shell / Navegación | `routing/**` · `vistas/marcogeneral/**` |

Ver definición completa en `meta/AGENTES.md`.

---

## 5. Convenciones del Proyecto

- **Nombres:** español para variables, funciones y clases de negocio. Inglés solo si lo impone el framework.
- **Formularios:** patrón `FormModel` inmutable + `mutableStateOf` en ViewModel.
- **Notificaciones:** siempre via `mainViewModel.showNotification(mensaje)`, nunca Toasts directos.
- **Repositorios:** prefijo `repo` en todas las funciones públicas (ej: `repoObtenerActividades`).
- **No inventar campos:** leer el modelo real antes de proponer cualquier acceso a datos.
- **Código comentado:** todo el código propuesto debe ir comentado línea a línea como guía didáctica, explicando el porqué de cada decisión, no solo el qué.
