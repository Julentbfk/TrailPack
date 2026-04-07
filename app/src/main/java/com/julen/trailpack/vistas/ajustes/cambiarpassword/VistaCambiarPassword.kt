package com.julen.trailpack.vistas.ajustes.cambiarpassword

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockReset
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.julen.trailpack.vistas.ajustes.AjustesViewModel
import com.julen.trailpack.vistas.componentes.formulario.OutlinedTextFieldMejorado
import com.julen.trailpack.vistas.utiles.ValidadorCampos
import kotlinx.coroutines.launch

@Composable
fun VistaCambiarPassword(guardarCambioPasswordClick: () -> Unit) {

    var viewModel: AjustesViewModel = viewModel()
    var formModel: CambiarPasswordFormModel = viewModel.cambiarpasswordState


    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope() // <--- IMPORTANTE

    Scaffold(
        snackbarHost = {SnackbarHost(snackbarHostState)}
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ){
            Spacer(modifier = Modifier.height(32.dp))
            Text(text = "Cambiar contraseña", style = MaterialTheme.typography.headlineSmall)
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 16.dp),
                thickness = 1.dp,
                color = Color.Gray
            )

            OutlinedTextFieldMejorado(
                value = formModel.passActual,
                onValueChange = {viewModel.updateCambiarPasswordFormModel(formModel.copy(passActual = it))},
                label = "Contraseña Actual",
                icon = Icons.Default.Lock,
                isPassword = true,
                validador = { ValidadorCampos.checkVacio(it) }
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextFieldMejorado(
                value = formModel.passNueva,
                onValueChange = {viewModel.updateCambiarPasswordFormModel(formModel.copy(passNueva = it))},
                label = "Contraseña Nueva",
                icon = Icons.Default.LockReset,
                isPassword = true,
                validador = { ValidadorCampos.validarPassword(it)}
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextFieldMejorado(
                value = formModel.passNuevaConfirmar,
                onValueChange = {viewModel.updateCambiarPasswordFormModel(formModel.copy(passNuevaConfirmar = it))},
                label = "Repite la contraseña",
                icon = Icons.Default.LockReset,
                isPassword = true,
                validador = {
                    if( ValidadorCampos.validarMatch(it,formModel.passNueva) ) {
                        ""
                    }else {
                        "Las contraseñas no coinciden"
                    }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.guardarCambiosPassword { success, error ->
                        scope.launch {
                            if (success) {
                                snackbarHostState.showSnackbar("Contraseña cambiada con éxito")
                                guardarCambioPasswordClick()
                            } else {
                                snackbarHostState.showSnackbar("Contraseña no coincide $error" )
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cambiar Contraseña")
            }

        }

    }

}

@Preview
@Composable
fun VistaCambiarPasswordPreview() {
    VistaCambiarPassword({})
}