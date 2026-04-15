package com.julen.trailpack.vistas.marcogeneral

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.julen.trailpack.data.AuthRepository
import com.julen.trailpack.routing.Enrutador
import com.julen.trailpack.vistas.actividadespublicadas.ActividadesViewModel
import com.julen.trailpack.vistas.mapa.VistaMapa
import com.julen.trailpack.vistas.perfilusuario.VistaPerfilUsuario
import com.julen.trailpack.vistas.actividadespublicadas.VistaRutasPublicadas
import com.julen.trailpack.vistas.mapa.MapaViewModel
import com.julen.trailpack.vistas.mapa.PopUpPublicarRuta

@SuppressLint("UnrememberedGetBackStackEntry")
@Composable
fun ScaffoldTrailPack(navController: NavHostController, mainviewModel: MainViewModel) {
    val mapaviewModel: MapaViewModel = viewModel()
    val actividadesviewModel: ActividadesViewModel = viewModel()

    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val uid = auth.currentUser?.uid
    val enrutador = remember(navController) { Enrutador(navController) }

    LaunchedEffect(Unit) {
        if(uid != null){
            mainviewModel.cargarUsuarioGlobal(uid)

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

    // El observador de notificaciones (Toast) se ha movido a AppNavegation para ser global y visible en todas las capas.

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                if(mainviewModel.selectedTab == 2){
                    TopBarTrailPack(
                        enrutador = enrutador,
                        onCerrarSesion = {
                            AuthRepository().authCerrarSesion(navController)
                        }
                    )
                }
            },
            bottomBar = { BottomBarTrailPack(
                selectedTab = mainviewModel.selectedTab,
                onTabSelected = {mainviewModel.selectedTab = it}
            ) },
            containerColor = MaterialTheme.colorScheme.background
        ) { paddingInterno ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingInterno)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    when (mainviewModel.selectedTab) {
                        0 -> {
                            VistaMapa(
                                enrutador = enrutador,
                                mainviewModel = mainviewModel,
                                mapaviewModel = mapaviewModel
                            )
                        }
                        1 -> {
                            VistaRutasPublicadas(mainviewModel, enrutador,actividadesviewModel)
                        }
                        2 -> {
                            VistaPerfilUsuario(mainviewModel,actividadesviewModel,enrutador)
                        }
                    }
                }
            }
        }
        
        // Diálogo global de publicación (se muestra solo cuando su estado interno es visible)
        PopUpPublicarRuta(mapaviewModel, mainviewModel)
    }
}
