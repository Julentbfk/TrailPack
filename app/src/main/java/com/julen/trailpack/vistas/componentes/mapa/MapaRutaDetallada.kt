package com.julen.trailpack.vistas.componentes.mapa

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.julen.trailpack.modelos.Ruta

@Composable
fun MapaRutaDetallada(ruta: Ruta) {

    when {
        ruta.fotosRuta.isNotEmpty() -> {
            AsyncImage(
                model = ruta.fotosRuta.first(),
                contentDescription = "foto de la ruta",
                modifier = Modifier.fillMaxWidth().height(250.dp),
                contentScale = ContentScale.Crop
            )
        }
        ruta.coordenadas.isNotEmpty() -> {
            val latLngs = remember(ruta.idruta) {
                ruta.coordenadas.map { LatLng(it.lat, it.lng) }
            }
            val bounds = remember(ruta.idruta) {
                LatLngBounds.Builder().apply { latLngs.forEach { include(it) } }.build()
            }
            val cameraPositionState = rememberCameraPositionState()

            GoogleMap(
                modifier = Modifier.fillMaxWidth().height(250.dp),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(mapType = MapType.TERRAIN),
                uiSettings = MapUiSettings(
                    scrollGesturesEnabled = false,
                    zoomControlsEnabled = false,
                    mapToolbarEnabled = false,
                    compassEnabled = false,
                    myLocationButtonEnabled = false
                ),
                onMapLoaded = {
                    cameraPositionState.move(CameraUpdateFactory.newLatLngBounds(bounds, 32))
                }
            ) {
                Polyline(
                    points = latLngs,
                    color = Color(0xFF4CAF50),
                    width = 8f
                )
            }
        }
        else -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )
        }
    }
}