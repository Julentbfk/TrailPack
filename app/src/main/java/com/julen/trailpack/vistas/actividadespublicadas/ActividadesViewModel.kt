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

}