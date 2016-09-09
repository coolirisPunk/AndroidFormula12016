package com.punkmkt.formula12016.models;

/**
 * Created by sebastianmendezgiron on 11/08/16.
 */

public class TiendaRestaurante {
    private Integer id;
    private String nombre;
    private String imagen;

    /*public TiendaRestaurante(Integer id, String nombre, String imagen) {
        this.id = id;
        this.nombre = nombre;
        this.imagen = imagen;
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

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
