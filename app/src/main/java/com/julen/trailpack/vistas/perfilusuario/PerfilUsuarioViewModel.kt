package com.julen.trailpack.vistas.perfilusuario

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.julen.trailpack.modelos.Usuario

class PerfilUsuarioViewModel: ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    //El estado que observara la Vista
    var usuarioState by mutableStateOf(Usuario())
    var isLoading by mutableStateOf(true)

    private fun obetenerDatosUsuario() {
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

    init {
        obetenerDatosUsuario()
    }
}