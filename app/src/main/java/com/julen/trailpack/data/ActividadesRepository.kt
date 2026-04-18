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
    fun repoObtenerUnaActividadPorId(id: String, onResult: (Actividad?, String?) -> Unit) {
        db.collection("actividades").document(id).get()
            .addOnSuccessListener { documentSnapshot ->
                val actividad = documentSnapshot.toObject(Actividad::class.java)
                onResult(actividad, null)
            }
            .addOnFailureListener { error ->
                onResult(null, error.localizedMessage)
            }
    }
    //Funcion para obtener las actividades
    fun repoObtenerActividadesPorCreador(uid: String, onResult: (List<Actividad>?, String?) -> Unit){
        db.collection("actividades").whereEqualTo("idcreador",uid).get()
            .addOnSuccessListener { query ->
                val actividades = query.documents.mapNotNull { it.toObject(Actividad::class.java) }
                onResult(actividades, null)
            }.addOnFailureListener { error ->
                onResult(null,error.localizedMessage)
            }
    }

    fun repoGestionarParticipacion(
        idActividad:String,
        uidUsuario: String,
        unirse: Boolean,
        onResult: (Boolean,String?) -> Unit
    ){
        val docRef = db.collection("actividades").document(idActividad)

        db.runTransaction { transaction ->
            val snapshot = transaction.get(docRef)
            val participantesActuales = snapshot.getLong("participantes") ?: 0
            val listaIds = snapshot.get("listaparticipantesIds") as? List<String> ?: emptyList()
            val nuevaLista = listaIds.toMutableList()

            if(unirse) {
                if(!nuevaLista.contains(uidUsuario)){
                    nuevaLista.add(uidUsuario)
                    transaction.update(docRef,"listaparticipantesIds",nuevaLista)
                    transaction.update(docRef,"participantes",participantesActuales+1)
                }
            }else{
                if (nuevaLista.contains(uidUsuario)) {
                    nuevaLista.remove(uidUsuario)
                    transaction.update(docRef, "listaparticipantesIds", nuevaLista)
                    transaction.update(docRef, "participantes", (participantesActuales - 1).coerceAtLeast(0))
                }
            }
            null //La transaccion debe devolver algo
        }.addOnSuccessListener {
            onResult(true,null)
        }.addOnFailureListener {
            onResult(false, it.localizedMessage)
        }

    }

    //Funcion que elimina la actividad
    fun repoEliminarActividad (idActividad: String, onResult: (Boolean, String?) -> Unit) {
        db.collection("actividades").document(idActividad).delete()
        .addOnSuccessListener { onResult(true,null) }
        .addOnFailureListener { onResult(false,it.localizedMessage) }
    }

    //Funcion para actualizar los campos de usuario, usamos map para que puedas editar 1 o varias cosas dependiendo de la UI (Lapices o popup de edicion)
    fun repoActualizarActividad(
        idActividad: String,
        campos: Map<String, Any>, // Ej: mapOf("fechasalida" to 12345L, "puntoencuentro" to "Plaza")
        onResult: (Boolean, String?) -> Unit
    ) {
        db.collection("actividades").document(idActividad)
            .update(campos)
            .addOnSuccessListener { onResult(true, null) }
            .addOnFailureListener { onResult(false, it.localizedMessage) }
    }




}