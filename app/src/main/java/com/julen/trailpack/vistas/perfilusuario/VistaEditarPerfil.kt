package com.julen.trailpack.vistas.perfilusuario

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddComment
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Male
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseAuth
import com.julen.trailpack.modelos.Pais
import com.julen.trailpack.vistas.componentes.formulario.OutlinedTextFieldMejorado
import com.julen.trailpack.vistas.componentes.formulario.SelectorPrefijoPais
import com.julen.trailpack.vistas.utiles.ValidadorCampos

@Composable
fun VistaEditarPerfil (
    editarPerfilClick: () -> Unit,
    cancelarEditClick: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    var guardarConExito by remember { mutableStateOf(false) }
    val listaPaises = remember {
        listOf(
            Pais("España", "ES", "+34"),
            Pais("Francia", "FR", "+33"),
            Pais("Portugal", "POR", "+351")
        )
    }
    val viewModel: PerfilUsuarioViewModel = viewModel()
    val formModel = viewModel.editarperfilFormModel

    LaunchedEffect(viewModel.usuarioState) {
        if(viewModel.usuarioState.uid.isNotEmpty()){
            viewModel.cargarDatosEnEditarPerfilForm(viewModel.usuarioState)
        }
    }
    //Este launched effect se lanza cuando se le da a guardar y hace que el ussuario pueda ver el num cambios y despues le mande a la pantalla de perfil
    LaunchedEffect(guardarConExito) {
        if (guardarConExito) {
            val numCambios = viewModel.contarDiferencias()
            snackbarHostState.showSnackbar("Se han modificado $numCambios campos")
            //Esperamos un poco para que el usuario lea el mensaje
            kotlinx.coroutines.delay(500)
            editarPerfilClick() //Navegamos
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->


        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text("Editar Perfil", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))

            //CAMPOS NO EDITABLES
            OutlinedTextFieldMejorado(
                value = formModel.username,
                onValueChange = {},
                label = "Username",
                icon = Icons.Default.AccountCircle,
                readOnly = true
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextFieldMejorado(
                value = formModel.email,
                onValueChange = {},
                label = "Email",
                icon = Icons.Default.Email,
                readOnly = true
            )

            //CAMPOS EDITABLES
            OutlinedTextFieldMejorado(
                value = formModel.nombre,
                onValueChange = { viewModel.updateEditarPerfilForm(formModel.copy(nombre = it)) },
                label = "Nombre",
                icon = Icons.Default.Person,
                validador = { ValidadorCampos.validarUsuario(it) }
            )
            OutlinedTextFieldMejorado(
                value = formModel.apellidos,
                onValueChange = { viewModel.updateEditarPerfilForm(formModel.copy(apellidos = it)) },
                label = "Apellidos",
                icon = Icons.Default.Person,
                validador = { ValidadorCampos.validarUsuario(it) }
            )

            //TELEFONO
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                //Selector de prefijo
                Box(modifier = Modifier.weight(0.35f)) {
                    val paisSeleccionado =
                        listaPaises.find { it.prefijo == viewModel.editarperfilFormModel.prefijo }
                            ?: listaPaises[0]
                    SelectorPrefijoPais(
                        paises = listaPaises,
                        paisSeleccionado = paisSeleccionado,
                        onPaisSelected = { nuevoPais ->
                            Log.d(
                                "VALIDACION",
                                "Validando: prefijo=${nuevoPais.prefijo}, codigo=${nuevoPais.codigo}"
                            )
                            viewModel.updateEditarPerfilForm(
                                viewModel.editarperfilFormModel.copy(
                                    prefijo = nuevoPais.prefijo
                                )
                            )
                        },

                        )
                }

                Spacer(
                    modifier = Modifier
                        .width(8.dp)
                        .align(Alignment.CenterVertically)
                )
                //Selector de num telefono
                Box(modifier = Modifier.weight(0.65f)) {
                    OutlinedTextFieldMejorado(
                        modifier = Modifier.padding(top = 16.dp),
                        value = viewModel.editarperfilFormModel.telefono,
                        onValueChange = {
                            viewModel.updateEditarPerfilForm(
                                viewModel.editarperfilFormModel.copy(
                                    telefono = it
                                )
                            )
                        },
                        label = "Teléfono",
                        icon = Icons.Default.Call, // Asegúrate de importar el icono,
                        validador = { valor ->
                            ValidadorCampos.validarTelefono(
                                valor,
                                viewModel.editarperfilFormModel.prefijo
                            )
                        }
                    )
                }
            }
            //---------------------------------
            OutlinedTextFieldMejorado(
                value = formModel.genero,
                onValueChange = { viewModel.updateEditarPerfilForm(formModel.copy(genero = it)) },
                label = "Genero",
                icon = Icons.Default.Male,
            )
            OutlinedTextFieldMejorado(
                value = formModel.nacionalidad,
                onValueChange = { viewModel.updateEditarPerfilForm(formModel.copy(nacionalidad = it)) },
                label = "Nacionalidad",
                icon = Icons.Default.Flag,
                validador = { ValidadorCampos.validarUsuario(it) }
            )
            OutlinedTextFieldMejorado(
                value = formModel.ubicacion,
                onValueChange = { viewModel.updateEditarPerfilForm(formModel.copy(ubicacion = it)) },
                label = "Ubicacion",
                icon = Icons.Default.LocationOn,
                validador = { ValidadorCampos.validarUsuario(it) }
            )
            OutlinedTextFieldMejorado(
                value = formModel.biografia,
                onValueChange = { viewModel.updateEditarPerfilForm(formModel.copy(biografia = it)) },
                label = "Biografia",
                icon = Icons.Default.AddComment,
                validador = { ValidadorCampos.validarUsuario(it) }
            )


            Spacer(modifier = Modifier.height(24.dp))

            Row(modifier = Modifier.fillMaxWidth()) {

                TextButton(onClick = { cancelarEditClick() }, modifier = Modifier.weight(1f)) {
                    Text("Cancelar", color = MaterialTheme.colorScheme.error)
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = {
                        val uid = FirebaseAuth.getInstance().currentUser?.uid
                        if(uid != null ){
                            val numcambios = viewModel.contarDiferencias()
                            viewModel.guardarCambiosPerfil(uid){success, error ->
                                if(success) {
                                    guardarConExito = true
                                }else{
                                    Log.d("error","error al editar cambios tipo $error")
                                }
                            }
                        }
                    },
                    modifier = Modifier.weight(1f),
                    enabled = viewModel.hasChanges
                ) {
                    Text("Guardar Cambios")
                }

            }//Cierra el row de los botones

        }//Cierra Column principal del formulatio
    }//Cierra Scaffold

}

@Preview
@Composable
fun VistaEditarPerfilPreview(){
    VistaEditarPerfil ({}, {})
}