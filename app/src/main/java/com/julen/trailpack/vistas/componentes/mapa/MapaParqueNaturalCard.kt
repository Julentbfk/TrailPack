package com.julen.trailpack.vistas.componentes.mapa

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import com.julen.trailpack.modelos.ParqueNatural
import com.julen.trailpack.modelos.Ruta
import com.julen.trailpack.routing.Enrutador
import okhttp3.OkHttpClient


//ESTA CLASE CONTENDRA LAS RUTA CARD Y PINTARA LO DE CADA CHINCHETA

@Composable
fun MapaParqueNaturalCard (
    parque: ParqueNatural,
    rutas: List<Ruta>,
    onRutaClick: (Ruta) -> Unit,
    onPublicarClick: (Ruta) -> Unit,
    onCrearRutaClick: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        //Cabecera
        Text(text = parque.nombre, style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(24.dp))
        //Imagen del parque
        val context = LocalContext.current
        //Necistamos un user agetn personalizado pues wikimedia rechaza el por defecto
        val imageLoader = remember(context) {
            ImageLoader.Builder(context)
                .okHttpClient {
                    OkHttpClient.Builder()
                        .addInterceptor { chain ->
                            chain.proceed(
                                chain.request().newBuilder()
                                    .header("User-Agent", "TrailPack/1.0 (Android; julen.tabuyo@gmail.com)")
                                    .build()
                            )
                        }
                        .build()
                }
                .build()
        }

        val painter = rememberAsyncImagePainter(
            model = parque.fotosparque.firstOrNull(),
            imageLoader = imageLoader
        )
        Image(
            painter = painter,
            contentDescription = "imagen de ${parque.nombre}",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )


        Text(
            text = parque.descripcion,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        HorizontalDivider()

        //Rutas del parque
        Text(
            text = "Rutas del parque",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        for (ruta in rutas) {
            MapaRutaCard(
                ruta=ruta,
                onRutaClick = { onRutaClick(ruta)},
                onPublicarClick = { onPublicarClick(ruta) }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { onCrearRutaClick() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("+ Crear ruta en este parque")
        }

    }
}