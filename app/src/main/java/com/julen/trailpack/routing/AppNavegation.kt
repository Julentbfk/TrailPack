package com.julen.trailpack.routing

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.julen.trailpack.vistas.login.VistaLogin
import com.julen.trailpack.vistas.registro.VistaRegistro
import com.julen.trailpack.vistas.perfilusuario.VistaPerfilUsuario
import com.julen.trailpack.vistas.registro.VistaConfirmacionEmail

// ------------------ CLASE CONTROLADORA DE LAS VISTAS ----------------


@Composable
fun AppNavegation() {

    val navHost: NavHostController = rememberNavController()
    val enrutador: Enrutador = remember(navHost){ Enrutador(navHost) }

    NavHost(navHost, startDestination = "login"){

        //Rutas lanzadas desde el login
        composable(route="login"){
            VistaLogin(
                //btn ¿no tienes cuenta aun?
                navToRegistro = { enrutador.navToRegistro() },
                //btn Login
                navToPerfilUsuario = {enrutador.navToPerfilUsuario()}
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
        composable(route="perfilusuario"){
            VistaPerfilUsuario(
                //Btn cerrar sesion
                navToLoginPopBack = {enrutador.navToLoginPopBack()}
            )
        }


    }

}