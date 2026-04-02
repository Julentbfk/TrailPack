package com.julen.trailpack.vistas.componentes.marcogeneral

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarTrailPack(onCerrarSesion: () -> Unit) {
    val colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
        containerColor = MaterialTheme.colorScheme.surfaceContainer, // Mismo fondo que la Nav Bar
        titleContentColor = MaterialTheme.colorScheme.onSurface,
        actionIconContentColor = MaterialTheme.colorScheme.onSurface
    )
    CenterAlignedTopAppBar(
        title = {
            Text(text = "TrailPack", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold)
        },
        colors = colors,
        //Icono de la izquierda, cambiar de claro a oscuro
        navigationIcon = {
            IconButton(onClick = {/*Cambiar de claro a oscuro */}) {
                Icon(Icons.Default.LightMode, contentDescription = "Mode de dia")
            }
        },
        //Iconos de la derecha
        actions = {
            //Search
            IconButton(onClick = {/* Buscar otros perfiles para agregar o añadir amigo */}) {
                Icon(Icons.Default.Search, contentDescription = "Buscar")
            }
            //Ajustes
            IconButton(onClick = { /* Ajustes */ }) {
                Icon(Icons.Default.Settings, contentDescription = "Ajustes")
            }
            //CerrarSesion
            IconButton(onClick = onCerrarSesion) {
                Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Salir", tint = Color.Red)
            }
        }
    )
}
@Preview
@Composable
fun TopBarTrailPackPreview () {
    TopBarTrailPack {}
}