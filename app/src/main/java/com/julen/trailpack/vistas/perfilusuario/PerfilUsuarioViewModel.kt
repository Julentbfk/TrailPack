package com.julen.trailpack.vistas.perfilusuario

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.julen.trailpack.data.MapsRepository
import com.julen.trailpack.data.UserRepository
import com.julen.trailpack.modelos.Ruta
import com.julen.trailpack.modelos.Usuario

class PerfilUsuarioViewModel : ViewModel() {

    private val userRepository = UserRepository()


    var isLoading by mutableStateOf(false)

    fun subirFotoPerfil(uid: String, imageUri: Uri, onResult: (Boolean, String?) -> Unit) {
        isLoading = true
        userRepository.subirFotoPerfil(uid, imageUri) { success, error ->
            isLoading = false
            onResult(success, error)
        }
    }

    //region ---- FORMULARIO COMPLETAR PERFIL ----
    var completarperfilFormModel by mutableStateOf(CompletarPerfilFormModel())
    fun updateCompletarPerfilFormModel(newFormState: CompletarPerfilFormModel) {
        completarperfilFormModel = newFormState
    }

    fun completarPerfilClick(uid: String, onResult: (Boolean, String?) -> Unit) {
        isLoading = true
        val actualizaciones = mapOf(
            "prefijo" to completarperfilFormModel.prefijo,
            "telefono" to completarperfilFormModel.telefono,
            "ubicacion" to completarperfilFormModel.ubicacion,
            "biografia" to completarperfilFormModel.biografia,
            "perfilcompletado" to true
        )
        Log.d("DEBUG_GUARDADO", "Telefono: ${completarperfilFormModel.telefono}")
        userRepository.actualizarPerfil(uid, actualizaciones) { success, error ->
            isLoading = false
            onResult(success, error)
        }
    }
    //endregion

    //region ---- FORMULARIO EDITAR PERFIL ----
    var editarperfilFormModel by mutableStateOf(EditarPerfilFormModel())
    var initialFormModel by mutableStateOf(EditarPerfilFormModel())
    val hasChanges by derivedStateOf { initialFormModel != editarperfilFormModel }

    fun contarDiferencias(): Int {
        var contador = 0
        if (initialFormModel.nombre != editarperfilFormModel.nombre) contador++
        if (initialFormModel.apellidos != editarperfilFormModel.apellidos) contador++
        if (initialFormModel.fechanac != editarperfilFormModel.fechanac) contador++
        if (initialFormModel.genero != editarperfilFormModel.genero) contador++
        if (initialFormModel.nacionalidad != editarperfilFormModel.nacionalidad) contador++
        if (initialFormModel.prefijo != editarperfilFormModel.prefijo) contador++
        if (initialFormModel.telefono != editarperfilFormModel.telefono) contador++
        if (initialFormModel.ubicacion != editarperfilFormModel.ubicacion) contador++
        if (initialFormModel.biografia != editarperfilFormModel.biografia) contador++
        return contador
    }

    fun cargarDatosEnEditarPerfilForm(usuario: Usuario) {
        val modelo = EditarPerfilFormModel(
            username = usuario.username,
            email = usuario.email,
            nacionalidad = usuario.nacionalidad,
            nombre = usuario.nombre,
            apellidos = usuario.apellidos,
            fechanac = usuario.fechanac,
            genero = usuario.genero,
            prefijo = usuario.prefijo,
            telefono = usuario.telefono,
            ubicacion = usuario.ubicacion,
            biografia = usuario.biografia
        )
        editarperfilFormModel = modelo
        initialFormModel = modelo
    }

    fun updateEditarPerfilForm(newFormState: EditarPerfilFormModel) {
        editarperfilFormModel = newFormState
    }

    fun guardarCambiosPerfil(uid: String, onResult: (Boolean, String?) -> Unit) {
        isLoading = true
        val camposActualizados = mapOf(
            "nombre" to editarperfilFormModel.nombre,
            "apellidos" to editarperfilFormModel.apellidos,
            "fechanac" to editarperfilFormModel.fechanac,
            "genero" to editarperfilFormModel.genero,
            "nacionalidad" to editarperfilFormModel.nacionalidad,
            "prefijo" to editarperfilFormModel.prefijo,
            "telefono" to editarperfilFormModel.telefono,
            "ubicacion" to editarperfilFormModel.ubicacion,
            "biografia" to editarperfilFormModel.biografia
        )
        userRepository.actualizarPerfil(uid, camposActualizados) { success, error ->
            isLoading = false
            onResult(success, error)
        }
    }
    //endregion

    //region CARGAR RUTAS FAVORITAS
    private val mapsRepository = MapsRepository()
    var rutasfavoritas by mutableStateOf<List<Ruta>>(emptyList())

    fun cargarRutasFavoritas(ids: List<String>) {
        mapsRepository.repoObtenerRutasPorId(ids){rutas, _ ->
            rutasfavoritas = rutas ?: emptyList()
        }
    }
    //enderegion

    //region SOCIAL

    //----  FUNCIONES PARA LAS LISTAS SEGUIDORES/SIGUIENDO/AMIGOS ----
    var listaSeguidores by mutableStateOf<List<Usuario>>(emptyList())
        private set
    var listaSiguiendo by mutableStateOf<List<Usuario>>(emptyList())
        private set
    var isLoadingList by mutableStateOf(false)
        private set

    //Lista de tus seguidores
    fun cargarSeguidores(ids: List<String>) {
        isLoadingList = true
        userRepository.repoObtenerUsuariosPorIds(ids) { usuarios, _ ->
            listaSeguidores = usuarios ?: emptyList()
            isLoadingList = false
        }
    }
    //Lista de la gente a la que sigues
    fun cargarSiguiendo(ids: List<String>) {
        isLoadingList = true
        userRepository.repoObtenerUsuariosPorIds(ids) { usuarios, _ ->
            listaSiguiendo = usuarios ?: emptyList()
            isLoadingList = false
        }
    }

    //endregion
}