package com.instituto.view;

import com.instituto.dao.AulaDAO;
import com.instituto.dao.ReservaDAO;
import com.instituto.model.*;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class AddReservaView extends JFrame {
    //Creamos las listas, campos de texto e inicializamos reservaDAO
    private JComboBox<Profesor> comboProfesores;
    private JComboBox<Aula> comboAulas;

    private JTextField txtInicio;
    private JTextField txtFin;

    private ReservaDAO reservaDAO = new ReservaDAO();

    //Creamos la parte visual
    public AddReservaView(List<Profesor> profesores, List<Aula> aulas) {

        //Establecemos titulos, medidas y grid
        setTitle("Crear Reserva");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(6, 1));

        //Inicializamos las listas
        comboProfesores = new JComboBox<>(profesores.toArray(new Profesor[0]));
        comboAulas = new JComboBox<>(aulas.toArray(new Aula[0]));

        //Establecemos un texto por defecto
        txtInicio = new JTextField("2026-05-21");
        txtFin = new JTextField("2026-05-21");


        //Creamos botones y etiquetas.
        JButton btnGuardar = new JButton("Guardar Reserva");

        add(new JLabel("Profesor:"));
        add(comboProfesores);

        add(new JLabel("Aula:"));
        add(comboAulas);

        add(new JLabel("Inicio (YYYY-MM-DD):"));
        add(txtInicio);

        add(new JLabel("Fin (YYYY-MM-DD):"));
        add(txtFin);

        add(btnGuardar);

        //Guardamos todo desde la funcion
        btnGuardar.addActionListener(e -> guardar());

        setVisible(true);
    }

    //Guardamos todo
    private void guardar() {

        //Establecemos en variables y guardamos todo en la clase y mostramos que todo salio bien
        Profesor profesor = (Profesor) comboProfesores.getSelectedItem();
        Aula aula = (Aula) comboAulas.getSelectedItem();

        LocalDate inicio = LocalDate.parse(txtInicio.getText());
        LocalDate fin = LocalDate.parse(txtFin.getText());

        Reserva reserva = new Reserva(inicio, fin, profesor, aula);

        reservaDAO.guardar(reserva);

        JOptionPane.showMessageDialog(this, "Reserva creada");

        this.dispose();
    }
}