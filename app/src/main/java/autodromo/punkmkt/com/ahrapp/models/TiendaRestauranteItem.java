package autodromo.punkmkt.com.ahrapp.models;

/**
 * Created by sebastianmendezgiron on 11/08/16.
 */

public class TiendaRestauranteItem {

    private Integer id;
    private String nombre;
    private Integer cantidad;
    private String descripcion;
    private Float precio;

    /*public TiendaRestauranteItem(Integer id, String nombre, String descripcion, Float precio) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
    }*/


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Float getPrecio() {
        return precio;
    }

    public void setPrecio(Float precio) {
        this.precio = precio;
    }
}
