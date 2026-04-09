package com.julen.trailpack.vistas.mapa

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.julen.trailpack.vistas.componentes.mapa.ListaRutasParqueBottomSheet
import com.julen.trailpack.vistas.componentes.mapa.MapaContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VistaMapa () {

    val viewModel: MapaViewModel = viewModel()
    val parqueSeleccionado = viewModel.parqueSeleccionado

    //Configuracion inicial de la camara en el mapa
    val camaraPositionState = rememberSaveable(saver = CameraPositionState.Saver) {
        CameraPositionState(
            position = CameraPosition.fromLatLngZoom(LatLng(40.4637, -3.7492), 5f)
        )
    }
    Box(modifier = Modifier.fillMaxSize()) {

        MapaContent(viewModel, camaraPositionState)

        //El Sheet solo se instancia cuando se necesita, pero al estar en un Box no afecta al Mapa.
        if (viewModel.parqueSeleccionado != null) {
            ListaRutasParqueBottomSheet(viewModel)
        }
    }

}


@Preview
@Composable
fun VistaMapaPreview (){
    VistaMapa ()
}