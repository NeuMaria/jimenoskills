package com.instituto.view;

import com.instituto.dao.AlumnoDAO;
import com.instituto.model.Alumno;

import javax.swing.*;
import java.awt.*;

public class RemAlumnoView extends JFrame {
    //Creamos listas e inicializamos alumnodao
    private JComboBox<Alumno> comboAlumnos;

    private AlumnoDAO alumnoDAO = new AlumnoDAO();

    //Parte visual
    public RemAlumnoView() {
        //Creamos titulo, medidas y grid
        setTitle("Eliminar Alumno");
        setSize(400, 200);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 1));

        comboAlumnos = new JComboBox<>();

        JButton btnEliminar = new JButton("Eliminar Alumno");

        add(new JLabel("Selecciona un alumno:"));
        add(comboAlumnos);
        add(btnEliminar);

        cargarAlumnos();

        //Mandamos todo a eliminar
        btnEliminar.addActionListener(e -> eliminar());

        setVisible(true);
    }

    //Cargamos todos los alumnos que haya
    private void cargarAlumnos() {

        comboAlumnos.removeAllItems();

        for (Alumno a : alumnoDAO.listar()) {
            comboAlumnos.addItem(a);
        }
    }

    //Los eliminamos
    private void eliminar() {

        //Establecemos el alumno seleccionado
        Alumno alumno = (Alumno) comboAlumnos.getSelectedItem();

        //Verificamos que lo este seleccionando
        if (alumno == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un alumno");
            return;
        }

        //Verificaciones
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "¿Seguro que quieres eliminar este alumno?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION
        );

        //Eliminamos todo y lo confirmamos.
        if (confirm == JOptionPane.YES_OPTION) {

            alumnoDAO.eliminar(alumno.getId());

            JOptionPane.showMessageDialog(this, "Alumno eliminado");

            cargarAlumnos(); // refrescar lista
        }
    }
}