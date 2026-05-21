package com.instituto.model;

import jakarta.persistence.*;
//Tabla de aulas, establecemos la id, que sea autoincremental
@Entity
@Table(name = "aulas")
public class Aula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_aula")
    private int id;

    public Aula() {}

    public Aula(int id) {
        this.id = id;
    }
    //Getter n Setter
    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Aula ID: " + id;
    }
}