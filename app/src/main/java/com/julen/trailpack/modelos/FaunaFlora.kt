package com.julen.trailpack.modelos

data class FaunaFlora (
    val id: String = "",
    val idparquenatural: String = "",
    val tipo: String = "",
    val nombre: String = "",
    val nombreCientifico: String = "",
    val descripcion: String = "",
    val foto: String = "",
    val enPeligroExtincion: Boolean = false
)