package com.julen.trailpack.modelos

data class Ruta(
    val idruta: String = "",
    val idparquenatural: String = "", //Referenciara al Id del parque al que pertenece la ruta
    val nombre: String = "",
    val dificultad: String = "",
    val distanciaKm: Double = 0.0,
    val desnivel: Int = 0,
    val descripcion: String = "",
    val fotosRuta: List<String> = emptyList()
)
