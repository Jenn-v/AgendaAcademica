package sv.edu.ufg.agenda.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import sv.edu.ufg.agenda.R;
import sv.edu.ufg.agenda.modelo.Tarea;

public class TareaAdapter extends RecyclerView.Adapter<TareaAdapter.Fila> {
    public interface Acciones { void editar(Tarea tarea); void eliminar(Tarea tarea); void cambiarEstado(Tarea tarea, boolean completada); }
    private final List<Tarea> datos = new ArrayList<>();
    private final Acciones acciones;
    public TareaAdapter(Acciones acciones) { this.acciones = acciones; }
    public void mostrar(List<Tarea> nuevas) { datos.clear(); datos.addAll(nuevas); notifyDataSetChanged(); }
    @NonNull @Override public Fila onCreateViewHolder(@NonNull ViewGroup padre, int tipo) {
        return new Fila(LayoutInflater.from(padre.getContext()).inflate(R.layout.item_tarea, padre, false));
    }
    @Override public void onBindViewHolder(@NonNull Fila fila, int posicion) {
        Tarea tarea = datos.get(posicion);
        fila.cbCompletada.setOnCheckedChangeListener(null);
        fila.cbCompletada.setText(tarea.getTitulo()); fila.cbCompletada.setChecked(tarea.isCompletada());
        fila.tvDetalle.setText("Entrega: " + tarea.getFechaEntrega() + " · Prioridad: " + tarea.getPrioridad());
        fila.cbCompletada.setOnCheckedChangeListener((button, marcado) -> acciones.cambiarEstado(tarea, marcado));
        fila.btnEditar.setOnClickListener(v -> acciones.editar(tarea));
        fila.btnEliminar.setOnClickListener(v -> acciones.eliminar(tarea));
    }
    @Override public int getItemCount() { return datos.size(); }
    static class Fila extends RecyclerView.ViewHolder {
        CheckBox cbCompletada; TextView tvDetalle; Button btnEditar, btnEliminar;
        Fila(@NonNull View vista) {
            super(vista); cbCompletada = vista.findViewById(R.id.cbCompletada); tvDetalle = vista.findViewById(R.id.tvDetalle);
            btnEditar = vista.findViewById(R.id.btnEditarTarea); btnEliminar = vista.findViewById(R.id.btnEliminarTarea);
        }
    }
}
