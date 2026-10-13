package sv.edu.ufg.agenda.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import sv.edu.ufg.agenda.modelo.Tarea;

/** Implementación alternativa en memoria: los datos se pierden al cerrar la app. */
public class TareaDAOMemoria implements TareaDAO {
    private final List<Tarea> datos = new ArrayList<>(); private int siguienteId = 1;
    @Override public void insertar(Tarea t) { t.setId(siguienteId++); datos.add(t); }
    @Override public Tarea buscar(int id) { for(Tarea t:datos) if(t.getId()==id) return t; return null; }
    @Override public List<Tarea> listar() { return new ArrayList<>(datos); }
    @Override public boolean actualizar(Tarea nuevo) { for(int i=0;i<datos.size();i++) if(datos.get(i).getId()==nuevo.getId()) { datos.set(i,nuevo); return true; } return false; }
    @Override public boolean eliminar(int id) { return datos.removeIf(t -> t.getId()==id); }
    @Override public List<Tarea> listarPorMateria(int materiaId) { List<Tarea> r=new ArrayList<>(); for(Tarea t:datos) if(t.getMateriaId()==materiaId) r.add(t); return r; }
    @Override public List<Tarea> buscarPorTitulo(String texto) { List<Tarea> r=new ArrayList<>(); String q=texto==null?"":texto.toLowerCase(Locale.ROOT); for(Tarea t:datos) if(t.getTitulo()!=null && t.getTitulo().toLowerCase(Locale.ROOT).contains(q)) r.add(t); return r; }
}
