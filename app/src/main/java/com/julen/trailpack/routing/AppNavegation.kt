package com.julen.trailpack.routing

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.julen.trailpack.vistas.actividadespublicadas.ActividadesViewModel
import com.julen.trailpack.vistas.actividadespublicadas.VistaDetalleActividad
import com.julen.trailpack.vistas.ajustes.VistaAjustes
import com.julen.trailpack.vistas.ajustes.cambiarpassword.VistaCambiarPassword
import com.julen.trailpack.vistas.marcogeneral.ScaffoldTrailPack
import com.julen.trailpack.vistas.login.VistaLogin
import com.julen.trailpack.vistas.mapa.MapaViewModel
import com.julen.trailpack.vistas.mapa.VistaCrearRuta
import com.julen.trailpack.vistas.mapa.VistaRutaDetalladaMapa
import com.julen.trailpack.vistas.marcogeneral.MainViewModel
import com.julen.trailpack.vistas.perfilusuario.VistaCompletarPerfil
import com.julen.trailpack.vistas.perfilusuario.VistaEditarPerfil
import com.julen.trailpack.vistas.perfilusuario.VistaPerfilPublico
import com.julen.trailpack.vistas.registro.VistaRegistro
import com.julen.trailpack.vistas.perfilusuario.VistaPerfilUsuario
import com.julen.trailpack.vistas.registro.VistaConfirmacionEmail

// ------------------ CLASE CONTROLADORA DE LAS VISTAS ----------------


@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Composable
fun AppNavegation() {

    val navHost: NavHostController = rememberNavController()
    val enrutador: Enrutador = remember(navHost){ Enrutador(navHost) }
    val mainViewModel: MainViewModel = viewModel()

    // --- OBSERVADOR GLOBAL DE NOTIFICACIONES ---
    val context = LocalContext.current
    LaunchedEffect(mainViewModel.notificationMessage) {
        mainViewModel.notificationMessage?.let { mensaje ->
            Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show()
            mainViewModel.notificationMessage = null
        }
    }

   if(mainViewModel.isCheckingAuth){
       Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
           CircularProgressIndicator()
       }
   }else{
       val startDestination = if(mainViewModel.isUserLoggedIn) "scaffoldtrailpack" else "login"
       NavHost(navHost, startDestination = startDestination){

           composable(route="login"){
               VistaLogin(
                   navToRegistro = { enrutador.navToRegistro() },
                   navToScaffoldTrailPack = {enrutador.navToScaffoldTrailPack()}
               )
           }

           composable(route="registro"){
               VistaRegistro(
                   navToLoginPopBack = {enrutador.popBack()},
                   navToConfirmacionEmail = {email -> enrutador.navToConfirmacionEmail(email)}
               )
           }

           composable(route="confirmacionemail/{email}"){ ruta ->
               val emailRecibido = ruta.arguments?.getString("email") ?: ""
               VistaConfirmacionEmail(
                   email = emailRecibido,
                   navToLogin = {enrutador.navToLogin()}
               )
           }

           composable(route="completarperfil"){
               VistaCompletarPerfil(
                   guardarClick = {
                        enrutador.navToScaffoldTrailPack()
                   }
               )
           }

           composable(route="perfilusuario"){
               val actividadesViewModel: ActividadesViewModel = viewModel()
               VistaPerfilUsuario(mainViewModel, actividadesViewModel,enrutador)
           }

           composable(
               route="perfilpublico/{uid}",
               arguments = listOf(navArgument("uid") {type = NavType.StringType})
           ){ backstackEntry ->
               val uid = backstackEntry.arguments?.getString("uid") ?: ""
               VistaPerfilPublico(
                   uid = uid,
                   mainViewModel = mainViewModel,
                   enrutador = enrutador
               )
           }

           composable(route="scaffoldtrailpack"){
               ScaffoldTrailPack(navHost,mainViewModel)
           }

           composable(route = "editarperfil") {
               VistaEditarPerfil(
                   mainViewModel = mainViewModel,
                   editarPerfilClick = { enrutador.navToScaffoldTrailPack() },
                   cancelarEditClick = { enrutador.popBack() }
               )
           }

           
           composable(route="ajustes") {
               VistaAjustes(
                   navToPerfilUsuario = {enrutador.popBack()},
                   navToCambiarPassword = {enrutador.navToCambiarPassword()},
                   navToLoginClearStack = {enrutador.navToLoginClearStack()}
               )
           }
           
           composable(route = "cambiarpassword"){
               VistaCambiarPassword(
                   guardarCambioPasswordClick = {enrutador.popBack()}
               )
           }


           composable(
               route = "crearruta"
           ){
                val scaffoldEntry = remember(navHost.currentBackStackEntry) {
                    navHost.getBackStackEntry("scaffoldtrailpack")
                }
                val mapaViewModel: MapaViewModel = viewModel(scaffoldEntry)

               VistaCrearRuta(
                   mapaviewModel = mapaViewModel,
                   mainviewModel = mainViewModel,
                   onBack = {enrutador.popBack()}
               )

           }

           composable(
               route = "detalleruta/{rutaId}",
               arguments = listOf(navArgument("rutaId") {type = NavType.StringType})
           ){ backstackEntry ->

               val scaffoldEntry = remember(navHost.currentBackStackEntry){
                   navHost.getBackStackEntry("scaffoldtrailpack")
               }
               val mapaviewModel: MapaViewModel = viewModel(scaffoldEntry)

               val rutaId = backstackEntry.arguments?.getString("rutaId") ?: ""

               val rutaSeleccionada = mapaviewModel.rutasparquenatural.find { it.idruta == rutaId }
                                    ?: mapaviewModel.rutaCargadaPorId?.takeIf { it.idruta == rutaId }

               LaunchedEffect(rutaId) {
                   if (rutaSeleccionada == null) {
                       mapaviewModel.cargarRutaPorID(rutaId)
                   }
               }

               if (rutaSeleccionada != null) {
                   VistaRutaDetalladaMapa(
                       ruta = rutaSeleccionada,
                       mapaviewModel = mapaviewModel,
                       onPublicarClick = {
                           mapaviewModel.togglePopupPublicacion(true, rutaSeleccionada)
                       },
                       onBack = {
                           mapaviewModel.seleccionarRutaParaDetalle(null)
                           enrutador.popBack()
                       },
                       mainviewModel = mainViewModel
                   )
               } else {
                   Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                       CircularProgressIndicator()
                   }
               }
           }

           composable(
               route = "detalleactividad/{actividadId}/{mostrarAcciones}",
               arguments = listOf(
                   navArgument("actividadId") {type = NavType.StringType},
                   navArgument("mostrarAcciones") {type = NavType.BoolType}
               )
           ){ backstackEnty ->
               val actividadId = backstackEnty.arguments?.getString("actividadId") ?: ""
               val mostrarAcciones = backstackEnty.arguments?.getBoolean("mostrarAcciones") ?: true
               VistaDetalleActividad(
                   actividadId=actividadId,
                   mainviewModel = mainViewModel,
                   mostrarAcciones = mostrarAcciones,
                   onBack = { enrutador.popBack()},
                   onCreadorClick = {uid -> enrutador.navToPerfilPublico(uid)}
               )
           }
       }
   }
}
