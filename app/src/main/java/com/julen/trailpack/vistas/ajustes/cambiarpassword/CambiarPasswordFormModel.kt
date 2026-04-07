package com.julen.trailpack.vistas.ajustes.cambiarpassword

data class CambiarPasswordFormModel(
    val passActual: String = "",
    val passNueva: String ="",
    val passNuevaConfirmar: String ="",
    val error: String? = null
)