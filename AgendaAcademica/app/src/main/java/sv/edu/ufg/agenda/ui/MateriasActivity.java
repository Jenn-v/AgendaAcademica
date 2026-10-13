package sv.edu.ufg.agenda.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import sv.edu.ufg.agenda.AgendaApp;
import sv.edu.ufg.agenda.R;
import sv.edu.ufg.agenda.dao.DAOFactory;
import sv.edu.ufg.agenda.dao.MateriaDAO;
import sv.edu.ufg.agenda.dao.TareaDAO;
import sv.edu.ufg.agenda.modelo.Materia;
import sv.edu.ufg.agenda.modelo.Tarea;

public class MateriasActivity extends AppCompatActivity implements MateriaAdapter.Acciones {
    private MateriaDAO materiaDAO; private TareaDAO tareaDAO; private MateriaAdapter adapter;
    private EditText etNombre, etDocente, etBuscar; private Button btnGuardar; private Materia enEdicion;
    @Override protected void onCreate(Bundle estado) {
        super.onCreate(estado); setContentView(R.layout.activity_materias);
        DAOFactory daos = ((AgendaApp) getApplication()).daos(); materiaDAO = daos.materias(); tareaDAO = daos.tareas();
        etNombre = findViewById(R.id.etNombre); etDocente = findViewById(R.id.etDocente); etBuscar = findViewById(R.id.etBuscarMateria); btnGuardar = findViewById(R.id.btnGuardar);
        RecyclerView lista = findViewById(R.id.rvMaterias); lista.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MateriaAdapter(this); lista.setAdapter(adapter);
        btnGuardar.setOnClickListener(v -> guardar());
        etBuscar.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int st, int count, int after) { }
            @Override public void onTextChanged(CharSequence s, int st, int before, int count) { refrescar(); }
            @Override public void afterTextChanged(Editable e) { }
        });
        refrescar();
    }
    private void guardar() {
        String nombre = etNombre.getText().toString().trim(); String docente = etDocente.getText().toString().trim();
        if (nombre.isEmpty()) { etNombre.setError("El nombre es obligatorio"); return; }
        if (enEdicion == null) materiaDAO.insertar(new Materia(0, nombre, docente));
        else { enEdicion.setNombre(nombre); enEdicion.setDocente(docente); materiaDAO.actualizar(enEdicion); }
        limpiarFormulario(); refrescar(); Toast.makeText(this, "Materia guardada", Toast.LENGTH_SHORT).show();
    }
    private void limpiarFormulario() { enEdicion = null; etNombre.setText(""); etDocente.setText(""); btnGuardar.setText("Agregar materia"); }
    private void refrescar() {
        String busqueda = etBuscar == null ? "" : etBuscar.getText().toString().trim();
        List<Materia> materias = busqueda.isEmpty() ? materiaDAO.listar() : materiaDAO.buscarPorNombre(busqueda);
        Map<Integer, Integer> pendientes = new HashMap<>();
        for (Materia m : materiaDAO.listar()) {
            int cuenta = 0; for (Tarea t : tareaDAO.listarPorMateria(m.getId())) if (!t.isCompletada()) cuenta++;
            pendientes.put(m.getId(), cuenta);
        }
        adapter.mostrar(materias, pendientes);
    }
    @Override public void editar(Materia materia) { enEdicion = materia; etNombre.setText(materia.getNombre()); etDocente.setText(materia.getDocente()); btnGuardar.setText("Actualizar materia"); etNombre.requestFocus(); }
    @Override public void verTareas(Materia materia) { Intent i = new Intent(this, TareasActivity.class); i.putExtra("materiaId", materia.getId()); startActivity(i); }
    @Override public void eliminar(Materia materia) {
        if (!tareaDAO.listarPorMateria(materia.getId()).isEmpty()) { Toast.makeText(this, "No se puede eliminar: la materia tiene tareas", Toast.LENGTH_LONG).show(); return; }
        materiaDAO.eliminar(materia.getId()); if (enEdicion != null && enEdicion.getId() == materia.getId()) limpiarFormulario(); refrescar();
        Toast.makeText(this, "Materia eliminada", Toast.LENGTH_SHORT).show();
    }
    @Override protected void onResume() { super.onResume(); if (adapter != null) refrescar(); }
}
