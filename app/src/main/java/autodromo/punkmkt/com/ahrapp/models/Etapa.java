package autodromo.punkmkt.com.ahrapp.models;

/**
 * Created by DaniPunk on 27/07/16.
 */
public class Etapa {
    private Integer id;
    private String nombre;
    private String tipo;

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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}