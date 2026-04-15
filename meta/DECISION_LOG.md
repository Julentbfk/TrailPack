# [LOG DE DECISIONES TÉCNICAS - TRAILPACK]

## Historial de Decisiones

1. **Arquitectura de Navegación (01/04/2024):** Se optó por un `Enrutador` desacoplado que utiliza `NavHostController`. Esto permite que la lógica de navegación no dependa directamente de los Composables de UI.

2. **Gestión de Formularios (05/04/2024):** Implementación de `FormModel` inmutables para cada flujo (Login, Registro, Perfil). Se decidió usar `mutableStateOf` en los ViewModels para manejar estos modelos, facilitando la validación y el reseteo de estados.

3. **Estructura de Datos en Firestore (10/04/2024):** Las rutas y los parques se mantienen en colecciones separadas. Se decidió no anidar rutas dentro de parques para permitir búsquedas globales y mejorar la escalabilidad.

4. **Sistema de Notificaciones Global (12/04/2024):** Se implementó un sistema centralizado de tosts en `MainViewModel`. Los ViewModels de negocio invocan `showNotification(mensaje)`, y el `Scaffold` principal observa este estado para mostrar el feedback visual.

5. **Estrategia de Join en Memoria (Hoy):**
    - **Problema:** Firestore no soporta joins nativos entre colecciones (`actividades` y `rutas`).
    - **Decisión:** El `ActividadesViewModel` realiza el cruce de datos en el cliente. Primero recupera las actividades, extrae los IDs de ruta únicos, pide esas rutas al `MapsRepository` y finalmente combina los objetos en un modelo `ActividadConRuta`.
    - **Razón:** Minimiza las lecturas a Firebase (usando `whereIn` por IDs) y mantiene la lógica de negocio clara y reactiva.

6. **Globalización del Perfil de Usuario (Hoy):**
    - **Decisión:** Al iniciar la app o tras el login, se carga el objeto `Usuario` en `usuarioGlobal` dentro de `MainViewModel`.
    - **Razón:** Elimina la necesidad de que múltiples pantallas (Perfil, Feed, Ajustes) realicen peticiones redundantes a Firestore para obtener los datos básicos del usuario (nombre, foto, nivel).

7. **Recarga Reactiva de Feed (Hoy):**
    - **Decisión:** Uso de `LaunchedEffect(Unit)` en la raíz de las vistas de pestañas para forzar la actualización de datos frescos al navegar entre ellas.
    - **Razón:** Asegura que el usuario vea siempre la información más reciente sin necesidad de un gesto "Pull-to-refresh" explícito en esta fase inicial.

8. **Parámetro booleano en ruta de navegación (2026-04-15):**
    - **Decisión:** `mostrarAcciones: Boolean` se pasa como segmento de path en la URL de navegación (`detalleactividad/{actividadId}/{mostrarAcciones}`) en lugar de como estado global o parámetro de ViewModel.
    - **Razón:** Mantiene el desacoplamiento entre pantallas. `VistaDetalleActividad` no necesita saber desde dónde viene — solo recibe si debe mostrar o no los botones de acción.

9. **Clasificación de actividades como funciones, no propiedades (2026-04-15):**
    - **Decisión:** Los 4 filtros de actividades en `ActividadesViewModel` son funciones que reciben `uid: String`, no propiedades computadas.
    - **Razón:** El `uid` vive en `MainViewModel`, no en `ActividadesViewModel`. Las funciones evitan el acoplamiento entre ViewModels manteniendo la lógica de clasificación en el lugar correcto (ViewModel de negocio).
