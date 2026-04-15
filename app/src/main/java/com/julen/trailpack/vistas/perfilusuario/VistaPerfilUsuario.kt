package com.julen.trailpack.vistas.perfilusuario


import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.unit.dp

import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseAuth
import com.julen.trailpack.routing.Enrutador
import com.julen.trailpack.vistas.actividadespublicadas.ActividadesViewModel
import com.julen.trailpack.vistas.actividadespublicadas.CardActividad

import com.julen.trailpack.vistas.componentes.usuario.DatosPersonalesPerfil
import com.julen.trailpack.vistas.componentes.usuario.FotoPerfil
import com.julen.trailpack.vistas.componentes.usuario.NivelUsuarioPerfil
import com.julen.trailpack.vistas.componentes.usuario.SeguidoresUsuarioPerfil
import com.julen.trailpack.vistas.marcogeneral.MainViewModel


//COMPOSABLE MARCO DONDE IRAN TODAS LAS PIEZAS
@Composable
fun VistaPerfilUsuario(mainviewModel: MainViewModel, actividadesviewModel: ActividadesViewModel,enrutador: Enrutador) {
    val perfilviewModel: PerfilUsuarioViewModel = viewModel()
    val user = mainviewModel.usuarioGlobal ?: return
    val uid = user.uid

    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
            uri?.let {
            perfilviewModel.subirFotoPerfil(uid, it) { success, error ->
                if (success) {
                    mainviewModel.showNotification("Foto actualizada")
                } else {
                    mainviewModel.showNotification("Error: $error")
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
                    url = user.fotoperfil,
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

        }//Cierra row de los datos
        Spacer(modifier = Modifier.height(16.dp))


            Spacer(modifier = Modifier.height(16.dp))

            var categoriaSeleccionada by remember { mutableStateOf("Creadas") }
            val categorias = listOf("Creadas", "Realizadas", "Favoritas")

            val listaActiva = when (categoriaSeleccionada) {
                "Creadas"     -> actividadesviewModel.getActividadesCreadas(uid)
                "Realizadas" -> actividadesviewModel.getActividadesUnido(uid)
                "Favoritas" -> emptyList()
                else          -> emptyList()
            }

            ScrollableTabRow(
                selectedTabIndex = categorias.indexOf(categoriaSeleccionada),
                edgePadding = 0.dp
            ) {
                categorias.forEach { categoria ->
                    Tab(
                        selected = categoriaSeleccionada == categoria,
                        onClick = { categoriaSeleccionada = categoria },
                        text = { Text(categoria) }
                    )
                }
            }

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(listaActiva) { acr ->
                    CardActividad(
                        actividadConRuta = acr,
                        fechaFormateada = mainviewModel.formatearFecha(acr.actividad.fechasalida),
                        usuarioActualUid = uid,
                        caducada = actividadesviewModel.actividadCaducada(acr.actividad),
                        mostrarBoton = false,
                        onUnirseClick = {},
                        onAbandonarClick = {},
                        onCardClick = {enrutador.navToActividadDetallada(acr.actividad.idactividad, false)}
                    )
                }
            }

    }//Cierra el main container

}

