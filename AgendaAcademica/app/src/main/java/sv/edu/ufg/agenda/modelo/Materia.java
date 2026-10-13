package sv.edu.ufg.agenda.modelo;

public class Materia implements Entidad {
    private int id;
    private String nombre;
    private String docente;

    public Materia() { }
    public Materia(int id, String nombre, String docente) {
        this.id = id; this.nombre = nombre; this.docente = docente;
    }
    @Override public int getId() { return id; }
    @Override public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDocente() { return docente; }
    public void setDocente(String docente) { this.docente = docente; }
}
