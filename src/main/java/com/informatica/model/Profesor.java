package com.informatica.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa a un profesor del instituto.
 * Contiene sus datos personales y la lista de cursos que imparte.
 */
@Entity
public class Profesor {

    @Id
    private String dni;

    private String nombre;
    private String apellido;
    private String direccion;
    private long telefono;

    // Lista de cursos almacenada en tabla auxiliar PROFESOR_CURSOS
    @ElementCollection(fetch = FetchType.EAGER) // <--- Cambiado aquí
    @CollectionTable(name = "profesor_cursos", joinColumns = @JoinColumn(name = "dni_profesor"))
    @Column(name = "curso")
    private List<String> cursos = new ArrayList<>();

    public Profesor() {}

    public Profesor(String dni, String nombre, String apellido, String direccion, long telefono, List<String> cursos) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.direccion = direccion;
        this.telefono = telefono;
        this.cursos = cursos;
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

    public List<String> getCursos()                    { return cursos; }
    public void setCursos(List<String> cursos)         { this.cursos = cursos; }

    @Override
    public String toString() {
        return "Profesor{dni=" + dni + ", nombre='" + nombre + "', apellido='" + apellido +
                "', direccion='" + direccion + "', telefono=" + telefono + ", cursos=" + cursos + '}';
    }
}