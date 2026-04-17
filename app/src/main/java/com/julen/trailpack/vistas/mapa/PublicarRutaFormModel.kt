package com.julen.trailpack.vistas.mapa

data class PublicarRutaFormModel(
    val numparticipantes: String = "",
    val fecha: String = "",
    val horasalida: String = "",
    val puntoencuentro: String = "",
    val fechaenmillis: Long = 0,
    val horasalidaenmillis: Long = 0
)