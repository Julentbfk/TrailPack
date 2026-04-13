package com.julen.trailpack.routing

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.julen.trailpack.vistas.actividadespublicadas.VistaDetalleActividad
import com.julen.trailpack.vistas.ajustes.VistaAjustes
import com.julen.trailpack.vistas.ajustes.cambiarpassword.VistaCambiarPassword
import com.julen.trailpack.vistas.marcogeneral.ScaffoldTrailPack
import com.julen.trailpack.vistas.login.VistaLogin
import com.julen.trailpack.vistas.mapa.MapaViewModel
import com.julen.trailpack.vistas.mapa.VistaRutaDetalladaMapa
import com.julen.trailpack.vistas.marcogeneral.MainViewModel
import com.julen.trailpack.vistas.perfilusuario.VistaCompletarPerfil
import com.julen.trailpack.vistas.perfilusuario.VistaEditarPerfil
import com.julen.trailpack.vistas.registro.VistaRegistro
import com.julen.trailpack.vistas.perfilusuario.VistaPerfilUsuario
import com.julen.trailpack.vistas.registro.VistaConfirmacionEmail

// ------------------ CLASE CONTROLADORA DE LAS VISTAS ----------------


@Composable
fun AppNavegation() {

    val navHost: NavHostController = rememberNavController()
    val enrutador: Enrutador = remember(navHost){ Enrutador(navHost) }
    val mainViewModel: MainViewModel = viewModel()


   if(mainViewModel.isCheckingAuth){
       Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
           CircularProgressIndicator()
       }
   }else{
       val startDestination = if(mainViewModel.isUserLoggedIn) "scaffoldtrailpack" else "login"
       NavHost(navHost, startDestination = startDestination){

           //Rutas lanzadas desde el login
           composable(route="login"){
               VistaLogin(
                   //btn ¿no tienes cuenta aun?
                   navToRegistro = { enrutador.navToRegistro() },
                   //btn Login
                   navToScaffoldTrailPack = {enrutador.navToScaffoldTrailPack()}
               )
           }

           //Rutas lanzadas desde el registro
           composable(route="registro"){
               VistaRegistro(
                   //btn ¿ya tienes cuenta? | btn Registro
                   navToLoginPopBack = {enrutador.popBack()},
                   //btn registro
                   navToConfirmacionEmail = {email -> enrutador.navToConfirmacionEmail(email)}
               )
           }

           //Rutas lanzadas desde confirmacion email
           composable(route="confirmacionemail/{email}"){ ruta ->
               val emailRecibido = ruta.arguments?.getString("email") ?: ""
               VistaConfirmacionEmail(
                   email = emailRecibido,
                   navToLogin = {enrutador.navToLogin()}
               )
           }

           //Rutas lanzadas desde el perfil
           composable(route="completarperfil"){
               VistaCompletarPerfil(
                   guardarClick = {
                       enrutador.navToScaffoldTrailPack()
                   }
               )
           }
           composable(route="perfilusuario"){
               VistaPerfilUsuario()
           }

           composable(route="scaffoldtrailpack"){
               ScaffoldTrailPack(navHost,mainViewModel)
           }

           //Rutas lanzadas desde editarperfil
           composable(route="editarperfil") {
               VistaEditarPerfil(
                   editarPerfilClick = {enrutador.navToScaffoldTrailPack()},
                   cancelarEditClick = {enrutador.popBack()}
               )
           }
           //rutas lanzadas desde ajustes
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

           //rutas lanzadas desde mapa
           composable(
               route = "detalleruta/{rutaId}",
               arguments = listOf(navArgument("rutaId") {type = NavType.StringType})
           ){ backstackEntry ->

               //Obtengo el navBackStackEntry de scaffold
               val scaffoldEntry = remember(navHost.currentBackStackEntry){
                   navHost.getBackStackEntry("scaffoldtrailpack")
               }
               //Obtengo el viewModel desde ese entry
               val viewModel: MapaViewModel = viewModel(scaffoldEntry)

               val rutaId = backstackEntry.arguments?.getString("rutaId") ?: ""
               val rutaSeleccionada = viewModel.rutasparquenatural.find{ it.idruta == rutaId }

               if(rutaSeleccionada != null ){
                   VistaRutaDetalladaMapa(
                       ruta = rutaSeleccionada,
                       viewModel = viewModel,
                       onPublicarClick = {
                           viewModel.togglePopupPublicacion(
                               true,
                               rutaSeleccionada
                           )
                       },
                       onBack = {
                           viewModel.seleccionarRutaParaDetalle(null)
                           navHost.popBackStack()
                       },
                       mainViewModel = mainViewModel
                   )
               }else{
                   // Si vemos esto en pantalla, confirmamos que el objeto no se encuentra
                   Text(text = "Error: Ruta no encontrada con ID: $rutaId")
               }
           }

           //Rutas lanzadas desde actividadesPublicadas
           composable(
               route = "detalleactividad/{actividadId}",
               arguments = listOf(navArgument("actividadId") {type = NavType.StringType})
           ){backstackEnty ->
               val actividadId = backstackEnty.arguments?.getString("actividadId") ?: ""

               VistaDetalleActividad(actividadId=actividadId,mainviewModel = mainViewModel)
           }


       }
   }



}