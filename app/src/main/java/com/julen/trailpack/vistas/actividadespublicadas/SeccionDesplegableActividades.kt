package com.julen.trailpack.vistas.actividadespublicadas

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.math.exp

@Composable
fun SeccionDesplegableActividades(
    titulo: String,
    cantidad: Int,
    expandido: Boolean = true,
    content: @Composable () -> Unit
) {
    var expandido by remember { mutableStateOf(expandido) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expandido = !expandido }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween

    ){

        Text(
            text = "$titulo ($cantidad)",
            style = MaterialTheme.typography.titleMedium
        )

        Icon(
            imageVector = if (expandido) Icons.Default.KeyboardArrowUp
            else Icons.Default.KeyboardArrowDown,
            contentDescription = null
        )
    }
    HorizontalDivider()

    if(expandido) {
        content()
    }

}