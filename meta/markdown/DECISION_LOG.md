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

10. **Favoritas como propiedad del Usuario, no de la Actividad/Ruta (2026-04-15):**
    - **Decisión:** `rutasFavoritas: List<String>` vive en el documento `Usuario` en Firestore, no en `Ruta`.
    - **Razón:** Favoritar es una preferencia personal del usuario. Guardarlo en `Ruta` mezclaría datos de contenido con datos sociales y haría crecer el documento de la ruta con IDs de usuarios. Sigue el mismo patrón que `listaseguidores`, `listasiguiendo`, `listaamigos`.

11. **Parques y rutas default: ingesta a Firestore, no llamadas en runtime (2026-04-15):**
    - **Decisión:** Los parques naturales y rutas default de OpenStreetMap se ingestarán a Firestore mediante scripts de carga única. La app siempre lee de Firestore, nunca de APIs externas en tiempo de ejecución.
    - **Razón:** Las APIs externas (Overpass, IGN) tienen latencia variable y pueden no estar disponibles. Firestore garantiza velocidad y disponibilidad. La API externa es solo la fuente de datos de ingesta, no una dependencia en producción.

9. **Clasificación de actividades como funciones, no propiedades (2026-04-15):**
    - **Decisión:** Los 4 filtros de actividades en `ActividadesViewModel` son funciones que reciben `uid: String`, no propiedades computadas.
    - **Razón:** El `uid` vive en `MainViewModel`, no en `ActividadesViewModel`. Las funciones evitan el acoplamiento entre ViewModels manteniendo la lógica de clasificación en el lugar correcto (ViewModel de negocio).

12. **Hito 6 Fase 10 (estilo de mapa) — diferido indefinidamente (2026-04-18):**
    - **Decisión:** No implementar estilos visuales sobre Google Maps SDK (JSON de estilo, polyline con caps neón, etc.).
    - **Razón:** Alta probabilidad de migración futura a Mapbox u otra API con soporte nativo de terreno 3D, polylines con relieve y efectos visuales avanzados. Cualquier trabajo de estilización sobre Google Maps sería descartado en esa migración. Se retoma únicamente si se descarta la migración a otra API de mapas.

13. **Modelo social: mantener `listaamigos` como conexión explícita bidireccional (2026-04-18):**
    - **Decisión:** Conservar `listaamigos` y `amigoscount` en el modelo `Usuario`. "Amigo" se define como una solicitud explícita aceptada por ambas partes, distinto del follow asimétrico.
    - **Razón:** El concepto de "amigo" tendrá una funcionalidad propia: nivel de privacidad en actividades ("Solo amigos"). Una actividad podrá ser pública, solo para amigos, o privada por invitación. Si fuera seguimiento mutuo automático, cualquier usuario que te siga de vuelta accedería a tus actividades privadas sin control consciente. La solicitud explícita da al usuario control real sobre quién entra en ese círculo.
    - **Flujo de aceptación:** mediante sistema de notificaciones in-app. El receptor ve la solicitud en su bandeja de notificaciones (icono en TopBar de perfil con badge naranja) y acepta/rechaza desde ahí. No hay pantalla de confirmación separada.
    - **Pendiente:** Implementar el sistema de notificaciones (`notificaciones` en Firestore + `notificacionesSinLeer: Int` en `Usuario` + UI de bandeja) y el campo de privacidad en `Actividad` en una fase posterior.

14. **Sistema de amistad diferido hasta buzón de notificaciones (2026-04-18):**
    - **Decisión:** El botón "Añadir amigo" y el flujo de solicitud/aceptación se implementan en el mismo hito que el buzón de notificaciones in-app. No se construye antes.
    - **Razón:** Sin el buzón no hay forma de que el receptor vea y acepte la solicitud. Construir el botón sin el receptor sería funcionalidad incompleta e inútil.

15. **Carga lazy de listas sociales (seguidores/siguiendo) (2026-04-18):**
    - **Decisión:** Las listas de `Usuario` para seguidores y siguiendo se cargan desde Firestore únicamente cuando el usuario pulsa el contador correspondiente, no al abrir el perfil.
    - **Razón:** La mayoría de visitas a un perfil no necesitan ver la lista completa. Cargarla siempre sería un gasto innecesario de lecturas de Firestore y tiempo de carga inicial.
