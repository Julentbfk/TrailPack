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

    //region SOCIAL

    //Funcion que incrementa o decrementa a travaes del boton de follow unfollow
    fun actualizarContadorSeguidores(delta: Int) {
        // usuario?.copy() crea una copia del objeto con solo seguidorescount modificado
        usuario = usuario?.copy(seguidorescount = (usuario?.seguidorescount ?: 0) + delta)
    }

    //---- FUNCIONES PARA LAS LISTAS SEGUIDORES/SIGUIENDO/AMIGOS ----

    var listaSeguidores by mutableStateOf<List<Usuario>>(emptyList())
        private set
    var listaSiguiendo by mutableStateOf<List<Usuario>>(emptyList())
        private set

    //Loader para las listas
    var isLoadingList by mutableStateOf(false)
        private set

    //Carga los seguidores del perfil visitado
    fun cargarSeguidores() {
        val ids = usuario?.listaseguidores ?: return
        isLoadingList = true
        userRepo.repoObtenerUsuariosPorIds(ids) {usuarios,_ ->
            listaSeguidores = usuarios ?: emptyList()
            isLoadingList = false
        }
    }
    //Carga los usuarios que sigue el perfil visitado
    fun cargarSiguiendo() {
        val ids = usuario?.listasiguiendo ?: return
        isLoadingList = true
        userRepo.repoObtenerUsuariosPorIds(ids) { usuarios, _ ->
            listaSiguiendo = usuarios ?: emptyList()
            isLoadingList = false
        }
    }

}