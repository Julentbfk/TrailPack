# [FILE_TREE: Estructura Actual del Proyecto]

|   MainActivity.kt
|   
+---data
|       ActividadesRepository.kt
|       AuthRepository.kt
|       MapsRepository.kt
|       UserRepository.kt
|       
+---modelos
|       Actividad.kt
|       ActividadConRuta.kt
|       Coordenada.kt
|       FaunaFlora.kt
|       Pais.kt
|       ParqueNatural.kt
|       Participante.kt
|       Ruta.kt
|       Usuario.kt
|       
+---routing
|       AppNavegation.kt
|       Enrutador.kt
|       
+---theme
|       Color.kt
|       Theme.kt
|       Type.kt
|       
\---vistas
    +---actividadespublicadas
    |       ActividadesViewModel.kt
    |       CardActividad.kt
    |       DetalleActividadViewModel.kt
    |       PopUpEditarActividad.kt
    |       SeccionDesplegableActividades.kt
    |       VistaActividades.kt
    |       VistaDetalleActividad.kt
    |       
    +---ajustes
    |   |   AjustesViewModel.kt
    |   |   VistaAjustes.kt
    |   |   
    |   \---cambiarpassword
    |           CambiarPasswordFormModel.kt
    |           VistaCambiarPassword.kt
    |           
    +---componentes
    |   +---formulario
    |   |       OutlinedTextFieldMejorado.kt
    |   |       SelectorFechaMejorado.kt
    |   |       SelectorPrefijoPais.kt
    |   |       
    |   +---mapa
    |   |       MapaContent.kt
    |   |       MapaParqueNaturalCard.kt
    |   |       MapaRutaCard.kt
    |   |       PopUpPublicarRuta.kt
    |   |       
    |   \---usuario
    |           DatosPersonalesPerfil.kt
    |           FotoPerfil.kt
    |           NivelUsuario.kt
    |           SeguidoresUsuarioPerfil.kt
    |           
    +---login
    |       LoginViewModel.kt
    |       VistaLogin.kt
    |       
    +---mapa
    |       ListaRutasParqueBottomSheet.kt
    |       MapaViewModel.kt
    |       PublicacionFormModel.kt
    |       VistaMapa.kt
    |       VistaRutaDetalladaMapa.kt
    |       
    +---marcogeneral
    |       BottomBarTrailPack.kt
    |       MainViewModel.kt
    |       ScaffoldTrailPack.kt
    |       TopBarTrailPack.kt
    |       
    +---perfilusuario
    |       CompletarPerfilFormModel.kt
    |       EditarPerfilFormModel.kt
    |       PerfilUsuarioViewModel.kt
    |       VistaCompletarPerfil.kt
    |       VistaEditarPerfil.kt
    |       VistaPerfilUsuario.kt
    |       
    +---registro
    |       RegistroFormModel.kt
    |       RegistroViewModel.kt
    |       VistaConfirmacionEmail.kt
    |       VistaRegistro.kt
    |       
    \---utiles
            ValidadorCamposFormulario.kt
