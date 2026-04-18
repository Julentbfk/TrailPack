package com.julen.trailpack.vistas.actividadespublicadas

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.julen.trailpack.routing.Enrutador
import com.julen.trailpack.vistas.marcogeneral.MainViewModel

@Composable
fun VistaRutasPublicadas(
    mainViewModel: MainViewModel,
    enrutador: Enrutador,
    actividadesviewModel: ActividadesViewModel
) {
    val uid = mainViewModel.usuarioGlobal?.uid ?: ""

    val actpublicadas = actividadesviewModel.getActividadesPublicadas(uid)
    val actcreadas = actividadesviewModel.getActividadesCreadas(uid)
    val actunido = actividadesviewModel.getActividadesUnido(uid)
    val actcaducada = actividadesviewModel.getActividadesCaducadas(uid)

    var paginaPublicadas by remember { mutableStateOf(1) }
    var paginaCreadas by remember { mutableStateOf(1) }
    var paginaUnido by remember { mutableStateOf(1) }
    var paginaCaducadas by remember { mutableStateOf(1) }

    // Forzamos la recarga cada vez que la vista se compone
    LaunchedEffect(Unit) {
        Log.d("VISTA_SOCIAL", "Recargando actividades...")
        actividadesviewModel.cargarActividades()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {

        //SECCION CREADAS
        item {
            SeccionDesplegableActividades ("Creadas por ti", actcreadas.size) {
                Column {
                    actcreadas.take(paginaCreadas * 10).forEach { acr ->
                        val esCaducada = acr.actividad.estaCaducada()
                        CardActividad(
                            actividadConRuta = acr,
                            fechaFormateada = mainViewModel.formatearFecha(acr.actividad.fechasalida),
                            usuarioActualUid = uid,
                            caducada = esCaducada,
                            onUnirseClick = {},
                            onAbandonarClick = {},
                            onCardClick = { enrutador.navToActividadDetallada(acr.actividad.idactividad) },
                            onCreadorClick = {enrutador.navToPerfilPublico(acr.actividad.idcreador)}
                        )
                    }
                    if (actcreadas.size > paginaCreadas * 10) {
                        Button(
                            onClick = { paginaCreadas++ },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            Text("Ver más")
                        }
                    }
                }
            }//Cierra seccion desplegable
        }//Cierra item 1

        //SECCION UNIDO
        item {
            SeccionDesplegableActividades ("Unido", actunido.size) {
                Column {
                    actunido.take(paginaUnido * 10).forEach { acr ->
                        val esCaducada = acr.actividad.estaCaducada()
                        CardActividad(
                            actividadConRuta = acr,
                            fechaFormateada = mainViewModel.formatearFecha(acr.actividad.fechasalida),
                            usuarioActualUid = uid,
                            caducada = esCaducada,
                            onUnirseClick = {},
                            onAbandonarClick = {
                                actividadesviewModel.gestionarParticipacionCard(acr.actividad.idactividad, uid, false)
                                mainViewModel.showNotification("Has abandonado la actividad")
                            },
                            onCardClick = { enrutador.navToActividadDetallada(acr.actividad.idactividad) },
                            onCreadorClick = {enrutador.navToPerfilPublico(acr.actividad.idcreador)}

                        )
                    }
                    if (actunido.size > paginaUnido * 10) {
                        Button(
                            onClick = { paginaUnido++ },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            Text("Ver más")
                        }
                    }
                }
            } // Ciera desplegable
        }//cierra item 2

        //SECCION PUBLICADAS
        item {
            SeccionDesplegableActividades("Publicadas",actpublicadas.size) {
                Column() {
                    actpublicadas.take(paginaPublicadas * 10).forEach { acr ->
                        CardActividad(
                            actividadConRuta = acr,
                            fechaFormateada = mainViewModel.formatearFecha(acr.actividad.fechasalida),
                            usuarioActualUid = uid,
                            caducada = false,
                            onUnirseClick = {
                                actividadesviewModel.gestionarParticipacionCard(acr.actividad.idactividad, uid, true)
                                mainViewModel.showNotification("¡Te has unido a ${acr.ruta?.nombre}!")
                            },
                            onAbandonarClick = {
                                actividadesviewModel.gestionarParticipacionCard(acr.actividad.idactividad, uid, false)
                                mainViewModel.showNotification("Has abandonado la actividad")
                            },
                            onCardClick = { enrutador.navToActividadDetallada(acr.actividad.idactividad) },
                            onCreadorClick = {enrutador.navToPerfilPublico(acr.actividad.idcreador)}

                        )
                    }
                    if(actpublicadas.size > paginaPublicadas*10){
                        Button(
                            onClick = { paginaPublicadas++},
                            modifier = Modifier.fillMaxWidth().padding(8.dp)
                        ){ Text("Ver mas") }
                    }

                }

            }//Cierra la Seccion Desplegable
        }//Cierra item3

        //SECCION CADUCADADS
        item {
            SeccionDesplegableActividades ("Caducadas", actcaducada.size) {
                Column {
                    actcaducada.take(paginaCaducadas * 10).forEach { acr ->
                        CardActividad(
                            actividadConRuta = acr,
                            fechaFormateada = mainViewModel.formatearFecha(acr.actividad.fechasalida),
                            usuarioActualUid = uid,
                            caducada = true,
                            onUnirseClick = {},
                            onAbandonarClick = {},
                            onCardClick = { enrutador.navToActividadDetallada(acr.actividad.idactividad) },
                            onCreadorClick = {enrutador.navToPerfilPublico(acr.actividad.idcreador)}

                        )
                    }
                    if (actcaducada.size > paginaCaducadas * 10) {
                        Button(
                            onClick = { paginaCaducadas++ },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            Text("Ver más")
                        }
                    }
                }
            }//cierra desplegable
        }//cierraitem 4

    }
}
