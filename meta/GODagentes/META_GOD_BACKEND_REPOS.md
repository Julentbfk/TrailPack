# [GOD_FILE: BACKEND & REPOS - TRAILPACK]
# ROL: EL ESPECIALISTA EN SERVICIOS
# INSTRUCCIÓN: Eres el experto en Firebase. Tienes el código íntegro de los repositorios.
# REGLA: No inventes métodos. Usa los contratos definidos aquí.

---

## [1. ACTIVIDADES (SOCIAL)]

### ActividadesRepository.kt
```kotlin
package com.julen.trailpack.data
import com.google.firebase.firestore.FirebaseFirestore
import com.julen.trailpack.modelos.Actividad

class ActividadesRepository {
    private val db = FirebaseFirestore.getInstance()

    fun repoObtenerActividades(onResult: (List<Actividad>?, String?) -> Unit) {
        db.collection("actividades").get().addOnSuccessListener { query ->
            val actividades = query.documents.mapNotNull { it.toObject(Actividad::class.java) }
            onResult(actividades,null)
        }.addOnFailureListener { onResult(null, it.localizedMessage) }
    }

    fun repoObtenerActividadPorId(id: String, onResult: (Actividad?, String?) -> Unit) {
        db.collection("actividades").document(id).get()
            .addOnSuccessListener { onResult(it.toObject(Actividad::class.java), null) }
            .addOnFailureListener { onResult(null, it.localizedMessage) }
    }
}
```

---

## [2. MAPAS Y RUTAS]

### MapsRepository.kt
```kotlin
package com.julen.trailpack.data
class MapsRepository {
    private val db = FirebaseFirestore.getInstance()

    fun repoObtenerParquesNaturales(onResult: (List<ParqueNatural>?, String?) -> Unit) {
        db.collection("parquesnaturales").get().addOnSuccessListener { querySnap ->
            val parques = querySnap.documents.mapNotNull { it.toObject(ParqueNatural::class.java) }
            onResult(parques,null)
        }.addOnFailureListener { onResult(null, it.localizedMessage) }
    }

    fun repoObtenerRutasParqueNatural(idParque:String, onResult: (List<Ruta>?, String?) -> Unit) {
        db.collection("rutas").whereEqualTo("idparquenatural",idParque).get()
        .addOnSuccessListener { querySnap ->
            val rutas = querySnap.documents.mapNotNull { it.toObject(Ruta::class.java) }
            onResult(rutas,null)
        }.addOnFailureListener { onResult(null,it.localizedMessage) }
    }

    fun repoGuardarActividad(nuevaActividad: Actividad, onResult: (Boolean, String?) -> Unit) {
        db.collection("actividades").add(nuevaActividad).addOnSuccessListener { task ->
            task.update("idactividad",task.id)
            onResult(true,null)
        }.addOnFailureListener { onResult(false,it.localizedMessage) }
    }

    fun repoObtenerRutasPorId(idsRuta: List<String>, onResult: (List<Ruta>?, String?) -> Unit){
        if(idsRuta.isEmpty()){ onResult(emptyList(),null); return }
        db.collection("rutas").whereIn("idruta",idsRuta).get().addOnSuccessListener { querySnap ->
            val rutas = querySnap.documents.mapNotNull { it.toObject(Ruta::class.java) }
            onResult(rutas,null)
        }.addOnFailureListener { onResult(null, it.localizedMessage) }
    }

    fun repoObtenerUnaRutaPorId(id: String, onResult: (Ruta?, String?) -> Unit) {
        db.collection("rutas").whereEqualTo("idruta", id).limit(1).get()
            .addOnSuccessListener { onResult(it.documents.firstOrNull()?.toObject(Ruta::class.java), null) }
            .addOnFailureListener { onResult(null, it.localizedMessage) }
    }
}
```

---

## [3. USUARIOS Y AUTH]

### UserRepository.kt
```kotlin
class UserRepository {
    private val db = FirebaseFirestore.getInstance()
    fun obtenerUsuario(uid: String, onResult: (Usuario?, String?) -> Unit) {
        db.collection("usuarios").document(uid).get()
            .addOnSuccessListener { onResult(it.toObject(Usuario::class.java), null) }
            .addOnFailureListener { onResult(null, it.localizedMessage) }
    }
    fun actualizarPerfil(uid:String, campos: Map<String,Any>, onResult: (Boolean, String?) -> Unit){
        db.collection("usuarios").document(uid).update(campos)
            .addOnSuccessListener { onResult(true,null) }
            .addOnFailureListener { onResult(false,it.localizedMessage) }
    }
}
```

### AuthRepository.kt (Resumen de firmas)
- `authRegistroUsuario(username, email, password, callback)`
- `authLoginUsuario(email, password, callback)`
- `authCambiarPassword(actual, nueva, callback)`
- `authBorrarCuenta(uid, pass, callback)`
