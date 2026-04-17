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

### Sesión 2026-04-15 — Tab de actividades con secciones y perfil social

- **`SeccionDesplegableActividades`:** Nuevo composable acordeón reutilizable con título, contador y flecha. Bug detectado y corregido en sesión: contenedor raíz era `Row` en lugar de `Column`, aplastando el contenido horizontalmente.
- **Clasificación de actividades:** 4 funciones filtradoras en `ActividadesViewModel` (`getActividadesPublicadas`, `getActividadesCreadas`, `getActividadesUnido`, `getActividadesCaducadas`) con lógica de caducidad basada en `horasalida` (Long combinado fecha+hora).
- **Paginación:** Sistema de `paginaX * 10` con botón "Ver más" por sección. `LazyColumn` exterior con `item{}` + `Column` interior con `forEach` para evitar nested lazy layouts.
- **`CardActividad` mejorada:** Parámetros `caducada` (gris + texto TERMINADA) y `mostrarBoton` (oculta botones en perfil). Bug corregido: orden del `when` — `!mostrarBoton` debe ir antes de `usuarioIn`.
- **Perfil social:** `VistaPerfilUsuario` refactorizada para recibir `mainViewModel` y `actividadesViewModel`. Eliminada carga duplicada del usuario (se usa `usuarioGlobal` de `mainViewModel`). `ScrollableTabRow` con categorías Creadas/Participadas/Favoritas sobre `LazyColumn` de cards.
- **Navegación con parámetro booleano:** `navToActividadDetallada(actividadId, mostrarAcciones)` en `Enrutador`. Ruta `detalleactividad/{actividadId}/{mostrarAcciones}` en `AppNavegation`. `VistaDetalleActividad` con `mostrarAcciones: Boolean` que oculta botones UNIRSE/SALIR/EDITAR/ELIMINAR desde el perfil.
- **Bug fix:** Botón SALIR visible para el creador en `VistaDetalleActividad` — corregido con `if (!esCreador && mostrarAcciones)`.
- **Debug caducada:** Actividades no aparecían en Caducadas por zona horaria incorrecta en emulador. Fix: comparación directa `horasalida < System.currentTimeMillis()`.
- **Refactor `PerfilUsuarioViewModel`:** Eliminados `usuarioState`, `obtenerDatosUsuario()`, `auth` e `init`. El ViewModel ahora solo gestiona foto de perfil y formularios. `VistaEditarPerfil` y `VistaPerfilUsuario` usan `mainViewModel.usuarioGlobal` como fuente de verdad del usuario.

### Sesión 2026-04-15 — Favoritas de rutas y cierre de Fase 9

- **Decisión de dominio:** Favoritas almacena rutas (`rutasFavoritas: List<String>` en `Usuario`), no actividades. Las actividades son eventos efímeros; las rutas son contenido permanente del catálogo.
- **Modelo:** `Usuario.rutasFavoritas` añadido como `List<String>` con default `emptyList()`. Compatible con documentos Firestore existentes sin migración.
- **`MainViewModel.toggleRutaFavorita(idRuta)`:** Toggle en Firestore vía `UserRepository.actualizarPerfil` (reutilizando función existente con `Map<String,Any>`). Actualización en memoria con `.copy()` — sin recarga de red.
- **`VistaRutaDetalladaMapa`:** Botón "Guardar" conectado. Estado visual: `OutlinedButton` con `Icons.Default.FavoriteBorder` cuando no guardada → `Button` con `errorContainer` color y `Icons.Default.Favorite` cuando guardada. Reactivo a `usuarioGlobal`.
- **`PerfilUsuarioViewModel.cargarRutasFavoritas(ids)`:** Carga objetos `Ruta` desde `MapsRepository.repoObtenerRutasPorId`. Disparado por `LaunchedEffect(user.rutasFavoritas)` en la vista — se actualiza solo al guardar/quitar.
- **`MapaRutaCard`:** `onPublicarClick` convertido a `(() -> Unit)? = null`. El botón "Publicar" solo se renderiza cuando se pasa el callback — no aparece en el perfil.
- **`VistaPerfilUsuario`:** Tab "Favoritas" separada del `when` de actividades — usa `List<Ruta>` con `MapaRutaCard`. Las tabs Creadas/Realizadas mantienen `CardActividad` sin cambios.
- **Navegación al detalle desde Favoritas:** `AppNavegation.detalleruta` ampliado con fallback: si la ruta no está en `rutasparquenatural` (caché del parque abierto), llama a `MapaViewModel.cargarRutaPorId` y muestra `CircularProgressIndicator` mientras carga.
- **Planificación Fase 10:** Definidos 5 hitos para el mapa real — parques desde IGN, rutas default desde OpenStreetMap (ingesta a Firestore), trazado de polyline, creación de rutas con waypoints, filtrado oficial/comunidad.

### Sesión 2026-04-16 — Caché de MapsRepository + Modelos escalables + Ingesta Parques (Fase 10, Hito 1)

