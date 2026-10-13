package sv.edu.ufg.agenda.dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import sv.edu.ufg.agenda.modelo.Entidad;

public abstract class JsonDAOBase<T extends Entidad> implements DAO<T> {
    private final File archivo;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Type tipoLista;

    protected JsonDAOBase(File archivo, Class<T> clase) {
        this.archivo = archivo;
        this.tipoLista = TypeToken.getParameterized(List.class, clase).getType();
    }
    @Override public void insertar(T entidad) {
        List<T> lista = cargar();
        entidad.setId(siguienteId(lista));
        lista.add(entidad);
        guardar(lista);
    }
    @Override public T buscar(int id) {
        for (T entidad : cargar()) if (entidad.getId() == id) return entidad;
        return null;
    }
    @Override public List<T> listar() { return cargar(); }
    @Override public boolean actualizar(T nuevo) {
        List<T> lista = cargar();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getId() == nuevo.getId()) {
                lista.set(i, nuevo); guardar(lista); return true;
            }
        }
        return false;
    }
    @Override public boolean eliminar(int id) {
        List<T> lista = cargar();
        boolean eliminado = lista.removeIf(e -> e.getId() == id);
        if (eliminado) guardar(lista);
        return eliminado;
    }
    protected List<T> cargar() {
        if (!archivo.exists()) return new ArrayList<>();
        try (Reader lector = new InputStreamReader(new FileInputStream(archivo), StandardCharsets.UTF_8)) {
            List<T> lista = gson.fromJson(lector, tipoLista);
            return lista == null ? new ArrayList<>() : lista;
        } catch (IOException ex) {
            throw new RuntimeException("No se pudo leer " + archivo, ex);
        }
    }
    protected void guardar(List<T> lista) {
        File carpeta = archivo.getParentFile();
        if (carpeta != null && !carpeta.exists() && !carpeta.mkdirs())
            throw new RuntimeException("No se pudo crear " + carpeta);
        try (Writer escritor = new OutputStreamWriter(new FileOutputStream(archivo), StandardCharsets.UTF_8)) {
            gson.toJson(lista, tipoLista, escritor);
        } catch (IOException ex) {
            throw new RuntimeException("No se pudo escribir " + archivo, ex);
        }
    }
    private int siguienteId(List<T> lista) {
        int mayor = 0;
        for (T entidad : lista) mayor = Math.max(mayor, entidad.getId());
        return mayor + 1;
    }
}
