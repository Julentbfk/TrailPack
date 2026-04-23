package com.julen.trailpack.data

import android.net.Uri
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.julen.trailpack.modelos.Usuario

class UserRepository {
    private val db = FirebaseFirestore.getInstance()

    fun obtenerUsuario(uid: String, onResult: (Usuario?, String?) -> Unit) {
        db.collection("usuarios").document(uid).get()
            .addOnSuccessListener { document ->
                val user = document.toObject(Usuario::class.java)
                onResult(user, null)
            }
            .addOnFailureListener { e ->
                onResult(null, e.localizedMessage)
            }
    }

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


    //Funcion para seguir a usuarios, atomica si no se hace todo no se hace nada para evitar inconsistencias
    fun repoToggleFollow(
        uidActual: String,
        uidObjetivo: String,
        seguir: Boolean,
        onResult: (Boolean, String?) -> Unit
    ){
        val batch = db.batch()//Necesario para ejecutar varias operaciones de un golpe
        val docActual = db.collection("usuarios").document(uidActual)//El que sigue
        val docObjetivo = db.collection("usuarios").document(uidObjetivo)//El que sera seguido

        if(seguir) {
            batch.update(docActual,"listasiguiendo", FieldValue.arrayUnion(uidObjetivo))
            batch.update(docActual, "siguiendocount", FieldValue.increment(1))
            batch.update(docObjetivo, "listaseguidores", FieldValue.arrayUnion(uidActual))
            batch.update(docObjetivo, "seguidorescount", FieldValue.increment(1))
        } else {
            batch.update(docActual, "listasiguiendo", FieldValue.arrayRemove(uidObjetivo))
            batch.update(docActual, "siguiendocount", FieldValue.increment(-1))
            batch.update(docObjetivo, "listaseguidores", FieldValue.arrayRemove(uidActual))
            batch.update(docObjetivo, "seguidorescount", FieldValue.increment(-1))
        }

        //Ejecutamos todas las operaciones de forma atomica
        batch.commit()
            .addOnSuccessListener { onResult(true,null) }
            .addOnFailureListener { onResult(false,it.localizedMessage) }

    }

    //Funcion para cargar los multiples usuarios a los que sigues y los que te siguen en una sola llamada evitando hacer N llamadas por cada usuario individual de la lista
    fun repoObtenerUsuariosPorIds(ids: List<String>, onResult: (List<Usuario>?, String?) -> Unit) {

        if(ids.isEmpty()) {
            onResult(emptyList(),null)
            return
        }

        db.collection("usuarios").whereIn("uid",ids).get().addOnSuccessListener { queryDocumentSnapshots ->
            val usuarios = queryDocumentSnapshots.documents.mapNotNull { it.toObject(Usuario::class.java) }
            onResult(usuarios,null)

        }.addOnFailureListener { onResult(null, it.localizedMessage) }

    }


}