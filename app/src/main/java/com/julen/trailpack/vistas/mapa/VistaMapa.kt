package com.julen.trailpack.vistas.mapa

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.julen.trailpack.routing.Enrutador
import com.julen.trailpack.vistas.componentes.mapa.MapaContent
import com.julen.trailpack.vistas.marcogeneral.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VistaMapa (enrutador: Enrutador, mainviewModel: MainViewModel, mapaviewModel: MapaViewModel) {


    LaunchedEffect(Unit) {
        mapaviewModel.seleccionarRutaParaDetalle(null)
    }

    //Configuracion inicial de la camara en el mapa
    val camaraPositionState = rememberSaveable(saver = CameraPositionState.Saver) {
        CameraPositionState(
            position = CameraPosition.fromLatLngZoom(LatLng(40.4637, -3.7492), 5f)
        )
    }
    Box(modifier = Modifier.fillMaxSize()) {

        MapaContent(mapaviewModel, camaraPositionState)

        //El Sheet solo se instancia cuando se necesita, pero al estar en un Box no afecta al Mapa.
        if (mapaviewModel.parqueSeleccionado != null) {
            ListaRutasParqueBottomSheet(
                viewModel = mapaviewModel,
                navToRutaDetalladaMapa = {rutaId ->
                    //Limpiamos ANTES de navegar
                    mapaviewModel.parqueSeleccionado = null
                    mapaviewModel.rutaSeleccionada = null
                    //NAVEGAMOS
                    enrutador.navToRutaDetalladaMapa(rutaId)
                }
            )
        }


    }

}
