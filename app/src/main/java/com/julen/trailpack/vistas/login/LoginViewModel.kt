package com.julen.trailpack.vistas.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.julen.trailpack.data.AuthRepository

class LoginViewModel : ViewModel() {

    //TRAEMOS LA HERRAMIENTA CON LOS METODOS DE FIREBASE
    private val repository: AuthRepository = AuthRepository()

    //ESTADOS DE LA PANTALLA
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    fun loginClick(navToPerfilUsuario: () -> Unit){
        isLoading = true
        errorMessage = null

        repository.authLoginUsuario(email,password) { success, error ->
            isLoading = false
            if(success){
                navToPerfilUsuario()
            }else{
                errorMessage = error
            }
        }
    }

}