package sv.edu.ufg.agenda.dao;

import java.util.List;
import sv.edu.ufg.agenda.modelo.Materia;

public interface MateriaDAO extends DAO<Materia> {
    List<Materia> buscarPorNombre(String texto);
}
