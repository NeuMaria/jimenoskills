package com.instituto.view;

import com.instituto.dao.AulaDAO;
import com.instituto.model.Aula;

import javax.swing.*;
import java.awt.*;

public class AddAulasView extends JFrame {
    //Inicializamos aula dao
    private AulaDAO aulaDAO = new AulaDAO();

    //Creamos la parte visual
    public AddAulasView() {

        //Creamos la ventana establecemos titulo grid etc
        setTitle("Añadir Aula");
        setSize(300, 200);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 1));

        //Creamos el boton e inicializamos guardar
        JButton btnGuardar = new JButton("Guardar");

        add(btnGuardar);

        btnGuardar.addActionListener(e -> guardar());

        setVisible(true);
    }

    private void guardar() {

        //Creamos aula y guardamos todo el aula
        Aula aula = new Aula();

        aulaDAO.guardar(aula);
        //Mostramos que todo fue correctamente en un pop up
        JOptionPane.showMessageDialog(this, "Aula creada correctamente");

        this.dispose();
    }
}