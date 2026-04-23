package com.julen.trailpack.modelos


data class Ruta(
    val idruta: String = "",
    val idparquenatural: String = "",
    val pais: String = "",
    val region: String ="",
    val nombre: String = "",
    val dificultad: String = "",
    val distancia: Double = 0.0,
    val duracion: String = "",
    val desnivelacumulado: Int = 0,
    val desnivelpositivo: Int = 0,
    val desnivelnegativo: Int = 0,
    val esOficial: Boolean = false,
    val creadorId: String = "",
    val nombreCreador: String = "",
    val descripcion: String = "",
    val fotosRuta: List<String> = emptyList(),
    val coordenadas: List<Coordenada> = emptyList(),
    val fechacreacion: Long = System.currentTimeMillis()
)
