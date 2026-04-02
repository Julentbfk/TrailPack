package com.julen.trailpack.modelos

data class Usuario(
    val uid: String = "",
    val username: String = "",
    val email: String = "",
    val adress: String = "",
    val telefono: Int = 0,
    val biografia: String = "",
    val fotoperfil: String ="",
    val nivel: Int = 0,

    val fechacreacion: Long = System.currentTimeMillis(),
    val verificado: Boolean = false,

    val seguidorescount: Int = 0,
    val siguiendocount: Int = 0,
    val amigoscount: Int = 0,
    val listaseguidores: List<String> = emptyList(),
    val listasiguiendo: List<String> = emptyList(),
    val listaamigos: List<String> = emptyList()

)
