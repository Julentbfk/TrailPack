package com.julen.trailpack.vistas.perfilusuario

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.julen.trailpack.data.UserRepository
import com.julen.trailpack.modelos.Usuario

class PerfilUsuarioViewModel: ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    //El estado que observara la Vista
    var usuarioState by mutableStateOf(Usuario())
    var isLoading by mutableStateOf(true)
    private val userRepository = UserRepository()

    //Metodo que se lanza automaticamente al crear un objeto de tipo PerfilUsuarioViewModel
    init {
        obtenerDatosUsuario()
    }

    //Metodo para cargar el PerfilViewModel con los datos del Usuario en sesion
    private fun obtenerDatosUsuario() {
        val uid = auth.currentUser?.uid
        if (uid != null) {
            userRepository.obtenerUsuario(uid) { user, error ->
                if (user != null) {
                    usuarioState = user
                }
                isLoading = false
            }
        }
    }

    fun subirFotoPerfil(uid: String, imageUri: Uri, onResult: (Boolean, String?) -> Unit){
        isLoading = true

        userRepository.subirFotoPerfil(uid,imageUri) {success, error ->
            if(success){
                //Si sube bien
                obtenerDatosUsuario()
                onResult(true, null)
            }else{
                isLoading = false
                onResult(false,error)
            }
        }
    }

    //region ---- PARA EL FORMULARIO COMPLETA TUS DATOS ----
    var completarperfilFormModel by mutableStateOf(CompletarPerfilFormModel())
    fun updateCompletarPerfilFormModel(newFormState: CompletarPerfilFormModel){
        completarperfilFormModel = newFormState
    }

    fun completarPerfilClick(uid: String, onResult: (Boolean, String?) -> Unit){
        isLoading = true
        val actualizaciones = mapOf(
            "prefijo" to completarperfilFormModel.prefijo,
            "telefono" to completarperfilFormModel.telefono,
            "ubicacion" to completarperfilFormModel.ubicacion,
            "biografia" to completarperfilFormModel.biografia,
            "perfilcompletado" to true
        )
        Log.d("DEBUG_GUARDADO", "Telefono: ${completarperfilFormModel.telefono}")
        userRepository.actualizarPerfil(uid,actualizaciones) {success, error ->
            isLoading = false
            onResult(success,error)
        }
    }
    //endregion



    //region ---- PARA EL FORMULARIO BTN EDITAR PERFIL ----
    var editarperfilFormModel by mutableStateOf(EditarPerfilFormModel())



    //Metodo inicial que se lanzara nada mas cargar la vista editar perfil con los datos del usuario
    fun cargarDatosEnEditarPerfilForm(usuario: Usuario){
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
        initialFormModel  = modelo

    }
    //region Para el Check de que se han hecho cambios
        //Guardamos la foto del original
        var initialFormModel by mutableStateOf(EditarPerfilFormModel())
        //Usamos derivedStateOf para que compose sepa si hay cambios automaticamente
        val hasChanges by derivedStateOf { initialFormModel != editarperfilFormModel }
        //Creamos una funcion para contar cuantos campos se han modificado
        fun contarDiferencias(): Int {
            var contador = 0
            // Comparamos campo por campo entre el original y el actual
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
    //endregion

    //Metodo que cargar el formModel del perfil con los datos copiados de cada campo del formulario
    fun updateEditarPerfilForm(newFormState: EditarPerfilFormModel){
        editarperfilFormModel = newFormState
    }

    //Guardamos los datos nuevos en un mapa y se lo pasamos a la funcion actualizar Perfil que actualizara el usuario de la bd con los cambios nuevos
    fun guardarCambiosPerfil(uid: String,onResult: (Boolean, String?) -> Unit){

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

        userRepository.actualizarPerfil(uid,camposActualizados){success, error ->
            isLoading = false
            if(success){
                //Recargamos el usuarioState con los nuevos datos
                obtenerDatosUsuario()
            }
            onResult(success,error)
        }
    }

    //endregion


}