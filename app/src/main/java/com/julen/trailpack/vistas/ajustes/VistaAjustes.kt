package com.julen.trailpack.vistas.ajustes

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseAuth
import com.julen.trailpack.routing.Enrutador
import com.julen.trailpack.vistas.componentes.formulario.OutlinedTextFieldMejorado
import com.julen.trailpack.vistas.utiles.ValidadorCampos

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VistaAjustes(navToPerfilUsuario: () -> Unit, navToCambiarPassword: () -> Unit, navToLoginClearStack: () -> Unit) {
    var viewModel: AjustesViewModel = viewModel()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Ajustes de la cuenta",
                style = MaterialTheme.typography.titleLarge // O el estilo que prefieras
            )

            IconButton(onClick = { navToPerfilUsuario()}) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Cerrar"
                )
            }
        }

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 16.dp),
            thickness = 1.dp,
            color = Color.Gray
        )
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {

            TextButton(
                onClick = {navToCambiarPassword()},
                modifier = Modifier.weight(1f)
            ) {

                Column(modifier = Modifier
                    .width(IntrinsicSize.Max)
                    .padding(0.dp)) {
                    Text(text = "Cambiar contraseña",fontWeight = FontWeight.Bold)
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = Color.DarkGray
                    )
                }
            }

            TextButton (
                onClick = { viewModel.showBorradoDialog = true },
                modifier = Modifier.weight(1f)
            ) {
                Column(modifier = Modifier
                    .width(IntrinsicSize.Max)
                    .padding(0.dp)) {
                    Text(text = "Eliminar cuenta",fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.error)
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = Color.DarkGray
                    )
                }

            }
        }
        if(viewModel.showBorradoDialog){
            AlertDialog(
                onDismissRequest = { viewModel.showBorradoDialog = false },
                title = { Text("¿Estás seguro?") },
                text = {
                    Column() {
                        Text("Esta accion es irreversible. Escribe 'BORRADO' abajo para confirmar.")
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextFieldMejorado(
                            value = viewModel.textoConfimacion,
                            onValueChange = {viewModel.textoConfimacion = it},
                            icon = Icons.Default.Delete,
                            label = "Escribe BORRADO",
                            validador = { ValidadorCampos.checkVacio(it) }
                        )
                        OutlinedTextFieldMejorado(
                            value = viewModel.passwordConfirmacionBorrado,
                            onValueChange = { viewModel.passwordConfirmacionBorrado = it },
                            label = "Contraseña actual",
                            icon = Icons.Default.Delete,
                            isPassword = true,
                            validador = { ValidadorCampos.validarPassword(it) }
                        )
                        viewModel.errorBorrado?.let { mensajeError ->
                            Text(
                                text = mensajeError,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            val uid = FirebaseAuth.getInstance().currentUser?.uid
                            if(uid != null){
                                viewModel.borrarCuenta(uid,viewModel.passwordConfirmacionBorrado) {success, error ->
                                    if(success) {
                                        viewModel.showBorradoDialog = false
                                        viewModel.textoConfimacion = ""//Limpiamos
                                        viewModel.passwordConfirmacionBorrado = ""
                                        //Le mandamos al login
                                        viewModel.errorBorrado = null
                                        navToLoginClearStack()
                                    }else{
                                        Log.d("error", "Error al borrar: \n $error")
                                        viewModel.errorBorrado = error ?: "Error desconocido"
                                    }
                                }
                            }
                        },
                        enabled = viewModel.textoConfimacion == "BORRADO"
                    ) {
                        Text("Borrar", color = MaterialTheme.colorScheme.error)
                    }
                },
                dismissButton = {
                    TextButton(onClick = {viewModel.showBorradoDialog = false}) {
                        Text("Cancelar")
                    }
                }
            )
        }

    }
}


@Preview
@Composable
fun  VistaAjustesPreview() {
   // VistaAjustes({ } )
}