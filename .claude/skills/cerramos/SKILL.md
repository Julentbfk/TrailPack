---
name: cerramos
description: Cierra la sesión de desarrollo de TrailPack. Actualiza TASKS.md y LOG_PROGRESO.md con el trabajo realizado.
---

Ejecuta el protocolo de cierre de sesión de TrailPack:

1. **Actualiza `meta/TASKS.md`:**
   - Marca como `[x]` las tareas completadas durante esta sesión.
   - Mueve a "En curso" cualquier tarea que haya quedado a medias.
   - Añade tareas nuevas que hayan surgido durante la sesión.
   - No borres nada del historial de completadas.

2. **Expande `meta/LOG_PROGRESO.md`:**
   - Añade una nueva entrada al final con el hito de esta sesión.
   - Formato: `### Sesión [fecha actual] — [título del hito]` seguido de bullet points técnicos concretos.
   - No reescribas entradas anteriores, solo añade.

3. **Actualiza `meta/DECISION_LOG.md` si aplica:**
   - Solo si durante la sesión se tomó una decisión arquitectónica relevante (nueva convención, cambio de patrón, decisión de diseño de datos).
   - Si no hubo ninguna, no toques el archivo.

Al terminar, preséntame un resumen de los cambios realizados en los logs.
