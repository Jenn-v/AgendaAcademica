package sv.edu.ufg.agenda.dao;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import sv.edu.ufg.agenda.modelo.Tarea;

public class TareaDAOJson extends JsonDAOBase<Tarea> implements TareaDAO {
    public TareaDAOJson(File archivo) { super(archivo, Tarea.class); }
    @Override public List<Tarea> listarPorMateria(int materiaId) {
        List<Tarea> resultado = new ArrayList<>();
        for (Tarea tarea : cargar()) if (tarea.getMateriaId() == materiaId) resultado.add(tarea);
        return resultado;
    }
    @Override public List<Tarea> buscarPorTitulo(String texto) {
        List<Tarea> resultado = new ArrayList<>();
        String busqueda = texto == null ? "" : texto.toLowerCase(Locale.ROOT).trim();
        for (Tarea tarea : cargar()) {
            if (tarea.getTitulo() != null && tarea.getTitulo().toLowerCase(Locale.ROOT).contains(busqueda)) resultado.add(tarea);
        }
        return resultado;
    }
}
