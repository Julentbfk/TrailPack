package com.julen.trailpack.vistas.componentes.mapa

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person2
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.julen.trailpack.modelos.Ruta

//Card basada en el img FeedRutaParquenaturalMapa (Solo se usara en el mapa no en el feed social)
@Composable
fun MapaRutaCard(ruta: Ruta, onClick: () -> Unit) {

    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp).clickable(onClick = onClick),
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
                Text(text = "Dificultad: ${ruta.dificultad}",style = MaterialTheme.typography.labelMedium)
                Text(text = "${ruta.distancia} - ${ruta.desnivel} - ${ruta.duracion}",style = MaterialTheme.typography.labelMedium)

                //creador
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top  = 4.dp)) {
                    Text(text = "Creado por ${ruta.nombreCreador}",style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                }
            }
        }
    }

}

