package com.julen.trailpack.data

import androidx.navigation.NavHostController
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.julen.trailpack.modelos.Usuario

class AuthRepository {

    private val auth = FirebaseAuth.getInstance()// la autentificacion de fireAuth

    private val firestoreDB = FirebaseFirestore.getInstance()// la base de datos de firestore

    //Callback para registrar usuario
    fun authRegistroUsuario(username: String, email: String, password: String, onResult: (Boolean, String?) -> Unit ){

        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { authTask ->
            if(authTask.isSuccessful){
                val user = auth.currentUser//currentUser devuelve lo que acaba de crear en la funcion createUserWithEmailAndPassword
                val uid = user?.uid//Esto saca el uid genereda auto del user recien creado

                if(uid != null) {

                    user.sendEmailVerification().addOnCompleteListener { emailTask ->
                        if(!emailTask.isSuccessful) {
                            onResult(false, emailTask.exception?.localizedMessage ?: "Error desconocido al enviar email confimacion")
                        }
                    }//Con este metodo enviamos email de verificacion y si da error por lo que sea pues te manda un error

                    //Se crea el usuario a inyectar
                    val nuevoUsuario = Usuario(
                        uid = user.uid,
                        username = username,
                        email = email,
                        nivel = 0,
                        verificado = false
                    )
                    //Se inyecta el usaurio en la coleccion usuarios de firebase
                    firestoreDB.collection("usuarios")
                        .document(uid)
                        .set(nuevoUsuario)
                        .addOnCompleteListener { inyectTask ->
                            if(inyectTask.isSuccessful){
                                onResult(true, null)
                            }else{
                                onResult(false, inyectTask.exception?.localizedMessage ?: "Error desconocido al inyectar usuario en la BD")
                            }
                        }
                }

            }else{
                onResult(false, authTask.exception?.localizedMessage ?: "Error desconocido al autentificar usuario")
            }
        }

    }

    //Callback para iniciar Sesion
    fun authLoginUsuario(email: String, password: String, onResult: (Boolean, String?) -> Unit){

        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener { loginTask ->
            if(loginTask.isSuccessful){
                val user = auth.currentUser

                //Chequeamos si ha verificado el correo
                if(user != null && user.isEmailVerified){

                    //Chequeamos si esta ya verificado de antes
                    firestoreDB.collection("usuarios").document(user.uid).get().addOnSuccessListener { docTask ->
                        val isVerified: Boolean = docTask.getBoolean("verificado") ?: false
                        if(!isVerified){
                            //Si es su primer login recuperamos de la coleccion users el campo con uid del usuario y updateaamos el campo verificado a true
                            firestoreDB.collection("usuarios")
                                .document(user.uid)
                                .update("verificado",true)
                                .addOnCompleteListener { onResult(true,null) }
                        }else{
                            //Si ya estaba verificado no hacemos nada
                            onResult(true,null)
                        }
                    }
                }else{
                    //Si no ha aceptado el correo le echamos de la sesion
                    auth.signOut()
                    onResult(false, "Verifica tu email para poder loguear")
                }
            }else{
                //Si falla el login
                onResult(false, loginTask.exception?.localizedMessage ?: "Error desconocido")
            }
        }

    }

    //Metodo para cerrar sesion
    fun authCerrarSesion (navController: NavHostController){
        FirebaseAuth.getInstance().signOut()

        //Navegamos al Login y BORRAMOS el historial para que no pueda volver a login
        navController.navigate("login"){
            popUpTo(0)
        }
    }

    //Metodo para cambiar las password
    fun authCambiarPassword(passactual: String, passNueva: String, onResult: (Boolean, String?) -> Unit){
        val user = auth.currentUser

        if(user != null && user.email != null){

            val credential = EmailAuthProvider.getCredential(user.email!!, passactual)
            //Reautentificacion obligatoria
            user.reauthenticate(credential).addOnCompleteListener { reauthTask ->
                if(reauthTask.isSuccessful){
                    user.updatePassword(passNueva)
                        .addOnCompleteListener { updateTask ->
                            if(updateTask.isSuccessful) {
                                onResult(true,null)
                            }else{
                                onResult(false, updateTask.exception?.localizedMessage ?: "Error al actualizar")
                            }
                        }
                }else{
                    onResult(false, "Contraseña actual incorrecta")
                }
            }

        } else{
            onResult(false, "No hay sesion activa...")
        }

    }

    //Metodo para borrar cuenta
    fun authBorrarCuenta(uid: String, passactual: String, onResult: (Boolean, String?) -> Unit){
        val user = auth.currentUser
        if (user != null && user.uid == uid && user.email != null) {
            val credential = EmailAuthProvider.getCredential(user.email!!, passactual)

            user.reauthenticate(credential).addOnCompleteListener { reauthTask ->

                if(reauthTask.isSuccessful){
                    firestoreDB.collection("usuarios").document(uid).delete().addOnCompleteListener { firestoredeleteTask ->
                        if(firestoredeleteTask.isSuccessful){
                            user.delete().addOnCompleteListener { authdeleteTask ->
                                if(authdeleteTask.isSuccessful){
                                    onResult(true,null)
                                }else{
                                    onResult(false, "Error al borrar de auth")
                                }
                            }
                        }else{
                            onResult(false,"Error al borrar de firestore")
                        }
                    }
                }else{
                    onResult(false, "Contraseña actual incorrecta")
                }
            }

        }else{
            onResult(false,"No hay sesion activa")
        }

    }



}