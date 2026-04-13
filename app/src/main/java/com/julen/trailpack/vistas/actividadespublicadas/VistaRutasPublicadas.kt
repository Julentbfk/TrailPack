package com.julen.trailpack.vistas.actividadespublicadas

import android.util.Log

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.julen.trailpack.routing.Enrutador
import com.julen.trailpack.vistas.marcogeneral.MainViewModel

@Composable
fun VistaRutasPublicadas (mainViewModel: MainViewModel,enrutador: Enrutador) {

    val actividadesviewModel: ActividadesViewModel = viewModel()

    LaunchedEffect(!actividadesviewModel.isLoading) {
        Log.d("VISTA_SOCIAL", "UI recibiendo actividades: ${actividadesviewModel.actividadesconruta.size}")
        actividadesviewModel.actividadesconruta.forEach { actividadconruta ->
            Log.d("VISTA_SOCIAL", "Ruta: ${actividadconruta.actividad.nombre} | Creador: ${actividadconruta.actividad.nombrecreador}")
            Log.d("VISTA_SOCIAL", "Actividad idRuta: ${actividadconruta.actividad.idruta} | Ruta idRuta: ${actividadconruta.ruta?.idruta}")

        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(8.dp)
    ) {
        items(actividadesviewModel.actividadesconruta) { actividadconruta ->
            CardActividad(
                actividadConRuta = actividadconruta,
                fechaFormateada = mainViewModel.formatearFecha(actividadconruta.actividad.fechasalida),
                usuarioActualUid = mainViewModel.usuarioGlobal?.uid,
                onUnirseClick = {
                    //funcion unirse a actividad
                    mainViewModel.showNotification("Funcionalidad de UNIRSE a actividad")
                },
                onAbandonarClick = {
                    //funcion de abandonar actividad
                    mainViewModel.showNotification("Funcionalidad de Abandonar actividad")
                },
                onCardClick = {
                    //Navegacion a detalle
                    mainViewModel.showNotification("Navegando a detalle...")
                    enrutador.navToActividadDetallada(actividadconruta.actividad.idactividad)
                }
            )
        }
    }



}