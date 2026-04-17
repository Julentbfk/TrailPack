package com.julen.trailpack.vistas.componentes.mapa

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.julen.trailpack.vistas.mapa.MapaViewModel

@Composable
fun MapaContent(viewModel: MapaViewModel,camaraPositionState: CameraPositionState){

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = camaraPositionState,
        properties = MapProperties(mapType = MapType.TERRAIN)
    ) {

        viewModel.parquesnaturales.forEach { parqueNatural ->
            key(parqueNatural.idparquenatural) {
                Marker(
                    state = MarkerState(
                        position = LatLng(
                            parqueNatural.lat,
                            parqueNatural.lng
                        )
                    ),
                    title = parqueNatural.nombre,
                    snippet = parqueNatural.descripcion,
                    onClick = {
                        viewModel.seleccionarParque(parqueNatural)
                        true
                    }
                )//Cierra el Marker
            }
        }//Cierra el foreach

    }//Cierra googleMap

}