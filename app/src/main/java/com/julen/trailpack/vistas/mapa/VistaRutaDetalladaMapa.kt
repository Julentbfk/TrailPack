package com.julen.trailpack.vistas.mapa

import androidx.activity.compose.BackHandler
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.julen.trailpack.modelos.Ruta
import com.julen.trailpack.vistas.componentes.mapa.MapaRutaDetallada
import com.julen.trailpack.vistas.marcogeneral.MainViewModel

@Composable
fun VistaRutaDetalladaMapa(
    ruta: Ruta,
    mapaviewModel: MapaViewModel,
    mainviewModel: MainViewModel,
    onPublicarClick: () -> Unit,
    onBack: () -> Unit
) {
    BackHandler {
        mapaviewModel.seleccionarRutaParaDetalle(null)
        onBack()
    }
    val esFavorita = mainviewModel.usuarioGlobal?.rutasfavoritas?.contains(ruta.idruta) == true
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            //Espacio para la foto, mapa de la ruta o nada a traves de componente MapaRutaDetallada
            MapaRutaDetallada(ruta)

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
                if (esFavorita) {
                    Button(
                        onClick = { mainviewModel.toggleRutaFavorita(ruta.idruta) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            contentColor = MaterialTheme.colorScheme.onErrorContainer
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Guardada")
                    }
                } else {
                    OutlinedButton(onClick = { mainviewModel.toggleRutaFavorita(ruta.idruta) }) {
                        Icon(
                            imageVector = Icons.Default.FavoriteBorder,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Guardar")
                    }
                }
                Button(onClick = { onPublicarClick() }) { Text("Publicar") }
            }
        }

        // CAPA DE POPUP (Igual que en ScaffoldTrailPack para consistencia)
        if (mapaviewModel.isPublicacionPopupVisible) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .clickable { mapaviewModel.togglePopupPublicacion(false) },
                contentAlignment = Alignment.Center
            ) {
                PopUpPublicarRuta(mapaviewModel, mainviewModel, onPublicado = onBack)
            }
        }
    }
}
