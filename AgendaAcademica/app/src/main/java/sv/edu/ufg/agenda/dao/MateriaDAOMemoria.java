package sv.edu.ufg.agenda.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import sv.edu.ufg.agenda.modelo.Materia;

/** Implementación alternativa en memoria: los datos se pierden al cerrar la app. */
public class MateriaDAOMemoria implements MateriaDAO {
    private final List<Materia> datos = new ArrayList<>(); private int siguienteId = 1;
    @Override public void insertar(Materia m) { m.setId(siguienteId++); datos.add(m); }
    @Override public Materia buscar(int id) { for (Materia m : datos) if (m.getId() == id) return m; return null; }
    @Override public List<Materia> listar() { return new ArrayList<>(datos); }
    @Override public boolean actualizar(Materia nuevo) { for (int i=0;i<datos.size();i++) if (datos.get(i).getId()==nuevo.getId()) { datos.set(i,nuevo); return true; } return false; }
    @Override public boolean eliminar(int id) { return datos.removeIf(m -> m.getId()==id); }
    @Override public List<Materia> buscarPorNombre(String texto) { List<Materia> r=new ArrayList<>(); String q=texto==null?"":texto.toLowerCase(Locale.ROOT); for(Materia m:datos) if(m.getNombre()!=null && m.getNombre().toLowerCase(Locale.ROOT).contains(q)) r.add(m); return r; }
}
