package com.julen.trailpack.vistas.mapa

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.julen.trailpack.data.MapsRepository
import com.julen.trailpack.modelos.Actividad
import com.julen.trailpack.modelos.Coordenada
import com.julen.trailpack.modelos.ParqueNatural
import com.julen.trailpack.modelos.Ruta
import com.julen.trailpack.vistas.marcogeneral.MainViewModel
import kotlin.math.pow

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
    fun cargarRutaPorID(id: String) {
        repository.repoObtenerUnaRutaPorId(id) {ruta, _ ->
            rutaCargadaPorId = ruta
        }
    }

    //region Creacion de ruta

    var waypointsRuta = mutableListOf<LatLng>()
    var distanciaRuta by mutableStateOf(0.0)
    var formCrearRuta by mutableStateOf(CrearRutaFormModel())

    fun agregarWaypoint(punto: LatLng){
        waypointsRuta.add(punto)
        distanciaRuta = calcularDistancia(waypointsRuta)
    }

    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    fun deshacerUltimoWaypoint(){
        waypointsRuta.removeLast()
        distanciaRuta = calcularDistancia(waypointsRuta)
    }

    fun limpiarCreacionRuta() {
        waypointsRuta.clear()
        distanciaRuta = 0.0
        formCrearRuta = CrearRutaFormModel()
    }

    fun updateFormCrearRuta(nuevo: CrearRutaFormModel){
        formCrearRuta = nuevo
    }


    //metodos apoyo
    fun calcularDistancia(waypointsList: List<LatLng>): Double {
        var total = 0.0
        for(i in 1 until waypointsList.size) {
            total += haversine(
                waypointsList[i-1].latitude, waypointsList[i-1].longitude,//punto anterior
                waypointsList[i].latitude, waypointsList[i].longitude // punto actual
            )
        }
        return Math.round(total * 100.0) / 100.0 //Redondeamos a 2 decimales

    }

    //Usamos la formula de haversine para medir la distancia entre puntos terraqueos
    private fun haversine(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Double {
        val R = 6371.0  // radio de la Tierra en km

        // Convertimos la diferencia de coordenadas a radianes
        // porque las funciones trigonométricas (sin, cos) trabajan en radianes, no en grados
        val dLat = Math.toRadians(lat2 - lat1)
        val dLng = Math.toRadians(lng2 - lng1)

        // Esta es la fórmula haversine en sí
        // 'a' es el cuadrado del semiseno del ángulo central entre los dos puntos
        val a = Math.sin(dLat/2).pow(2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(dLng/2).pow(2)

        // Convertimos ese ángulo a distancia multiplicando por el radio
        return 2 * R * Math.asin(Math.sqrt(a))
    }

    //Funcion crear ruta
    fun guardarRuta(mainviewModel: MainViewModel, onBack: () -> Unit){
        val usuario = mainviewModel.usuarioGlobal
        val parque = parqueSeleccionado

        if(usuario == null ||parque == null){
            mainviewModel.showNotification("Error: datos incompletos")
            return
        }
        if(formCrearRuta.nombre.isBlank() || waypointsRuta.size < 2) {
            mainviewModel.showNotification("Error: Añade un nombre y al menos 2 puntos")
            return
        }

        val idruta = "${parque.idparquenatural}_${usuario.uid}_${System.currentTimeMillis()}"

        val nuevaRuta = Ruta(
            idruta = idruta,
            idparquenatural = parque.idparquenatural,
            pais = parque.pais,
            region = parque.region,
            nombre = formCrearRuta.nombre,
            dificultad = formCrearRuta.dificultad,
            descripcion = formCrearRuta.descripcion,
            distancia = distanciaRuta,
            esOficial = false,
            creadorId = usuario.uid,
            nombreCreador = usuario.username ?: "Anónimo",
            coordenadas = waypointsRuta.map { Coordenada(lat = it.latitude, lng = it.longitude) }
        )

        repository.repoGuardarRuta(nuevaRuta) {success, error ->
            if(success) {
                limpiarCreacionRuta()
                seleccionarParque(parque)
                mainviewModel.showNotification("RUTA CREADA !!!")
                onBack()
            }else{
                mainviewModel.showNotification("Error al guardar ruta: $error")
            }
        }

    }
    //endregion


    //region Creacion de Actividad
    var isPublicacionPopupVisible: Boolean by mutableStateOf(false)

    fun togglePopupPublicacion(visible: Boolean, ruta: Ruta? = null) {
        isPublicacionPopupVisible = visible
        if (visible && ruta != null) {
            rutaSeleccionada = ruta
        }
    }

    var formPublicacion by mutableStateOf(PublicarRutaFormModel())
    fun updateFormPublicacion(newFormState: PublicarRutaFormModel) {
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

    //endregion

    //Funcion para cargar una ruta
    var rutaCargadaPorId by mutableStateOf<Ruta?>(null)



}
