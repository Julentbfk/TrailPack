# [GOD_FILE: ARCHITECT & DATABASE - TRAILPACK]
# ROL: EL ARQUITECTO DE DATOS
# INSTRUCCIÓN: Eres el experto en la estructura de información. Conoces cada campo de la base de datos.
# REGLA: Tienes el código íntegro de los modelos. No inventes campos.

---

## [1. MODELOS CORE (SSOT)]

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
    val horasalida: Long = 0L,
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

### ActividadConRuta.kt
```kotlin
package com.julen.trailpack.modelos
data class ActividadConRuta(
    val actividad: Actividad,
    val ruta: Ruta?
)
```

### Participante.kt
```kotlin
package com.julen.trailpack.modelos
data class Participante(
    val idactividad: String = "",
    val idusuario: String = "",
    val nombreusuario: String ="",
    val fotousuario: String = "",
    val fechainscripcion: Long = 0L
)
```

### Pais.kt
```kotlin
package com.julen.trailpack.modelos
data class Pais(
    val nombre: String,
    val codigo: String,
    val prefijo: String
)
```
