package sv.edu.ufg.agenda.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import sv.edu.ufg.agenda.AgendaApp;
import sv.edu.ufg.agenda.R;
import sv.edu.ufg.agenda.dao.DAOFactory;
import sv.edu.ufg.agenda.dao.MateriaDAO;
import sv.edu.ufg.agenda.dao.TareaDAO;
import sv.edu.ufg.agenda.modelo.Materia;
import sv.edu.ufg.agenda.modelo.Tarea;

public class TareasActivity extends AppCompatActivity implements TareaAdapter.Acciones {
    private MateriaDAO materiaDAO; private TareaDAO tareaDAO; private int materiaId;
    private Materia materia; private TareaAdapter adapter; private EditText etTitulo, etFecha, etBuscar;
    private Spinner spPrioridad; private Button btnGuardar; private TextView tvMateria, tvResumen; private Tarea enEdicion;
    @Override protected void onCreate(Bundle estado) {
        super.onCreate(estado); setContentView(R.layout.activity_tareas);
        DAOFactory daos = ((AgendaApp) getApplication()).daos(); materiaDAO = daos.materias(); tareaDAO = daos.tareas();
        materiaId = getIntent().getIntExtra("materiaId", -1); materia = materiaDAO.buscar(materiaId);
        if (materia == null) { Toast.makeText(this, "La materia ya no existe", Toast.LENGTH_LONG).show(); finish(); return; }
        tvMateria = findViewById(R.id.tvMateria); tvResumen = findViewById(R.id.tvResumen); tvMateria.setText("Tareas: " + materia.getNombre());
        etTitulo = findViewById(R.id.etTitulo); etFecha = findViewById(R.id.etFecha); etBuscar = findViewById(R.id.etBuscarTarea); spPrioridad = findViewById(R.id.spPrioridad); btnGuardar = findViewById(R.id.btnGuardarTarea);
        ArrayAdapter<String> prioridades = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[]{"Alta", "Media", "Baja"}); prioridades.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); spPrioridad.setAdapter(prioridades);
        RecyclerView lista = findViewById(R.id.rvTareas); lista.setLayoutManager(new LinearLayoutManager(this)); adapter = new TareaAdapter(this); lista.setAdapter(adapter);
        btnGuardar.setOnClickListener(v -> guardar());
        etBuscar.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int st, int count, int after) { }
            @Override public void onTextChanged(CharSequence s, int st, int before, int count) { refrescar(); }
            @Override public void afterTextChanged(Editable e) { }
        });
        findViewById(R.id.btnVolver).setOnClickListener(v -> finish()); refrescar();
    }
    private boolean fechaValida(String texto) {
        if (texto == null || !texto.matches("\\d{4}-\\d{2}-\\d{2}")) return false;
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd", Locale.US); formato.setLenient(false);
        try { Date fecha = formato.parse(texto); return fecha != null && formato.format(fecha).equals(texto); }
        catch (ParseException ex) { return false; }
    }
    private void guardar() {
        String titulo = etTitulo.getText().toString().trim(); String fecha = etFecha.getText().toString().trim();
        if (titulo.isEmpty()) { etTitulo.setError("El título es obligatorio"); return; }
        if (!fechaValida(fecha)) { etFecha.setError("Usa una fecha real con formato AAAA-MM-DD"); return; }
        String prioridad = spPrioridad.getSelectedItem().toString();
        if (enEdicion == null) { Tarea tarea = new Tarea(0, materiaId, titulo, fecha); tarea.setPrioridad(prioridad); tareaDAO.insertar(tarea); }
        else { enEdicion.setTitulo(titulo); enEdicion.setFechaEntrega(fecha); enEdicion.setPrioridad(prioridad); tareaDAO.actualizar(enEdicion); }
        limpiarFormulario(); refrescar(); Toast.makeText(this, "Tarea guardada", Toast.LENGTH_SHORT).show();
    }
    private void limpiarFormulario() { enEdicion = null; etTitulo.setText(""); etFecha.setText(""); spPrioridad.setSelection(1); btnGuardar.setText("Agregar tarea"); }
    private void refrescar() {
        if (adapter == null) return;
        String busqueda = etBuscar == null ? "" : etBuscar.getText().toString().trim();
        List<Tarea> tareas = busqueda.isEmpty() ? tareaDAO.listarPorMateria(materiaId) : filtrarMateria(tareaDAO.buscarPorTitulo(busqueda));
        Collections.sort(tareas, (a, b) -> a.getFechaEntrega().compareTo(b.getFechaEntrega()));
        int completadas = 0; for (Tarea tarea : tareaDAO.listarPorMateria(materiaId)) if (tarea.isCompletada()) completadas++;
        tvResumen.setText(completadas + " de " + tareaDAO.listarPorMateria(materiaId).size() + " completadas"); adapter.mostrar(tareas);
    }
    private List<Tarea> filtrarMateria(List<Tarea> encontradas) { encontradas.removeIf(t -> t.getMateriaId() != materiaId); return encontradas; }
    @Override public void editar(Tarea tarea) {
        enEdicion = tarea; etTitulo.setText(tarea.getTitulo()); etFecha.setText(tarea.getFechaEntrega());
        String p = tarea.getPrioridad(); spPrioridad.setSelection(p.equals("Alta") ? 0 : p.equals("Baja") ? 2 : 1); btnGuardar.setText("Actualizar tarea");
    }
    @Override public void eliminar(Tarea tarea) {
        tareaDAO.eliminar(tarea.getId()); if (enEdicion != null && enEdicion.getId() == tarea.getId()) limpiarFormulario(); refrescar();
        Toast.makeText(this, "Tarea eliminada", Toast.LENGTH_SHORT).show();
    }
    @Override public void cambiarEstado(Tarea tarea, boolean completada) { tarea.setCompletada(completada); tareaDAO.actualizar(tarea); refrescar(); }
}
