package com.example.serj.futbol;

import java.io.Serializable;

public class Partido implements Serializable{

    private long id;
    private long idJugador;
    private float valoracion;
    private String contrincante;

    public Partido(){}

    public Partido(long id, long idJugador, float valoracion, String contrincante) {
        this.id = id;
        this.idJugador = idJugador;
        this.valoracion = valoracion;
        this.contrincante = contrincante;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdJugador() {
        return idJugador;
    }

    public void setIdJugador(long idJugador) {
        this.idJugador = idJugador;
    }

    public float getValoracion() {
        return valoracion;
    }

    public void setValoracion(float valoracion) {
        this.valoracion = valoracion;
    }

    public String getContrincante() {
        return contrincante;
    }

    public void setContrincante(String contrincante) {
        this.contrincante = contrincante;
    }

    @Override
    public String toString() {
        return "Partido{" +
                "id=" + id +
                ", idJugador=" + idJugador +
                ", valoracion=" + valoracion +
                ", contrincante='" + contrincante + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || ((Object) this).getClass() != o.getClass()) return false;

        Partido partido = (Partido) o;

        if (id != partido.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
