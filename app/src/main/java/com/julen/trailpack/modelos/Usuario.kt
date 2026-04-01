package com.julen.trailpack.modelos

data class Usuario(
    val uid: String = "",
    val username: String = "",
    val email: String = "",
    val biografia: String = "",
    val fotoperfil: String ="",
    val nivel: Int = 0,
    val fechacreacion: Long = System.currentTimeMillis(),
    val verificado: Boolean = false
)
