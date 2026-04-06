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
    fun navToLoginPopBack() {
        //Si te registras te ha popback a login evitando crear mas espacio en memoria y no permitiendote volver al registro sin pulsar el boton
        navController.popBackStack()
    }
    fun navToLogin() {
        navController.navigate("login")
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

    //Nav to ScaffoldTrailPack
    fun navToScaffoldTrailPack() {
        navController.navigate("scaffoldtrailpack") {
            popUpTo("login") {
                inclusive = true
            }
        }
    }


}