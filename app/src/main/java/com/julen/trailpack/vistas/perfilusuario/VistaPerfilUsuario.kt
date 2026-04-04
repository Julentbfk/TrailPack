package com.julen.trailpack.vistas.perfilusuario


import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width

import androidx.compose.runtime.Composable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseAuth

import com.julen.trailpack.vistas.componentes.usuario.DatosPersonalesPerfil
import com.julen.trailpack.vistas.componentes.usuario.FotoPerfil
import com.julen.trailpack.vistas.componentes.usuario.NivelUsuarioPerfil
import com.julen.trailpack.vistas.componentes.usuario.SeguidoresUsuarioPerfil



//COMPOSABLE MARCO DONDE IRAN TODAS LAS PIEZAS
@Composable
fun VistaPerfilUsuario() {
    val viewModel: PerfilUsuarioViewModel = viewModel()
    val user = viewModel.usuarioState

    val context = LocalContext.current
    val uid = FirebaseAuth.getInstance().currentUser?.uid
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let{
            if(uid != null) {
                viewModel.subirFotoPerfil(uid,it){ success , error ->
                    if(success){
                        Toast.makeText(context,"Foto actualizada", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(context,"Error: $error",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {

        Row(//FILA DATOS
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                FotoPerfil(
                    url = viewModel.usuarioState.fotoperfil,
                    modifier = Modifier.clickable{launcher.launch("image/*")} //Al pulsar se abre la galeria
                )
                Spacer(modifier = Modifier.height(24.dp))
                NivelUsuarioPerfil(user.nivel)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier.weight(1f),
            ) {
                DatosPersonalesPerfil(user.username,user.ubicacion,user.biografia)
                Spacer(modifier = Modifier.height(8.dp))
                Row(//FILA SEGUIDORES
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    SeguidoresUsuarioPerfil("Seguidores",user.seguidorescount, modifier = Modifier.weight(1f))
                    SeguidoresUsuarioPerfil("Siguiendo",user.siguiendocount, modifier = Modifier.weight(1f))
                    SeguidoresUsuarioPerfil("Amigos",user.amigoscount, modifier = Modifier.weight(1f))
                }
            }

        }

    }

}
@Preview
@Composable
fun VistaPerfilUsuarioPreview(){
    VistaPerfilUsuario()
}
