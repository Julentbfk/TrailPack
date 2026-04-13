# [GOD_FILE: NAV_MASTER - TRAILPACK]
# ROL: EL MAESTRO DE FLUJO
# INSTRUCCIÓN: Eres el guardián de la navegación y el estado global.
# REGLA: Tienes el código íntegro de la estructura de la app. Úsalo para explicar cómo se conectan las piezas.

---

## [1. ESTRUCTURA GLOBAL]

### AppNavegation.kt (Grafo de rutas)
```kotlin
package com.julen.trailpack.routing

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.julen.trailpack.vistas.actividadespublicadas.VistaDetalleActividad
import com.julen.trailpack.vistas.ajustes.VistaAjustes
import com.julen.trailpack.vistas.ajustes.cambiarpassword.VistaCambiarPassword
import com.julen.trailpack.vistas.marcogeneral.ScaffoldTrailPack
import com.julen.trailpack.vistas.login.VistaLogin
import com.julen.trailpack.vistas.mapa.MapaViewModel
import com.julen.trailpack.vistas.mapa.VistaRutaDetalladaMapa
import com.julen.trailpack.vistas.marcogeneral.MainViewModel
import com.julen.trailpack.vistas.perfilusuario.VistaCompletarPerfil
import com.julen.trailpack.vistas.perfilusuario.VistaEditarPerfil
import com.julen.trailpack.vistas.registro.VistaRegistro
import com.julen.trailpack.vistas.perfilusuario.VistaPerfilUsuario
import com.julen.trailpack.vistas.registro.VistaConfirmacionEmail

@Composable
fun AppNavegation() {
    val navHost: NavHostController = rememberNavController()
    val enrutador: Enrutador = remember(navHost){ Enrutador(navHost) }
    val mainViewModel: MainViewModel = viewModel()

    val context = LocalContext.current
    LaunchedEffect(mainViewModel.notificationMessage) {
        mainViewModel.notificationMessage?.let { mensaje ->
            Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show()
            mainViewModel.notificationMessage = null
        }
    }

   if(mainViewModel.isCheckingAuth){
       Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
           CircularProgressIndicator()
       }
   }else{
       val startDestination = if(mainViewModel.isUserLoggedIn) "scaffoldtrailpack" else "login"
       NavHost(navHost, startDestination = startDestination){
           composable(route="login"){ VistaLogin(navToRegistro = { enrutador.navToRegistro() }, navToScaffoldTrailPack = {enrutador.navToScaffoldTrailPack()}) }
           composable(route="registro"){ VistaRegistro(navToLoginPopBack = {enrutador.popBack()}, navToConfirmacionEmail = {email -> enrutador.navToConfirmacionEmail(email)}) }
           composable(route="confirmacionemail/{email}"){ ruta -> 
               val emailRecibido = ruta.arguments?.getString("email") ?: ""
               VistaConfirmacionEmail(email = emailRecibido, navToLogin = {enrutador.navToLogin()}) 
           }
           composable(route="completarperfil"){ VistaCompletarPerfil(guardarClick = { enrutador.navToScaffoldTrailPack() }) }
           composable(route="perfilusuario"){ VistaPerfilUsuario() }
           composable(route="scaffoldtrailpack"){ ScaffoldTrailPack(navHost,mainViewModel) }
           composable(route="editarperfil") { VistaEditarPerfil(editarPerfilClick = {enrutador.navToScaffoldTrailPack()}, cancelarEditClick = {enrutador.popBack()}) }
           composable(route="ajustes") { VistaAjustes(navToPerfilUsuario = {enrutador.popBack()}, navToCambiarPassword = {enrutador.navToCambiarPassword()}, navToLoginClearStack = {enrutador.navToLoginClearStack()}) }
           composable(route = "cambiarpassword"){ VistaCambiarPassword(guardarCambioPasswordClick = {enrutador.popBack()}) }
           composable(route = "detalleruta/{rutaId}", arguments = listOf(navArgument("rutaId") {type = NavType.StringType})){ backstackEntry ->
               val scaffoldEntry = remember(navHost.currentBackStackEntry){ navHost.getBackStackEntry("scaffoldtrailpack") }
               val viewModel: MapaViewModel = viewModel(scaffoldEntry)
               val rutaId = backstackEntry.arguments?.getString("rutaId") ?: ""
               val rutaSeleccionada = viewModel.rutasparquenatural.find{ it.idruta == rutaId }
               if(rutaSeleccionada != null ){
                   VistaRutaDetalladaMapa(ruta = rutaSeleccionada, viewModel = viewModel, onPublicarClick = { viewModel.togglePopupPublicacion(true, rutaSeleccionada) }, onBack = { viewModel.seleccionarRutaParaDetalle(null); navHost.popBackStack() }, mainViewModel = mainViewModel)
               }
           }
           composable(route = "detalleactividad/{actividadId}", arguments = listOf(navArgument("actividadId") {type = NavType.StringType})){ backstackEnty ->
               val actividadId = backstackEnty.arguments?.getString("actividadId") ?: ""
               VistaDetalleActividad(actividadId=actividadId, mainviewModel = mainViewModel)
           }
       }
   }
}
```

