package com.julen.trailpack.vistas.actividadespublicadas

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun VistaRutasPublicadas () {

    val actividadesviewModel: ActividadesViewModel = viewModel()

    LaunchedEffect(!actividadesviewModel.isLoading) {
        Log.d("VISTA_SOCIAL", "UI recibiendo actividades: ${actividadesviewModel.actividades.size}")
        actividadesviewModel.actividades.forEach { actividad ->
            Log.d("VISTA_SOCIAL", "Ruta: ${actividad.nombre} | Creador: ${actividad.nombrecreador}")
        }
    }


    Column(
        modifier = Modifier.padding(16.dp).fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


    }

}


@Preview
@Composable
fun VistaRutasPublicadasPreview (){
    VistaRutasPublicadas ()
}