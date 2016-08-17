package com.punkmkt.formula12016.models;

/**
 * Created by DaniPunk on 27/07/16.
 */
public class Posicion {
    private Integer id;
    private Integer posicion;
    private String tiempo;
    private String gap;
    private String laps;
    private String q1;
    private String q2;
    private String q3;
    private String puntos;
    private String piloto_sobrenombre;
    private String escuderia;
    private String escuderia_img;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPosicion() {
        return posicion;
    }

    public void setPosicion(Integer posicion) {
        this.posicion = posicion;
    }

    public String getTiempo() {
        return tiempo;
    }

    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
    }

    public String getGap() {
        return gap;
    }

    public void setGap(String gap) {
        this.gap = gap;
    }

    public String getLaps() {
        return laps;
    }

    public void setLaps(String laps) {
        this.laps = laps;
    }

    public String getQ1() {
        return q1;
    }

    public void setQ1(String q1) {
        this.q1 = q1;
    }

    public String getQ2() {
        return q2;
    }

    public void setQ2(String q2) {
        this.q2 = q2;
    }

    public String getQ3() {
        return q3;
    }

    public void setQ3(String q3) {
        this.q3 = q3;
    }

    public String getPuntos() {
        return puntos;
    }

    public void setPuntos(String puntos) {
        this.puntos = puntos;
    }

    public String getPiloto_sobrenombre() {
        return piloto_sobrenombre;
    }

    public void setPiloto_sobrenombre(String piloto_sobrenombre) {
        this.piloto_sobrenombre = piloto_sobrenombre;
    }

    public String getEscuderia() {
        return escuderia;
    }

    public void setEscuderia(String escuderia) {
        this.escuderia = escuderia;
    }

    public String getEscuderia_img() {
        return escuderia_img;
    }

    public void setEscuderia_img(String escuderia_img) {
        this.escuderia_img = escuderia_img;
    }
}