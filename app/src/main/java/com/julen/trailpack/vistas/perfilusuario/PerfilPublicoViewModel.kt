package com.julen.trailpack.vistas.perfilusuario

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.julen.trailpack.data.ActividadesRepository
import com.julen.trailpack.data.MapsRepository
import com.julen.trailpack.data.UserRepository
import com.julen.trailpack.modelos.ActividadConRuta
import com.julen.trailpack.modelos.Usuario

class PerfilPublicoViewModel: ViewModel() {

    private val userRepo = UserRepository()
    private val mapsRepo = MapsRepository()
    private val actividadesRepo = ActividadesRepository()

    var usuario by mutableStateOf<Usuario?>(null)
        private set
    var actividadesCreadas by mutableStateOf<List<ActividadConRuta>>(emptyList())
        private set
    var isLoading by mutableStateOf(false)
        private set

    fun cargarPerfil(uid: String)  {
        isLoading = true
        userRepo.obtenerUsuario(uid) {user,_ ->
            usuario = user
            if(user!=null) cargarActividades(uid) else isLoading = false
        }
    }

    private fun cargarActividades(uid: String) {
        actividadesRepo.repoObtenerActividadesPorCreador(uid) {actividades,_ ->

            //Checkeamos que devuelva algo
            if(actividades.isNullOrEmpty()){
                actividadesCreadas = emptyList()
                isLoading = false
                return@repoObtenerActividadesPorCreador
            }

            val rutaIds = actividades.mapNotNull { it.idruta }.distinct()

            mapsRepo.repoObtenerRutasPorId(rutaIds) {rutas, _ ->

                val rutasMap = rutas?.associateBy { it.idruta } ?: emptyMap()

                actividadesCreadas = actividades.mapNotNull { actividad ->
                    rutasMap[actividad.idruta]?.let { ruta ->
                        ActividadConRuta(actividad,ruta)
                    }
                }
                isLoading = false

            }

        }
    }

}