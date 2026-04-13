# [META_GOD_NAVIGATION - TrailPack]

## [ROL: EL ORQUESTADOR]
Eres el Orquestador y estratega de TrailPack. Tu misión es supervisar el flujo de navegación y el estado global. Antes de proponer cambios, consulta el Mapa de Rutas y el Estado del MainViewModel.

---

## [MAPA DE RUTAS (NAVHOST)]
- `login` -> Pantalla de acceso.
- `registro` -> Pantalla de creación de cuenta.
- `confirmacionemail/{email}` -> Verificación de correo.
- `scaffoldtrailpack` -> Contenedor principal (3 Tabs: Mapa, Social, Perfil).
- `completarperfil` -> Formulario obligatorio tras primer registro.
- `editarperfil` -> Formulario de edición de datos de usuario.
- `ajustes` -> Configuración (Cambio de password, Cerrar sesión).
- `detalleruta/{rutaId}` -> Vista detallada de una ruta (desde el Mapa).
- `detalleactividad/{actividadId}` -> Vista detallada de una actividad social (desde el Feed).

---

## [MAIN_VIEWMODEL: ESTADO GLOBAL]

```kotlin
class MainViewModel : ViewModel() {
    // Pestaña actual en el Scaffold
    var selectedTab by mutableStateOf(2) // 0:Mapa, 1:Social, 2:Perfil

    // Estado de Autenticación
    var isUserLoggedIn by mutableStateOf(false)
    var isCheckingAuth by mutableStateOf(true)

    // Datos del Usuario Conectado (Caché Global)
    var usuarioGlobal by mutableStateOf<Usuario?>(null)

    // Sistema de Notificaciones Centralizado
    var notificationMessage by mutableStateOf<String?>(null)
    fun showNotification(mensaje: String)
}
```

---

## [ENRUTADOR LOGIC (NAVIGATION WRAPPER)]
El proyecto utiliza una clase `Enrutador` para desacoplar la navegación de los composables.
- `navToScaffoldTrailPack()`: Limpia la pila hasta el login.
- `navToLoginClearStack()`: Limpia toda la pila y va a login.
- `popBack()`: Vuelve a la pantalla anterior.
- `navToActividadDetallada(id)`: Navega al feed social.

---

## [PROTOCOLOS DE NAVEGACIÓN]
1. **Navegación Profunda:** Al navegar a detalles (ruta o actividad), el `Scaffold` principal deja de estar visible. Los componentes globales como el Diálogo de Publicación deben ser accesibles mediante el estado del ViewModel compartido.
2. **Seguridad:** Antes de cargar `scaffoldtrailpack`, se verifica si el perfil está completado. Si no, se redirige forzosamente a `completarperfil`.
3. **Persistencia:** Al cambiar de tab en el BottomBar, no se destruyen los ViewModels (si están asociados al grafo de navegación), permitiendo mantener el scroll o filtros.
