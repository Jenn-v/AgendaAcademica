# Agenda Académica — Guía práctica 3 DAO con JSON

Proyecto Android en Java basado en los conceptos del proyecto original `dao-json`: entidades, interfaces DAO, `JsonDAOBase<T>`, implementaciones JSON y `DAOFactory`.

## Abrir en Android Studio
1. Descomprime `AgendaAcademica.zip`.
2. En Android Studio selecciona **Open** y elige la carpeta `AgendaAcademica` (la que contiene `settings.gradle`).
3. Espera a que Gradle termine la sincronización. Si pregunta por instalar SDK, instala Android SDK 35 y usa JDK 17 para Gradle.
4. Crea un emulador desde Device Manager o conecta un teléfono con depuración USB.
5. Pulsa Run.

## Funciones incluidas
- CRUD de materias y tareas.
- Persistencia en `materias.json` y `tareas.json` dentro del almacenamiento privado de la aplicación.
- Una materia puede tener muchas tareas, vinculadas mediante `materiaId`.
- Validación de campos obligatorios y fechas reales `AAAA-MM-DD`.
- Bloqueo al eliminar materias que tienen tareas.
- Prioridad Alta, Media o Baja en cada tarea.
- Búsqueda por nombre de materia y título de tarea.
- Resumen de tareas pendientes en cada materia y contador de completadas en la pantalla de tareas.
- Orden por fecha de entrega y marcar tareas como completadas.

## Archivos JSON
En Android Studio abre **View > Tool Windows > Device Explorer** y busca:
`/data/data/sv.edu.ufg.agenda/files/datos/`

Los archivos se crean después de guardar datos. El almacenamiento privado se borra si desinstalas la aplicación.

## Estructura
- `modelo`: Entidad, Materia, Tarea.
- `dao`: DAO, MateriaDAO, TareaDAO, JsonDAOBase, MateriaDAOJson, TareaDAOJson, DAOFactory.
- `ui`: Activities y adapters.

## Relación con el ZIP original
El proyecto original era una aplicación Java de consola con Estudiante y Curso. Android necesita Activities, layouts XML, un manifiesto y dependencias Android; por eso la estructura se adapta, pero se conserva el patrón DAO y la lectura/escritura JSON genérica.

## Actividades adicionales 6 y 7
- Se incluyen `MateriaDAOMemoria` y `TareaDAOMemoria` para probar el DAO sin archivos. Para alternar temporalmente, cambia las dos asignaciones dentro del constructor de `DAOFactory` por `new MateriaDAOMemoria()` y `new TareaDAOMemoria()`. En memoria los datos no persisten al cerrar la app; vuelve a las clases JSON para la entrega principal.
- Se incluye `TareaDAOJsonTest`, una prueba JUnit con `TemporaryFolder` que comprueba insertar, listar por materia y eliminar. Ejecuta la prueba desde Android Studio con clic derecho sobre el archivo > Run.
