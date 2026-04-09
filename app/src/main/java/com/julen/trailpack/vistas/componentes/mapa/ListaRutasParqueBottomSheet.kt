package com.julen.trailpack.vistas.componentes.mapa

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.julen.trailpack.vistas.mapa.MapaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaRutasParqueBottomSheet(viewModel: MapaViewModel) {
    ModalBottomSheet(
        onDismissRequest = {viewModel.parqueSeleccionado = null}
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = viewModel.parqueSeleccionado?.nombre ?: "", style = MaterialTheme.typography.titleMedium)

            if (viewModel.isLoadingRutas) {
                CircularProgressIndicator()
            } else {
                //Componente Parque natural con rutas
                viewModel.rutasparquenatural.forEach { ruta ->
                    Text(text = ruta.nombre)
                }
            }
        }
    }
}