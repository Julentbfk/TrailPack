package com.julen.trailpack.vistas.actividadespublicadas

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.julen.trailpack.data.ActividadesRepository
import com.julen.trailpack.modelos.Actividad

class ActividadesViewModel: ViewModel() {

    private val repository = ActividadesRepository()

    //Estados observables para la UI
    var actividades by mutableStateOf<List<Actividad>>(emptyList())
    var isLoading by mutableStateOf(true)

    //Nada mas cargar la pantalla cargamos las actividades
    init {
        cargarActividades()
    }

    //Funcion para cargar las actividades
    fun cargarActividades() {
        isLoading = true

        repository.repoObtenerActividades { listaActividades, error ->
            isLoading = false

            if(listaActividades!=null) {
                actividades = listaActividades
                Log.d("ACT_VM", "Actividades cargadas: ${actividades.size}")
            }else{
                Log.d("ACT_VM", "Error al cargar Actividades $error")
            }
        }
    }




}