package com.julen.trailpack.modelos

data class ParqueNatural(
    val idparquenatural: String ="",
    val nombre: String ="",
    val descripcion: String ="",
    val region: String ="",
    val fotosparque: List<String> = emptyList(),
    //Coordenadas google maps
    val lat: Double =0.0,
    val lng: Double =0.0
)
