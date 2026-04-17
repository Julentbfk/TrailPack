package com.julen.trailpack.vistas.mapa

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material.icons.filled.Route
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.julen.trailpack.vistas.componentes.formulario.OutlinedTextFieldMejorado
import com.julen.trailpack.vistas.marcogeneral.MainViewModel

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VistaCrearRuta( mapaviewModel: MapaViewModel, mainviewModel: MainViewModel, onBack: () -> Unit ) {

    BackHandler() {
        mapaviewModel.limpiarCreacionRuta()
        onBack()
    }

    val parque = mapaviewModel.parqueSeleccionado
    val centroInicial = if(parque != null) {
        LatLng(parque.lat, parque.lng)
    }else{
        LatLng(40.0,-3.7)//Centro de España como fallback
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(centroInicial,12f)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxWidth().weight(0.6f),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(mapType = MapType.TERRAIN),
            uiSettings = MapUiSettings(
                zoomControlsEnabled = true,
                mapToolbarEnabled = false,
                myLocationButtonEnabled = false
            ),
            onMapClick = {latlng ->
                mapaviewModel.agregarWaypoint(latlng)
            }
        ) {
            //marcador en cada waypoint
            mapaviewModel.waypointsRuta.forEachIndexed { index, punto ->
                Marker(
                    state = MarkerState(position = punto),
                    title = "P.${index}"
                )
            }
            //Trazado en vivo
            if(mapaviewModel.waypointsRuta.size >1) {
                Polyline(
                    points = mapaviewModel.waypointsRuta.toList(),
                    color = Color(0xFF4CAF50),
                    width = 8f
                )
            }

        }//Cierra el googlemap

        Column(
            modifier = Modifier.fillMaxWidth().weight(0.4f).verticalScroll(rememberScrollState()).padding(16.dp)
        ) {
            Text(
                text = "Distancia: ${mapaviewModel.distanciaRuta}km - ${mapaviewModel.waypointsRuta.size} puntos",
                style = MaterialTheme.typography.labelMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextFieldMejorado(
                value = mapaviewModel.formCrearRuta.nombre,
                onValueChange = {
                    mapaviewModel.updateFormCrearRuta(mapaviewModel.formCrearRuta.copy(nombre = it))
                },
                label = "Nombre de la ruta",
                icon = Icons.Default.Route,
                validador = { if (it.isBlank()) "El nombre no puede estar vacío" else "" }
            )

            Spacer(modifier = Modifier.height(8.dp))

            val opciones = listOf("Fácil", "Moderada", "Difícil", "LETAL")
            var expandido by remember { mutableStateOf(false) }

            ExposedDropdownMenuBox(
                expanded = expandido,
                onExpandedChange = {expandido = !expandido}
            ) {
                OutlinedTextField(
                    value = mapaviewModel.formCrearRuta.dificultad,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Dificultad") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandido) },
                    modifier = Modifier.fillMaxWidth().menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expandido,
                    onDismissRequest = { expandido = false }
                ) {
                    opciones.forEach { opcion ->
                        DropdownMenuItem(
                            text = { Text(opcion) },
                            onClick = {
                                mapaviewModel.updateFormCrearRuta(
                                    mapaviewModel.formCrearRuta.copy(dificultad = opcion)
                                )
                                expandido = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextFieldMejorado(
                value = mapaviewModel.formCrearRuta.descripcion,
                onValueChange = {
                    mapaviewModel.updateFormCrearRuta(mapaviewModel.formCrearRuta.copy(descripcion = it))
                },
                label = "Descripción",
                icon = Icons.Default.Notes
            )

            Spacer(modifier = Modifier.height(16.dp))

            //Botones de accion
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ){

                OutlinedButton(
                    onClick = { mapaviewModel.deshacerUltimoWaypoint() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Deshacer")
                }
                Button(
                    onClick = { mapaviewModel.guardarRuta(mainviewModel, onBack) },
                    enabled = mapaviewModel.formCrearRuta.nombre.isNotBlank()
                            && mapaviewModel.waypointsRuta.size >= 2,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Guardar ruta")
                }

            }

        }//Cierra la column del formulario

    }//Cierra la column principal



}