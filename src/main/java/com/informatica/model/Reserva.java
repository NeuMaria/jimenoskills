package com.informatica.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "dni_profesor")
    private Profesor profesor;

    @ManyToOne
    @JoinColumn(name = "num_aula")
    private Aula aula;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "reserva_alumno",
            joinColumns = @JoinColumn(name = "id_reserva"),
            inverseJoinColumns = @JoinColumn(name = "dni_alumno")
    )
    private List<Alumno> alumnos;

    private LocalDate fecha;

    private String hora;

    public Reserva() {
    }

    public Reserva(Profesor profesor, Aula aula, List<Alumno> alumnos,
                   LocalDate fecha, String hora) {

        this.profesor = profesor;
        this.aula = aula;
        this.alumnos = alumnos;
        this.fecha = fecha;
        this.hora = hora;
    }

    public int getId() {
        return id;
    }

    public Profesor getProfesor() {
        return profesor;
    }

    public void setProfesor(Profesor profesor) {
        this.profesor = profesor;
    }

    public Aula getAula() {
        return aula;
    }

    public void setAula(Aula aula) {
        this.aula = aula;
    }

    public List<Alumno> getAlumnos() {
        return alumnos;
    }

    public void setAlumnos(List<Alumno> alumnos) {
        this.alumnos = alumnos;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    @Override
    public String toString() {
        return "Reserva{" +
                "id=" + id +
                ", profesor=" + profesor +
                ", aula=" + aula +
                ", alumnos=" + alumnos +
                ", fecha=" + fecha +
                ", hora='" + hora + '\'' +
                '}';
    }
}
