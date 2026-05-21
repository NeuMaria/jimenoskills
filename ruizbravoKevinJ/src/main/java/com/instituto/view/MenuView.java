package com.instituto.view;

import com.instituto.dao.AulaDAO;
import com.instituto.dao.ProfesorDAO;
import com.instituto.model.Aula;
import com.instituto.model.Profesor;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MenuView extends JFrame {
    //Creamos la parte visual
    public MenuView(Profesor profesor) {

        //Establecemos tamaños y titulos
        setTitle("Sistema Instituto - Menú");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        //Mostramos el profesor seleccionado
        JLabel lblUser = new JLabel(
                "Profesor: " + profesor.getNombre(),
                SwingConstants.CENTER
        );
        //La fuente
        lblUser.setFont(new Font("Arial", Font.BOLD, 18));
        add(lblUser, BorderLayout.NORTH);

        //Creamos los paneles y botones, llamando a las otras vistas del swing
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2, 10, 10));


        JButton btnAddAula = new JButton("➕ Añadir Aula");
        JButton btnRemAula = new JButton("🗑 Borrar Aula");

        btnAddAula.addActionListener(e -> new AddAulasView());
        btnRemAula.addActionListener(e -> new RemAulasView());


        JButton btnAddAlumno = new JButton("➕ Añadir Alumno");
        JButton btnRemAlumno = new JButton("🗑 Borrar Alumno");

        btnAddAlumno.addActionListener(e -> new AddAlumnoView());
        btnRemAlumno.addActionListener(e -> new RemAlumnoView());


        JButton btnAddIncidencia = new JButton("➕ Añadir Incidencia");
        JButton btnRemIncidencia = new JButton("🗑 Borrar Incidencia");

        btnAddIncidencia.addActionListener(e -> new AddIncidenciaView());
        btnRemIncidencia.addActionListener(e -> new RemIncidenciaView()); // ✔ CORREGIDO


        JButton btnAddReserva = new JButton("➕ Añadir Reserva");
        JButton btnRemReserva = new JButton("🗑 Borrar Reserva");

        btnAddReserva.addActionListener(e -> {

            List<Profesor> profesores = new ProfesorDAO().listar();
            List<Aula> aulas = new AulaDAO().listar();

            new AddReservaView(profesores, aulas);
        });

        btnRemReserva.addActionListener(e -> new RemReservaView());


        panel.add(btnAddAula);
        panel.add(btnRemAula);

        panel.add(btnAddAlumno);
        panel.add(btnRemAlumno);

        panel.add(btnAddIncidencia);
        panel.add(btnRemIncidencia);

        panel.add(btnAddReserva);
        panel.add(btnRemReserva);

        add(panel, BorderLayout.CENTER);

        //Creando tambien un footer con la gestion del instituto
        JLabel footer = new JLabel("Sistema de Gestión de Instituto", SwingConstants.CENTER);
        footer.setFont(new Font("Arial", Font.ITALIC, 12));
        add(footer, BorderLayout.SOUTH);

        setVisible(true);
    }
}