package com.julen.trailpack.vistas.componentes.formulario

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectorHoraMejorado(
    horaTexto: String,
    onHoraSeleccionada: (Int,Int) -> Unit
) {
    var showTimePicker by remember { mutableStateOf(false) }
    val state = rememberTimePickerState()

    OutlinedTextFieldMejorado(
        value = horaTexto,
        onValueChange = {},
        label = "Hora de salida",
        readOnly = true,
        icon = Icons.Default.AccessTime,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { showTimePicker = true }
    )

    if (showTimePicker) {
        // Usamos un Dialog genérico de Compose para envolver el TimePicker
        Dialog(onDismissRequest = { showTimePicker = false }) {
            Surface(
                shape = MaterialTheme.shapes.extraLarge,
                tonalElevation = 6.dp,
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Selecciona la hora",
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(bottom = 20.dp)
                    )

                    TimePicker(state = state)

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.End
                    ) {
                        TextButton(onClick = {
                            onHoraSeleccionada(state.hour, state.minute)
                            showTimePicker = false
                        }) {
                            Text("Aceptar")
                        }
                        TextButton(onClick = { showTimePicker = false }) {
                            Text("Cancelar")
                        }
                    }
                }
            }
        }
    }
}