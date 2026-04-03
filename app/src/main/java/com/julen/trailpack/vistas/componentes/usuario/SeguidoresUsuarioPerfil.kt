package com.julen.trailpack.vistas.componentes.usuario

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SeguidoresUsuarioPerfil(etiqueta: String, numero: Int, modifier: Modifier){

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = etiqueta, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        Text(text = numero.toString(), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
    }
}

@Preview
@Composable
fun SeguidoresUsuarioPerfilPreview(){
    SeguidoresUsuarioPerfil("", 1, modifier = Modifier)
}