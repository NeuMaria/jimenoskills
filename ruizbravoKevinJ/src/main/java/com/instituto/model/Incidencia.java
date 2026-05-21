package com.instituto.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "incidencias")
public class Incidencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String descripcion;

    private LocalDate fecha;

    @ManyToOne(optional = false)
    @JoinColumn(name = "alumno_id")
    private Alumno alumno;

    @ManyToOne
    @JoinColumn(name = "aula_id")
    private Aula aula;

    public Incidencia() {}

    public Incidencia(String descripcion, LocalDate fecha,
                      Alumno alumno, Aula aula) {
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.alumno = alumno;
        this.aula = aula;
    }

    // getters y setters

    public int getId() {
        return id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    public Aula getAula() {
        return aula;
    }

    public void setAula(Aula aula) {
        this.aula = aula;
    }

    @Override
    public String toString() {
        return descripcion + " - " + fecha + " - " + alumno;
    }
}