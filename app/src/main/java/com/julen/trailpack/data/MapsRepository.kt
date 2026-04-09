package com.julen.trailpack.data;

import com.google.firebase.firestore.FirebaseFirestore
import com.julen.trailpack.modelos.ParqueNatural
import com.julen.trailpack.modelos.Ruta

class MapsRepository {
    private val db = FirebaseFirestore.getInstance()

    fun repoObtenerParquesNaturales(onResult: (List<ParqueNatural>?, String?) -> Unit) {
        db.collection("parquesnaturales").get().addOnSuccessListener { querySnap ->
            //Mapeamos todos los resultados
            val parques = querySnap.documents.mapNotNull {documentSnapshot ->
                 documentSnapshot.toObject(ParqueNatural::class.java)
            }
            onResult(parques,null)
        }
            .addOnFailureListener { error ->
                onResult(null, error.localizedMessage)
            }
    }

    fun repoObtenerRutasParqueNatural(idParque:String, onResult: (List<Ruta>?, String?) -> Unit) {
        db.collection("rutas").whereEqualTo("idparquenatural",idParque).get()
        .addOnSuccessListener { querySnap ->
            val rutas = querySnap.documents.mapNotNull { documentSnapshot ->
                documentSnapshot.toObject(Ruta::class.java)
            }
            onResult(rutas,null)
        }
        .addOnFailureListener { error ->
            onResult(null,error.localizedMessage)
        }
    }
}
