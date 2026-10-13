package sv.edu.ufg.agenda.dao;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import java.io.File;
import java.util.List;
import static org.junit.Assert.*;
import sv.edu.ufg.agenda.modelo.Tarea;

public class TareaDAOJsonTest {
    @Rule public TemporaryFolder carpeta = new TemporaryFolder();

    @Test public void insertarListarPorMateriaYEliminar() throws Exception {
        File archivo = new File(carpeta.newFolder(), "tareas.json");
        TareaDAO dao = new TareaDAOJson(archivo);
        dao.insertar(new Tarea(0, 1, "Práctica", "2026-10-20"));
        dao.insertar(new Tarea(0, 2, "Proyecto", "2026-10-25"));
        dao.insertar(new Tarea(0, 1, "Cuestionario", "2026-10-22"));

        List<Tarea> materiaUno = dao.listarPorMateria(1);
        assertEquals(2, materiaUno.size());
        int id = materiaUno.get(0).getId();
        assertTrue(dao.eliminar(id));
        assertEquals(1, dao.listarPorMateria(1).size());
    }
}
