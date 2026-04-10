package com.julen.trailpack.vistas.componentes.mapa

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.julen.trailpack.vistas.mapa.MapaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaRutasParqueBottomSheet(viewModel: MapaViewModel) {

    ModalBottomSheet(
        onDismissRequest = {
            if (viewModel.rutaSeleccionada != null) {
                viewModel.rutaSeleccionada = null
            } else {
                viewModel.parqueSeleccionado = null
            }
        }
    ) {
        //PINTAREMOS AQUI LA CARD DEL PARQUE NATURAL QUE TOQUE
        MapaParqueNaturalCard(
            parque = viewModel.parqueSeleccionado!!,
            rutas = viewModel.rutasparquenatural,
            onRutaClick =  {

            }
        )
    }
}
