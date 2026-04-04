package com.julen.trailpack.vistas.utiles

import android.util.Log


object ValidadorCampos{
    //--REGLAS DE VALIDACION--
    //Los metodos validaran el contenido del campo del formulario y retornaran vacio si no hay error o  el mensaje de error si la losing condition se cumple
    private fun checkVacio(texto:String) : String {
        return if(texto.isEmpty()){
            "El campo no puede estar vacio"
        }else{
            ""
        }
    }
    private fun checkPatternEmail(texto: String) : String{
        val emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$".toRegex()
        return if (!texto.matches(emailRegex)) {
            "Formato de email inválido, prueba - xxxXXxx213._-+%@xxxxxxx.xxx  -"
        } else {
            ""
        }
    }
    private fun checkLongitudUsername(texto: String) : String{
        return if(texto.length < 6){
            "El username tiene que tener al menos 6 caracteres"
        }else{
            ""
        }
    }
    private fun checkLongitudPassword(texto : String): String{
        return if(texto.length < 8 ){
            "La contraseña tiene que tener al menos 8 caracteres"
        }else{
            ""
        }
    }
    private fun checkTieneNumero(texto: String): String {
        return if (!texto.any { it.isDigit() }) "La contraseña debe tener al menos un número" else ""
    }
    private fun checkTieneEspecial(texto: String): String {
        val especial = "!@#$%^&*()-._=+"
        return if (!texto.any { it in especial }) "La contraseña debe tener un carácter especial ($especial)" else ""
    }
    private fun checkMinusculas(texto: String) : String {
        return if(!texto.any { it.isLowerCase() }){
            "La contraseña tiene que tener al menos una minuscula"
        }else{
            ""
        }
    }
    private fun checkMayusculas(texto: String) : String {
        return if(!texto.any { it.isUpperCase() }){
            "La contraseña tiene que tener al menos una mayuscula"
        }else{
            ""
        }
    }
    private fun checkLongitudBiografia(texto: String): String{
        return if (texto.length < 10){
            "Escribe al menos 10 caracteres"
        }else{
            ""
        }
    }
    private fun checkLongitudTelefono(texto: String) : String {
        return if(texto.length < 7) {
            "El telefono es demasiado corto"
        }else{
            ""
        }
    }
    private val reglasTelefono = mapOf(
        // Ejemplo de España (9 dígitos, empezando por 6, 7, 9)
        "+34" to Regex("^[679]\\d{8}$"),
        // Ejemplo Francia (9 dígitos, empezando por 1 al 9, sin el cero inicial)
        "+33" to Regex("^[1-9]\\d{8}$")
    )

    fun checkTelefonoSegunPais(numero: String, prefijo: String): String {
        Log.d("VALIDACION", "Validando: numero=$numero, prefijo=$prefijo")

        val regex = reglasTelefono[prefijo]
            ?: return "País no soportado o prefijo inválido"

        val esValido = numero.matches(regex)
        Log.d("VALIDACION", "Es válido para $prefijo?: $esValido") // <--- AÑADE ESTO

        return if (!esValido) "Formato inválido para $prefijo" else ""

    }
    fun checkSoloNumeros(texto: String): String{
        if (!texto.all { it.isDigit() }){
            return "Solo números"
        } else {
            return ""
        }
    }

    //----------- VALIDADORES -------------
    fun validarTelefono(input: String, prefijo: String) : String {

        val funcionesvalidadoras = listOf(
            ::checkVacio,
            ::checkSoloNumeros
        )

        for (funcion in funcionesvalidadoras){
            val error = funcion(input)
            if(error.isNotEmpty()) {
                return error
            }
        }

        val error = checkTelefonoSegunPais(input,prefijo)
        if(error.isNotEmpty()) {
            return error
        }
        return ""

    }

    fun validarEmail(input:String) : String{

        val funcionesvalidadoras = listOf(
            ::checkVacio,
            ::checkPatternEmail
        )

        for (funcion in funcionesvalidadoras){
            val error = funcion(input)
            if(error.isNotEmpty()) {
                return error
            }
        }
        return ""
    }

    fun validarPassword(input: String) : String {
        //Hacemos una lista de funciones de validacion
        val funcionesvalidadoras = listOf(
            ::checkVacio,
            ::checkMinusculas,
            ::checkMayusculas,
            ::checkTieneNumero,
            ::checkTieneEspecial,
            ::checkLongitudPassword
        )
        //Recorremos la lista para que vaya condicion por condicion en orden validando si esta cumpiendose la losing condition
        for (funcion in funcionesvalidadoras){
            //Si el resultado no fuera vacio es por que se  estaria cumpliendo la losing condition en alguno de los metodos
            val error = funcion(input)
            if(error.isNotEmpty()){
                return error
            }
        }
        return ""
    }

    fun validarUsuario(input: String) : String{
        val funcionesvalidadoras = listOf(
            ::checkVacio,
            ::checkLongitudUsername
        )
        for(funcion in funcionesvalidadoras){
            val error:String = funcion(input)
            if(error.isNotEmpty()){
                return error
            }
        }
        return ""
    }

    fun validarMatch(p1: String, p2: String) : Boolean {
        return p1 == p2
    }

    fun validarUbicacion(input: String) : String {
        val funcionesValidadoras = listOf(
            ::checkVacio
        )

        for(f in funcionesValidadoras) {
            val error = f(input)
            return error
        }
        return ""
    }

    fun validarBiografia(input: String): String {
        val funcionesValidadoras = listOf(
            ::checkVacio,
            ::checkLongitudBiografia
        )
        for (f in funcionesValidadoras) {
            val error = f(input)
            if (error.isNotEmpty()) return error
        }
        return ""
    }


}
