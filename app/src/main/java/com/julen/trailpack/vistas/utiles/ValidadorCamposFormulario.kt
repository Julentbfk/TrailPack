package com.julen.trailpack.vistas.utiles

import android.util.Patterns


object ValidadorCampos{
    //--REGLAS DE VALIDACION--
    //Los metodos validaran el contenido del campo del formulario y retornaran vacio si no hay error o  el mensaje de error si la losing condition se cumple
    private fun checkVacio(texto:String) : String {
        if(texto.isEmpty()){
            return "El campo no puede estar vacio"
        }else{
            return ""
        }
    }
    private fun checkPatternEmail(texto: String) : String{
        if(!Patterns.EMAIL_ADDRESS.matcher(texto).matches()){
            return "email no valido: Formato aceptado xxx123.,-_@xxx.xxx"
        }else{
            return ""
        }
    }
    private fun checkLongitudUsername(texto: String) : String{
        if(texto.length < 2){
            return "El username tiene que tener al menos 6 caracteres"
        }else{
            return ""
        }
    }
    private fun checkLongitudPassword(texto : String): String{
        if(texto.length < 3 ){
            return "La contraseña tiene que tener al menos 8 caracteres"
        }else{
            return ""
        }
    }
    private fun checkMinusculas(texto: String) : String {
        if(!texto.any() { it.isLowerCase() }){
            return "La contraseña tiene que tener al menos una minuscula"
        }else{
            return ""
        }
    }
    private fun checkMayusculas(texto: String) : String {
        if(!texto.any() { it.isUpperCase() }){
            return "La contraseña tiene que tener al menos una mayuscula"
        }else{
            return ""
        }
    }



    //----------- VALIDADORES -------------
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
        if(p1 != p2){
            return false
        }else{
            return true
        }
    }

    fun validaTodo(email:String, password:String, usuario:String) : Boolean{
        if(validarEmail(email).isEmpty() && validarUsuario(usuario).isEmpty() && validarPassword(password).isEmpty()){
            return true
        }else{
            return false
        }
    }


}
