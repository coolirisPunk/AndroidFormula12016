package autodromo.punkmkt.com.ahrapp.models;


/**
 * Created by DaniPunk on 28/07/16.
 */
public class Piloto {
    private Integer id;
    private String name;
    private String picture;
    private String numero;
    private String nacionalidad;
    private String fecha_nacimiento;
    private String lugar_nacimiento;
    private String campeonatos;

    public Piloto() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }


    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public String getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public void setFecha_nacimiento(String fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }

    public String getCampeonatos() {
        return campeonatos;
    }

    public void setCampeonatos(String campeonatos) {
        this.campeonatos = campeonatos;
    }

    public String getLugar_nacimiento() {
        return lugar_nacimiento;
    }

    public void setLugar_nacimiento(String lugar_nacimiento) {
        this.lugar_nacimiento = lugar_nacimiento;
    }
}
