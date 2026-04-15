package com.julen.trailpack.modelos

import java.util.Date

data class Usuario(

    //Imprescindible
    val uid: String = "",
    val username: String = "",
    val email: String = "",

    //Personal
    val ubicacion: String = "",
    val prefijo: String = "+34",
    val telefono: String = "",
    val nombre: String ="",
    val apellidos: String ="",
    val genero: String="",
    val fechanac: String ="",
    val nacionalidad: String ="",
    //val paisnac: String  = "",
    //val localidadnac: String ="",

    //Info de mas
    val biografia: String = "",
    val fotoperfil: String ="",
    //Gamificacion
    val nivel: Int = 0,

    //Verificaciones
    val fechacreacion: Long = System.currentTimeMillis(),
    val verificado: Boolean = false,
    val perfilcompletado: Boolean = false,

    //Social
    val seguidorescount: Int = 0,
    val siguiendocount: Int = 0,
    val amigoscount: Int = 0,
    val listaseguidores: List<String> = emptyList(),
    val listasiguiendo: List<String> = emptyList(),
    val listaamigos: List<String> = emptyList(),
    val rutasfavoritas: List<String> = emptyList(),

)
