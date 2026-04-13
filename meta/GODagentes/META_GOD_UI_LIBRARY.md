# [GOD_FILE: UI_LIBRARY - TRAILPACK]
# ROL: EL ARTESANO DE COMPONENTES
# INSTRUCCIÓN: Eres el guardián de la consistencia visual. Conoces cada Composable reutilizable.
# REGLA: Tienes el código íntegro. Antes de proponer un UI, verifica si ya existe aquí.

---

## [1. FORMULARIO & INPUTS]

### OutlinedTextFieldMejorado.kt
```kotlin
@Composable
fun OutlinedTextFieldMejorado(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    readOnly: Boolean = false,
    icon: ImageVector,
    isPassword: Boolean = false,
    validador: (String) -> (String) = { "" }
) {
    var tocado by remember { mutableStateOf(false) }
    var mensajeError by remember(validador, value) { mutableStateOf("") }
    OutlinedTextField(
        value = value,
        onValueChange = {
            onValueChange(it)
            if (tocado) mensajeError = validador(it)
        },
        label = { Text(label) },
        readOnly = readOnly,
        leadingIcon = { Icon(imageVector = icon, contentDescription = null) },
        visualTransformation = if(isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        isError = mensajeError.isNotEmpty(),
        modifier = modifier.fillMaxWidth().onFocusChanged { 
            if(it.isFocused) tocado = true else if(tocado) mensajeError = validador(value)
        },
        supportingText = { if(mensajeError.isNotEmpty()) Text(text = mensajeError, color = MaterialTheme.colorScheme.error) }
    )
}
```

### SelectorFechaMejorado.kt
```kotlin
@Composable
fun SelectorFechaMejorado(fechaTexto: String, onFechaSeleccionada: (Long) -> Unit) {
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    OutlinedTextFieldMejorado(
        value = fechaTexto, onValueChange = {}, label = "Día de salida",
        readOnly = true, icon = Icons.Default.DateRange,
        modifier = Modifier.fillMaxWidth().clickable { showDatePicker = true }
    )
    if(showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = { TextButton(onClick = { datePickerState.selectedDateMillis?.let{onFechaSeleccionada(it)}; showDatePicker = false }) { Text("Aceptar") } }
        ) { DatePicker(state = datePickerState) }
    }
}
```

### SelectorPrefijoPais.kt (Firma)
```kotlin
@Composable
fun SelectorPrefijoPais(paises: List<Pais>, paisSeleccionado: Pais, onPaisSelected: (Pais) -> Unit)
```

---

## [2. COMPONENTES SOCIALES]

### CardActividad.kt (Firma & Lógica)
```kotlin
@Composable
fun CardActividad(actividadConRuta: ActividadConRuta, fechaFormateada: String, usuarioActualUid: String?, onUnirseClick: () -> Unit, onAbandonarClick: () -> Unit, onCardClick: () -> Unit)
// Lógica interna: val usuarioIn = usuarioActualUid != null && actividad.listaparticipantesIds.contains(usuarioActualUid)
```

### StatItem.kt
```kotlin
@Composable
fun StatItem(label: String, value: String) {
    Column {
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        Text(text = value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
    }
}
```

### UserAvatar.kt
```kotlin
@Composable
fun UserAvatar(usuario: Usuario) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        AsyncImage(model = usuario.fotoperfil, modifier = Modifier.size(40.dp).clip(CircleShape))
        Text(text = usuario.username ?: "", style = MaterialTheme.typography.labelSmall)
    }
}
```
