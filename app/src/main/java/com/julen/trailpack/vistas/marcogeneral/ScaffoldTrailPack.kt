package com.julen.trailpack.vistas.marcogeneral

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
    val actividadesviewModel: ActividadesViewModel = viewModel() // <--- Instanciado aquí

    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val uid = auth.currentUser?.uid
    val enrutador = remember(navController) { Enrutador(navController) }

    //Lanzamos el formulario una vez al entrar
    LaunchedEffect(Unit) {
        if(uid != null){
            Log.d("DEBUG_SCAFFOLD", "Lanzando carga para: $uid")
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

    //Observamos si el mainviewmodel tiene mensajes pendientes
    val context = LocalContext.current
    LaunchedEffect(mainviewModel.notificationMessage) {
        mainviewModel.notificationMessage?.let { mensaje ->
            Toast.makeText(context,mensaje,Toast.LENGTH_SHORT).show()
            //Limpiamos el mensaje para que no se repita
            mainviewModel.notificationMessage = null
        }
    }

    // Scaffold gestiona el espacio para las barras automáticamente
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
                    Log.d("DEBUG_UI", "Seleccionada tab: ${mainviewModel.selectedTab}")
                    when (mainviewModel.selectedTab) {
                        0 -> {
                            VistaMapa(
                                enrutador = remember(navController) { Enrutador(navController) },
                                mainviewModel = mainviewModel,
                                mapaviewModel = mapaviewModel
                            )
                        }
                        1 -> {
                            VistaRutasPublicadas(mainviewModel, enrutador,actividadesviewModel)
                        }
                        2 -> {
                            VistaPerfilUsuario()
                        }
                    }
                }
            }
        }
    }
    // 2. EL POPUP VA AQUÍ, FUERA DEL SCAFFOLD
    if (mapaviewModel.isPublicacionPopupVisible) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable { mapaviewModel.togglePopupPublicacion(false) },
            contentAlignment = Alignment.Center
        ) {
            // Aquí va tu contenido del PopUp
            PopUpPublicarRuta(mapaviewModel, mainviewModel)
        }
    }
}



