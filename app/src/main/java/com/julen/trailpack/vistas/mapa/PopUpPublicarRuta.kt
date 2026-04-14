package com.julen.trailpack.vistas.mapa

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.julen.trailpack.vistas.componentes.formulario.OutlinedTextFieldMejorado
import com.julen.trailpack.vistas.componentes.formulario.SelectorFechaMejorado
import com.julen.trailpack.vistas.componentes.formulario.SelectorHoraMejorado
import com.julen.trailpack.vistas.marcogeneral.MainViewModel

@Composable
fun PopUpPublicarRuta(viewModel: MapaViewModel, mainviewModel: MainViewModel) {
    
    // Si no debe ser visible, no renderizamos nada
    if (!viewModel.isPublicacionPopupVisible) return

    // Utilizamos Dialog para que se pinte en una capa superior a cualquier vista/scaffold
    Dialog(
        onDismissRequest = { viewModel.togglePopupPublicacion(false) },
        properties = DialogProperties(usePlatformDefaultWidth = false) // Permite controlar el ancho
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.9f) // Ocupa el 90% del ancho de la pantalla
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Opciones de publicación",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Fecha de salida
                SelectorFechaMejorado(
                    fechaTexto = viewModel.formPublicacion.fecha,
                    onFechaSeleccionada = { millis ->
                        val fechaFormateada = mainviewModel.formatearFecha(millis)
                        viewModel.updateFormPublicacion(
                            viewModel.formPublicacion.copy(
                                fecha = fechaFormateada,
                                fechaenmillis = millis
                            )
                        )
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Hora de salida
                SelectorHoraMejorado(
                    horaTexto = viewModel.formPublicacion.horasalida,
                    onHoraSeleccionada = { h, m ->
                        //Formateamos para la UI (HH:mm)
                        val horaFormateada = String.format("%02d:%02d", h, m)

                        val millisTotales = mainviewModel.combinarFechaYHora(
                            viewModel.formPublicacion.fechaenmillis, h, m
                        )

                        viewModel.updateFormPublicacion(
                            viewModel.formPublicacion.copy(
                                horasalida = horaFormateada,
                                horasalidaenmillis = millisTotales
                            )
                        )
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Campo de Participantes
                OutlinedTextFieldMejorado(
                    value = viewModel.formPublicacion.numparticipantes,
                    onValueChange = { participantes ->
                        viewModel.updateFormPublicacion(viewModel.formPublicacion.copy(numparticipantes = participantes))
                    },
                    label = "Máximo de participantes",
                    icon = Icons.Default.Person
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Punto de encuentro
                OutlinedTextFieldMejorado(
                    value = viewModel.formPublicacion.puntoencuentro,
                    onValueChange = { punto ->
                        viewModel.updateFormPublicacion(viewModel.formPublicacion.copy(puntoencuentro = punto))
                    },
                    label = "Punto de encuentro",
                    icon = Icons.Default.LocationOn
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Botón Publicar
                Button(
                    onClick = {
                        viewModel.publicarRuta(mainviewModel)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Publicar Actividad")
                }
                
                // Botón Cancelar (Opcional pero recomendado en Diálogos)
                androidx.compose.material3.TextButton(
                    onClick = { viewModel.togglePopupPublicacion(false) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cancelar", color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}
