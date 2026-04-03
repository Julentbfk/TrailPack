package com.julen.trailpack.vistas.componentes.usuario

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun DatosPersonalesPerfil(nombreusuario: String,direccionusuario: String,biografiausuario: String){


    Text(
        text = nombreusuario,
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Bold
    )
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 2.dp)
    ) {
        if(!direccionusuario.isBlank()){
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = direccionusuario,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.DarkGray
            )
        }

    }

    Spacer(modifier = Modifier.height(8.dp))

    var showDialog by remember {mutableStateOf(false)}
    var showReadMore by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { showDialog = true }
    ) {
        Text(
            text = biografiausuario,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            onTextLayout =  {textLayouResult ->
                if(textLayouResult.lineCount > 3){
                    showReadMore = true
                }
            }
        )
        if(showReadMore){
            Text(
                text = "Leer más",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top=4.dp)
            )
        }
    }

    //Dialogo emergente
    if(showDialog){
        Dialog(onDismissRequest = { showDialog = false}) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Biografia Completa",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        IconButton(onClick = {showDialog = false}) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = null
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(modifier = Modifier.heightIn(max = 400.dp)){
                        Text(
                            text = biografiausuario,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.verticalScroll(rememberScrollState())
                        )
                    }

                }
            }//Cierra el Surface
        }//Cierra el dialog

    }

}
@Preview
@Composable
fun datosPersonalesPerfilPreview(){
    DatosPersonalesPerfil(
        "Jose",
        "Torrejon de ardoz, Comunidad de Madrid, España",
        "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum" +
                " has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to" +
                " make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially " +
                "unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop " +
                "publishing software like Aldus PageMaker including versions of Lorem Ipsu"
    )
}