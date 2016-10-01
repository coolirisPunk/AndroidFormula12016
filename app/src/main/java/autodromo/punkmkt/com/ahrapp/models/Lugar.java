package autodromo.punkmkt.com.ahrapp.models;

/**
 * Created by germanpunk on 31/08/16.
 */
public class Lugar {
    private Integer id;
    private String nombre;
    private String ubicacion;
    private String telefono;
    private String urlmap;
    private String latitud_mapa;
    private String longitud_mapa;
    private String imagen;
    private String exposicion;

    public Lugar(){

    }
    public Lugar(Integer id, String nombre, String ubicacion, String telefono,String latitud_mapa, String longitud_mapa, String imagen){
        this.id = id;
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.telefono = telefono;
        this.latitud_mapa  = latitud_mapa;
        this.longitud_mapa = longitud_mapa;
        this.imagen = imagen;
    }
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

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getUrlmap() {
        return urlmap;
    }

    public void setUrlmap(String urlmap) {
        this.urlmap = urlmap;
    }

    public String getLatitud_mapa() {
        return latitud_mapa;
    }

    public void setLatitud_mapa(String latitud_mapa) {
        this.latitud_mapa = latitud_mapa;
    }

    public String getLongitud_mapa() {
        return longitud_mapa;
    }

    public void setLongitud_mapa(String longitud_mapa) {
        this.longitud_mapa = longitud_mapa;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getExposicion() {
        return exposicion;
    }

    public void setExposicion(String exposicion) {
        this.exposicion = exposicion;
    }
}