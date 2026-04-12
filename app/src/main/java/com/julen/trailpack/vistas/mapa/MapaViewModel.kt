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

//region Creacion de parques naturales con sus rutas
    var parquesnaturales by mutableStateOf <List<ParqueNatural>>(emptyList())
    var isLoading by mutableStateOf(true)

    init {
        obtenerParquesNaturales()
    }

    private fun obtenerParquesNaturales(){
        isLoading = true
        repository.repoObtenerParquesNaturales { listaParques, error ->
            isLoading = false
            if(listaParques != null){
                parquesnaturales = listaParques
            }else{
                Log.d("errorObtencionParques", "Error al obtener los parques naturales \n $error")
            }
        }

    }


    var rutasparquenatural by mutableStateOf<List<Ruta>>(emptyList())
    var rutaSeleccionada by mutableStateOf<Ruta?>(null)
    var parqueSeleccionado by mutableStateOf<ParqueNatural?>(null)
    var isLoadingRutas by mutableStateOf(false) // Nuevo estado

    fun seleccionarParque(parque: ParqueNatural) {
        isLoadingRutas = true
        parqueSeleccionado = parque
        rutasparquenatural = emptyList()

        repository.repoObtenerRutasParqueNatural(parque.idparquenatural ) { rutas, error ->

            isLoadingRutas = false
            if(rutas != null ){
                rutasparquenatural = rutas
            }else{
                Log.d("Error rutas", "Error al cargar las rutas  $error")
                rutasparquenatural = emptyList()
            }

        }
    }
//endregion

//region Acciones de los card ruta

    fun seleccionarRutaParaDetalle(ruta: Ruta?){
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
        isLoading = true
        Log.d("DEBUG_DEBUG", "Entrando en publicarRuta")
        Log.d("DEBUG_DEBUG", "Usuario en VM: ${mainViewModel.usuarioGlobal?.username ?: "NULO"}")

        val usuarioglobal = mainViewModel.usuarioGlobal
        if (usuarioglobal == null) {
            Log.d("DEBUG_DEBUG", "ABORTANDO: usuarioGlobal es NULO")
            return // <--- Aquí se está yendo
        }

        Log.d("DEBUG_DEBUG", "Usuario OK, continuando...")
        //Mapeo a objeto Actividad
        val nuevaActividad = Actividad(
            idruta = rutaSeleccionada!!.idruta,
            idcreador = usuarioglobal.uid,
            nombrecreador = usuarioglobal.username ?: "Anonimo",
            fotocreador = usuarioglobal.fotoperfil ?: "",
            fechasalida = formPublicacion.fechenmillis,
            maxparticipantes = formPublicacion.numparticipantes.toIntOrNull() ?: 10,
            estado = "OPEN"
        )
        Log.d("actividad","Botonfunciona ${nuevaActividad.idruta} + ${nuevaActividad.nombrecreador} + ${nuevaActividad.maxparticipantes}")

        //Guardamos en db
        repository.repoGuardarActividad(nuevaActividad) {success, error ->
            isLoading = false
            if(success){
                Log.d("repo","Guardando {${nuevaActividad.estado}")
                isPublicacionPopupVisible = false
                //mostramos mensaje exitoso
                mainViewModel.showNotification("¡¡¡Ruta publicada con exito!!!")

            }else{
                mainViewModel.showNotification("¡¡¡ERROR AL PUBLICAR!!! \n $error")
                Log.d("errorGuardarActividad", "error al guardar la actividad ene firebase $error")
            }
        }

    }

//endregion


}