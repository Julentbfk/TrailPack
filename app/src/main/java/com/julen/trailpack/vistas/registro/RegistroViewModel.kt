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
        isLoading = true
        //Registramos en Firebase si ha pasado todas las validaciones
        repository.authRegistroUsuario(registroFormstate.username,registroFormstate.email,registroFormstate.password){ success, error ->
            isLoading = false
            if(success){
                registroExitoso = true
            }else{
                errorMessage = error
            }
        }

    }

}