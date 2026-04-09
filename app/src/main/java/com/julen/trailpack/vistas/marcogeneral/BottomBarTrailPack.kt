package com.julen.trailpack.vistas.marcogeneral

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

@Composable
fun BottomBarTrailPack(selectedTab: Int, onTabSelected: (Int) -> Unit) {

    NavigationBar {

        NavigationBarItem(
            icon = { Icon (Icons.Default.Map, null) },
            label = { Text("Mapa") },
            selected = selectedTab == 0,
            onClick = {onTabSelected(0)}
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Explore, null) },
            label = { Text("Rutas") },
            selected = selectedTab == 1,
            onClick = { onTabSelected(1) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, null) },
            label = { Text("Perfil") },
            selected = selectedTab == 2, // Por ahora estamos en Perfil
            onClick = { onTabSelected(2) }
        )
    }

}
@Preview
@Composable
fun BottomBarTrailPackPreview(){
    BottomBarTrailPack(0,{})
}