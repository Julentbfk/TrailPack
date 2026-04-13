package com.julen.trailpack.vistas.componentes.actividades

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.julen.trailpack.modelos.Usuario

@Composable
fun UserAvatar(usuario: Usuario) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        AsyncImage(
            model = usuario.fotoperfil,
            contentDescription = null,
            modifier = Modifier.size(40.dp).clip(CircleShape)
        )
        Text(text = usuario.username ?: "", style = MaterialTheme.typography.labelSmall, maxLines = 1)
    }
}