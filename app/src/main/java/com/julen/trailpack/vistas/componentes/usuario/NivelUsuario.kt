package com.julen.trailpack.vistas.componentes.usuario

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun NivelUsuarioPerfil(nivel: Int){
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.primary
    ) {
        Text(
            text = "NIVEL $nivel",
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}
@Preview
@Composable
fun NivelUsuarioPerfilPreview(){
    NivelUsuarioPerfil(10)
}
