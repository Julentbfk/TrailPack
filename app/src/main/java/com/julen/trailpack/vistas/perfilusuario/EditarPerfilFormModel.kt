package com.julen.trailpack.vistas.perfilusuario

data class EditarPerfilFormModel(
    // Campos no editables (pero visibles)
    val username: String = "",
    val email: String = "",

    val nacionalidad: String = "",
    val nombre: String = "",
    val apellidos: String ="",
    val fechanac: String = "",
    val genero: String = "",
    val prefijo: String = "",
    val telefono: String = "",
    val ubicacion: String = "",
    val biografia: String = ""
)
