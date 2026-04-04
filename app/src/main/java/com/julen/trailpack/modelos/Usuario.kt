package com.julen.trailpack.modelos

import java.util.Date

data class Usuario(

    val uid: String = "",

    val username: String = "",
    val email: String = "",
    val ubicacion: String = "",
    val prefijo: String = "+34",
    val telefono: String = "",
    val edad: String ="",
    val genero: String="",
    val fechanac: Date = Date(),
    val nacionalidad: String  = "",

    val biografia: String = "",
    val fotoperfil: String ="",
    val nivel: Int = 0,

    val fechacreacion: Long = System.currentTimeMillis(),
    val verificado: Boolean = false,
    val perfilcompletado: Boolean = false,

    val seguidorescount: Int = 0,
    val siguiendocount: Int = 0,
    val amigoscount: Int = 0,
    val listaseguidores: List<String> = emptyList(),
    val listasiguiendo: List<String> = emptyList(),
    val listaamigos: List<String> = emptyList(),

    )
