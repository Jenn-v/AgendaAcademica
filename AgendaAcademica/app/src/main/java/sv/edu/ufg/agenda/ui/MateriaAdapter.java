package sv.edu.ufg.agenda.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import sv.edu.ufg.agenda.R;
import sv.edu.ufg.agenda.modelo.Materia;

public class MateriaAdapter extends RecyclerView.Adapter<MateriaAdapter.Fila> {
    public interface Acciones {
        void editar(Materia materia);
        void verTareas(Materia materia);
        void eliminar(Materia materia);
    }
    private final List<Materia> datos = new ArrayList<>();
    private Map<Integer, Integer> pendientes = new HashMap<>();
    private final Acciones acciones;
    public MateriaAdapter(Acciones acciones) { this.acciones = acciones; }
    public void mostrar(List<Materia> nuevas, Map<Integer, Integer> conteo) {
        datos.clear(); datos.addAll(nuevas); pendientes = new HashMap<>(conteo); notifyDataSetChanged();
    }
    @NonNull @Override public Fila onCreateViewHolder(@NonNull ViewGroup padre, int tipo) {
        return new Fila(LayoutInflater.from(padre.getContext()).inflate(R.layout.item_materia, padre, false));
    }
    @Override public void onBindViewHolder(@NonNull Fila fila, int posicion) {
        Materia materia = datos.get(posicion);
        fila.tvTexto.setText(materia.getNombre() + "\nDocente: " + (materia.getDocente() == null || materia.getDocente().trim().isEmpty() ? "No indicado" : materia.getDocente()));
        int totalPendientes = pendientes.containsKey(materia.getId()) ? pendientes.get(materia.getId()) : 0;
        fila.tvPendientes.setText("Tareas pendientes: " + totalPendientes);
        fila.itemView.setOnClickListener(v -> acciones.editar(materia));
        fila.btnTareas.setOnClickListener(v -> acciones.verTareas(materia));
        fila.btnEliminar.setOnClickListener(v -> acciones.eliminar(materia));
    }
    @Override public int getItemCount() { return datos.size(); }
    static class Fila extends RecyclerView.ViewHolder {
        TextView tvTexto, tvPendientes; Button btnTareas, btnEliminar;
        Fila(@NonNull View vista) {
            super(vista); tvTexto = vista.findViewById(R.id.tvTexto); tvPendientes = vista.findViewById(R.id.tvPendientes);
            btnTareas = vista.findViewById(R.id.btnTareas); btnEliminar = vista.findViewById(R.id.btnEliminar);
        }
    }
}
