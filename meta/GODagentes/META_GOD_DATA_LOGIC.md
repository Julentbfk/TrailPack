# [GOD_FILE: DATA & LOGIC - TRAILPACK]
# ROL: EL ARQUITECTO (Experto en Firebase, Kotlin y MVVM)
# INSTRUCCIÓN: Eres el Arquitecto. Tu misión es diseñar la lógica de datos.
# REGLA: Tienes aquí el código íntegro. No inventes campos, lee las data class.

---

## [1. MODELOS DE DATOS (SSOT)]

### Usuario.kt
```kotlin
package com.julen.trailpack.modelos
data class Usuario(
    val uid: String = "",
    val username: String = "",
    val email: String = "",
    val ubicacion: String = "",
    val prefijo: String = "+34",
    val telefono: String = "",
    val nombre: String ="",
    val apellidos: String ="",
    val genero: String="",
    val fechanac: String ="",
    val nacionalidad: String ="",
    val biografia: String = "",
    val fotoperfil: String ="",
    val nivel: Int = 0,
    val fechacreacion: Long = System.currentTimeMillis(),
    val verificado: Boolean = false,
    val perfilcompletado: Boolean = false,
    val seguidorescount: Int = 0,
    val siguiendocount: Int = 0,
    val amigoscount: Int = 0,
    val listaseguidores: List<String> = emptyList(),
    val listasiguiendo: List<String> = emptyList(),
    val listaamigos: List<String> = emptyList()
)
```

### Actividad.kt
```kotlin
package com.julen.trailpack.modelos
data class Actividad(
    val idactividad : String = "",
    val idruta: String ="",
    val idcreador: String = "",
    val listaparticipantesIds: List<String> = emptyList(),
    val nombrecreador: String = "",
    val nombre: String = "",
    val fotocreador: String ="",
    val puntoencuentro: String="",
    val fechasalida: Long = 0L,
    val maxparticipantes: Int = 10,
    val participantes: Int = 1,
    val espublica: Boolean = true,
    val estado: String = "ABIERTA"
)
```

### Ruta.kt
```kotlin
package com.julen.trailpack.modelos
import com.google.firebase.firestore.GeoPoint
data class Ruta(
    val idruta: String = "",
    val idparquenatural: String = "",
    val nombre: String = "",
    val dificultad: String = "",
    val distancia: Double = 0.0,
    val duracion: String = "",
    val desnivel: Int = 0,
    val creadorId: String = "",
    val nombreCreador: String = "",
    val descripcion: String = "",
    val fotosRuta: List<String> = emptyList(),
    val coordenadas: List<GeoPoint> = emptyList(),
    val fechacreacion: Long = System.currentTimeMillis()
)
```

### ParqueNatural.kt
```kotlin
package com.julen.trailpack.modelos
data class ParqueNatural(
    val idparquenatural: String ="",
    val nombre: String ="",
    val descripcion: String ="",
    val region: String ="",
    val fotosparque: List<String> = emptyList(),
    val lat: Double =0.0,
    val lng: Double =0.0
)
```

---

## [2. REPOSITORIOS (LÓGICA FIREBASE)]

### ActividadesRepository.kt
```kotlin
class ActividadesRepository {
    private val db = FirebaseFirestore.getInstance()
    fun repoObtenerActividades(onResult: (List<Actividad>?, String?) -> Unit) {
        db.collection("actividades").get().addOnSuccessListener { query ->
            val actividades = query.documents.mapNotNull { it.toObject(Actividad::class.java) }
            onResult(actividades,null)
        }.addOnFailureListener { onResult(null, it.localizedMessage) }
    }
    fun repoObtenerActividadPorId(id: String, onResult: (Actividad?, String?) -> Unit) {
        db.collection("actividades").document(id).get().addOnSuccessListener {
            onResult(it.toObject(Actividad::class.java), null)
        }.addOnFailureListener { onResult(null, it.localizedMessage) }
    }
}
```

### MapsRepository.kt (Resumen de firmas críticas)
```kotlin
fun repoObtenerParquesNaturales(onResult: (List<ParqueNatural>?, String?) -> Unit)
fun repoObtenerRutasParqueNatural(idParque:String, onResult: (List<Ruta>?, String?) -> Unit)
fun repoGuardarActividad(nuevaActividad: Actividad, onResult: (Boolean, String?) -> Unit)
fun repoObtenerRutasPorId(idsRuta: List<String>, onResult: (List<Ruta>?, String?) -> Unit)
fun repoObtenerUnaRutaPorId(id: String, onResult: (Ruta?, String?) -> Unit) // Busca por whereEqualTo("idruta", id)
```

---

## [3. VIEWMODELS (LÓGICA MVVM)]

### DetalleActividadViewModel.kt (Código íntegro)
```kotlin
class DetalleActividadViewModel: ViewModel() {
    private val actividadesRepository = ActividadesRepository()
    private val mapasRepository = MapsRepository()
    private val userRepository = UserRepository()

    var actividadconruta by mutableStateOf<ActividadConRuta?>(null)
    var participantes by mutableStateOf<List<Usuario>>(emptyList())
    var isLoading by mutableStateOf(true)

    fun cargarDetalles(actividadId: String) {
        isLoading = true
        actividadesRepository.repoObtenerActividadPorId(actividadId) { actividad, _ ->
            if (actividad != null) {
                mapasRepository.repoObtenerUnaRutaPorId(actividad.idruta) { ruta, _ ->
                    actividadconruta = ActividadConRuta(actividad, ruta)
                    cargarParticipantes(actividad.listaparticipantesIds)
                }
            } else { isLoading = false }
        }
    }

    private fun cargarParticipantes(ids: List<String>) {
        val usuariosCargados = mutableListOf<Usuario>()
        var pendientes = ids.size
        if(pendientes == 0) { participantes = emptyList(); isLoading = false; return }
        ids.forEach { uid ->
            userRepository.obtenerUsuario(uid) {user, _ ->
                if(user != null) usuariosCargados.add(user)
                pendientes--; if(pendientes == 0) { participantes = usuariosCargados; isLoading = false }
            }
        }
    }
}
```
