package com.julen.trailpack.vistas.componentes.formulario

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun OutlinedTextFieldMejorado(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector,
    isPassword: Boolean = false,
    validador: (String) -> (String) = { "" }
){
    var tocado : Boolean by remember { mutableStateOf(false) }//Variable para revisar que el componente ha tenido foco
    var mensajeError: String by remember { mutableStateOf("") }//Variable que lanzara el mensaje de error

    OutlinedTextField(
        value = value,
        onValueChange = {
            mensajeError = validador(it)//Validamos en tiempo real para que el error aparezca o desaparezca segun escriba
            onValueChange(it)//hace que value valga la tecla pulsada, pero no lo tendra en cuenta hasta la segunda tecla
        },
        label = {Text(label)},
        leadingIcon = { Icon(imageVector = icon, contentDescription = null) },
        visualTransformation = if(isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        isError = mensajeError.isNotEmpty(),
        modifier = Modifier.fillMaxWidth().onFocusChanged{ focusState ->
            //Esta funcion la necesito para que el error de campo vacio no salte la primera vez que toco el elemento si no cuando lo abandono habiendolo tocado y dejado vacio
            if(focusState.isFocused){//Cuando toque el campo
                tocado = true
            }else{//Cuando salga de el y toque otro por ejemplo
                if(tocado){
                    mensajeError = validador(value)
                }
            }
        },
        supportingText = {
            if(mensajeError.isNotEmpty()){
                Text(
                    text = mensajeError,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    )
}