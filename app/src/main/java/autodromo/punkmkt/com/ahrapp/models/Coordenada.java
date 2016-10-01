package autodromo.punkmkt.com.ahrapp.models;

/**
 * Created by DaniPunk on 26/07/16.
 */
public class Coordenada {
    private Double Latitud;
    private Double Longitud;
    private String Titulo;
    private String descripcion;
    public Coordenada(Double latitud, Double longitud, String titulo, String descripcion){
        this.Latitud = latitud;
        this.Longitud = longitud;
        this.Titulo = titulo;
        this.descripcion = descripcion;
    }
    public Double getLatitud() {
        return Latitud;
    }

    public void setLatitud(Double latitud) {
        Latitud = latitud;
    }

    public Double getLongitud() {
        return Longitud;
    }

    public void setLongitud(Double longitud) {
        Longitud = longitud;
    }


    public String getTitulo() {
        return Titulo;
    }

    public void setTitulo(String ciudad) {
        Titulo = ciudad;
    }
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
