package com.julen.trailpack.vistas.ajustes

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.julen.trailpack.data.AuthRepository
import com.julen.trailpack.vistas.ajustes.cambiarpassword.CambiarPasswordFormModel


class AjustesViewModel: ViewModel() {
    private val authrepository = AuthRepository()
    var cambiarpasswordState by mutableStateOf(CambiarPasswordFormModel())
    var isLoading by mutableStateOf(true)


    fun updateCambiarPasswordFormModel(newFormState: CambiarPasswordFormModel) {
        cambiarpasswordState = newFormState
    }


    fun guardarCambiosPassword(onResult: (Boolean, String?) -> Unit) {
        isLoading = true

        if(cambiarpasswordState.passNueva != cambiarpasswordState.passNuevaConfirmar){
            onResult(false, "Las contraseñas no coinciden")
            return
        }
        authrepository.authCambiarPassword(
            cambiarpasswordState.passActual,
            cambiarpasswordState.passNueva
        ){success, error ->
            isLoading = false
            if(success){
                onResult(true,null)
            }else{
                onResult(false,error)
            }
        }
    }

    var showBorradoDialog by mutableStateOf(false)
    var textoConfimacion by mutableStateOf("")
    var passwordConfirmacionBorrado by mutableStateOf("")
    var errorBorrado by mutableStateOf<String?>(null) // Para mostrar el feedback

    fun borrarCuenta(uid: String, passActual: String,onResult: (Boolean, String?) -> Unit){
        isLoading = true

        authrepository.authBorrarCuenta(uid,passActual){success, error ->
            isLoading = false
            if(success){
                onResult(true,null)
            }else{
                onResult(false,error)
            }
        }
    }
}