package com.instituto.view;

import com.instituto.dao.AlumnoDAO;
import com.instituto.dao.AulaDAO;
import com.instituto.dao.IncidenciaDAO;
import com.instituto.model.Alumno;
import com.instituto.model.Aula;
import com.instituto.model.Incidencia;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class AddIncidenciaView extends JFrame {

    //Creamos las listas, inicializamos alumno, aula e incidencia dao
    private JComboBox<Alumno> comboAlumnos;
    private JComboBox<Aula> comboAulas;
    private JTextField txtDescripcion;

    private AlumnoDAO alumnoDAO = new AlumnoDAO();
    private AulaDAO aulaDAO = new AulaDAO();
    private IncidenciaDAO incidenciaDAO = new IncidenciaDAO();

    //Creamos la parte visual
    public AddIncidenciaView() {

        //Establecemos dimensiones titulo y grid
        setTitle("Crear Incidencia");
        setSize(450, 300);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(7, 1));

        //Inzializamos las listas
        comboAlumnos = new JComboBox<>();
        comboAulas = new JComboBox<>();
        txtDescripcion = new JTextField();

        //Creamos el boton de guardar incidencia y de las etiquetas de alumno aula y descripcion
        JButton btnGuardar = new JButton("Guardar Incidencia");

        add(new JLabel("Alumno:"));
        add(comboAlumnos);

        add(new JLabel("Aula:"));
        add(comboAulas);

        add(new JLabel("Descripción:"));
        add(txtDescripcion);

        add(btnGuardar);

        cargarDatos();

        //Guardamos todo desde la funcion guardar
        btnGuardar.addActionListener(e -> guardar());

        setVisible(true);
    }

    private void cargarDatos() {

        //Limpiamos las listas
        comboAlumnos.removeAllItems();
        comboAulas.removeAllItems();

        //Añadimos todo a las listas para cargarlo todo
        for (Alumno a : alumnoDAO.listar()) {
            comboAlumnos.addItem(a);
        }

        for (Aula a : aulaDAO.listar()) {
            comboAulas.addItem(a);
        }

        //Verificamos que haya alumnos para hacer incidencias
        if (comboAlumnos.getItemCount() == 0 || comboAulas.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this,
                    "Debes tener alumnos y aulas creados antes de crear incidencias.");
        }
    }

    private void guardar() {

        //Cogemos los items seleccionados de las listas y creamos la descripción
        Alumno alumno = (Alumno) comboAlumnos.getSelectedItem();
        Aula aula = (Aula) comboAulas.getSelectedItem();
        String desc = txtDescripcion.getText();

        //Validamos que no haya campos vacios
        if (alumno == null || aula == null || desc.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Rellena todos los campos");
            return;
        }

        //Creamos incidencia y setteamos sus variables
        Incidencia inc = new Incidencia();
        inc.setAlumno(alumno);
        inc.setAula(aula);
        inc.setDescripcion(desc);
        inc.setFecha(LocalDate.now());

        //Guardamos incidencia y mostramos que se ha creado
        incidenciaDAO.guardar(inc);

        JOptionPane.showMessageDialog(this, "Incidencia creada");

        this.dispose();
    }
}