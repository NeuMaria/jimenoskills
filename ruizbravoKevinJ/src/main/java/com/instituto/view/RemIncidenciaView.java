package com.instituto.view;

import com.instituto.dao.IncidenciaDAO;
import com.instituto.model.Incidencia;

import javax.swing.*;
import java.awt.*;

public class RemIncidenciaView extends JFrame {

    private JComboBox<Incidencia> comboIncidencias;

    private IncidenciaDAO incidenciaDAO = new IncidenciaDAO();

    public RemIncidenciaView() {

        setTitle("Eliminar Incidencia");
        setSize(400, 200);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 1));

        comboIncidencias = new JComboBox<>();
        JButton btnEliminar = new JButton("Eliminar");

        add(new JLabel("Selecciona incidencia:"));
        add(comboIncidencias);
        add(btnEliminar);

        cargar();

        btnEliminar.addActionListener(e -> eliminar());

        setVisible(true);
    }

    private void cargar() {

        comboIncidencias.removeAllItems();

        for (Incidencia i : incidenciaDAO.listar()) {
            comboIncidencias.addItem(i);
        }
    }

    private void eliminar() {

        Incidencia inc = (Incidencia) comboIncidencias.getSelectedItem();

        if (inc == null) return;

        incidenciaDAO.eliminar(inc.getId());

        JOptionPane.showMessageDialog(this, "Eliminada");

        cargar();
    }
}