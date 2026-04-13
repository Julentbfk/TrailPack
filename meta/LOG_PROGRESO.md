# [LOG DE PROGRESO - TRAILPACK]

## Cronología Técnica

### Fase 1-4: Cimientos
- Estructura base del proyecto, Autenticación Firebase, Navegación inicial y Perfil de usuario.

### Fase 5: Seguridad y Refactorización
- Implementación de cambio de contraseña, borrado de cuenta y validación de formularios.

### Fase 6: Núcleo Funcional (MVP - Mapas)
- **Navegación:** Implementación de navegación completa entre tabs (Mapa, Social, Perfil) con estado centralizado.
- **Mapas:** Desarrollo de `VistaMapa` con marcadores reactivos y `MapsRepository`.
- **Rutas:** Carga dinámica de rutas asociadas a parques vía `BottomSheet` y persistencia en Firestore.
- **Publicación:** Implementación de `PopUpPublicarRuta` y lógica de guardado de actividades vinculadas a rutas.

### Fase 7: Feed Social y Actividades (En curso - Hoy)
- **Estructura Social:** Creación del paquete `vistas/actividadespublicadas` para gestionar el feed de la comunidad.
- **Lógica de Negocio (ViewModel):** 
    - `ActividadesViewModel`: Implementación de "Join" en memoria (Actividad + Ruta).
- **Refactorización y Arquitectura de UI (Sesión Actual):**
    - **Globalización de Notificaciones (Toasts):** Se ha movido el observador de `notificationMessage` a `AppNavegation.kt`. Esto garantiza que los mensajes de feedback sean visibles desde cualquier profundidad de la pila de navegación (como las vistas de detalle).
    - **Estandarización de PopUps:** Se ha refactorizado `PopUpPublicarRuta` para que sea un componente `Dialog` de Compose. Esto soluciona los problemas de superposición (Z-index) y permite que el diálogo sea visible tanto en el `Scaffold` principal como en las vistas detalladas.
    - **Fixes de Datos:** Corregida la recuperación de rutas individuales en `MapsRepository` para usar el campo `idruta` como clave.
    - **Sincronización de UI:** Unificada la lógica de los botones "Unirse/Salir" en `VistaDetalleActividad` para ser coherente con el estado de Firebase.
- **Globalización de Estado:**
    - Integración de `usuarioGlobal` en `MainViewModel`.
    - Centralización de utilidades core (fechas, notificaciones).
