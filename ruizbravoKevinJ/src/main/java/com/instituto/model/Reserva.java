package com.instituto.model;

import jakarta.persistence.*;
import java.time.LocalDate;

//Tabla de reservas, donde establecemos el id autoincremental, fecha de inicio y fin, y las claves foraneas de profesor y aula
@Entity
@Table(name = "reservas")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private LocalDate inicio;
    private LocalDate fin;

    @ManyToOne
    @JoinColumn(name = "profesor_id")
    private Profesor profesor;

    @ManyToOne
    @JoinColumn(name = "aula_id")
    private Aula aula;

    public Reserva() {}

    public Reserva(LocalDate inicio, LocalDate fin,
                   Profesor profesor, Aula aula) {
        this.inicio = inicio;
        this.fin = fin;
        this.profesor = profesor;
        this.aula = aula;
    }

    //Getter n Setter
    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Reserva{" +
                "id=" + id +
                ", inicio=" + inicio +
                ", fin=" + fin +
                ", profesor=" + profesor +
                ", aula=" + aula +
                '}';
    }
}