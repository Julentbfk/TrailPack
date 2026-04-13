package com.julen.trailpack.vistas.actividadespublicadas

import android.util.Log
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
        Log.d("DEBUG_DETALLE", "Iniciando carga para actividad ID: $actividadId")

        actividadesRepository.repoObtenerActividadPorId(actividadId) { actividad, error ->
            if (actividad != null) {
                Log.d("DEBUG_DETALLE", "Actividad encontrada: ${actividad.nombre}. ID Ruta: ${actividad.idruta}")
                
                // Obtengo la ruta asociada
                mapasRepository.repoObtenerUnaRutaPorId(actividad.idruta) { ruta, errorRuta ->
                    if (ruta != null) {
                        Log.d("DEBUG_DETALLE", "Ruta vinculada encontrada: ${ruta.nombre}")
                    } else {
                        Log.e("DEBUG_DETALLE", "Ruta NO encontrada para ID: ${actividad.idruta}. Error: $errorRuta")
                    }
                    
                    actividadconruta = ActividadConRuta(actividad, ruta)

                    // Obtengo los datos de los participantes
                    cargarParticipantes(actividad.listaparticipantesIds)
                }
            } else {
                Log.e("DEBUG_DETALLE", "Error al cargar la actividad: $error")
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

    //Funcion para unirte a actividad
    fun gestionarParticipacion(actividadId: String, usuarioId: String, unirse: Boolean){
        isLoading = true

        actividadesRepository.repoGestionarParticipacion(actividadId,usuarioId,unirse){success, error ->

            if(success){
                //si sale OK volvemos a cargar los detalles para refrescar la lista de participantes y el contador
                cargarDetalles(actividadId)
            }else {
                isLoading = false
                Log.e("DEBUG_PARTICIPATION", "Error al gestionar la participacion $error")
            }
        }

    }


}