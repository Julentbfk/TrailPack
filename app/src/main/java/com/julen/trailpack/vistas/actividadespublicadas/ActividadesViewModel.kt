package com.julen.trailpack.vistas.actividadespublicadas

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.julen.trailpack.data.ActividadesRepository
import com.julen.trailpack.data.MapsRepository
import com.julen.trailpack.modelos.Actividad
import com.julen.trailpack.modelos.ActividadConRuta
import java.time.Instant
import java.time.ZoneId

class ActividadesViewModel: ViewModel() {

    private val actividadesrepository = ActividadesRepository()
    private val mapasRepository = MapsRepository()


    //Estados observables para la UI
    var actividadesconruta by mutableStateOf<List<ActividadConRuta>>(emptyList())
    var actividades by mutableStateOf<List<Actividad>>(emptyList())
    var isLoading by mutableStateOf(true)

    //Nada mas cargar la pantalla cargamos las actividades
    init {
        cargarActividades()
    }

    //Funcion para cargar las actividades
    fun cargarActividades() {
        isLoading = true

        actividadesrepository.repoObtenerActividades { listaActividades, errorActividades ->
            if(listaActividades!=null){
                //Extraemos los id ruta por actividad
                val idsRuta  = listaActividades.map{it.idruta}.distinct()

                mapasRepository.repoObtenerRutasPorId(idsRuta) {listaRutas,errorRutas ->
                    isLoading = false
                    if(listaRutas!= null) {
                        //Join de actividades y rutas
                        actividadesconruta = listaActividades.map { actividad ->
                            ActividadConRuta(
                                actividad = actividad,
                                ruta = listaRutas.find { it.idruta == actividad.idruta }
                            )
                        }

                    }

                }//Cierra la callback de maprepository

            }

        }//Cierra la callback de actividadesrepository

    }//Cierra la funcion cargarActividades

    //Funcion para unirte a actividad desde la card
    fun gestionarParticipacionCard(actividadId: String, usuarioId: String, unirse: Boolean) {

        actividadesrepository.repoGestionarParticipacion(actividadId,usuarioId,unirse){success, error ->
            if(success) {
                //Refrescamos toda la lista para que se actualicen contadores y botones
                cargarActividades()
            }else{
                Log.e("DEBUG_LISTA", "Error al participar desde la card: $error")
            }
        }
    }


    //region Filtros de actividades

    //Funcion para ver si la actividad esta caducada
    fun actividadCaducada(actividad: Actividad): Boolean {
        Log.d("DEBUG_CADUCADA", "ID: ${actividad.idactividad} | fechasalida: ${actividad.fechasalida} | horasalida: ${actividad.horasalida}")
        return actividad.horasalida > 0L && actividad.horasalida < System.currentTimeMillis()
    }
    //Funcion para ver actividades publicadas
    fun getActividadesPublicadas(uid: String): List<ActividadConRuta> =
        actividadesconruta.filter { act ->
            act.actividad.idcreador != uid && uid !in act.actividad.listaparticipantesIds && !actividadCaducada(act.actividad)
        }

    //Funcion para ver actividades creadas por ti
    fun getActividadesCreadas(uid: String): List<ActividadConRuta> =
        actividadesconruta.filter { act ->
            act.actividad.idcreador == uid && !actividadCaducada(act.actividad)
        }
    //Funcion para ver actividades en las que estas unido
    fun getActividadesUnido(uid: String): List<ActividadConRuta> =
        actividadesconruta.filter { act->
            uid in act.actividad.listaparticipantesIds && act.actividad.idcreador != uid
        }

    //Funcion para ver actividades caducadas
    fun getActividadesCaducadas(uid: String): List<ActividadConRuta> =
        actividadesconruta.filter { act->
            actividadCaducada(act.actividad)
        }
    //endregion


}