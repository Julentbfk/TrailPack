package com.julen.trailpack.vistas.marcogeneral

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.julen.trailpack.data.UserRepository
import com.julen.trailpack.modelos.Usuario

class MainViewModel : ViewModel(){

    //region UI GLOBAL
    var selectedTab by mutableStateOf(2)
    //endregion

    // region ESTADO GLOBAL DE ATUTENTIFICACION
    var isUserLoggedIn by mutableStateOf(false)
    var isCheckingAuth by mutableStateOf(true) //Loader global

    init {
        checkAuthStatus()
    }

    private fun checkAuthStatus() {
        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        isUserLoggedIn = user != null && user.isEmailVerified
        isCheckingAuth = false
    }
    //enregion

    //region PERFIL GLOBAL
    var usuarioGlobal by mutableStateOf<Usuario?>(null)
    private val userRepository  = UserRepository()
    fun cargaPerfilGlobal(uid:String) {
        userRepository.obtenerUsuario(uid) {user, error ->
            if(user != null){
                usuarioGlobal = user
            }else{
                Log.d("errorUsuarioGlobal", "error al cargar el usuarioGlobal desde MainViewModel $error")
            }
        }
    }

    //endregion
}