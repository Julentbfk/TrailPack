package com.julen.trailpack.routing

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.julen.trailpack.vistas.componentes.marcogeneral.ScaffoldTrailPack
import com.julen.trailpack.vistas.login.VistaLogin
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

    //consultamos sesion activa en firebase
    val auth = FirebaseAuth.getInstance()
    val usuarioActual = auth.currentUser

    //Decidimos la ruta de inicio
    val puntoDeArranque = if(usuarioActual != null && usuarioActual.isEmailVerified){
        "scaffoldtrailpack"
    }else{
        "login"
    }

    NavHost(navHost, startDestination = puntoDeArranque){

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
                navToLoginPopBack = {enrutador.navToLoginPopBack()},
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
        composable(route="editarperfil") {
            VistaEditarPerfil(
                editarPerfilClick = {enrutador.navToScaffoldTrailPack()},
                cancelarEditClick = {enrutador.navToScaffoldTrailPack()}
            )
        }

        composable(route="scaffoldtrailpack"){
            ScaffoldTrailPack(navHost)
        }


    }

}