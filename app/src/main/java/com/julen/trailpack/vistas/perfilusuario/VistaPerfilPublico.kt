package com.julen.trailpack.vistas.perfilusuario

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.julen.trailpack.routing.Enrutador
import com.julen.trailpack.vistas.actividadespublicadas.CardActividad
import com.julen.trailpack.vistas.componentes.usuario.DatosPersonalesPerfil
import com.julen.trailpack.vistas.componentes.usuario.FotoPerfil
import com.julen.trailpack.vistas.componentes.usuario.NivelUsuarioPerfil
import com.julen.trailpack.vistas.componentes.usuario.SeguidoresUsuarioPerfil
import com.julen.trailpack.vistas.marcogeneral.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VistaPerfilPublico(
    uid: String,
    mainViewModel: MainViewModel,
    enrutador: Enrutador
) {

    val perfilPublicoViewModel: PerfilPublicoViewModel = viewModel()
    val usuario = perfilPublicoViewModel.usuario

    LaunchedEffect(uid) {
        perfilPublicoViewModel.cargarPerfil(uid)
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(usuario?.username ?: "NONE") },
                navigationIcon = {
                    IconButton(onClick = {enrutador.popBack()}) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "regresar")
                    }
                }
            )
        }
    ) { paddingValues ->

        if(perfilPublicoViewModel.isLoading){
            Box(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }
        if(usuario==null) return@Scaffold

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    FotoPerfil(url = usuario.fotoperfil, modifier = Modifier)
                    Spacer(modifier = Modifier.height(24.dp))
                    NivelUsuarioPerfil(usuario.nivel)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    DatosPersonalesPerfil(usuario.username, usuario.ubicacion, usuario.biografia)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SeguidoresUsuarioPerfil("Seguidores", usuario.seguidorescount, modifier = Modifier.weight(1f))
                        SeguidoresUsuarioPerfil("Siguiendo", usuario.siguiendocount, modifier = Modifier.weight(1f))
                        SeguidoresUsuarioPerfil("Amigos", usuario.amigoscount, modifier = Modifier.weight(1f))
                    }
                }
            }//Cierro row datos personales

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Actividades publicadas",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(perfilPublicoViewModel.actividadesCreadas) { acr ->
                    CardActividad(
                        actividadConRuta = acr,
                        fechaFormateada = mainViewModel.formatearFecha(acr.actividad.fechasalida),
                        usuarioActualUid = mainViewModel.usuarioGlobal?.uid,
                        caducada = acr.actividad.estaCaducada(),
                        mostrarBoton = false,
                        onUnirseClick = {},
                        onAbandonarClick = {},
                        onCardClick = {
                            enrutador.navToActividadDetallada(acr.actividad.idactividad, true)
                        }
                    )
                }
            }


        }//Cierra el scaffold

    }
}