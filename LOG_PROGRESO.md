# [LOG DE PROGRESO - TRAILPACK]

## Cronología Técnica (Fases 1-5)
- **Fase 1-2:** Componentes reutilizables y Navegación desacoplada (`Enrutador`).
- **Fase 3:** MVVM, `AuthRepository` y relación 1:1 UID en Firestore.
- **Fase 4:** Verificación de email y Guardia de Navegación (`ScaffoldTrailPack`).
- **Fase 5 (Hoy):** 
    - Migración a `FormModel` inmutables.
    - Firebase Storage: Avatares vinculados a Firestore.
    - Layout: Estándar 56.dp y alineación con `weight`.
    - Onboarding: Redirección obligatoria mediante `LaunchedEffect`.
    - Edición: Lógica de precarga de datos y actualizaciones parciales (`mapOf`).

## Notas Técnicas Recientes
- Refactorización de campo `direccion` a `ubicacion`.
- Implementación de `remember(validador, value)` en inputs para reactividad.

## Pendientes Próximos
1. **Paso 3:** Formulario de Edición Avanzada (Género, Fecha Nacimiento con DatePicker, Nacionalidad).
2. **Seguridad:** Re-autenticación para cambios de Password/Email y Borrado de cuenta.
3. **Geolocalización:** Autocompletado de ubicaciones con Google Places API.
