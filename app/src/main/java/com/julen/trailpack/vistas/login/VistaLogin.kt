package com.julen.trailpack.vistas.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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


@Composable
fun VistaLogin(navToRegistro:() -> Unit, navToPerfilUsuario:() -> Unit) {

    val viewModel: LoginViewModel = viewModel()

    Column(//modifier son estilos
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_trailpack),
            contentDescription = "Logo TrailPack",
            modifier = Modifier.size(400.dp)
        )
        OutlinedTextFieldMejorado(
            value = viewModel.email,
            onValueChange = { viewModel.email = it },
            label = "Email",
            icon = Icons.Default.Email,
            validador = { ValidadorCampos.validarEmail(input=it) }
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextFieldMejorado(
            value = viewModel.password,
            onValueChange = { viewModel.password = it },
            label = "Contraseña ",
            icon = Icons.Default.Lock,
            isPassword = true,
            validador = { ValidadorCampos.validarPassword(it) }
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (viewModel.isLoading) {
            CircularProgressIndicator() // El circulito de espera
        }
        viewModel.errorMessage?.let { error ->
            Text(text = error, color = Color.Red) // El mensaje de error de AuthRepository
        }

        Button(
            onClick = {

                val checkemail = ValidadorCampos.validarEmail(viewModel.email)
                val checkpassword = ValidadorCampos.validarPassword(viewModel.password)

                if(checkemail.isEmpty() && checkpassword.isEmpty()){
                    viewModel.loginClick {
                        navToPerfilUsuario()
                    }
                }
            },
            enabled = !viewModel.isLoading
        ){
            Text("INICIAR SESION")
        }

        Spacer(modifier = Modifier.height(32.dp))

        TextButton(onClick = navToRegistro){
            Text("¿No tienes cuenta? Regístrate aquí")
        }
    }
}

