package com.julen.trailpack.vistas.mapa

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import com.julen.trailpack.vistas.componentes.mapa.MapaParqueNaturalCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaRutasParqueBottomSheet(viewModel: MapaViewModel, navToRutaDetalladaMapa: (String) -> Unit ) {

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
            onRutaClick = { ruta ->
                viewModel.parqueSeleccionado = null
                navToRutaDetalladaMapa(ruta.idruta)
            },
            onPublicarClick = { ruta ->
                viewModel.togglePopupPublicacion(true, ruta)
            }
        )
    }
}
