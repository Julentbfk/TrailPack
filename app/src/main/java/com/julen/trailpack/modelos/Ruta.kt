package com.julen.trailpack.modelos

import com.google.firebase.firestore.GeoPoint

data class Ruta(
    val idruta: String = "",
    val idparquenatural: String = "", //Referenciara al Id del parque al que pertenece la ruta
    val nombre: String = "",
    val dificultad: String = "",
    val distancia: Double = 0.0,
    val duracion: String = "",
    val desnivel: Int = 0,
    val creadorId: String = "",
    val nombreCreador: String = "",
    val descripcion: String = "",// Solo se ve en el detalle
    val fotosRuta: List<String> = emptyList(),
    val coordenadas: List<GeoPoint> = emptyList(),// Para el mapa del detalle
    val fechacreacion: Long = System.currentTimeMillis()
)
