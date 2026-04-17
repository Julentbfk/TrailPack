package com.julen.trailpack.vistas.mapa

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
            viewModel.isBottomSheetVisible = false
            viewModel.parqueSeleccionado = null
            viewModel.rutaSeleccionada = null
        }
    ) {
        // Mostramos la card del parque con sus rutas
        Box(modifier = Modifier.fillMaxWidth()){
            MapaParqueNaturalCard(
                parque = viewModel.parqueSeleccionado!!,
                rutas = viewModel.rutasparquenatural,
                onRutaClick = { ruta ->
                    //Cerramos el sheet
                    viewModel.isBottomSheetVisible = false
                    //Navegamos
                    navToRutaDetalladaMapa(ruta.idruta)
                },
                onPublicarClick = { ruta ->
                    //Cerramos el BottomSheet poniendo el parque a null
                    // Esto libera la pantalla para que el Popup tome el foco
                    viewModel.isBottomSheetVisible = false

                    //Abrimos el popup de publicación
                    viewModel.togglePopupPublicacion(true, ruta)
                }
            )
            FloatingActionButton(
                onClick = {
                    viewModel.isBottomSheetVisible = false
                    navToCrearRuta()
                },
                modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Crear ruta")
            }
        }

    }//Cierra el bottom sheet

}
