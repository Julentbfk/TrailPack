# [TASKS — TrailPack]

---

## En curso

_(ninguna)_

---

## Pendientes — Fase 10 (Mapa Real) — EN CURSO

- [x] **Hito 1 — Parques Nacionales de España:** 16 parques nacionales ingestados desde Wikidata via `scripts/ingesta_parques.py`. IDs legibles normalizados (`teide`, `donana`…), `region` con comunidad autónoma, foto Wikimedia (thumburl via Commons API), coordenadas y superficie. Fotos cargan con `ImageLoader` personalizado (User-Agent Wikimedia). `contorno` pendiente de enriquecimiento con OSM.
- [x] **Hito 2 — Rutas default por parque:** `scripts/ingesta_rutas.py` con Overpass API. Reintentos con backoff (10/30/60s), filtro ≤40km, sleep 8s entre parques. `nombreCreador: "TrailPack"`. Thumbnail en `MapaRutaCard` con `GoogleMap` lite mode + `Polyline` cuando `fotosRuta` está vacío. Overlay transparente para bloquear apertura de Google Maps al tocar.
- [x] **Hito 3 — Trazado en mapa:** `GoogleMap` interactivo (250dp) en `VistaRutaDetalladaMapa` reemplaza `AsyncImage` cuando no hay foto. `Polyline` verde, bounds automáticos con padding 32, scroll gestures desactivados para compatibilidad con `Column`. `MapType.TERRAIN` activado.
- [x] **Hito 4 — Creación de rutas por usuarios:** `VistaCrearRuta` con `GoogleMap` TERRAIN (60%) + formulario (40%). `mutableStateListOf<LatLng>` para waypoints en vivo. Haversine para distancia acumulada. `CrearRutaFormModel` con nombre, dificultad (dropdown), descripción. `repoGuardarRuta` con `document(id).set()` + invalidación de caché. `isBottomSheetVisible` desacoplado de `parqueSeleccionado`. Bug fix: colección `ruta` → `rutas`. Reglas Firestore actualizadas a `request.auth != null`.
- [x] **Hito 5 — Filtrado del listado:** Secciones desplegables "Rutas oficiales" y "Creadas por la comunidad" en `MapaParqueNaturalCard`. FAB `+` fixed en `ListaRutasParqueBottomSheet`. `ThumbnailRuta` extraída como composable público y reutilizada en `CardActividad` y `VistaDetalleActividad`.
- [ ] **Hito 6 — Estilo de mapa personalizado:** JSON de estilo oscuro/topográfico tipo Death Stranding via `MapStyleOptions`. Polyline estilizada (neón, caps). Último hito del bloque.

---

## Pendientes — Fase 11 (Flujo MVP Completo)

- [x] **Publicar actividades:** Flujo completo restaurado. Bugs resueltos: form no reseteaba tras cerrar popup, no navegaba atrás tras publicar (callback `onPublicado`), foto de card y detalle vacía (`ThumbnailRuta` + mapa polyline), foto de perfil no se actualizaba sin reiniciar app (`cargarUsuarioGlobal` tras `subirFotoPerfil`).
- [ ] **Hito 4 (social) — Visitar perfiles:** Ver el perfil público de otro usuario desde una actividad o ruta. Foto, nivel, actividades publicadas.
- [ ] **Hito 5 (social) — Amigos / Seguidores:** Botón Seguir/Dejar de seguir. `listasiguiendo` y `listaseguidores` ya existen en el modelo `Usuario`.
- [ ] **Hito 6 (social) — Feed prioritario:** Sección "De tus amigos" en el desplegable de actividades publicadas. Filtra por `listaparticipantesIds` o `idcreador` de usuarios seguidos.

---

## Pendientes — Fase 12 (Estabilidad y Lanzamiento APK)

- [ ] **Crashlytics:** Integrar Firebase Crashlytics para captura de crashes en dispositivos de testers.
- [ ] **Reglas de seguridad Firestore:** Restringir lectura/escritura por `uid`. Ningún dato accesible sin autenticación.
- [ ] **Validación de formularios:** Revisar campos críticos (nombre de ruta, fecha de actividad, perfil) — ninguno enviable vacío.
- [ ] **Estados de carga y error:** Feedback visual cuando Firestore tarda o falla. Evitar pantallas en blanco.
- [ ] **Placeholder foto de perfil:** Imagen por defecto cuando el usuario no tiene foto subida.
- [ ] **Documento de casos de prueba manual:** 10-15 flujos críticos en `meta/` para guiar a los testers.

---

## Pendientes — Prioridad Baja (Post-MVP)

- [ ] **Perfil de elevación:** Gráfica de altitud vs distancia bajo el mapa en `VistaRutaDetalladaMapa`. Usa `altitud` de `List<Coordenada>`.
- [ ] **Cálculo automático de dificultad:** Enriquecer coordenadas con elevación real (Google Elevation API o Open-Topo-Data). Calcular dificultad desde distancia + D+ + pendiente máxima + altitud máxima. Eliminar campo manual del formulario.
- [ ] **Mapa 3D:** Migración de Google Maps a Mapbox o equivalente para terreno tridimensional con polyline siguiendo el relieve.
- [ ] **Notificaciones push:** Firebase Cloud Messaging para alertas de nuevas actividades.
- [ ] **Google Places API:** Búsqueda de puntos de encuentro en el formulario de publicación.
- [ ] **Skill `/refactor-ui`:** Auditoría Jetpack Compose (recomposiciones, hoist de estado, keys en LazyColumn). Solo cuando el proyecto esté estable.

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
- [x] **Tab de actividades con 4 secciones desplegables:** `SeccionDesplegableActividades`, filtros en `ActividadesViewModel`, paginación de 10 en 10, `CardActividad` con estado `caducada`.
- [x] **Perfil social (Fase 9):** `VistaPerfilUsuario` con tabs Creadas/Participadas/Favoritas, cards de actividades, navegación al detalle con `mostrarAcciones`.
- [x] **Bug fix:** Botón SALIR oculto para el creador en `VistaDetalleActividad`.
- [x] **Bug fix:** Actividades caducadas excluidas de "Creadas por ti" en el feed.
- [x] **Favoritas de rutas (Fase 9):** `rutasFavoritas: List<String>` en `Usuario`. Toggle en `MainViewModel`. Botón con corazón animado en `VistaRutaDetalladaMapa`. Tab Favoritas en perfil con `MapaRutaCard`. Navegación al detalle con fallback de carga desde Firestore cuando la ruta no está en caché.
- [x] **Caché local de rutas:** `companion object` con `HashMap`. Métodos de invalidación por parque, por IDs y completa.
- [x] **FILE_TREE actualizado:** `VistaActividades.kt`, `Coordenada.kt`, `FaunaFlora.kt`, `PopUpEditarActividad.kt`, `SeccionDesplegableActividades.kt`.
