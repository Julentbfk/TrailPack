package com.julen.trailpack.vistas.actividadespublicadas

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.julen.trailpack.modelos.ActividadConRuta


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardActividad(
    actividadConRuta: ActividadConRuta,
    fechaFormateada: String,
    usuarioActualUid: String?,
    onUnirseClick: () -> Unit,
    onAbandonarClick: () -> Unit,
    onCardClick: () -> Unit
) {
    val actividad = actividadConRuta.actividad
    val ruta = actividadConRuta.ruta

    //Logica para saber si el usuario actual esta entre los participantes
    val usuarioIn = usuarioActualUid != null && actividad.listaparticipantesIds.contains(usuarioActualUid)

    //LOG PARA DIAGNOSTICAR
    Log.d("DEBUG_CARD", "Actividad ID: ${actividad.idactividad} | IDRuta esperado: ${actividad.idruta} | ¿Ruta encontrada?: ${ruta != null}")
    if (ruta != null) {
        Log.d("DEBUG_CARD", "Ruta encontrada: ${ruta.nombre} con ID: ${ruta.idruta}")
    }


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onCardClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically // Alineación centrada
        ) {
            // 1. IMAGEN (Cargando la primera foto de la lista)
            AsyncImage(
                model = ruta?.fotosRuta?.firstOrNull(),
                contentDescription = "Mapa ruta",
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            // 2. DATOS CENTRALES (Sustituimos weight por un modificador más simple)
            Column(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .fillMaxWidth(0.6f) // Ocupa el 60% del ancho fijo
            ) {
                Text(
                    text = ruta?.nombre ?: "SIn Nombre",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary // Forzamos color primario
                )
                Text(
                    text = "${ruta?.distancia ?: "0"}km - ${ruta?.desnivel ?: "0"}m",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Fecha: $fechaFormateada",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = actividad.nombrecreador,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }

            // 3. ACCIONES (Columna derecha)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(100.dp) // Ancho fijo para evitar que empuje al centro
            ) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Text(
                        text = "${actividad.participantes}/${actividad.maxparticipantes}",
                        modifier = Modifier.padding(4.dp),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))


                if(usuarioIn) {
                    Button(
                        onClick = { onAbandonarClick()},
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                    ) {
                        Text("SALIR", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onErrorContainer)
                    }
                }else{
                    Button(
                        onClick = { onUnirseClick() },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = actividad.participantes < actividad.maxparticipantes
                    )
                    {
                        Text("Unirse", style = MaterialTheme.typography.labelSmall)
                    }
                }

            }
        }
    }
}