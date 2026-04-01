package com.julen.trailpack.vistas.perfilusuario

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun VistaPerfilUsuario(navToLoginPopBack:() -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth().padding(16.dp)
    ) {
        Text(
            text = "Bienvenido a tu perfil de usuario",
            style = MaterialTheme.typography.headlineLarge.copy(
                color = Color.Green,
                letterSpacing = 2.sp,
                fontWeight = FontWeight.ExtraBold
            )
        )
        Button(
            onClick = {
                navToLoginPopBack()
            }
        ){
            Text("CERRAR SESION")
        }
    }
}