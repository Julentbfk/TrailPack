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

### Fase 7: Feed Social y Actividades
- **Estructura Social:** Creación del paquete `vistas/actividadespublicadas` para gestionar el feed de la comunidad.
- **Lógica de Negocio (ViewModel):** 
    - `ActividadesViewModel`: Implementación de "Join" en memoria (Actividad + Ruta).
- **Refactorización y Arquitectura de UI:**
    - **Globalización de Notificaciones (Toasts):** Se ha movido el observador de `notificationMessage` a `AppNavegation.kt`.
    - **Estandarización de PopUps:** Se ha refactorizado `PopUpPublicarRuta` para que sea un componente `Dialog` de Compose.
    - **Fixes de Datos:** Corregida la recuperación de rutas individuales en `MapsRepository`.
    - **Sincronización de UI:** Unificada la lógica de los botones "Unirse/Salir" en `VistaDetalleActividad`.

### Fase 8: Gestión de Actividades y Refinamiento (Hoy)
- **Modelo de Datos:** Ampliación de `Actividad` con campos específicos para `horasalida` y `puntoencuentro`.
- **UI de Entrada:** Implementación de `SelectorHoraMejorado` con `TimePicker` nativo de Compose.
- **Gestión de Autoría:** Implementación de permisos basados en `idcreador` para habilitar funciones de edición y borrado.
- **Edición Atómica:** Creación de `PopUpEditarActividad` para realizar actualizaciones masivas de datos en Firestore mediante un `Map<String, Any>`.
- **Eliminación:** Implementación de borrado físico de actividades en Firestore con retorno seguro a la vista anterior mediante el `Enrutador`.
- **Arquitectura de Navegación:** Desacoplamiento de vistas mediante callbacks (`onBack`) inyectados desde el `AppNavigation`.
