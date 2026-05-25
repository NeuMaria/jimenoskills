package com.informatica.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa a un alumno del instituto.
 * Contiene sus datos personales, el curso en el que está matriculado
 * y las asignaturas que cursa.
 */
@Entity
public class Alumno {

    @Id
    private String dni;

    private String nombre;
    private String apellido;
    private String direccion;
    private long telefono;
    private String curso;

    // Lista de asignaturas almacenada en tabla auxiliar ALUMNO_ASIGNATURAS
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "alumno_asignaturas", joinColumns = @JoinColumn(name = "dni_alumno"))
    @Column(name = "asignatura")
    private List<String> asignaturas = new ArrayList<>();

    public Alumno() {}

    public Alumno(String dni, String nombre, String apellido, String direccion, long telefono,
                  String curso, List<String> asignaturas) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.direccion = direccion;
        this.telefono = telefono;
        this.curso = curso;
        this.asignaturas = asignaturas;
    }

    public String getDni()                    { return dni; }
    public void setDni(String dni)            { this.dni = dni; }

    public String getNombre()              { return nombre; }
    public void setNombre(String nombre)   { this.nombre = nombre; }

    public String getApellido()                    { return apellido; }
    public void setApellido(String apellido)       { this.apellido = apellido; }

    public String getDireccion()                   { return direccion; }
    public void setDireccion(String direccion)     { this.direccion = direccion; }

    public long getTelefono()              { return telefono; }
    public void setTelefono(long telefono) { this.telefono = telefono; }

    public String getCurso()               { return curso; }
    public void setCurso(String curso)     { this.curso = curso; }

    public List<String> getAsignaturas()               { return asignaturas; }
    public void setAsignaturas(List<String> a)         { this.asignaturas = a; }

    @Override
    public String toString() {
        return "Alumno{dni=" + dni + ", nombre='" + nombre + "', apellido='" + apellido +
                "', curso='" + curso + "', asignaturas=" + asignaturas + '}';
    }
}