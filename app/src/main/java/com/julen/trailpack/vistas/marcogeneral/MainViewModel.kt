package com.julen.trailpack.vistas.marcogeneral

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import com.julen.trailpack.data.UserRepository
import com.julen.trailpack.modelos.Usuario

// CLASE EN FABRICACION, SE OCUPARA DE GESTIONAR ESTADOS GLOBALES COMO AUTH O SESION DE USUARIO CONECTADO
// POR EL MOMENTO SOLO GESTIONA LA NAVEGACION ENTRE PAGINAS Y REVISA SI EL USUARIO ESTA CONECTADO Y AUTENTIFICADO
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
    //endregion

    //region Estado global del usuario principal conectado
    private val userRepository = UserRepository()
    var usuarioGlobal by mutableStateOf<Usuario?>(null)

    fun cargarUsuarioGlobal(uid: String){
        Log.d("DEBUG_MAIN", "Iniciando carga para uid: $uid")
        userRepository.obtenerUsuario(uid) { user, error ->
            if (user != null) {
                Log.d("DEBUG_MAIN", "Usuario cargado: ${user.username}")
                usuarioGlobal = user
            } else {
                Log.e("DEBUG_MAIN", "Error cargando usuario: $error")
            }
        }
    }

    //metodo para añadir o quitar ruta de favoritos
    fun toggleRutaFavorita(idRuta: String) {
        val user = usuarioGlobal ?: return
        val listaActual = user.rutasfavoritas.toMutableList()

        if(idRuta in listaActual) listaActual.remove(idRuta) else listaActual.add(idRuta)
        userRepository.actualizarPerfil(user.uid, mapOf("rutasfavoritas" to listaActual)){success, error ->
            if(success) usuarioGlobal = user.copy(rutasfavoritas = listaActual)
        }
    }
    //endregion

    //region FORMATTER
    fun formatearFecha(millis: Long) : String {
        val fecha  = Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        return fecha
    }

    fun formatearHora(millis: Long) : String {
        val hora = Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("HH:mm"))
        return hora
    }

    fun combinarFechaYHora(fechaMillis: Long, hora: Int, minutos: Int): Long {
        val zonedDateTime = Instant.ofEpochMilli(fechaMillis)
            .atZone(ZoneId.systemDefault())
            .withHour(hora)
            .withMinute(minutos)
            .withSecond(0)
        return zonedDateTime.toInstant().toEpochMilli()
    }

    //endregion

    //region NOTIFICACIONES
    var notificationMessage by mutableStateOf<String?>(null)
    fun showNotification(mensaje: String) {
        notificationMessage = mensaje
    }

    //endregion


}