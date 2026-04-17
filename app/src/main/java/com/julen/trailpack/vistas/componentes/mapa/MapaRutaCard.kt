package com.julen.trailpack.vistas.componentes.mapa

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.julen.trailpack.modelos.Ruta
import com.julen.trailpack.vistas.marcogeneral.MainViewModel

//Card basada en el img FeedRutaParquenaturalMapa (Solo se usara en el mapa no en el feed social)
@Composable
fun MapaRutaCard( ruta: Ruta, onRutaClick: () -> Unit,onPublicarClick: (() -> Unit)? = null) {

    //Formateo la fecha del usuario
    val milis = ruta.fechacreacion
    val mainViewModel: MainViewModel = viewModel()
    val fechaFormateada = mainViewModel.formatearFecha(milis)
    //-----------------------------

    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp).clickable(onClick = onRutaClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {

            AsyncImage(
                model = ruta.fotosRuta.firstOrNull(),
                contentDescription = null,
                modifier = Modifier.size(100.dp).clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(start = 16.dp)) {
                Text(text = "${ruta.nombre}",style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Dificultad: ${ruta.dificultad}",style = MaterialTheme.typography.labelMedium)
                Text(text = "${ruta.distancia}km - ${ruta.desnivelacumulado}m - ${ruta.duracion}",style = MaterialTheme.typography.labelMedium)

                //creador
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().padding(top  = 4.dp), horizontalArrangement = Arrangement.SpaceBetween ) {
                    Text(text = "Creado por ${ruta.nombreCreador}\nCreada el $fechaFormateada",style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                    if (onPublicarClick != null) {
                        TextButton(
                            onClick = { onPublicarClick() }
                        ) {
                            Text("Publicar", color = Color.Magenta, fontWeight = FontWeight.Bold)
                        }
                    }
                }

            }
        }
    }

}

