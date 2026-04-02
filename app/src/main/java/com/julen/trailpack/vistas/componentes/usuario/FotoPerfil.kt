package com.julen.trailpack.vistas.componentes.usuario

import android.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun FotoPerfil(url: String){

    AsyncImage(
        model = url,
        contentDescription = "Foto de perfil",
        // ESTO ES LA CLAVE: Se verá en la Preview y mientras carga la real
        placeholder = painterResource(id = R.drawable.ic_menu_gallery),
        error = painterResource(id = R.drawable.stat_notify_error),
        modifier = Modifier
            .size(140.dp)
            .clip(shape = RoundedCornerShape(12.dp))
            .background(Color.LightGray), // Fondo gris por si la foto falla
        contentScale = ContentScale.Crop
    )
}
//El previsualizador
@Preview
@Composable
fun FotoPerfilPreview(){
    FotoPerfil(url = "https://cdn.phototourl.com/free/2026-04-01-f1855b03-56db-4bf5-a551-25568c71c945.png")
}
