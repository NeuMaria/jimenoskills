package com.instituto.view;

import com.instituto.dao.AlumnoDAO;
import com.instituto.model.Alumno;

import javax.swing.*;
import java.awt.*;

public class AddAlumnoView extends JFrame {

    //Creamos los campos de texto de nombre y apelldios
    private JTextField txtNombre;
    private JTextField txtApellidos;

    //Creamos alumno dao
    private AlumnoDAO alumnoDAO = new AlumnoDAO();

    //Creamos la parte visual
    public AddAlumnoView() {
        //Añadimos titulo, quitamos la ubicación de aparicion y establecemos el grid
        setTitle("Añadir Alumno");
        setSize(300, 220);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 1));

        //Inicializamos los campos de texto
        txtNombre = new JTextField();
        txtApellidos = new JTextField();

        //Creamos los botones y las etiquetas
        JButton btnGuardar = new JButton("Guardar Alumno");

        add(new JLabel("Nombre:"));
        add(txtNombre);

        add(new JLabel("Apellidos:"));
        add(txtApellidos);

        add(btnGuardar);
        //Llamaos a la función de guardar
        btnGuardar.addActionListener(e -> guardar());

        setVisible(true);
    }

    private void guardar() {
        //Metemos el texto del nombre y de los apellidos en variables
        String nombre = txtNombre.getText();
        String apellidos = txtApellidos.getText();

        //Validamos que no este vacio
        if (nombre.isEmpty() || apellidos.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Rellena todos los campos");
            return;
        }

        //Creamos alumno y le setteamos las variables y guardamos todo
        Alumno alumno = new Alumno();
        alumno.setNombre(nombre);
        alumno.setApellidos(apellidos);

        alumnoDAO.guardar(alumno);

        //Mostramos que todo fue correctamente en un pop up
        JOptionPane.showMessageDialog(this, "Alumno creado correctamente");

        this.dispose();
    }
}