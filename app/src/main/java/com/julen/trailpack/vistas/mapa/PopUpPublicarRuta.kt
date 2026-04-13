package com.julen.trailpack.vistas.mapa

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.julen.trailpack.vistas.componentes.formulario.OutlinedTextFieldMejorado
import com.julen.trailpack.vistas.componentes.formulario.SelectorFechaMejorado
import com.julen.trailpack.vistas.marcogeneral.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PopUpPublicarRuta(viewModel: MapaViewModel, mainviewModel: MainViewModel) {
    // Surface actúa como el contenedor blanco (o del color de tu tema) del popup
    Surface(
        modifier = Modifier
            .fillMaxWidth()
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
                icon = Icons.Default.Info
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Campo de Participantes
            OutlinedTextFieldMejorado(
                value = viewModel.formPublicacion.numparticipantes,
                onValueChange = { participantes ->
                    viewModel.updateFormPublicacion(viewModel.formPublicacion.copy(numparticipantes = participantes))
                },
                label = "Número de participantes máximo",
                icon = Icons.Default.Person
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botón Publicar
            Button(
                onClick = {
                    viewModel.publicarRuta(mainviewModel)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Publicar")
            }
        }
    }
}