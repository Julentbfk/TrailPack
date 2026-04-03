package com.julen.trailpack.vistas.registro

data class RegistroFormState(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val repassword: String = "",
    // Añadimos estados de error
    val usernameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val repasswordError: String? = null
)