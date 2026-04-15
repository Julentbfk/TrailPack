package com.julen.trailpack.routing

import androidx.navigation.NavController

class Enrutador(private val navController: NavController) {

    //Nav to VistaRegistro
    fun navToRegistro(){
        navController.navigate("registro")
    }
    //Nav to VistaConfirmacionEmail
    fun navToConfirmacionEmail(email: String) {
        navController.navigate("confirmacionemail/$email")
    }


    //Nav to vistaLogin popBackEdition
    fun popBack() {
        //Si te registras te ha popback a login evitando crear mas espacio en memoria y no permitiendote volver al registro sin pulsar el boton
        navController.popBackStack()
    }
    fun navToLogin() {
        navController.navigate("login")
    }
    // Borra toda la pila y va a Login
    fun navToLoginClearStack() {
        navController.navigate("login") {
            popUpTo(0) { inclusive = true }
        }
    }

    //Nav to VistaCompletarPerfil
    fun navToCompletarPerfil() {
        navController.navigate("completarperfil") {
            popUpTo("scaffoldtrailpack") {inclusive = true}
        }
    }
    //Nav to VistaPerfilUsuario
    fun navToPerfilUsuario(){
        navController.navigate("perfilusuario")
    }
    //Nav to EditarPerfil
    fun navToEditarPerfil() {
        navController.navigate("editarperfil")
    }

    //Nav to Ajustes
    fun navToAjustes() {
        navController.navigate("ajustes")
    }
    //Nav to Cambiar Password
    fun navToCambiarPassword() {
        navController.navigate("cambiarpassword")
    }

    //Nav to ScaffoldTrailPack
    fun navToScaffoldTrailPack() {
        navController.navigate("scaffoldtrailpack") {
            popUpTo("login") {
                inclusive = true
            }
        }
    }


    //Nav to ruta detallada mapa
    fun navToRutaDetalladaMapa(rutaId: String) {
        navController.navigate("detalleruta/$rutaId")

    }


    //Nav to actividad detallada
    fun navToActividadDetallada(actividadId: String,mostrarAcciones: Boolean = true) {
        navController.navigate("detalleactividad/$actividadId/$mostrarAcciones")
    }
}