package com.julen.trailpack.vistas.mapa

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.julen.trailpack.data.MapsRepository
import com.julen.trailpack.modelos.ParqueNatural
import com.julen.trailpack.modelos.Ruta

class MapaViewModel : ViewModel() {

    private val repository = MapsRepository()

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

}