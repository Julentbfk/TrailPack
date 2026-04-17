# [TASKS — TrailPack]

---

## En curso

_(ninguna)_

---

## Pendientes — Prioridad Alta (Fase 9)

- [x] **Caché local de rutas:** `companion object` con `HashMap` para rutas por parque, rutas por IDs y ruta individual. Caché de parques con `List?`. Métodos `limpiarCacheParques`, `limpiarCacheRutasParque`, `limpiarCacheRutas` y `limpiarCacheCompleta`.
- [x] **FILE_TREE actualizado:** `VistaRutasPublicadas.kt` renombrado a `VistaActividades.kt`. Añadidos `Coordenada.kt`, `FaunaFlora.kt`, `PopUpEditarActividad.kt`, `SeccionDesplegableActividades.kt`.
- [x] **Favoritas:** Implementar lógica de rutas favoritas (modelo, repositorio, filtro en ViewModel) para completar la tercera categoría del perfil.

---

## Pendientes — Fase 10 (Mapa Real)

- [x] **Hito 1 — Parques Nacionales de España:** 16 parques nacionales ingestados desde Wikidata via `scripts/ingesta_parques.py`. IDs legibles normalizados (`teide`, `donana`…), `region` con comunidad autónoma, foto Wikimedia (thumburl via Commons API), coordenadas y superficie. Fotos cargan con `ImageLoader` personalizado (User-Agent Wikimedia). `contorno` pendiente de enriquecimiento con OSM.
- [x] **Hito 2 — Rutas default por parque:** `scripts/ingesta_rutas.py` con Overpass API. Reintentos con backoff (10/30/60s), filtro ≤40km, sleep 8s entre parques. `nombreCreador: "TrailPack"`. Thumbnail en `MapaRutaCard` con `GoogleMap` lite mode + `Polyline` cuando `fotosRuta` está vacío. Overlay transparente para bloquear apertura de Google Maps al tocar.
- [x] **Hito 3 — Trazado en mapa:** `GoogleMap` interactivo (250dp) en `VistaRutaDetalladaMapa` reemplaza `AsyncImage` cuando no hay foto. `Polyline` verde, bounds automáticos con padding 32, scroll gestures desactivados para compatibilidad con `Column`.
- [ ] **Hito 4 — Creación de rutas por usuarios:** Botón "Crear ruta" en card del parque. Formulario + mapa interactivo con waypoints ilimitados. Cálculo automático de distancia desde `List<GeoPoint>`.
- [ ] **Hito 5 — Filtrado del listado:** Secciones desplegables "Rutas oficiales" y "Creadas por la comunidad" con paginación, al estilo del feed de actividades.

---

## Pendientes — Prioridad Baja (Futuro)

- [ ] **Skill `/refactor-ui`:** Crear skill de auditoría Jetpack Compose con mejores prácticas (recomposiciones, hoist de estado, keys en LazyColumn, tema). Activar solo cuando el proyecto esté estable y haya una fase de refactoring dedicada.
- [ ] **Perfil de elevación:** Gráfica de altitud vs distancia bajo el mapa en `VistaRutaDetalladaMapa`. Usa `altitud` de `List<Coordenada>`. Post-MVP.
- [ ] **Mapa 3D:** Migración de Google Maps a Mapbox o equivalente para terreno tridimensional con polyline siguiendo el relieve. Post-MVP.

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
- [x] **Tab de actividades con 4 secciones desplegables:** `SeccionDesplegableActividades`, filtros en `ActividadesViewModel`, paginación de 10 en 10, `CardActividad` con estado `caducada`.
- [x] **Perfil social (Fase 9):** `VistaPerfilUsuario` con tabs Creadas/Participadas/Favoritas, cards de actividades, navegación al detalle con `mostrarAcciones`.
- [x] **Bug fix:** Botón SALIR oculto para el creador en `VistaDetalleActividad`.
- [x] **Bug fix:** Actividades caducadas excluidas de "Creadas por ti" en el feed.
- [x] **Favoritas de rutas (Fase 9):** `rutasFavoritas: List<String>` en `Usuario`. Toggle en `MainViewModel`. Botón con corazón animado en `VistaRutaDetalladaMapa`. Tab Favoritas en perfil con `MapaRutaCard`. Navegación al detalle con fallback de carga desde Firestore cuando la ruta no está en caché.
