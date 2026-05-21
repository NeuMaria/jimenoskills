package com.informatica.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Profesor extends Persona {
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> cursos = new ArrayList<>();

    public Profesor(){

    }

    public Profesor(String dni, String nombre, String apellido, String direccion, int telefono, List cursos) {
        super(dni, nombre, apellido, direccion, telefono);
        this.cursos = cursos;
    }

    public List<String> getCursos() {
        return cursos;
    }

    public void setCursos(List<String> cursos) {
        this.cursos = cursos;
    }

    @Override
    public String toString() {
        return "Profesor{" +
                "cursos=" + cursos +
                '}';
    }
}
