package com.julen.trailpack.data;

import com.google.firebase.firestore.FirebaseFirestore
import com.julen.trailpack.modelos.Actividad
import com.julen.trailpack.modelos.ParqueNatural
import com.julen.trailpack.modelos.Ruta

class MapsRepository {
    private val db = FirebaseFirestore.getInstance()

    //Objeto donde almacenaremos los datos para ser recuperados evitando llamadas constantes a firebase
    companion object {
        private val cacheRutasPorParque  = HashMap<String, List<Ruta>>()
        private val cacheRutasPorId = HashMap<String, List<Ruta>> ()
        private val cacheUnaRutaPorId = HashMap<String,Ruta>()
        private var cacheParques: List<ParqueNatural>? = null
    }


    fun repoObtenerParquesNaturales(onResult: (List<ParqueNatural>?, String?) -> Unit) {

        cacheParques?.let { onResult(it, null); return }

        db.collection("parquesnaturales").get().addOnSuccessListener { querySnap ->
            //Mapeamos todos los resultados
            val parques = querySnap.documents.mapNotNull {documentSnapshot ->
                 documentSnapshot.toObject(ParqueNatural::class.java)
            }
            cacheParques = parques
            onResult(parques,null)
        }
            .addOnFailureListener { error ->
                onResult(null, error.localizedMessage)
            }
    }

    fun repoObtenerRutasParqueNatural(idParque:String, onResult: (List<Ruta>?, String?) -> Unit) {

        //Si ya se ha cargado la cache una vez y no ha habido cambios, chupa de aqui
        cacheRutasPorParque[idParque]?.let { onResult(it,null); return }

        //Si no habia datos cargados chupara de aqui
        db.collection("rutas").whereEqualTo("idparquenatural",idParque).get()
        .addOnSuccessListener { querySnap ->
            val rutas = querySnap.documents.mapNotNull { it.toObject(Ruta::class.java)}
            cacheRutasPorParque[idParque] = rutas
            onResult(rutas,null)
        }
        .addOnFailureListener { error ->
            onResult(null,error.localizedMessage)
        }
    }

    fun repoGuardarRuta(ruta: Ruta, onResult: (Boolean, String?) -> Unit) {
        db.collection("rutas").document(ruta.idruta).set(ruta)
            .addOnSuccessListener {
                limpiarCacheRutas()
                onResult(true,null)
            }
            .addOnFailureListener { error ->
                onResult(false,error.localizedMessage)
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
        val cacheKey = idsRuta.sorted().joinToString(",")
        cacheRutasPorId[cacheKey]?.let { onResult(it,null); return }

        db.collection("rutas").whereIn("idruta",idsRuta).get().addOnSuccessListener { querySnap ->
            val rutas = querySnap.documents.mapNotNull { it.toObject(Ruta::class.java) }
            cacheRutasPorId[cacheKey] = rutas
            rutas.forEach { cacheUnaRutaPorId[it.idruta] = it }
            onResult(rutas,null)
        }
        .addOnFailureListener { error ->
            onResult(null, error.localizedMessage)
        }
    }

    fun repoObtenerUnaRutaPorId(id: String, onResult: (Ruta?, String?) -> Unit) {

        cacheUnaRutaPorId[id]?.let { onResult(it,null); return}

        // Buscamos por el campo "idruta" para ser consistentes con repoObtenerRutasPorId
        db.collection("rutas").whereEqualTo("idruta", id).limit(1).get()
            .addOnSuccessListener { querySnapshot ->
                val ruta = querySnapshot.documents.firstOrNull()?.toObject(Ruta::class.java)
               if(ruta!= null) {
                   cacheUnaRutaPorId[id] = ruta
               }
                onResult(ruta,null)
            }
            .addOnFailureListener { error ->
                onResult(null, error.localizedMessage)
            }
    }

    //region Limpiamos cache

    fun limpiarCacheParques() {
        cacheParques = null
    }

    fun limpiarCacheRutasParuqe(idParque: String) {
        cacheRutasPorParque.remove(idParque)
    }

    fun limpiarCacheRutas() {
        cacheRutasPorParque.clear()
        cacheRutasPorId.clear()
        cacheUnaRutaPorId.clear()
    }

    fun limpiarCacheCompleta() {
        cacheParques = null
        cacheRutasPorParque.clear()
        cacheRutasPorId.clear()
        cacheUnaRutaPorId.clear()
    }

    //endregion

}