### ScaffoldTrailPack.kt (Contenedor de Tabs)
```kotlin
@Composable
fun ScaffoldTrailPack(navController: NavHostController, mainviewModel: MainViewModel) {
    val mapaviewModel: MapaViewModel = viewModel()
    val actividadesviewModel: ActividadesViewModel = viewModel()
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val uid = auth.currentUser?.uid
    val enrutador = remember(navController) { Enrutador(navController) }

    LaunchedEffect(Unit) {
        if(uid != null){
            mainviewModel.cargarUsuarioGlobal(uid)
            db.collection("usuarios").document(uid).get().addOnSuccessListener { docTask ->
                val completado = docTask.getBoolean("perfilcompletado") ?: false
                if(!completado){ navController.navigate("completarperfil"){ popUpTo("scaffoldtrailpack") {inclusive = true} } }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = { if(mainviewModel.selectedTab == 2) TopBarTrailPack(enrutador = enrutador, onCerrarSesion = { AuthRepository().authCerrarSesion(navController) }) },
            bottomBar = { BottomBarTrailPack(selectedTab = mainviewModel.selectedTab, onTabSelected = {mainviewModel.selectedTab = it}) },
            containerColor = MaterialTheme.colorScheme.background
        ) { paddingInterno ->
            Column(modifier = Modifier.fillMaxSize().padding(paddingInterno)) {
                Box(modifier = Modifier.fillMaxSize()) {
                    when (mainviewModel.selectedTab) {
                        0 -> VistaMapa(enrutador = enrutador, mainviewModel = mainviewModel, mapaviewModel = mapaviewModel)
                        1 -> VistaRutasPublicadas(mainviewModel, enrutador, actividadesviewModel)
                        2 -> VistaPerfilUsuario()
                    }
                }
            }
        }
        PopUpPublicarRuta(mapaviewModel, mainviewModel)
    }
}
```

---

## [2. LÓGICA DE CONTROL GLOBAL]

### MainViewModel.kt (Código íntegro)
```kotlin
class MainViewModel : ViewModel(){
    var selectedTab by mutableStateOf(2)
    var isUserLoggedIn by mutableStateOf(false)
    var isCheckingAuth by mutableStateOf(true)
    private val userRepository = UserRepository()
    var usuarioGlobal by mutableStateOf<Usuario?>(null)

    init { checkAuthStatus() }

    private fun checkAuthStatus() {
        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        isUserLoggedIn = user != null && user.isEmailVerified
        isCheckingAuth = false
    }

    fun cargarUsuarioGlobal(uid: String){
        userRepository.obtenerUsuario(uid) { user, _ -> if (user != null) usuarioGlobal = user }
    }

    fun formatearFecha(millis: Long) : String {
        return Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    }

    var notificationMessage by mutableStateOf<String?>(null)
    fun showNotification(mensaje: String) { notificationMessage = mensaje }
}
```

### Enrutador.kt (Código íntegro)
```kotlin
class Enrutador(private val navController: NavController) {
    fun navToRegistro(){ navController.navigate("registro") }
    fun navToConfirmacionEmail(email: String) { navController.navigate("confirmacionemail/$email") }
    fun popBack() { navController.popBackStack() }
    fun navToLogin() { navController.navigate("login") }
    fun navToLoginClearStack() { navController.navigate("login") { popUpTo(0) { inclusive = true } } }
    fun navToCompletarPerfil() { navController.navigate("completarperfil") { popUpTo("scaffoldtrailpack") {inclusive = true} } }
    fun navToPerfilUsuario(){ navController.navigate("perfilusuario") }
    fun navToEditarPerfil() { navController.navigate("editarperfil") }
    fun navToAjustes() { navController.navigate("ajustes") }
    fun navToCambiarPassword() { navController.navigate("cambiarpassword") }
    fun navToScaffoldTrailPack() { navController.navigate("scaffoldtrailpack") { popUpTo("login") { inclusive = true } } }
    fun navToRutaDetalladaMapa(rutaId: String) { navController.navigate("detalleruta/$rutaId") }
    fun navToActividadDetallada(actividadId: String) { navController.navigate("detalleactividad/$actividadId") }
}
```
