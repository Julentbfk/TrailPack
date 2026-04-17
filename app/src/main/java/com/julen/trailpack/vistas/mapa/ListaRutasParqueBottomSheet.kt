package com.julen.trailpack.vistas.mapa

import android.util.Log
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import com.julen.trailpack.vistas.componentes.mapa.MapaParqueNaturalCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaRutasParqueBottomSheet(
    viewModel: MapaViewModel,
    navToRutaDetalladaMapa: (String) -> Unit,
    navToCrearRuta: () -> Unit
) {

    ModalBottomSheet(
        onDismissRequest = {
            Log.d("DEBUG_UI", "BottomSheet cerrándose")
            viewModel.parqueSeleccionado = null
            viewModel.rutaSeleccionada = null
        }
    ) {
        // Mostramos la card del parque con sus rutas
        MapaParqueNaturalCard(
            parque = viewModel.parqueSeleccionado!!,
            rutas = viewModel.rutasparquenatural,
            onRutaClick = { ruta ->
                //Cerramos el sheet
                viewModel.parqueSeleccionado = null
                //Navegamos
                navToRutaDetalladaMapa(ruta.idruta)
            },
            onPublicarClick = { ruta ->
                //Cerramos el BottomSheet poniendo el parque a null
                // Esto libera la pantalla para que el Popup tome el foco
                viewModel.parqueSeleccionado = null
                
                //Abrimos el popup de publicación
                viewModel.togglePopupPublicacion(true, ruta)
            },
            onCrearRutaClick = {
                viewModel.parqueSeleccionado = null //Cierra el bottomsheet
                navToCrearRuta()
            }
        )
    }
}
