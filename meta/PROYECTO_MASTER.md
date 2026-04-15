# [PROYECTO_MASTER: TrailPack — Arquitectura y Metodología]

> Fuente de verdad del proyecto. Cualquier cambio en la arquitectura debe reflejarse aquí.

---

## 1. Stack Tecnológico

| Capa | Tecnología |
|---|---|
| **UI** | Jetpack Compose (Material3) |
| **Arquitectura** | MVVM |
| **Backend** | Firebase (Auth, Firestore, Storage) |
| **Mapas** | Google Maps + maps-compose |
| **Navegación** | NavHost centralizado + Scaffold con BottomBar |
| **Gestión de Estado** | `mutableStateOf` en ViewModels · `MainViewModel` para estado global |

---

## 2. Metodología de Desarrollo

- **Interacción:** El usuario se comunica exclusivamente con el Orquestador.
- **Protocolo de edición (inviolable):** El Orquestador propone código en texto. El usuario es el único que aplica cambios al código fuente.
- **Fuente de verdad del código:** Los archivos `.kt` del proyecto. No documentos de contexto.
- **Protocolo de sesión:** Ver `CLAUDE.md` (raíz del proyecto).
- **Definición de agentes:** Ver `meta/AGENTES.md`.

---

## 3. Estructura de Datos (Firestore)

- Colecciones planas: `actividades`, `rutas`, `parques`, `usuarios`.
- Sin anidamiento entre colecciones para permitir búsquedas globales.
- Los "joins" entre colecciones se realizan en el cliente, en el ViewModel, usando `whereIn` por IDs.

---

## 4. Patrones de Diseño Estándar

| Patrón | Implementación |
|---|---|
| **Formularios** | `FormModel` inmutable + `mutableStateOf` en ViewModel |
| **Notificaciones** | `mainViewModel.showNotification(msg)` observado en `AppNavegation` |
| **Repositorios** | Prefijo `repo` en funciones públicas. Sin lógica de negocio. |
| **Diálogos/Popups** | Componentes `Dialog` de Compose, no `AlertDialog` básico para layouts custom |
| **Callbacks de nav** | Inyectados desde `AppNavegation`, no creados dentro de Composables |

---

## 5. Evolución del Proyecto (Fases)

| Fase | Descripción | Estado |
|---|---|---|
| 1–5 | Auth, Perfil, Seguridad base, Cambio de contraseña | Completado |
| 6 | Mapas reactivos, rutas por parque, publicación de actividades | Completado |
| 7 | Feed social, CardActividad, DetalleActividad, lógica Unirse/Salir | Completado |
| 8 | Gestión de actividades: edición, borrado, selector de hora, permisos por creador | Completado |
| 9 | Perfil social: actividades creadas/unidas. Caché local de rutas. | Pendiente |
| 10 | Notificaciones push (FCM). Google Places API. | Pendiente |
