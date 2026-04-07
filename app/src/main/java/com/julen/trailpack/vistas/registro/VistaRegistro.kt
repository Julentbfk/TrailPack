package com.julen.trailpack.vistas.registro

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.julen.trailpack.R
import com.julen.trailpack.vistas.componentes.formulario.OutlinedTextFieldMejorado
import com.julen.trailpack.vistas.utiles.ValidadorCampos

@Composable
fun VistaRegistro(navToLoginPopBack:() -> Unit, navToConfirmacionEmail:(String) -> Unit) {

    //viewModel para acceder a los datos
    val viewModel: RegistroViewModel = viewModel()

    //VENTANA PARA INFORMAR DE VERIFICAR CORREO
    LaunchedEffect(viewModel.registroExitoso) {
        if(viewModel.registroExitoso){
            navToConfirmacionEmail(viewModel.registroFormstate.email)
        }
    }
    //-------------------------------------------------


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Image(
                painter = painterResource(id = R.drawable.logo_trailpack),
                contentDescription = "Logo TrailPack",
                modifier = Modifier.size(300.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextFieldMejorado(
                value = viewModel.registroFormstate.username,
                onValueChange = { viewModel.updateForm(viewModel.registroFormstate.copy(username = it)) },
                label = "Nombre Usuario",
                icon = Icons.Default.AccountCircle,
                validador = { ValidadorCampos.validarUsuario(it) }
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextFieldMejorado(
                value = viewModel.registroFormstate.email,
                onValueChange = { viewModel.updateForm(viewModel.registroFormstate.copy(email = it)) },
                label = "Email",
                icon = Icons.Default.Email,
                validador = { ValidadorCampos.validarEmail(it) }
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextFieldMejorado(
                value = viewModel.registroFormstate.password,
                onValueChange = { viewModel.updateForm(viewModel.registroFormstate.copy(password = it)) },
                label = "Contraseña ",
                icon = Icons.Default.Lock,
                isPassword = true,
                validador = { ValidadorCampos.validarPassword(it) }
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextFieldMejorado(
                value = viewModel.registroFormstate.repassword,
                onValueChange = { viewModel.updateForm(viewModel.registroFormstate.copy(repassword = it)) },
                label = "Confirma contraseña ",
                icon = Icons.Default.Lock,
                isPassword = true,
                validador = {
                    if(ValidadorCampos.validarMatch(it,viewModel.registroFormstate.password) ) {
                        ""
                    }else {
                        "Las contraseñas no coinciden"
                    }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))
            if (viewModel.isLoading) {
                CircularProgressIndicator() // El circulito de espera
            }
            viewModel.errorMessage?.let { error ->
                Text(text = error, color = Color.Red) // El mensaje de error de AuthRepository
            }

            //------------------- BOTON DE REGISTRO -----------------------------------
            Button(onClick = {
                viewModel.registroClick()
            }, contentPadding = PaddingValues(10.dp), enabled = !viewModel.isLoading) {
                Text("REGISTRAR USUARIO")
            }

            Spacer(modifier = Modifier.height(24.dp))

            //------------------ INTERCAMBIADOR A VISTA LOGIN ------------------------
            TextButton(onClick = navToLoginPopBack) {
                Text("¿Ya estas registrado? Inicia sesión")
            }



        }//Cierra el column
    }




