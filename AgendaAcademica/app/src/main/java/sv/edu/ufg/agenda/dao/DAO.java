package sv.edu.ufg.agenda.dao;

import java.util.List;

public interface DAO<T> {
    void insertar(T entidad);
    T buscar(int id);
    List<T> listar();
    boolean actualizar(T entidad);
    boolean eliminar(int id);
}
