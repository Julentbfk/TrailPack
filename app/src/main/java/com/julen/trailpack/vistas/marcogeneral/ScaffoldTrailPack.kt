package com.julen.trailpack.vistas.marcogeneral

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.julen.trailpack.data.AuthRepository
import com.julen.trailpack.routing.Enrutador
import com.julen.trailpack.vistas.mapa.VistaMapa
import com.julen.trailpack.vistas.perfilusuario.VistaPerfilUsuario
import com.julen.trailpack.vistas.actividadespublicadas.VistaRutasPublicadas

@SuppressLint("UnrememberedGetBackStackEntry")
@Composable
fun ScaffoldTrailPack(navController: NavHostController, mainviewModel: MainViewModel) {

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
                Box(modifier = Modifier.matchParentSize().alpha(if (mainviewModel.selectedTab == 0) 1f else 0f)) {
                    VistaMapa(
                        enrutador = remember(navController) {Enrutador(navController)},
                        mainviewModel = mainviewModel
                    )
                }
                Box(modifier = Modifier.matchParentSize().alpha(if (mainviewModel.selectedTab == 1) 1f else 0f)) {
                    VistaRutasPublicadas(mainviewModel)
                }
                Box(modifier = Modifier.alpha(if (mainviewModel.selectedTab == 2) 1f else 0f)) {
                    VistaPerfilUsuario()
                }
            }
        }
    }
}
