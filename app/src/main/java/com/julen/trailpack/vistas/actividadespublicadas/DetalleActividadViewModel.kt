package com.julen.trailpack.vistas.actividadespublicadas

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.julen.trailpack.data.ActividadesRepository
import com.julen.trailpack.data.MapsRepository
import com.julen.trailpack.data.UserRepository
import com.julen.trailpack.modelos.ActividadConRuta
import com.julen.trailpack.modelos.Usuario

class DetalleActividadViewModel: ViewModel() {
    private val actividadesRepository = ActividadesRepository()
    private val mapasRepository = MapsRepository()
    private val userRepository = UserRepository()

    var actividadconruta by mutableStateOf<ActividadConRuta?>(null)
    var participantes by mutableStateOf<List<Usuario>>(emptyList())
    var isLoading by mutableStateOf(true)

    fun cargarDetalles(actividadId: String) {
        isLoading = true

        actividadesRepository.repoObtenerActividadPorId(actividadId) {actividad, error ->
            if(actividad != null ){
                //Obtengo la ruta asociada
                mapasRepository.repoObtenerUnaRutaPorId(actividad.idruta){ruta, error ->
                    actividadconruta = ActividadConRuta(actividad,ruta)

                    //obtengo los datos de los participantes
                    cargarParticipantes(actividad.listaparticipantesIds)
                }
            }else{
                isLoading = false
            }
        }
    }

    private fun cargarParticipantes(ids: List<String>) {
        val usuariosCargados = mutableListOf<Usuario>()
        var pendientes = ids.size

        if(pendientes == 0) {
            participantes = emptyList()
            isLoading = false
            return
        }

        ids.forEach { uid ->
            userRepository.obtenerUsuario(uid) {user, _ ->
                if(user != null)
                    usuariosCargados.add(user)
                pendientes--
                if(pendientes == 0) {
                    participantes = usuariosCargados
                    isLoading = false
                }
            }
        }

    }


}