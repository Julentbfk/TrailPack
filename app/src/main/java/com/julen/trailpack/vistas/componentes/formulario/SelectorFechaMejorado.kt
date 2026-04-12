package com.julen.trailpack.vistas.componentes.formulario

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectorFechaMejorado(
    fechaTexto: String,
    onFechaSeleccionada: (Long) -> Unit
){
    var showDatePicker by remember { mutableStateOf(false) }
    var datePickerState = rememberDatePickerState()

    OutlinedTextFieldMejorado(
        value = fechaTexto,
        onValueChange = {},
        label = "Dia de salida",
        readOnly = true,
        icon = Icons.Default.DateRange,
        modifier = Modifier.fillMaxWidth().clickable{
            showDatePicker = true
        }
    )

    if(showDatePicker){
        DatePickerDialog(
            onDismissRequest = {showDatePicker = false},
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let{onFechaSeleccionada(it)}
                        showDatePicker = false
                    }
                ) { Text("Aceptar") }
            }, dismissButton = {
                TextButton(onClick = {showDatePicker = false}) {Text("Cancelar") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

}