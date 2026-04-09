package com.julen.trailpack.vistas.marcogeneral

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.julen.trailpack.data.AuthRepository
import com.julen.trailpack.routing.Enrutador
import com.julen.trailpack.vistas.mapa.VistaMapa
import com.julen.trailpack.vistas.perfilusuario.VistaPerfilUsuario
import com.julen.trailpack.vistas.social.VistaRutasPublicadas

@Composable
fun ScaffoldTrailPack(navController: NavHostController) {

    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val uid = auth.currentUser?.uid
    val enrutador = remember(navController) { Enrutador(navController) }
    //Lanzamos el formulario una vez al entrar
    LaunchedEffect(Unit) {
        if(uid != null){
            db.collection("usuarios").document(uid).get().addOnSuccessListener { docTask ->
                val completado = docTask.getBoolean("perfilcompletado") ?: false
                if(!completado){
                    navController.navigate("completarperfil"){
                        popUpTo("scaffoldtrailpack") {inclusive = true}
                    }
                }
            }
        }
    }
    var selectedTab by remember { mutableStateOf(2) }

    // Scaffold gestiona el espacio para las barras automáticamente
    Scaffold(
        topBar = {
            if(selectedTab == 2){
                TopBarTrailPack(
                    enrutador = enrutador,
                    onCerrarSesion = {
                        AuthRepository().authCerrarSesion(navController)
                    }
                )
            }
        },
        bottomBar = { BottomBarTrailPack(
            selectedTab = selectedTab,
            onTabSelected = {selectedTab = it}
        ) },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingInterno ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingInterno)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Box(modifier = Modifier.matchParentSize().alpha(if (selectedTab == 0) 1f else 0f)) {
                    VistaMapa()
                }
                Box(modifier = Modifier.matchParentSize().alpha(if (selectedTab == 1) 1f else 0f)) {
                    VistaRutasPublicadas()
                }
                Box(modifier = Modifier.alpha(if (selectedTab == 2) 1f else 0f)) {
                    VistaPerfilUsuario()
                }
            }
        }
    }
}
