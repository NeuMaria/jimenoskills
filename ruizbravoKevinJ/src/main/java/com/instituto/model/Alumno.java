package com.instituto.model;

import jakarta.persistence.*;

import java.util.List;
//Tabla de alumnos, con los atributos nombre apellidos e id_alumno, estableciendo las columnas y longitudes de atributos
@Entity
@Table(name = "alumnos")
public class Alumno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "id_alumno")
    private int id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "appelidos", length = 100)
    private String apellidos;

    @OneToMany(mappedBy = "alumno", cascade = CascadeType.REMOVE)
    private List<Incidencia> incidencias;
    public Alumno() {}

    public Alumno(int id, String nombre, String apellidos) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
    }

    //Getter n Setter
    public int getId() {
        return id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    @Override
    public String toString() {
        return "Alumno{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", apellidos='" + apellidos + '\'' +
                '}';
    }
}
