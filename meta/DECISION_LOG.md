dec# [DECISION_LOG: Diario de Decisiones Técnicas]

1. **Estructura de Estado Global (11/04/2026):** Se decide migrar la gestión de pestañas (`selectedTab`) y el estado de carga inicial fuera de los ViewModels específicos (`MapaViewModel`) y centralizarlo en un `MainViewModel` global para evitar problemas de persistencia al navegar.
2. **Navegación:** Se corrige la navegación hacia la ruta detallada sincronizando los nombres de ruta entre `Enrutador` y `AppNavegation`. Se implementa limpieza de estado en `LaunchedEffect` en `VistaMapa` para evitar persistencia errónea del `BottomSheet` al volver.
