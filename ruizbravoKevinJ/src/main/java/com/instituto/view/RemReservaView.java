package com.instituto.view;

import com.instituto.dao.ReservaDAO;
import com.instituto.model.Reserva;

import javax.swing.*;
import java.awt.*;

public class RemReservaView extends JFrame {

    private JComboBox<Reserva> comboReservas;

    private ReservaDAO reservaDAO = new ReservaDAO();

    public RemReservaView() {

        setTitle("Eliminar Reserva");
        setSize(400, 200);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 1));

        comboReservas = new JComboBox<>();

        JButton btnEliminar = new JButton("Eliminar Reserva");

        add(new JLabel("Selecciona una reserva:"));
        add(comboReservas);
        add(btnEliminar);

        cargarReservas();

        btnEliminar.addActionListener(e -> eliminar());

        setVisible(true);
    }

    // 📥 cargar reservas en el combo
    private void cargarReservas() {

        comboReservas.removeAllItems();

        for (Reserva r : reservaDAO.listarTodas()) {
            comboReservas.addItem(r);
        }
    }

    // 🗑️ eliminar reserva
    private void eliminar() {

        Reserva reserva = (Reserva) comboReservas.getSelectedItem();

        if (reserva == null) {
            JOptionPane.showMessageDialog(this, "No hay reserva seleccionada");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "¿Seguro que quieres eliminar esta reserva?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {

            reservaDAO.eliminar(reserva.getId());

            JOptionPane.showMessageDialog(this, "Reserva eliminada");

            cargarReservas(); // refrescar lista
        }
    }
}