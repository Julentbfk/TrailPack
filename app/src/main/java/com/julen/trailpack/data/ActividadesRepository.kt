package com.julen.trailpack.data

import com.google.firebase.firestore.FirebaseFirestore
import com.julen.trailpack.modelos.Actividad

class ActividadesRepository {

    private val db = FirebaseFirestore.getInstance()

    fun repoObtenerActividades(onResult: (List<Actividad>?, String?) -> Unit) {

        db.collection("actividades").get().addOnSuccessListener { query ->

            val actividades = query.documents.mapNotNull { documentSnapshot ->
                documentSnapshot.toObject(Actividad::class.java)
            }
            onResult(actividades,null)

        }.addOnFailureListener { error ->
            onResult(null, error.localizedMessage)
        }
    }

}