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

    init {
        obtenerParquesNaturales()
    }

    private fun obtenerParquesNaturales() {
        isLoading = true
        repository.repoObtenerParquesNaturales { listaParques, error ->
            isLoading = false
            if (listaParques != null) {
                parquesnaturales = listaParques
            } else {
                Log.d("errorObtencionParques", "Error al obtener los parques naturales \n $error")
            }
        }
    }

    var rutasparquenatural by mutableStateOf<List<Ruta>>(emptyList())
    var rutaSeleccionada by mutableStateOf<Ruta?>(null)
    var parqueSeleccionado by mutableStateOf<ParqueNatural?>(null)
    var isLoadingRutas by mutableStateOf(false)

    fun seleccionarParque(parque: ParqueNatural) {
        isLoadingRutas = true
        parqueSeleccionado = parque
        rutasparquenatural = emptyList()

        repository.repoObtenerRutasParqueNatural(parque.idparquenatural) { rutas, error ->
            isLoadingRutas = false
            if (rutas != null) {
                rutasparquenatural = rutas
            } else {
                Log.d("Error rutas", "Error al cargar las rutas $error")
                rutasparquenatural = emptyList()
            }
        }
    }

    fun seleccionarRutaParaDetalle(ruta: Ruta?) {
        rutaSeleccionada = ruta
    }

    var isPublicacionPopupVisible: Boolean by mutableStateOf(false)

    fun togglePopupPublicacion(visible: Boolean, ruta: Ruta? = null) {
        isPublicacionPopupVisible = visible
        if (visible && ruta != null) {
            rutaSeleccionada = ruta
        }
    }

    var formPublicacion by mutableStateOf(PublicacionFormModel())
    fun updateFormPublicacion(newFormState: PublicacionFormModel) {
        formPublicacion = newFormState
    }

    fun publicarRuta(mainViewModel: MainViewModel) {
        val usuarioglobal = mainViewModel.usuarioGlobal
        val ruta = rutaSeleccionada

        if (usuarioglobal == null || ruta == null) {
            mainViewModel.showNotification("Error: Datos incompletos")
            return
        }

        isLoading = true

        val nuevaActividad = Actividad(
            idruta = ruta.idruta,
            idcreador = usuarioglobal.uid,
            listaparticipantesIds = listOf(usuarioglobal.uid),
            nombrecreador = usuarioglobal.username ?: "Anónimo",
            fotocreador = usuarioglobal.fotoperfil ?: "",
            fechasalida = formPublicacion.fechaenmillis,
            horasalida = formPublicacion.horasalidaenmillis,
            puntoencuentro = formPublicacion.puntoencuentro,
            maxparticipantes = formPublicacion.numparticipantes.toIntOrNull() ?: 10
        )

        repository.repoGuardarActividad(nuevaActividad) { success, error ->
            isLoading = false
            if (success) {
                isPublicacionPopupVisible = false
                rutaSeleccionada = null // <--- IMPORTANTE: Limpiamos la selección
                mainViewModel.showNotification("¡Ruta publicada con éxito!")
                
                // Forzamos la actualización de la lista de rutas
                parqueSeleccionado?.let { seleccionarParque(it) }
            } else {
                mainViewModel.showNotification("Error al publicar: $error")
            }
        }
    }
}
