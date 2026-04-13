package com.julen.trailpack.vistas.mapa

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.julen.trailpack.modelos.Ruta
import com.julen.trailpack.vistas.marcogeneral.MainViewModel

@Composable
fun VistaRutaDetalladaMapa(
    ruta: Ruta,
    viewModel: MapaViewModel,
    mainViewModel: MainViewModel,
    onPublicarClick: () -> Unit,
    onBack: () -> Unit
) {
    BackHandler {
        viewModel.seleccionarRutaParaDetalle(null)
        onBack()
    }

    Column(
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        AsyncImage(
            model = ruta.fotosRuta.firstOrNull(),
            contentDescription = "mapa de la ruta",
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            contentScale = ContentScale.Crop
        )

        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = ruta.nombre, style = MaterialTheme.typography.headlineMedium)

            Text(text = "Descripción", style = MaterialTheme.typography.titleLarge)
            Text(text = ruta.descripcion, style = MaterialTheme.typography.bodyMedium)
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { /* Favoritos */ }) { Text("Guardar") }
            Button(onClick = { onPublicarClick() }) { Text("Publicar") }
        }
    }
}
