package com.julen.trailpack.vistas.actividadespublicadas

import androidx.compose.animation.core.copy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.julen.trailpack.vistas.componentes.formulario.OutlinedTextFieldMejorado
import com.julen.trailpack.vistas.componentes.formulario.SelectorFechaMejorado
import com.julen.trailpack.vistas.componentes.formulario.SelectorHoraMejorado
import com.julen.trailpack.vistas.marcogeneral.MainViewModel

@Composable
fun PopUpEditarActividad (viewModel: DetalleActividadViewModel, mainviewModel: MainViewModel, actividadId: String) {

    if (!viewModel.isEditPopUpVisible) return

    Dialog(onDismissRequest = { viewModel.isEditPopUpVisible = false }) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Editar Actividad", style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(16.dp))

                // FECHA
                SelectorFechaMejorado(
                    fechaTexto = viewModel.editForm.fecha,
                    onFechaSeleccionada = { millis ->
                        viewModel.editForm =
                            viewModel.editForm.copy(
                            fecha = mainviewModel.formatearFecha(millis),
                            fechaenmillis = millis
                        )
                    }
                )

                // HORA
                SelectorHoraMejorado(
                    horaTexto = viewModel.editForm.horasalida,
                    onHoraSeleccionada = { h, m ->
                        val horaStr = String.format("%02d:%02d", h, m)
                        val millis = mainviewModel.combinarFechaYHora(viewModel.editForm.fechaenmillis, h, m)
                        viewModel.editForm =
                            viewModel.editForm.copy(horasalida = horaStr, horasalidaenmillis = millis)
                    }
                )

                // PUNTO ENCUENTRO
                OutlinedTextFieldMejorado(
                    value = viewModel.editForm.puntoencuentro,
                    onValueChange = { viewModel.editForm =
                       viewModel.editForm.copy(puntoencuentro = it) },
                    label = "Punto de encuentro",
                    icon = Icons.Default.Place
                )

                Spacer(Modifier.height(24.dp))

                Button(
                    onClick = { viewModel.actualizarActividad(actividadId) },
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Guardar Cambios") }
            }
        }
    }

}