package com.julen.trailpack.data

import com.google.firebase.firestore.FirebaseFirestore
import com.julen.trailpack.vistas.perfilusuario.PerfilFormModel

class UserRepository {
    private val db = FirebaseFirestore.getInstance()

    fun actualizarPerfil(uid:String, campos: Map<String,Any>, onResult: (Boolean, String?) -> Unit){

        db.collection("usuarios")
            .document(uid)
            .update(campos)
            .addOnSuccessListener { onResult(true,null) }
            .addOnFailureListener { error -> onResult(false,error.localizedMessage) }
    }
}