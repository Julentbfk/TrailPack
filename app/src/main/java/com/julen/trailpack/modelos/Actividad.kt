package com.julen.trailpack.modelos

data class Actividad(
    val idactividad : String = "",
    val idruta: String ="", //Para saber que ruta se va a hacer
    val idcreador: String = "", //UID del usuario que la empieza
    val listaparticipantesIds: List<String> = emptyList(),

    val nombrecreador: String = "",
    val nombre: String = "",
    val fotocreador: String ="",
    val puntoencuentro: String="",
    val fechasalida: Long = 0L, //Usamos long para operaciones con fechas en milisegundos mas sencillas
    val horasalida: Long = 0L,
    val maxparticipantes: Int = 10,
    val participantes: Int = 1,
    val espublica: Boolean = true,
    val estado: String = "ABIERTA"
){
    fun estaCaducada(): Boolean =
        horasalida > 0L && horasalida < System.currentTimeMillis()
}