package com.julen.trailpack.vistas.componentes.marcogeneral

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.julen.trailpack.data.AuthRepository
import com.julen.trailpack.vistas.perfilusuario.VistaPerfilUsuario

@Composable
fun ScaffoldTrailPack(navController: NavHostController) {

    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val uid = auth.currentUser?.uid

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

    // Scaffold gestiona el espacio para las barras automáticamente
    Scaffold(
        topBar = { TopBarTrailPack(onCerrarSesion = {
            AuthRepository().cerrarSesion(navController)
        }) },
        bottomBar = { BottomBarTrailPack() },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingInterno ->
        // USAR EL PADDING ES VITAL: Si no, el perfil se solapa con las barras
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingInterno)
        ) {
            VistaPerfilUsuario()
        }
    }
}
