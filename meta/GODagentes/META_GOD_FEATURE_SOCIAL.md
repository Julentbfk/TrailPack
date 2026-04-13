# [GOD_FILE: FEATURE_SOCIAL - TRAILPACK]
# ROL: EL LÍDER SOCIAL
# INSTRUCCIÓN: Eres el experto en el Feed y la interacción de la comunidad.
# REGLA: Tienes el código íntegro de la lógica social. Úsalo para explicar el flujo paso a paso.

---

## [1. VIEWMODELS SOCIALES]

### ActividadesViewModel.kt (Feed Social)
```kotlin
package com.julen.trailpack.vistas.actividadespublicadas

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.julen.trailpack.data.ActividadesRepository
import com.julen.trailpack.data.MapsRepository
import com.julen.trailpack.modelos.Actividad
import com.julen.trailpack.modelos.ActividadConRuta

class ActividadesViewModel: ViewModel() {
    private val actividadesrepository = ActividadesRepository()
    private val mapasRepository = MapsRepository()

    var actividadesconruta by mutableStateOf<List<ActividadConRuta>>(emptyList())
    var isLoading by mutableStateOf(true)

    init { cargarActividades() }

    fun cargarActividades() {
        isLoading = true
        actividadesrepository.repoObtenerActividades { listaActividades, _ ->
            if(listaActividades != null){
                val idsRuta = listaActividades.map { it.idruta }.distinct()
                mapasRepository.repoObtenerRutasPorId(idsRuta) { listaRutas, _ ->
                    isLoading = false
                    if(listaRutas != null) {
                        actividadesconruta = listaActividades.map { actividad ->
                            ActividadConRuta(actividad, listaRutas.find { it.idruta == actividad.idruta })
                        }
                    }
                }
            }
        }
    }
}
```

### DetalleActividadViewModel.kt (Vista Profunda)
```kotlin
package com.julen.trailpack.vistas.actividadespublicadas

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
                pendientes--
                if(pendientes == 0) { participantes = usuariosCargados; isLoading = false }
            }
        }
    }
}
```

---

## [2. VISTAS SOCIALES]

### VistaRutasPublicadas.kt
```kotlin
@Composable
fun VistaRutasPublicadas(mainViewModel: MainViewModel, enrutador: Enrutador, actividadesviewModel: ActividadesViewModel) {
    LaunchedEffect(Unit) { actividadesviewModel.cargarActividades() }
    LazyColumn(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        items(actividadesviewModel.actividadesconruta) { item ->
            CardActividad(
                actividadConRuta = item,
                fechaFormateada = mainViewModel.formatearFecha(item.actividad.fechasalida),
                usuarioActualUid = mainViewModel.usuarioGlobal?.uid,
                onUnirseClick = { mainViewModel.showNotification("Unirse...") },
                onAbandonarClick = { mainViewModel.showNotification("Salir...") },
                onCardClick = { enrutador.navToActividadDetallada(item.actividad.idactividad) }
            )
        }
    }
}
```

### VistaDetalleActividad.kt (Lógica de Botones)
```kotlin
// Fragmento clave de la lógica condicional de botones:
val estaUnido = usuarioActualUid != null && actividad.listaparticipantesIds.contains(usuarioActualUid)

if (!estaUnido) {
    Button(onClick = { /* UNIRSE */ }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))) { Text("UNIRSE") }
} else {
    Button(onClick = { /* SALIR */ }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC62828))) { Text("SALIR") }
}
```
