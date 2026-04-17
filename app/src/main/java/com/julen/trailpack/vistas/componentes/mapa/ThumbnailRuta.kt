package com.julen.trailpack.vistas.componentes.mapa

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMapOptions
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
fun ThumbnailRuta(ruta: Ruta) {
    val modifier = Modifier.size(100.dp).clip(RoundedCornerShape(8.dp))

    when{
        ruta.fotosRuta.isNotEmpty() -> {
            AsyncImage(
                model = ruta.fotosRuta.first(),
                contentDescription = null,
                modifier = modifier,
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
            val cameraPositionState = rememberCameraPositionState ()

            Box(modifier = modifier){
                GoogleMap(
                    modifier = Modifier.matchParentSize(),
                    cameraPositionState = cameraPositionState,
                    properties = MapProperties(mapType = MapType.TERRAIN),
                    googleMapOptionsFactory = { GoogleMapOptions().liteMode(true)},
                    uiSettings = MapUiSettings (
                        zoomControlsEnabled = false,
                        scrollGesturesEnabled = false,
                        zoomGesturesEnabled = false,
                        tiltGesturesEnabled = false,
                        rotationGesturesEnabled = false,
                        mapToolbarEnabled = false,
                        compassEnabled = false,
                        myLocationButtonEnabled = false
                    ),
                    onMapLoaded = {
                        cameraPositionState.move(CameraUpdateFactory.newLatLngBounds(bounds,8))
                    }
                ){
                    Polyline(
                        points = latLngs,
                        color = Color(0xFF4CAF50),
                        width = 5F
                    )
                }
                Box(modifier = Modifier.matchParentSize().clickable { })
            }


        }
        else -> {
            Box(
                modifier = modifier.background(MaterialTheme.colorScheme.surfaceVariant)
            )
        }
    }
}