package mx.tecnm.mirecordatorios.models;

public class Recordatorios {

    public String descripcion;
    public String hora;
    public String dia;
    private int color;
    private boolean importante;

    public Recordatorios() {
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public boolean isImportante() {
        return importante;
    }

    public void setImportante(boolean importante) {
        this.importante = importante;
    }

    @Override
    public String toString() {
        return "Recordatorios{" +
                "descripcion='" + descripcion + '\'' +
                ", hora='" + hora + '\'' +
                ", dia='" + dia + '\'' +
                ", color=" + color +
                ", importante=" + importante +
                '}';
    }
}