- **Caché `MapsRepository`:** `companion object` (equivalente a `static` en JVM) con `HashMap` para rutas por parque, rutas por IDs y ruta individual. Caché de parques como `List?`. Patrón `cache[key]?.let { onResult(it, null); return }` en todos los métodos de lectura. Métodos de invalidación `limpiarCacheParques`, `limpiarCacheRutasParque`, `limpiarCacheRutas`, `limpiarCacheCompleta` preparados para Fase 10.
- **Modelo `ParqueNatural` ampliado:** Nuevos campos `pais`, `tipo`, `superficie: Double`, `contorno: List<GeoPoint>`. Compatible con documentos existentes (default values). Preparado para parques de todo el mundo.
- **Modelo `Ruta` ampliado:** Renombrado `desnivel` → `desnivelTotal`. Añadidos `pais`, `region`, `desnivelPositivo`, `desnivelNegativo`, `altitudMaxima`, `altitudMinima`, `esOficial`. Tipo `coordenadas` cambiado de `List<GeoPoint>` a `List<Coordenada>`. Usos actualizados en `CardActividad`, `MapaRutaCard`, `VistaDetalleActividad`.
- **Modelo `Coordenada` (nuevo):** `lat`, `lng`, `altitud: Double`. Permite perfiles de elevación y cálculo de D+/D-.
- **Modelo `FaunaFlora` (nuevo, desconectado):** Preparado para Fase 11. `tipo`, `nombre`, `nombreCientifico`, `descripcion`, `foto`, `enPeligroExtincion`.
- **Crash fix:** Documentos Firestore existentes tenían `coordenadas` como `List<GeoPoint>` — incompatible con `Coordenada`. Eliminados datos de prueba en colecciones `rutas` y `actividades`.
- **`scripts/ingesta_parques.py`:** Consulta SPARQL Wikidata con `wdt:P31/wdt:P279* wd:Q46169` (jerarquía de subclases) y `wdt:P131+ ?region . ?region wdt:P31 wd:Q10742` (comunidad autónoma). Deduplicación por Q-ID. `limpiar_nombre()` elimina prefijos "Parque nacional de". `normalizar_nombre()` genera IDs legibles (`teide`, `donana`). Borra colección antes de reingestar. 16 parques únicos cargados.
- **Fotos Wikimedia:** URL oficial vía `w/api.php?action=query&prop=imageinfo&iiprop=url&iiurlwidth=800` (descartada construcción manual de URL MD5 — Wikimedia la bloquea). Nombre de fichero con `unquote()` para manejar `%20`.
- **HTTP 403 Coil fix:** Wikimedia bloquea el User-Agent por defecto de OkHttp. Fix: `ImageLoader` personalizado en `MapaParqueNaturalCard` con interceptor OkHttp que añade `User-Agent: TrailPack/1.0 (Android; julen.tabuyo@gmail.com)`. Cambiado `AsyncImage` → `rememberAsyncImagePainter` para mejor control de estados.
- **Cleanup:** Eliminado `LaunchedEffect` de logging de errores Coil. Imports huérfanos eliminados.

### Sesión 2026-04-17 — Ingesta rutas OSM + Trazado Polyline (Fase 10, Hitos 2 y 3)

- **`scripts/ingesta_rutas.py`:** Lee parques de Firestore, consulta Overpass API (`relation[route=hiking]` en radio 15km). Reintentos con backoff exponencial (10/30/60s) ante 429/504. Filtro ≤40km (rutas más largas son grandes recorridos o etapas). Sleep 8s entre parques. Guarda en colección `rutas` con `esOficial: true`, `nombreCreador: "TrailPack"`, `coordenadas: List<{lat,lng,altitud}>`. ~34 rutas cargadas en primera ejecución real.
- **`MapaRutaCard` — thumbnail de trazado:** Nuevo composable privado `ThumbnailRuta` con lógica de tres casos: foto real → `AsyncImage`; sin foto + coordenadas → `GoogleMap` lite mode con `Polyline` verde; sin nada → `Box` neutro. `remember(ruta.idruta)` para evitar recálculo de bounds en recomposiciones. Overlay `Box` transparente para bloquear apertura de Google Maps al tocar el thumbnail en lite mode.
- **`VistaRutaDetalladaMapa` — mapa interactivo en detalle:** `AsyncImage` del header reemplazado por `when` de tres casos idéntico al thumbnail pero a tamaño completo (250dp, sin lite mode). `scrollGesturesEnabled = false` para compatibilidad con `Column` scrolleable. `onMapLoaded` con `newLatLngBounds(bounds, 32)` para encuadre automático del trazado.

### Fase 8: Gestión de Actividades y Refinamiento
- **Modelo de Datos:** Ampliación de `Actividad` con campos específicos para `horasalida` y `puntoencuentro`.
- **UI de Entrada:** Implementación de `SelectorHoraMejorado` con `TimePicker` nativo de Compose.
- **Gestión de Autoría:** Implementación de permisos basados en `idcreador` para habilitar funciones de edición y borrado.
- **Edición Atómica:** Creación de `PopUpEditarActividad` para realizar actualizaciones masivas de datos en Firestore mediante un `Map<String, Any>`.
- **Eliminación:** Implementación de borrado físico de actividades en Firestore con retorno seguro a la vista anterior mediante el `Enrutador`.
- **Arquitectura de Navegación:** Desacoplamiento de vistas mediante callbacks (`onBack`) inyectados desde el `AppNavigation`.
