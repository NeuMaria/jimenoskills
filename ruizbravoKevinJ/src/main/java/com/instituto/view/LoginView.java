package com.instituto.view;

import com.instituto.dao.ProfesorDAO;
import com.instituto.model.Profesor;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginView extends JFrame {

    private JTextField txtNombreLogin;

    private JTextField txtNombre;
    private JTextField txtApellidos;

    private ProfesorDAO profesorDAO = new ProfesorDAO();

    public LoginView() {

        setTitle("Sistema de Reservas - Instituto");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Fondo general
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(2, 1, 10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // ======================
        // 🔐 PANEL LOGIN
        // ======================
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridLayout(3, 1, 5, 5));
        loginPanel.setBorder(BorderFactory.createTitledBorder("🔐 Login"));

        txtNombreLogin = new JTextField();

        JButton btnLogin = new JButton("Entrar");
        btnLogin.setBackground(new Color(70, 130, 180));
        btnLogin.setForeground(Color.WHITE);

        loginPanel.add(new JLabel("Nombre del profesor:"));
        loginPanel.add(txtNombreLogin);
        loginPanel.add(btnLogin);

        // ======================
        // ➕ PANEL REGISTRO
        // ======================
        JPanel registerPanel = new JPanel();
        registerPanel.setLayout(new GridLayout(5, 1, 5, 5));
        registerPanel.setBorder(BorderFactory.createTitledBorder("➕ Registro"));

        txtNombre = new JTextField();
        txtApellidos = new JTextField();

        JButton btnRegistrar = new JButton("Crear Profesor");
        btnRegistrar.setBackground(new Color(34, 139, 34));
        btnRegistrar.setForeground(Color.WHITE);

        registerPanel.add(new JLabel("Nombre:"));
        registerPanel.add(txtNombre);

        registerPanel.add(new JLabel("Apellidos:"));
        registerPanel.add(txtApellidos);

        registerPanel.add(btnRegistrar);

        // añadir paneles
        mainPanel.add(loginPanel);
        mainPanel.add(registerPanel);

        add(mainPanel);

        // acciones
        btnLogin.addActionListener(e -> login());
        btnRegistrar.addActionListener(e -> registrar());

        setVisible(true);
    }

    // 🔐 LOGIN
    private void login() {

        String nombre = txtNombreLogin.getText();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Introduce un nombre");
            return;
        }

        Profesor profesor = profesorDAO.buscarPorNombre(nombre);

        if (profesor != null) {

            JOptionPane.showMessageDialog(this,
                    "Bienvenido " + profesor.getNombre());

            new MenuView(profesor);
            this.dispose();

        } else {

            JOptionPane.showMessageDialog(this,
                    "Profesor no encontrado");
        }
    }

    // ➕ REGISTRO
    private void registrar() {

        String nombre = txtNombre.getText();
        String apellidos = txtApellidos.getText();

        if (nombre.isEmpty() || apellidos.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Rellena todos los campos");
            return;
        }

        Profesor profesor = new Profesor();
        profesor.setNombre(nombre);
        profesor.setApellidos(apellidos);

        profesorDAO.guardar(profesor);

        JOptionPane.showMessageDialog(this,
                "Profesor creado correctamente");

        txtNombre.setText("");
        txtApellidos.setText("");
    }
}