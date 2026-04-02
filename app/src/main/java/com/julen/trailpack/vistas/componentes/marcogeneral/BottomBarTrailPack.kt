package com.julen.trailpack.vistas.componentes.marcogeneral

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.Navigator

@Composable
fun BottomBarTrailPack() {

    NavigationBar {

        NavigationBarItem(
            icon = { Icon (Icons.Default.Map, null) },
            label = { Text("Mapa") },
            selected = false,
            onClick = {/* NAVEGAR A LA PANTALLA MAPA */}
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Explore, null) },
            label = { Text("Rutas") },
            selected = false,
            onClick = { /* Navegar a Rutas */ }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, null) },
            label = { Text("Perfil") },
            selected = true, // Por ahora estamos en Perfil
            onClick = { /* Navegar al Perfil */ }
        )
    }

}
@Preview
@Composable
fun BottomBarTrailPackPreview(){
    BottomBarTrailPack()
}