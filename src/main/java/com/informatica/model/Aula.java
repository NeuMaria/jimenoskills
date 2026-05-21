package com.informatica.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Aula {

    @Id
    private int numAula;

    private int capacidad;

    public Aula(){

    }

    public Aula(int numAula, int capacidad) {
        this.numAula = numAula;
        this.capacidad = capacidad;
    }

    public int getNumAula() {
        return numAula;
    }

    public void setNumAula(int numAula) {
        this.numAula = numAula;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    @Override
    public String toString() {
        return "Aula{" +
                "numAula=" + numAula +
                ", capacidad=" + capacidad +
                '}';
    }
}
