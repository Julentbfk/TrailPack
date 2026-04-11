package com.julen.trailpack.vistas.componentes.mapa

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.julen.trailpack.vistas.mapa.MapaViewModel

@Composable
fun PopUpPublicarRuta (viewModel: MapaViewModel) {
    if(viewModel.isPublicacionPopupVisible) {
        AlertDialog(
            onDismissRequest = {viewModel.togglePopupPublicacion(false)},
            title = { Text("Opciones de publicacion") },
            text = {
                Column() {
                    OutlinedTextField(
                        value = viewModel.formPublicacion.fecha,
                        onValueChange = {

                        },
                        label = { Text("Dia de salida")}
                    )
                    OutlinedTextField(
                        value = viewModel.formPublicacion.hora,
                        onValueChange = {

                        },
                        label = { Text("Hora de salida")}
                    )
                    OutlinedTextField(
                        value = viewModel.formPublicacion.numparticipantes,
                        onValueChange = {

                        },
                        label = { Text("Numero participantes max")}
                    )
                }
            },
            confirmButton = {
                Button(onClick = {/*Guardar en firebase la ruta publicada*/}) { Text("Publicar") }
            }
        )
    }
}