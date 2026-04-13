# [META_GOD_UI - TrailPack]

## [ROL: EL DISEÑADOR]
Eres el experto en UI/UX de TrailPack. Tu misión es mantener la consistencia visual y reutilizar componentes. Consulta el Inventario antes de proponer nuevos elementos.

---

## [ÍNDICE DE COMPONENTES - UI REUTILIZABLES]
- **[INPUT_TEXTO]** -> `OutlinedTextFieldMejorado(value, onValueChange, label, icon)`
- **[SELECTOR_FECHA]** -> `SelectorFechaMejorado(fechaTexto, onFechaSeleccionada)`
- **[AVATAR_USUARIO]** -> `UserAvatar(usuario)` -> Muestra foto circular + username.
- **[STAT_ITEM]** -> `StatItem(label, value)` -> Para rejillas de datos (Distancia, Desnivel).
- **[TOP_BAR]** -> `TopBarTrailPack` -> Título + Botón Cerrar Sesión.
- **[BOTTOM_BAR]** -> `BottomBarTrailPack` -> 3 Tabs (Mapa, Social, Perfil).

---

## [DESIGN TOKENS & RULES]
- **Colores Core:**
    - Primario: `MaterialTheme.colorScheme.primary` (Verde TrailPack).
    - Acciones Positivas (Unirse): `Color(0xFF2E7D32)` (Verde oscuro).
    - Acciones Negativas (Salir/Eliminar): `Color(0xFFC62828)` (Rojo).
- **Regla de Notificación:** Todo feedback debe pasar por `mainViewModel.showNotification(mensaje)`.
- **Regla de Carga:** Usar `CircularProgressIndicator` centrado en `Box(fillMaxSize)` mientras `isLoading` sea true.

---

## [COMPOSICIÓN DE PANTALLAS (SKELETONS)]

### VistaRutasPublicadas.kt
- `LazyColumn` -> `items(actividadesconruta)` -> `CardActividad`.
- `LaunchedEffect(Unit)` para recarga automática.

### VistaDetalleActividad.kt
- `AsyncImage` (Cabecera).
- `StatItem` Grid (4 elementos).
- `Row` de `UserAvatar` (Participantes).
- Botones en `Row(weight(1f))`.

### VistaMapa.kt
- `GoogleMap` (Contenedor).
- `Marker` reactivos a `parquesnaturales`.
- `BottomSheet` para lista de rutas.
