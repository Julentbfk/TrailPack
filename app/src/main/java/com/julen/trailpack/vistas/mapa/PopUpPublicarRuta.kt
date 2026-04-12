package com.julen.trailpack.vistas.mapa

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.julen.trailpack.vistas.componentes.formulario.OutlinedTextFieldMejorado
import com.julen.trailpack.vistas.componentes.formulario.SelectorFechaMejorado
import com.julen.trailpack.vistas.marcogeneral.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PopUpPublicarRuta (viewModel: MapaViewModel, mainviewModel: MainViewModel) {


    if(viewModel.isPublicacionPopupVisible) {

        AlertDialog(
            onDismissRequest = {viewModel.togglePopupPublicacion(false)},
            title = { Text("Opciones de publicacion") },
            text = {
                Column() {
                    //Fecha de salida
                    SelectorFechaMejorado(
                        fechaTexto = viewModel.formPublicacion.fecha,
                        onFechaSeleccionada = { millis ->
                            val fechaFormateada = mainviewModel.formatearFecha(millis)
                            //Actualizo modelo
                            viewModel.updateFormPublicacion(
                                viewModel.formPublicacion.copy(
                                    fecha = fechaFormateada,
                                    fechenmillis = millis
                                )
                            )


                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // Hora de salida
                    OutlinedTextFieldMejorado(
                        value = viewModel.formPublicacion.hora,
                        onValueChange = { nuevoValor ->
                            viewModel.updateFormPublicacion(viewModel.formPublicacion.copy(hora = nuevoValor))
                        },
                        label = "Hora de salida",
                        icon = Icons.Default.Info // Cambia por icono de reloj si tienes
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // Campo de Participantes
                    OutlinedTextFieldMejorado(
                        value = viewModel.formPublicacion.numparticipantes,
                        onValueChange = { participantes ->
                            viewModel.updateFormPublicacion(viewModel.formPublicacion.copy(numparticipantes = participantes))
                        },
                        label = "Numero de participantes maximo",
                        icon = Icons.Default.Person
                    )

                }


           },confirmButton = {
                Button(onClick = {
                    Log.d("DEBUG_POPUP", "Antes de llamar a publicarRuta")
                    Log.d("DEBUG_POPUP", "Instancia VM: ${mainviewModel.usuarioGlobal}") // Esto nos dirá qué objeto es

                    viewModel.publicarRuta(mainviewModel)
                }) {
                    Text("Publicar")
                }
            }
        )


    }

}