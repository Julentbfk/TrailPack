package com.julen.trailpack.data;

import com.google.firebase.firestore.FirebaseFirestore
import com.julen.trailpack.modelos.Actividad
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

    fun repoGuardarActividad(nuevaActividad: Actividad, onResult: (Boolean, String? ) -> Unit) {

        db.collection("actividades").add(nuevaActividad).addOnSuccessListener { task ->
            task.update("idactividad",task.id)//Le da a la actividad el id que le pone firebase
            onResult(true,null)
        }.addOnFailureListener { error ->
            onResult(false,error.localizedMessage)
        }

    }

    fun repoObtenerRutasPorId(idsRuta: List<String>, onResult: (List<Ruta>?, String?) -> Unit){
        if(idsRuta.isEmpty()){
            onResult(emptyList(),null)
            return
        }

        db.collection("rutas").whereIn("idruta",idsRuta).get().addOnSuccessListener { querySnap ->
            val rutas = querySnap.documents.mapNotNull { it.toObject(Ruta::class.java) }
            onResult(rutas,null)
        }
        .addOnFailureListener { error ->
            onResult(null,error.localizedMessage)
        }
    }

    fun repoObtenerUnaRutaPorId(id: String, onResult: (Ruta?, String?) -> Unit) {
        db.collection("rutas").document(id).get()
            .addOnSuccessListener { documentSnapshot ->
                val ruta = documentSnapshot.toObject(Ruta::class.java)
                onResult(ruta, null)
            }
            .addOnFailureListener { error ->
                onResult(null, error.localizedMessage)
            }
    }
}
