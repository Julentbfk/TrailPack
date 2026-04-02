package com.julen.trailpack.vistas.componentes.marcogeneral

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.julen.trailpack.vistas.perfilusuario.VistaPerfilUsuario

@Composable
fun ScaffoldTrailPack() {
    // Scaffold gestiona el espacio para las barras automáticamente
    Scaffold(
        topBar = { TopBarTrailPack(onCerrarSesion = { /* Lógica mañana */ }) },
        bottomBar = { BottomBarTrailPack() },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingInterno ->
        // USAR EL PADDING ES VITAL: Si no, el perfil se solapa con las barras
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingInterno)
        ) {
            VistaPerfilUsuario(navToLoginPopBack = { /* ... */ })
        }
    }
}
@Preview
@Composable
fun ScaffoldTrailPackPreview() {
    ScaffoldTrailPack()
}