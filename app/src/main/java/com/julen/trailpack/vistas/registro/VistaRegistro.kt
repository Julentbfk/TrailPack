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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.julen.trailpack.R
import com.julen.trailpack.vistas.componentes.OutlinedTextFieldMejorado
import com.julen.trailpack.vistas.utiles.ValidadorCampos
import kotlinx.coroutines.launch

@Composable
fun VistaRegistro(navToLoginPopBack:() -> Unit, navToConfirmacionEmail:(String) -> Unit) {

    //viewModel para acceder a los datos
    val viewModel: RegistroViewModel = viewModel()

    //VENTANA PARA INFORMAR DE VERIFICAR CORREO
    LaunchedEffect(viewModel.registroExitoso) {
        if(viewModel.registroExitoso){
            navToConfirmacionEmail(viewModel.email)
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
                value = viewModel.username,
                onValueChange = { viewModel.username = it },
                label = "Nombre Usuario",
                icon = Icons.Default.AccountCircle,
                validador = { ValidadorCampos.validarUsuario(it) }
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextFieldMejorado(
                value = viewModel.email,
                onValueChange = { viewModel.email = it },
                label = "Email",
                icon = Icons.Default.Email,
                validador = { ValidadorCampos.validarEmail(it) }
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextFieldMejorado(
                value = viewModel.password,
                onValueChange = { viewModel.password = it },
                label = "Contraseña ",
                icon = Icons.Default.Lock,
                isPassword = true,
                validador = { ValidadorCampos.validarPassword(it) }
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextFieldMejorado(
                value = viewModel.repassword,
                onValueChange = { viewModel.repassword = it },
                label = "Confirma contraseña ",
                icon = Icons.Default.Lock,
                isPassword = true,
                validador = { valoractual ->
                    if (valoractual == viewModel.password) {
                        ""
                    } else {
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




