package com.julen.trailpack.vistas.perfilusuario

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.julen.trailpack.data.UserRepository
import com.julen.trailpack.modelos.Usuario

class PerfilUsuarioViewModel: ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    //El estado que observara la Vista
    var usuarioState by mutableStateOf(Usuario())
    var isLoading by mutableStateOf(true)

    init {
        obtenerDatosUsuario()
    }
    private fun obtenerDatosUsuario() {
        val uid = auth.currentUser?.uid

        if(uid != null) {
            db.collection("usuarios").document(uid).get()
                .addOnSuccessListener { docTask ->
                    val user = docTask.toObject(Usuario::class.java)
                    if (user != null) {
                        usuarioState = user
                    }
                    isLoading = false
                }
                .addOnFailureListener {
                    isLoading = false
                }
        }
    }

    //---- PARA EL FORMULARIO COMPLETA TUS DATOS ----
    var perfilFormModel by mutableStateOf(PerfilFormModel())
    fun updatePerfilFormModel(newFormState: PerfilFormModel){
        perfilFormModel = newFormState
    }

    private val userRepository = UserRepository()

    fun guardarPerfilClick(uid: String,onResult: (Boolean, String?) -> Unit){
        isLoading = true
        val actualizaciones = mapOf(
            "prefijo" to perfilFormModel.prefijo,
            "telefono" to perfilFormModel.telefono,
            "direccion" to perfilFormModel.direccion,
            "biografia" to perfilFormModel.biografia,
            "perfilcompletado" to true
        )
        userRepository.actualizarPerfil(uid,actualizaciones) {success, error ->
            isLoading = false
            onResult(success,error)
        }
    }


}