package com.julen.trailpack.modelos

import com.google.firebase.firestore.GeoPoint

data class ParqueNatural(
    val idparquenatural: String ="",
    val nombre: String ="",
    val descripcion: String ="",
    val pais: String = "",
    val region: String ="",
    val tipo:String="",
    val superficie: Double = 0.0,
    val fotosparque: List<String> = emptyList(),

    //Coordenadas google maps
    val lat: Double =0.0,
    val lng: Double =0.0,
    val contorno: List<GeoPoint> = emptyList()
)
