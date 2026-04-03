package com.julen.trailpack.vistas.registro

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.julen.trailpack.data.AuthRepository
import com.julen.trailpack.vistas.utiles.ValidadorCampos

class RegistroViewModel : ViewModel() {
    val repository: AuthRepository = AuthRepository()
    var registroFormstate by mutableStateOf(RegistroFormModel())
    fun updateForm(newFormState: RegistroFormModel){
        registroFormstate = newFormState.copy(

        )
    }
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    var registroExitoso by mutableStateOf(false)//check para el regoistro
    fun registroClick(){
        //Checkeamos el usuario
        val errorUsuario = ValidadorCampos.validarUsuario(registroFormstate.username)
        if(errorUsuario.isNotEmpty()){
            errorMessage = errorUsuario
            return
        }
        //Checkeamos el email
        val errorEmail = ValidadorCampos.validarEmail(registroFormstate.email)
        if(errorEmail.isNotEmpty()){
            errorMessage = errorEmail
            return
        }
        //Checkeamos el passsword
        val errorPassword = ValidadorCampos.validarPassword(registroFormstate.password)
        if(errorPassword.isNotEmpty()){
            errorMessage = ValidadorCampos.validarPassword(registroFormstate.password)
            return
        }
        //Checkeamos que las passwords sean iguales
        if(!ValidadorCampos.validarMatch(registroFormstate.password,registroFormstate.repassword) ){
            errorMessage = "Las contraseñas no coinciden"
            return
        }

        isLoading = true
        //Registramos en Firebase si ha pasado todas las validaciones
        repository.registroUsuario(registroFormstate.username,registroFormstate.email,registroFormstate.password){ success, error ->
            isLoading = false
            if(success){
                registroExitoso = true
            }else{
                errorMessage = error
            }
        }

    }

}