package com.julen.trailpack.vistas.actividadespublicadas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
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
import com.julen.trailpack.modelos.Usuario
import com.julen.trailpack.vistas.componentes.actividades.StatItem
import com.julen.trailpack.vistas.componentes.actividades.UserAvatar
import com.julen.trailpack.vistas.marcogeneral.MainViewModel

@Composable
fun VistaDetalleActividad(
    actividadId: String,
    mainviewModel: MainViewModel,
    mostrarAcciones: Boolean=true,
    onBack:()-> Unit,
    onCreadorClick: ((String) -> Unit) ? = null
) {

    val detalleviewModel: DetalleActividadViewModel = viewModel()

    // Cargamos los datos al iniciar
    LaunchedEffect(actividadId) {
        detalleviewModel.cargarDetalles(actividadId)
    }

    // El observador de notificaciones (Toast) se ha movido a AppNavegation para ser global.

    if (detalleviewModel.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {

        val datos = detalleviewModel.actividadconruta ?: return
        val ruta = datos.ruta
        val actividad = datos.actividad

        //POPUP DE EDICION
        PopUpEditarActividad(
            viewModel = detalleviewModel,
            mainviewModel = mainviewModel,
            actividadId = actividad.idactividad
        )
        //-----------------


        // Comprobamos si el usuario actual ya está unido
        val usuarioActualUid = mainviewModel.usuarioGlobal?.uid
        val estaUnido = usuarioActualUid != null && actividad.listaparticipantesIds.contains(usuarioActualUid)

        //Comprobamos si el usuario actual es el creador
        val esCreador  = usuarioActualUid != null && actividad.idcreador == usuarioActualUid


        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Imagen de cabecera (de la ruta)
            when {
                ruta?.fotosRuta?.isNotEmpty() == true -> {
                    AsyncImage(
                        model = ruta.fotosRuta.first(),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp),
                        contentScale = ContentScale.Crop
                    )
                }
                ruta?.coordenadas?.isNotEmpty() == true -> {
                    val latLngs = remember(ruta.idruta) {
                        ruta.coordenadas.map { LatLng(it.lat, it.lng) }
                    }
                    val bounds = remember(ruta.idruta) {
                        LatLngBounds.Builder().apply { latLngs.forEach { include(it) } }.build()
                    }
                    val cameraPositionState = rememberCameraPositionState()

                    GoogleMap(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp),
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
                        Polyline(points = latLngs, color = Color(0xFF4CAF50), width = 8f)
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

            Column(modifier = Modifier.padding(16.dp)) {

                // Título: Nombre de la Actividad
                Text(
                    text = if (actividad.nombre.isNotEmpty()) actividad.nombre.uppercase() else ruta?.nombre?.uppercase() ?: "SIN NOMBRE",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                
                if (actividad.nombre.isNotEmpty() && ruta != null) {
                    Text(
                        text = "Ruta: ${ruta.nombre}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Creador
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                        .then(
                            if (onCreadorClick != null)
                                Modifier.clickable { onCreadorClick(actividad.idcreador) }
                            else Modifier
                        )
                ) {
                    UserAvatar(
                        usuario = Usuario(
                            uid = actividad.idcreador,
                            username = actividad.nombrecreador,
                            fotoperfil = actividad.fotocreador
                        ),
                        mostrarNombre = false
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = actividad.nombrecreador,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Organizador",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }

                // Stats GRID
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    StatItem("Distancia", "${ruta?.distancia ?: "--"} km")
                    StatItem("Dificultad", ruta?.dificultad ?: "--")
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    StatItem("Desnivel Acumulado ", "${ruta?.desnivelacumulado ?: "--"} m")
                    StatItem("Duración", ruta?.duracion ?: "--")
                }

                Spacer(modifier = Modifier.height(24.dp))



                // Información de la Actividad
                Text(text = "Informacion de la salida", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ){
                    StatItem("Fecha", mainviewModel.formatearFecha(actividad.fechasalida))
                    StatItem("Hora", mainviewModel.formatearHora(actividad.horasalida))
                }


                Text(text = "Punto de encuentro", style = MaterialTheme.typography.titleSmall, color = Color.Gray)
                Text(text = actividad.puntoencuentro.ifEmpty { "No especificado" }, style = MaterialTheme.typography.bodyLarge)
                
                Spacer(modifier = Modifier.height(16.dp))

                // Participantes
                Text(text = "Participantes (${actividad.participantes}/${actividad.maxparticipantes})", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (detalleviewModel.participantes.isEmpty()) {
                        Text("Cargando participantes...", style = MaterialTheme.typography.bodySmall)
                    } else {
                        detalleviewModel.participantes.forEach { usuario ->
                            UserAvatar(
                                usuario = usuario,
                                mostrarNombre = true,
                                onAvatarClick = { onCreadorClick?.invoke(usuario.uid) }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Descripción
                Text(text = "Sobre esta ruta", style = MaterialTheme.typography.titleMedium)
                Text(
                    text = ruta?.descripcion ?: "Sin descripción disponible para esta ruta.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Botones de Gestion
                if(!esCreador && mostrarAcciones){
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {


                        if (!estaUnido) {
                            Button(
                                onClick = {
                                    if (usuarioActualUid != null) {
                                        detalleviewModel.gestionarParticipacion(actividad.idactividad, usuarioActualUid, true)
                                        mainviewModel.showNotification("Te has unido a la actividad")
                                    }
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                                enabled = actividad.participantes < actividad.maxparticipantes
                            ) {
                                if (detalleviewModel.isLoading) {
                                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                                } else {
                                    Text("UNIRSE")
                                }
                            }
                        } else {
                            Button(
                                onClick = {
                                    if (usuarioActualUid != null) {
                                        detalleviewModel.gestionarParticipacion(actividad.idactividad, usuarioActualUid, false)
                                        mainviewModel.showNotification("Has abandonado la actividad")
                                    }
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC62828)),
                                enabled = !detalleviewModel.isLoading
                            ) {
                                if (detalleviewModel.isLoading) {
                                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                                } else {
                                    Text("SALIR")
                                }
                            }
                        }
                    }

                    if (estaUnido) {
                        Text(
                            text = "Ya formas parte de esta actividad",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF2E7D32),
                            modifier = Modifier
                                .padding(top = 8.dp)
                                .align(Alignment.CenterHorizontally)
                        )
                    }
                }

                if(esCreador && mostrarAcciones){
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "Gestión de la Actividad",
                        style = MaterialTheme.typography.titleSmall,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                val fechaStr = mainviewModel.formatearFecha(actividad.fechasalida)
                                val horaStr = mainviewModel.formatearHora(actividad.horasalida)
                                detalleviewModel.abrirPopUpEdicion(actividad, fechaStr, horaStr)
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Text("EDITAR")
                        }

                        Button(
                            onClick = {
                                detalleviewModel.eliminarActividad(actividad.idactividad) {
                                    mainviewModel.showNotification("Actividad eliminada")
                                    onBack()
                                }
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC62828))
                        ) {
                            Text("ELIMINAR")
                        }
                    }
                }
            }
        }
    }
}
