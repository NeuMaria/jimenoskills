package com.informatica.model;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Alumno extends Persona{
    private String curso;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> asignaturas = new ArrayList<>();

    public Alumno(){

    }

    public Alumno(String dni, String nombre, String apellido, String direccion, int telefono, List asignaturas) {
        super(dni, nombre, apellido, direccion, telefono);
        this.asignaturas = asignaturas;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public List<String> getAsignaturas() {
        return asignaturas;
    }

    public void setAsignaturas(List<String> asignaturas) {
        this.asignaturas = asignaturas;
    }

    @Override
    public String toString() {
        return "Alumno{" +
                "curso='" + curso + '\'' +
                ", asignaturas=" + asignaturas +
                '}';
    }
}
