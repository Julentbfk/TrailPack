package com.julen.trailpack.vistas.componentes.formulario

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text2.input.rememberTextFieldState
import androidx.compose.foundation.text2.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.julen.trailpack.modelos.Pais
import kotlin.math.exp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectorPrefijoPais(
    paises: List<Pais>,
    paisSeleccionado: Pais,
    onPaisSelected: (Pais) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }


    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = {expanded=!expanded}) {
        //campo que muetra el pais seleccionado
        OutlinedTextField(
            value="${paisSeleccionado.prefijo}(${paisSeleccionado.nombre})",
            onValueChange = {},
            readOnly = true,
            label = {Text(text = "Pais")},
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)},
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )
        //El menu que se despliega
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {expanded = false}
        ) {
            paises.forEach { pais ->
                DropdownMenuItem(
                    text = {Text("${pais.prefijo} ${pais.nombre}")},
                    onClick = {
                        onPaisSelected(pais)
                        expanded = false
                    }
                )
            }
        }
    }

}


@Preview
@Composable
fun SelectorPrefijoPaisPreview() {

    val paises = listOf(
        Pais("España","ES","+34"),
        Pais("Francia","FR","+33"),
        Pais("Portugal","POR","+353")
    )
    var paisActual by remember { mutableStateOf(paises[0]) }

    SelectorPrefijoPais(paises,paisActual, onPaisSelected = {nuevopais ->
        paisActual = nuevopais
    })
}