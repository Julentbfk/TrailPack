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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
fun VistaCompletarPerfil(
    guardarClick: () -> Unit
) {

    val viewModel: PerfilUsuarioViewModel = viewModel()

    val listaPaises = remember {
        listOf(
            Pais("España", "ES", "+34"),
            Pais("Francia", "FR", "+33"),
            Pais("Portugal", "POR", "+351")
        )
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Completa tu perfil", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        // --- TELEFONO ---
        Row(
            modifier =  Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ){
            //Selector de prefijo
            Box(modifier = Modifier.weight(0.35f)){
                val paisSeleccionado = listaPaises.find { it.prefijo == viewModel.perfilFormModel.prefijo } ?: listaPaises[0]
                SelectorPrefijoPais(
                    paises = listaPaises,
                    paisSeleccionado = paisSeleccionado,
                    onPaisSelected = { nuevoPais ->
                        Log.d("VALIDACION", "Validando: prefijo=${nuevoPais.prefijo}, codigo=${nuevoPais.codigo}")
                        viewModel.updatePerfilFormModel(viewModel.perfilFormModel.copy(prefijo = nuevoPais.prefijo))
                    },

                )
            }

             Spacer(modifier = Modifier
                 .width(8.dp)
                 .align(Alignment.CenterVertically))
            //Selector de num telefono
            Box(modifier = Modifier.weight(0.65f)){
                OutlinedTextFieldMejorado(
                    modifier = Modifier.padding(top = 16.dp),
                    value = viewModel.perfilFormModel.telefono,
                    onValueChange = { viewModel.updatePerfilFormModel(viewModel.perfilFormModel.copy(telefono = it)) },
                    label = "Teléfono",
                    icon = Icons.Default.Call, // Asegúrate de importar el icono,
                    validador = { valor ->
                        ValidadorCampos.validarTelefono(valor, viewModel.perfilFormModel.prefijo)
                    }
                )
            }



        }
        Spacer(modifier = Modifier.height(16.dp))


        // --- DIRECCIÓN ---
        OutlinedTextFieldMejorado(
            value = viewModel.perfilFormModel.ubicacion,
            onValueChange = { viewModel.updatePerfilFormModel(viewModel.perfilFormModel.copy(ubicacion = it)) },
            label = "Localidad, Ciudad",
            icon = Icons.Default.LocationOn,
            modifier = Modifier,
            validador = { ValidadorCampos.validarUbicacion(it)}
        )
        Spacer(modifier = Modifier.height(16.dp))

        // --- BIOGRAFÍA ---
        OutlinedTextFieldMejorado(
            value = viewModel.perfilFormModel.biografia,
            onValueChange = { viewModel.updatePerfilFormModel(viewModel.perfilFormModel.copy(biografia = it)) },
            label = "Biografía",
            icon = Icons.Default.Person,
            modifier = Modifier,
            validador = { ValidadorCampos.validarBiografia(it) }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {
            val uid = FirebaseAuth.getInstance().currentUser?.uid
            if(uid != null){
                viewModel.guardarPerfilClick(uid){success,error ->
                    if(success){
                        //Metodo que redireccionara a donde sea despues de guardar
                        guardarClick()
                    }else{
                        //Error
                    }
                }
            }
        }){
            Text("Guardar Perfil")
        }
    }

}
@Preview
@Composable
fun VistaCompletarPerfilPreview(){
    VistaCompletarPerfil {}
}