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
import com.julen.trailpack.routing.Enrutador
import com.julen.trailpack.vistas.marcogeneral.MainViewModel

@Composable
fun VistaRutasPublicadas(
    mainViewModel: MainViewModel,
    enrutador: Enrutador,
    actividadesviewModel: ActividadesViewModel
) {
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
        items(actividadesviewModel.actividadesconruta) { actividadconruta ->
            CardActividad(
                actividadConRuta = actividadconruta,
                fechaFormateada = mainViewModel.formatearFecha(actividadconruta.actividad.fechasalida),
                usuarioActualUid = mainViewModel.usuarioGlobal?.uid,
                onUnirseClick = {
                    mainViewModel.showNotification("Funcionalidad de UNIRSE a actividad")
                },
                onAbandonarClick = {
                    mainViewModel.showNotification("Funcionalidad de Abandonar actividad")
                },
                onCardClick = {
                    enrutador.navToActividadDetallada(actividadconruta.actividad.idactividad)
                }
            )
        }
    }
}
