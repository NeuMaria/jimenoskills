package com.informatica.model;

import jakarta.persistence.*;

@Entity
public class Incidencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "dni_alumno")
    private Alumno alumno;

    @ManyToOne
    @JoinColumn(name = "id_reserva")
    private Reserva reserva;

    private String descripcion;

    public Incidencia() {
    }

    public Incidencia(Alumno alumno, Reserva reserva, String descripcion) {
        this.alumno = alumno;
        this.reserva = reserva;
        this.descripcion = descripcion;
    }

    public int getId() {
        return id;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    public Reserva getReserva() {
        return reserva;
    }

    public void setReserva(Reserva reserva) {
        this.reserva = reserva;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "Incidencia{" +
                "id=" + id +
                ", alumno=" + alumno +
                ", reserva=" + reserva +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}
