package com.julen.trailpack.data

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class UserRepository {
    private val db = FirebaseFirestore.getInstance()

    fun actualizarPerfil(uid:String, campos: Map<String,Any>, onResult: (Boolean, String?) -> Unit){

        db.collection("usuarios")
            .document(uid)
            .update(campos)
            .addOnSuccessListener { onResult(true,null) }
            .addOnFailureListener { error -> onResult(false,error.localizedMessage) }
    }

    fun subirFotoPerfil(uid: String,imageUri: Uri, onResult: (Boolean, String?) -> Unit){

        //Referencia unica para las fotos del usuario
        val storageRef = FirebaseStorage.getInstance().reference.child("fotos_perfil/$uid.jpg")

        storageRef.putFile(imageUri).addOnSuccessListener {
            //Obtenemos la URL de la imagen
            storageRef.downloadUrl.addOnSuccessListener { uri ->
                //Guardamos esa URL en el campo "fotoperfil" del modelo Usuario
                db.collection("usuarios").document(uid)
                    .update("fotoperfil", uri.toString())
                    .addOnSuccessListener { onResult(true,null) }
                    .addOnFailureListener { e -> onResult(false,e.localizedMessage) }
            }
        }.addOnFailureListener { e ->
            onResult(false,e.localizedMessage)
        }
    }




}