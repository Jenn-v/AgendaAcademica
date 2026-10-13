package sv.edu.ufg.agenda.dao;

import java.util.List;
import sv.edu.ufg.agenda.modelo.Tarea;

public interface TareaDAO extends DAO<Tarea> {
    List<Tarea> listarPorMateria(int materiaId);
    List<Tarea> buscarPorTitulo(String texto);
}
