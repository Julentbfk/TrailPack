package com.julen.trailpack.vistas.registro

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.julen.trailpack.data.AuthRepository
import com.julen.trailpack.vistas.utiles.ValidadorCampos

class RegistroViewModel : ViewModel() {
    val repository: AuthRepository = AuthRepository()
    var formstate by mutableStateOf(RegistroFormState())
    fun updateForm(newFormState: RegistroFormState){
        formstate = newFormState.copy(

        )
    }
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    var registroExitoso by mutableStateOf(false)//check para el regoistro
    fun registroClick(){
        //Checkeamos el usuario
        val errorUsuario = ValidadorCampos.validarUsuario(formstate.username)
        if(errorUsuario.isNotEmpty()){
            errorMessage = errorUsuario
            return
        }
        //Checkeamos el email
        val errorEmail = ValidadorCampos.validarEmail(formstate.email)
        if(errorEmail.isNotEmpty()){
            errorMessage = errorEmail
            return
        }
        //Checkeamos el passsword
        val errorPassword = ValidadorCampos.validarPassword(formstate.password)
        if(errorPassword.isNotEmpty()){
            errorMessage = ValidadorCampos.validarPassword(formstate.password)
            return
        }
        //Checkeamos que las passwords sean iguales
        if(!ValidadorCampos.validarMatch(formstate.password,formstate.repassword) ){
            errorMessage = "Las contraseñas no coinciden"
            return
        }

        isLoading = true
        //Registramos en Firebase si ha pasado todas las validaciones
        repository.registroUsuario(formstate.username,formstate.email,formstate.password){success, error ->
            isLoading = false
            if(success){
                registroExitoso = true
            }else{
                errorMessage = error
            }
        }

    }

}