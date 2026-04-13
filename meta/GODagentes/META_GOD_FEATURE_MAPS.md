# [GOD_FILE: FEATURE_MAPS - TRAILPACK]
# ROL: EL LÍDER DE MAPAS
# INSTRUCCIÓN: Eres el experto en geolocalización, parques y rutas.
# REGLA: Tienes el código íntegro de la lógica de mapas. Úsalo para explicar el flujo paso a paso.

---

## [1. VIEWMODELS DE MAPAS]

### MapaViewModel.kt
```kotlin
package com.julen.trailpack.vistas.mapa

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.julen.trailpack.data.MapsRepository
import com.julen.trailpack.modelos.Actividad
import com.julen.trailpack.modelos.ParqueNatural
import com.julen.trailpack.modelos.Ruta
import com.julen.trailpack.vistas.marcogeneral.MainViewModel

class MapaViewModel : ViewModel() {
    private val repository = MapsRepository()

    var parquesnaturales by mutableStateOf<List<ParqueNatural>>(emptyList())
    var isLoading by mutableStateOf(true)

    init { obtenerParquesNaturales() }

    private fun obtenerParquesNaturales() {
        isLoading = true
        repository.repoObtenerParquesNaturales { listaParques, _ ->
            isLoading = false
            if (listaParques != null) parquesnaturales = listaParques
        }
    }

    var rutasparquenatural by mutableStateOf<List<Ruta>>(emptyList())
    var parqueSeleccionado by mutableStateOf<ParqueNatural?>(null)
    var rutaSeleccionada by mutableStateOf<Ruta?>(null)

    fun seleccionarParque(parque: ParqueNatural) {
        parqueSeleccionado = parque
        repository.repoObtenerRutasParqueNatural(parque.idparquenatural) { rutas, _ ->
            if (rutas != null) rutasparquenatural = rutas
        }
    }

    // Lógica de Publicación
    var isPublicacionPopupVisible: Boolean by mutableStateOf(false)
    var formPublicacion by mutableStateOf(PublicacionFormModel())

    fun togglePopupPublicacion(visible: Boolean, ruta: Ruta? = null) {
        isPublicacionPopupVisible = visible
        if (visible && ruta != null) rutaSeleccionada = ruta
    }

    fun publicarRuta(mainViewModel: MainViewModel) {
        val usuarioglobal = mainViewModel.usuarioGlobal
        val ruta = rutaSeleccionada
        if (usuarioglobal == null || ruta == null) return

        val nuevaActividad = Actividad(
            idruta = ruta.idruta,
            idcreador = usuarioglobal.uid,
            listaparticipantesIds = listOf(usuarioglobal.uid),
            nombrecreador = usuarioglobal.username ?: "Anónimo",
            fechasalida = formPublicacion.fechenmillis,
            maxparticipantes = formPublicacion.numparticipantes.toIntOrNull() ?: 10
        )

        repository.repoGuardarActividad(nuevaActividad) { success, _ ->
            if (success) {
                isPublicacionPopupVisible = false
                mainViewModel.showNotification("¡Ruta publicada!")
            }
        }
    }
}
```

---

## [2. VISTAS DE MAPAS]

### VistaMapa.kt
```kotlin
@Composable
fun VistaMapa (enrutador: Enrutador, mainviewModel: MainViewModel, mapaviewModel: MapaViewModel) {
    val camaraPositionState = rememberSaveable(saver = CameraPositionState.Saver) {
        CameraPositionState(position = CameraPosition.fromLatLngZoom(LatLng(40.4637, -3.7492), 5f))
    }
    Box(modifier = Modifier.fillMaxSize()) {
        MapaContent(mapaviewModel, camaraPositionState)
        if (mapaviewModel.parqueSeleccionado != null) {
            ListaRutasParqueBottomSheet(
                viewModel = mapaviewModel,
                navToRutaDetalladaMapa = { enrutador.navToRutaDetalladaMapa(it) }
            )
        }
    }
}
```

### PopUpPublicarRuta.kt (Diálogo real)
```kotlin
@Composable
fun PopUpPublicarRuta(viewModel: MapaViewModel, mainviewModel: MainViewModel) {
    if (!viewModel.isPublicacionPopupVisible) return
    Dialog(onDismissRequest = { viewModel.togglePopupPublicacion(false) }) {
        Surface(shape = RoundedCornerShape(16.dp)) {
            Column {
                Text("Opciones de publicación")
                SelectorFechaMejorado(...)
                OutlinedTextFieldMejorado(label = "Hora", ...)
                OutlinedTextFieldMejorado(label = "Participantes", ...)
                Button(onClick = { viewModel.publicarRuta(mainviewModel) }) { Text("Publicar") }
            }
        }
    }
}
```
