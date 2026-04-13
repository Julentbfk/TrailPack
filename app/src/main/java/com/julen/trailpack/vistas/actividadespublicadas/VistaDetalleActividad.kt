package com.julen.trailpack.vistas.actividadespublicadas

import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.julen.trailpack.vistas.componentes.actividades.StatItem
import com.julen.trailpack.vistas.componentes.actividades.UserAvatar
import com.julen.trailpack.vistas.marcogeneral.MainViewModel

@Composable
fun VistaDetalleActividad(actividadId: String, mainviewModel: MainViewModel) {

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
        
        // Comprobamos si el usuario actual ya está unido
        val usuarioActualUid = mainviewModel.usuarioGlobal?.uid
        val estaUnido = usuarioActualUid != null && actividad.listaparticipantesIds.contains(usuarioActualUid)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Imagen de cabecera (de la ruta)
            AsyncImage(
                model = ruta?.fotosRuta?.firstOrNull(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                contentScale = ContentScale.Crop
            )

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

                // Stats GRID
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    StatItem("Distancia", "${ruta?.distancia ?: "--"} km")
                    StatItem("Dificultad", ruta?.dificultad ?: "--")
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    StatItem("Desnivel +", "${ruta?.desnivel ?: "--"} m")
                    StatItem("Duración", ruta?.duracion ?: "--")
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Información de la Actividad
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
                            UserAvatar(usuario)
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

                // Botones de Acción
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedButton(
                        onClick = { mainviewModel.showNotification("Guardado en favoritos") },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Favorito")
                    }

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
                                    mainviewModel.showNotification("Te has unido a la actividad")
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
                        modifier = Modifier.padding(top = 8.dp).align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}
