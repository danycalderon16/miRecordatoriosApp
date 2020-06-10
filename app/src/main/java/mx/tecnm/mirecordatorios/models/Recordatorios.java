package mx.tecnm.mirecordatorios.models;

public class Recordatorios {

    public String descripcion;
    public String hora;
    public String dia;

    public Recordatorios() {
    }

    public Recordatorios(String descripcion, String hora, String dia) {
        this.descripcion = descripcion;
        this.hora = hora;
        this.dia = dia;
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

    @Override
    public String toString() {
        return  "descripcion='" + descripcion + '\'' +
                ", hora='" + hora + '\'' +
                ", dia='" + dia + '\'' +
                '}';
    }
}
