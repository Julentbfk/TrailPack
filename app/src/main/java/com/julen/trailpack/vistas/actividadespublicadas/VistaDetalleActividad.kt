package com.julen.trailpack.vistas.actividadespublicadas

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
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

    //Cargamos los datos al iniciar
    LaunchedEffect(actividadId) {
        detalleviewModel.cargarDetalles(actividadId)
    }

    //Cargamos los mensajes que pueda haber
    val context = LocalContext.current
    LaunchedEffect(mainviewModel.notificationMessage) {
        mainviewModel.notificationMessage?.let { mensaje ->
            Toast.makeText(context,mensaje,Toast.LENGTH_SHORT).show()
            //Limpiamos el mensaje para que no se repita
            mainviewModel.notificationMessage = null
        }
    }


    if(detalleviewModel.isLoading){
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }else{
        val datos = detalleviewModel.actividadconruta ?: return
        val ruta = datos.ruta
        val actividad = datos.actividad

        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).background(MaterialTheme.colorScheme.background)
        ) {
            //Imagen
            AsyncImage(
                model = ruta?.fotosRuta?.firstOrNull(),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth().height(250.dp),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(16.dp)) {

                //Nombre de la ruta
                Text(
                    text = ruta?.nombre?.uppercase() ?: "nameless",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                //Stats GRID
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    StatItem("Distancia", "${ruta?.distancia ?: 0} km")
                    StatItem("Dificultad", ruta?.dificultad ?: "none")
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    StatItem("Desnivel +", "${ruta?.desnivel ?: 0} m")
                    StatItem("Duración", ruta?.duracion ?: "none")
                }

                Spacer(modifier = Modifier.height(24.dp))

                //Participantes
                Text(text = "Participantes", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    detalleviewModel.participantes.forEach { usuario ->
                        UserAvatar(usuario)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                //Descripcion
                Text(text = "Descripcion de la ruta", style = MaterialTheme.typography.titleMedium)
                Text(
                    text = ruta?.descripcion ?: "Sin descripcion disponible",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

                //Botones
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedButton(
                        onClick = { mainviewModel.showNotification("Guardado en favoritos") },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Guardar fav")
                    }

                    Button(
                        onClick = { mainviewModel.showNotification("Te has unido") },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
                    ) {
                        Text("UNIRSE")
                    }
                }
                Button(
                    onClick = { mainviewModel.showNotification("Te has salido") },
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC62828))
                ) {
                    Text("SALIRSE")
                }

            }

        }//Cierra column principal


    }//Cierra el condicion loading


}
