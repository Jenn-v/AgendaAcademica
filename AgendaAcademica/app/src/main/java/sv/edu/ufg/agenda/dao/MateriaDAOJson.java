package sv.edu.ufg.agenda.dao;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import sv.edu.ufg.agenda.modelo.Materia;

public class MateriaDAOJson extends JsonDAOBase<Materia> implements MateriaDAO {
    public MateriaDAOJson(File archivo) { super(archivo, Materia.class); }
    @Override public List<Materia> buscarPorNombre(String texto) {
        List<Materia> resultado = new ArrayList<>();
        String busqueda = texto == null ? "" : texto.toLowerCase(Locale.ROOT).trim();
        for (Materia materia : cargar()) {
            if (materia.getNombre() != null && materia.getNombre().toLowerCase(Locale.ROOT).contains(busqueda)) resultado.add(materia);
        }
        return resultado;
    }
}
