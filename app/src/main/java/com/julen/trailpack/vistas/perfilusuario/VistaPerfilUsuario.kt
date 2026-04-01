package com.julen.trailpack.vistas.perfilusuario

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun VistaPerfilUsuario(navToLoginPopBack: () -> Unit) {
    val viewModel: PerfilUsuarioViewModel = viewModel()
    val user = viewModel.usuarioState
    val cargando = viewModel.isLoading

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        if (cargando) {
            // Mientras descarga, mostramos un circulito de carga
            CircularProgressIndicator()
        } else {
            // Cuando termina, mostramos los datos
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Bienvenido,", style = MaterialTheme.typography.bodyLarge)
                Text(text = user.email, style = MaterialTheme.typography.headlineMedium)
                Text(text = "Nivel: ${user.nivel}")
            }
        }
    }
}