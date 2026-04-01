package com.julen.trailpack.vistas.registro

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.julen.trailpack.data.AuthRepository
import com.julen.trailpack.vistas.utiles.ValidadorCampos

class RegistroViewModel : ViewModel() {
    val repository: AuthRepository = AuthRepository()

    var username by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var repassword by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    var registroExitoso by mutableStateOf(false)//check para el regoistro
    fun registroClick(){
        //Checkeamos el usuario
        var errorUsuario = ValidadorCampos.validarUsuario(username)
        if(errorUsuario.isNotEmpty()){
            errorMessage = errorUsuario
            return
        }
        //Checkeamos el email
        var errorEmail = ValidadorCampos.validarEmail(email)
        if(errorEmail.isNotEmpty()){
            errorMessage = errorEmail
            return
        }
        //Checkeamos el passsword
        var errorPassword = ValidadorCampos.validarPassword(password)
        if(errorPassword.isNotEmpty()){
            errorMessage = errorPassword
            return
        }
        //Checkeamos que las passwords sean iguales
        if(!ValidadorCampos.validarMatch(password,repassword) ){
            errorMessage = "Las contraseñas no coinciden"
            return
        }

        isLoading = true
        //Registramos en Firebase si ha pasado todas las validaciones
        repository.registroUsuario(username,email,password){success, error ->
            isLoading = false
            if(success){
                registroExitoso = true
            }else{
                errorMessage = error
            }
        }
    }
}