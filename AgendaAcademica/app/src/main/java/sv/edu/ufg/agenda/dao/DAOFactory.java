package sv.edu.ufg.agenda.dao;

import java.io.File;

public class DAOFactory {
    private final MateriaDAO materiaDAO;
    private final TareaDAO tareaDAO;
    public DAOFactory(File carpetaDatos) {
        materiaDAO = new MateriaDAOJson(new File(carpetaDatos, "materias.json"));
        tareaDAO = new TareaDAOJson(new File(carpetaDatos, "tareas.json"));
    }
    public MateriaDAO materias() { return materiaDAO; }
    public TareaDAO tareas() { return tareaDAO; }
}
