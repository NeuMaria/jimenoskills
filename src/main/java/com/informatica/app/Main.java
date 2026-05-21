package com.informatica.app;

import com.informatica.dao.*;
import com.informatica.model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

//Clase principal de la aplicación que lanza la ventana principal con interfaz gráfica Swing.

public class Main {

    public static void main(String[] args) {
        // Ejecutar la interfaz en el hilo de eventos de Swing
        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal();
            ventana.setVisible(true);
        });
    }
}

//Ventana principal de la aplicación.

class VentanaPrincipal extends JFrame {

    public VentanaPrincipal() {
        setTitle("Gestión de Reservas - Aula de Informática");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar en pantalla

        // Panel de pestañas principal
        JTabbedPane pestanas = new JTabbedPane();
        pestanas.addTab("Profesores",  new PanelProfesor());
        pestanas.addTab("Aulas",       new PanelAula());
        pestanas.addTab("Alumnos",     new PanelAlumno());
        pestanas.addTab("Reservas",    new PanelReserva());
        pestanas.addTab("Incidencias", new PanelIncidencia());

        add(pestanas);
    }
}

// PESTAÑA PROFESORES

class PanelProfesor extends JPanel {

    private final ProfesorDAO profesorDAO = new ProfesorDAO();

    private final JTextField campoDni      = new JTextField(10);
    private final JTextField campoNombre   = new JTextField(15);
    private final JTextField campoApellido = new JTextField(15);
    private final JTextField campoDirec    = new JTextField(20);
    private final JTextField campoTelef    = new JTextField(10);
    private final JTextField campoCursos   = new JTextField(20);

    private final DefaultTableModel modeloTabla = new DefaultTableModel(
            new String[]{"DNI", "Nombre", "Apellido", "Dirección", "Teléfono", "Cursos"}, 0
    );
    private final JTable tabla = new JTable(modeloTabla);

    //Panel de Profesor donde se crea lo visual
    public PanelProfesor() {
        setLayout(new BorderLayout(10, 10));

        JPanel formulario = new JPanel(new GridLayout(0, 2, 5, 5));
        formulario.setBorder(BorderFactory.createTitledBorder("Nuevo Profesor"));

        formulario.add(new JLabel("DNI:"));          formulario.add(campoDni);
        formulario.add(new JLabel("Nombre:"));       formulario.add(campoNombre);
        formulario.add(new JLabel("Apellido:"));     formulario.add(campoApellido);
        formulario.add(new JLabel("Dirección:"));    formulario.add(campoDirec);
        formulario.add(new JLabel("Teléfono:"));     formulario.add(campoTelef);
        formulario.add(new JLabel("Cursos (,):"));   formulario.add(campoCursos);

        JButton btnGuardar  = new JButton("Guardar");
        JButton btnListar   = new JButton("Listar todos");
        JButton btnLimpiar  = new JButton("Limpiar");
        JButton btnEliminar = new JButton("Eliminar");

        JPanel botones = new JPanel();
        botones.add(btnGuardar);
        botones.add(btnListar);
        botones.add(btnLimpiar);
        botones.add(btnEliminar);

        JPanel norte = new JPanel(new BorderLayout());
        norte.add(formulario, BorderLayout.CENTER);
        norte.add(botones, BorderLayout.SOUTH);

        add(norte, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        btnGuardar.addActionListener(e -> guardarProfesor());
        btnListar.addActionListener(e -> listarProfesores());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        btnEliminar.addActionListener(e -> eliminarProfesor());
    }

    //Guardar profesor aquí es donde se pide los datos y se hace las comprobaciones de estos
    private void guardarProfesor() {
        try {
            String dni      = campoDni.getText().trim();
            String nombre   = campoNombre.getText().trim();
            String apellido = campoApellido.getText().trim();
            String dir      = campoDirec.getText().trim();
            String telefStr = campoTelef.getText().trim();

            if (dni.isEmpty() || nombre.isEmpty() || apellido.isEmpty() || dir.isEmpty() || telefStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, rellena todos los campos obligatorios.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (dni.length() != 9) {
                JOptionPane.showMessageDialog(this, "El DNI debe tener exactamente 9 caracteres.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!telefStr.matches("\\d{9}")) {
                JOptionPane.showMessageDialog(this, "El teléfono debe contener exactamente 9 números.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int telef = Integer.parseInt(telefStr);
            List<String> cursos = Arrays.asList(campoCursos.getText().split(","));

            if (profesorDAO.buscarPorId(dni) != null) {
                JOptionPane.showMessageDialog(this, "Ya existe un profesor con ese DNI.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Profesor p = new Profesor(dni, nombre, apellido, dir, telef, cursos);
            profesorDAO.insertar(p);
            JOptionPane.showMessageDialog(this, "Profesor guardado correctamente.");
            limpiarFormulario();
            listarProfesores();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Asegúrate de introducir valores numéricos donde corresponde.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Eliminar Profesor aquí donde se elimina el profesor
    private void eliminarProfesor() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un profesor de la tabla.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String dni = (String) modeloTabla.getValueAt(fila, 0);
        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Eliminar al profesor con DNI " + dni + "?", "Confirmar", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            if (profesorDAO.eliminar(dni)) {
                JOptionPane.showMessageDialog(this, "Profesor eliminado.");
                listarProfesores();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo eliminar. Puede que tenga reservas asociadas.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    //Listar Profesores aquí es donde se lista todos los profesores
    private void listarProfesores() {
        modeloTabla.setRowCount(0);
        List<Profesor> lista = profesorDAO.listarTodos();
        for (Profesor p : lista) {
            modeloTabla.addRow(new Object[]{
                    p.getDni(), p.getNombre(), p.getApellido(),
                    p.getDireccion(), p.getTelefono(), p.getCursos()
            });
        }
    }

    //Limpiar Formulario aquí es donde se limpia los campos si están llenos
    private void limpiarFormulario() {
        campoDni.setText("");
        campoNombre.setText("");
        campoApellido.setText("");
        campoDirec.setText("");
        campoTelef.setText("");
        campoCursos.setText("");
    }
}

// PESTAÑA AULAS

class PanelAula extends JPanel {

    private final AulaDAO aulaDAO = new AulaDAO();

    private final JTextField campoNum      = new JTextField(10);
    private final JTextField campoCapacidad= new JTextField(10);

    private final DefaultTableModel modeloTabla = new DefaultTableModel(
            new String[]{"Número Aula", "Capacidad"}, 0
    );
    private final JTable tabla = new JTable(modeloTabla);


    //Panel de Aula donde se crea lo visual
    public PanelAula() {
        setLayout(new BorderLayout(10, 10));

        JPanel formulario = new JPanel(new GridLayout(0, 2, 5, 5));
        formulario.setBorder(BorderFactory.createTitledBorder("Nueva Aula"));

        formulario.add(new JLabel("Número de Aula:")); formulario.add(campoNum);
        formulario.add(new JLabel("Capacidad:"));      formulario.add(campoCapacidad);

        JButton btnGuardar = new JButton("Guardar");
        JButton btnListar  = new JButton("Listar todas");

        JPanel botones = new JPanel();
        botones.add(btnGuardar);
        botones.add(btnListar);

        JPanel norte = new JPanel(new BorderLayout());
        norte.add(formulario, BorderLayout.CENTER);
        norte.add(botones, BorderLayout.SOUTH);

        add(norte, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        btnGuardar.addActionListener(e -> guardarAula());
        btnListar.addActionListener(e -> listarAulas());
    }

    //Guardar Aula aquí es donde se pide los datos y se hace las comprobaciones de estos
    private void guardarAula() {
        try {
            String numStr = campoNum.getText().trim();
            String capStr = campoCapacidad.getText().trim();

            if (numStr.isEmpty() || capStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No dejes ni el Número de Aula ni la Capacidad vacíos.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int num  = Integer.parseInt(numStr);
            int cap  = Integer.parseInt(capStr);

            if (cap <= 0) {
                JOptionPane.showMessageDialog(this, "La capacidad debe ser mayor que 0.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Aula aulaExistente = aulaDAO.buscarPorId(num);
            if (aulaExistente != null) {
                JOptionPane.showMessageDialog(this, "El aula " + num + " ya existe en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Aula a = new Aula(num, cap);
            aulaDAO.insertar(a);
            JOptionPane.showMessageDialog(this, "Aula guardada correctamente.");
            campoNum.setText("");
            campoCapacidad.setText("");
            listarAulas();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Los campos deben ser números.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Listar Profesores aquí es donde se lista todas las aulas
    private void listarAulas() {
        modeloTabla.setRowCount(0);
        List<Aula> lista = aulaDAO.listarTodos();
        for (Aula a : lista) {
            modeloTabla.addRow(new Object[]{a.getNumAula(), a.getCapacidad()});
        }
    }
}

//PESTAÑA ALUMNOS

class PanelAlumno extends JPanel {

    private final AlumnoDAO alumnoDAO = new AlumnoDAO();

    private final JTextField campoDni        = new JTextField(10);
    private final JTextField campoNombre     = new JTextField(15);
    private final JTextField campoApellido   = new JTextField(15);
    private final JTextField campoDirec      = new JTextField(20);
    private final JTextField campoTelef      = new JTextField(10);
    private final JTextField campoAsignaturas= new JTextField(20);

    private final DefaultTableModel modeloTabla = new DefaultTableModel(
            new String[]{"DNI", "Nombre", "Apellido", "Dirección", "Teléfono", "Asignaturas"}, 0
    );
    private final JTable tabla = new JTable(modeloTabla);

    //Panel de Alumno donde se crea lo visual
    public PanelAlumno() {
        setLayout(new BorderLayout(10, 10));

        JPanel formulario = new JPanel(new GridLayout(0, 2, 5, 5));
        formulario.setBorder(BorderFactory.createTitledBorder("Nuevo Alumno"));

        formulario.add(new JLabel("DNI:"));               formulario.add(campoDni);
        formulario.add(new JLabel("Nombre:"));             formulario.add(campoNombre);
        formulario.add(new JLabel("Apellido:"));           formulario.add(campoApellido);
        formulario.add(new JLabel("Dirección:"));          formulario.add(campoDirec);
        formulario.add(new JLabel("Teléfono:"));           formulario.add(campoTelef);
        formulario.add(new JLabel("Asignaturas (,):"));    formulario.add(campoAsignaturas);

        JButton btnGuardar = new JButton("Guardar");
        JButton btnListar  = new JButton("Listar todos");
        JButton btnLimpiar = new JButton("Limpiar");
        JButton btnEliminar = new JButton("Eliminar");

        JPanel botones = new JPanel();
        botones.add(btnGuardar);
        botones.add(btnListar);
        botones.add(btnLimpiar);
        botones.add(btnEliminar);

        JPanel norte = new JPanel(new BorderLayout());
        norte.add(formulario, BorderLayout.CENTER);
        norte.add(botones, BorderLayout.SOUTH);

        add(norte, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        btnGuardar.addActionListener(e -> guardarAlumno());
        btnListar.addActionListener(e -> listarAlumnos());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        btnEliminar.addActionListener(e -> eliminarAlumno());
    }

    //Guardar Alumno aquí es donde se pide los datos y se hace las comprobaciones de estos
    private void guardarAlumno() {
        try {
            String dni      = campoDni.getText().trim();
            String nombre   = campoNombre.getText().trim();
            String apellido = campoApellido.getText().trim();
            String dir      = campoDirec.getText().trim();
            String telefStr = campoTelef.getText().trim();

            if (dni.isEmpty() || nombre.isEmpty() || apellido.isEmpty() || dir.isEmpty() || telefStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, rellena todos los campos obligatorios.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (dni.length() != 9) {
                JOptionPane.showMessageDialog(this, "El DNI debe tener exactamente 9 caracteres.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!telefStr.matches("\\d{9}")) {
                JOptionPane.showMessageDialog(this, "El teléfono debe contener exactamente 9 números.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int telef = Integer.parseInt(telefStr);
            List<String> asignaturas = Arrays.asList(campoAsignaturas.getText().split(","));

            if (alumnoDAO.buscarPorId(dni) != null) {
                JOptionPane.showMessageDialog(this, "Ya existe un alumno con ese DNI.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Alumno a = new Alumno(dni, nombre, apellido, dir, telef, asignaturas);
            alumnoDAO.insertar(a);
            JOptionPane.showMessageDialog(this, "Alumno guardado correctamente.");
            limpiarFormulario();
            listarAlumnos();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Asegúrate de introducir valores numéricos donde corresponde.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Eliminar Alumno aquí donde se elimina el alumno
    private void eliminarAlumno() {
        int filaSeleccionada = tabla.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un alumno de la tabla para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String dni = (String) modeloTabla.getValueAt(filaSeleccionada, 0);

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Estás seguro de que deseas eliminar el alumno con DNI " + dni + "?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            boolean exito = alumnoDAO.eliminar(dni);
            if (exito) {
                JOptionPane.showMessageDialog(this, "Alumno eliminado correctamente.");
                listarAlumnos();
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se pudo eliminar. Es posible que el alumno tenga reservas o incidencias asociadas.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    //Listar Alumnos aquí es donde se lista todos los alumnos
    private void listarAlumnos() {
        modeloTabla.setRowCount(0);
        List<Alumno> lista = alumnoDAO.listarTodos();
        for (Alumno a : lista) {
            modeloTabla.addRow(new Object[]{
                    a.getDni(), a.getNombre(), a.getApellido(),
                    a.getDireccion(), a.getTelefono(), a.getAsignaturas()
            });
        }
    }

    //Limpiar Formulario aquí es donde se limpia los campos si están llenos
    private void limpiarFormulario() {
        campoDni.setText("");
        campoNombre.setText("");
        campoApellido.setText("");
        campoDirec.setText("");
        campoTelef.setText("");
        campoAsignaturas.setText("");
    }
}

// PESTAÑA RESERVAS

class PanelReserva extends JPanel {

    private final ReservaDAO  reservaDAO  = new ReservaDAO();
    private final ProfesorDAO profesorDAO = new ProfesorDAO();
    private final AulaDAO     aulaDAO     = new AulaDAO();
    private final AlumnoDAO   alumnoDAO   = new AlumnoDAO();

    private final JComboBox<String> comboProfesor = new JComboBox<>();
    private final JComboBox<String> comboAula     = new JComboBox<>();

    private final JTextField campoFecha = new JTextField("2025-06-01", 12);
    private final JTextField campoHora  = new JTextField("08:00", 8);

    private final DefaultListModel<String> modeloAlumnos = new DefaultListModel<>();
    private final JList<String> listaAlumnos = new JList<>(modeloAlumnos);

    private final DefaultTableModel modeloTabla = new DefaultTableModel(
            new String[]{"ID", "Profesor", "Aula", "Fecha", "Hora", "Alumnos"}, 0
    );
    private final JTable tabla = new JTable(modeloTabla);

    //Panel de Reserva donde se crea lo visual
    public PanelReserva() {
        setLayout(new BorderLayout(10, 10));

        // --- NUEVO DISEÑO DIVIDIDO PARA EL FORMULARIO ---
        JPanel formulario = new JPanel(new BorderLayout(15, 10)); // Separación entre izquierda y derecha
        formulario.setBorder(BorderFactory.createTitledBorder("Nueva Reserva"));

        // 1. Panel Izquierdo: Solo para los campos de 1 sola línea
        JPanel panelCampos = new JPanel(new GridLayout(4, 2, 5, 5));
        panelCampos.add(new JLabel("Profesor:"));
        panelCampos.add(comboProfesor);
        panelCampos.add(new JLabel("Aula:"));
        panelCampos.add(comboAula);
        panelCampos.add(new JLabel("Fecha (YYYY-MM-DD):"));
        panelCampos.add(campoFecha);
        panelCampos.add(new JLabel("Hora (HH:MM):"));
        panelCampos.add(campoHora);

        // 2. Panel Derecho: Solo para la lista de alumnos
        JPanel panelLista = new JPanel(new BorderLayout(0, 5));
        panelLista.add(new JLabel("Alumnos (Ctrl+clic):"), BorderLayout.NORTH);
        listaAlumnos.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scrollLista = new JScrollPane(listaAlumnos);
        scrollLista.setPreferredSize(new Dimension(250, 100)); // Evita que se haga gigante
        panelLista.add(scrollLista, BorderLayout.CENTER);

        // Unimos ambas partes al formulario
        formulario.add(panelCampos, BorderLayout.CENTER);
        formulario.add(panelLista, BorderLayout.EAST);
        // ------------------------------------------------

        // Los botones se quedan igual
        JButton btnCargar  = new JButton("Cargar datos");
        JButton btnGuardar = new JButton("Guardar reserva");
        JButton btnListar  = new JButton("Listar reservas");
        JButton btnEliminar = new JButton("Eliminar");

        JPanel botones = new JPanel();
        botones.add(btnCargar);
        botones.add(btnGuardar);
        botones.add(btnListar);
        botones.add(btnEliminar);

        JPanel norte = new JPanel(new BorderLayout());
        norte.add(formulario, BorderLayout.CENTER);
        norte.add(botones, BorderLayout.SOUTH);

        add(norte, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // ... Mismos action listeners que ya tienes abajo
        btnCargar.addActionListener(e -> cargarDatos());
        btnGuardar.addActionListener(e -> guardarReserva());
        btnListar.addActionListener(e -> listarReservas());
        btnEliminar.addActionListener(e -> eliminarReserva());

        cargarDatos();
    }

    //Cargar Datos aquí es donde se carga los datos de la bd en caso de que no aparezcan
    private void cargarDatos() {
        comboProfesor.removeAllItems();
        for (Profesor p : profesorDAO.listarTodos()) {
            comboProfesor.addItem(p.getDni() + " - " + p.getNombre() + " " + p.getApellido());
        }

        comboAula.removeAllItems();
        for (Aula a : aulaDAO.listarTodos()) {
            comboAula.addItem(a.getNumAula() + " (cap: " + a.getCapacidad() + ")");
        }

        modeloAlumnos.clear();
        for (Alumno a : alumnoDAO.listarTodos()) {
            modeloAlumnos.addElement(a.getDni() + " - " + a.getNombre() + " " + a.getApellido());
        }
    }

    //Guardar Reserva aquí es donde se pide los datos y se hace las comprobaciones de estos
    private void guardarReserva() {
        try {
            String selProfesor = (String) comboProfesor.getSelectedItem();
            if (selProfesor == null) { JOptionPane.showMessageDialog(this, "Selecciona un profesor."); return; }
            String dniProfesor = selProfesor.split(" - ")[0].trim();

            String selAula = (String) comboAula.getSelectedItem();
            if (selAula == null) { JOptionPane.showMessageDialog(this, "Selecciona un aula."); return; }
            int numAula = Integer.parseInt(selAula.split(" ")[0].trim());

            LocalDate fecha = LocalDate.parse(campoFecha.getText().trim());
            String hora     = campoHora.getText().trim();

            // 1. NUEVA VALIDACIÓN: Comprobar que han hecho clic en al menos un alumno
            List<String> seleccionados = listaAlumnos.getSelectedValuesList();
            if (seleccionados.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debes seleccionar al menos un alumno (haz clic en su nombre para que se ponga azul).", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 2. Comprobar conflicto de horarios
            List<Reserva> todasReservas = reservaDAO.listarTodos();
            for (Reserva r : todasReservas) {
                if (r.getAula().getNumAula() == numAula
                        && r.getFecha().equals(fecha)
                        && r.getHora().equals(hora)) {
                    JOptionPane.showMessageDialog(this,
                            "⚠ Conflicto: el aula " + numAula + " ya está reservada el " + fecha + " a las " + hora,
                            "Conflicto de horario", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }

            // 3. Crear la lista de alumnos a partir de los seleccionados
            List<Alumno> alumnosReserva = new java.util.ArrayList<>();
            for (String s : seleccionados) {
                String dni = s.split(" - ")[0].trim();
                Alumno a = alumnoDAO.buscarPorId(dni);
                if (a != null) alumnosReserva.add(a);
            }

            Profesor profesor = profesorDAO.buscarPorId(dniProfesor);
            Aula aula         = aulaDAO.buscarPorId(numAula);

            Reserva reserva = new Reserva(profesor, aula, alumnosReserva, fecha, hora);
            reservaDAO.insertar(reserva);
            JOptionPane.showMessageDialog(this, "Reserva guardada correctamente.");
            listarReservas();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Eliminar Reserva aquí donde se elimina la reserva
    private void eliminarReserva() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una reserva de la tabla.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (int) modeloTabla.getValueAt(fila, 0);
        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Eliminar la reserva #" + id + "?", "Confirmar", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            if (reservaDAO.eliminar(id)) {
                JOptionPane.showMessageDialog(this, "Reserva eliminada.");
                listarReservas();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo eliminar la reserva.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    //Listar Reservas aquí es donde se lista todas las reservas
    private void listarReservas() {
        modeloTabla.setRowCount(0);
        List<Reserva> lista = reservaDAO.listarTodos();
        for (Reserva r : lista) {
            modeloTabla.addRow(new Object[]{
                    r.getId(),
                    r.getProfesor().getNombre() + " " + r.getProfesor().getApellido(),
                    r.getAula().getNumAula(),
                    r.getFecha(),
                    r.getHora(),
                    r.getAlumnos().size() + " alumno(s)"
            });
        }
    }
}

//PESTAÑA INCIDENCIAS

class PanelIncidencia extends JPanel {

    private final IncidenciaDAO incidenciaDAO = new IncidenciaDAO();
    private final AlumnoDAO     alumnoDAO     = new AlumnoDAO();
    private final ReservaDAO    reservaDAO    = new ReservaDAO();

    private final JComboBox<String> comboAlumno  = new JComboBox<>();
    private final JComboBox<String> comboReserva = new JComboBox<>();
    private final JTextArea campoDescripcion = new JTextArea(3, 30);

    private final DefaultTableModel modeloTabla = new DefaultTableModel(
            new String[]{"ID", "Alumno", "Reserva ID", "Descripción"}, 0
    );
    private final JTable tabla = new JTable(modeloTabla);

    //Panel de Incidencia donde se crea lo visual
    public PanelIncidencia() {
        setLayout(new BorderLayout(10, 10));

        JPanel formulario = new JPanel(new GridLayout(0, 2, 5, 5));
        formulario.setBorder(BorderFactory.createTitledBorder("Nueva Incidencia"));

        formulario.add(new JLabel("Alumno:"));       formulario.add(comboAlumno);
        formulario.add(new JLabel("Reserva:"));      formulario.add(comboReserva);
        formulario.add(new JLabel("Descripción:"));  formulario.add(new JScrollPane(campoDescripcion));

        JButton btnCargar  = new JButton("Cargar datos");
        JButton btnGuardar = new JButton("Registrar incidencia");
        JButton btnListar  = new JButton("Listar incidencias");
        JButton btnEliminar = new JButton("Eliminar");

        JPanel botones = new JPanel();
        botones.add(btnCargar);
        botones.add(btnGuardar);
        botones.add(btnListar);
        botones.add(btnEliminar);

        JPanel norte = new JPanel(new BorderLayout());
        norte.add(formulario, BorderLayout.CENTER);
        norte.add(botones, BorderLayout.SOUTH);

        add(norte, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        btnCargar.addActionListener(e -> cargarDatos());
        btnGuardar.addActionListener(e -> guardarIncidencia());
        btnListar.addActionListener(e -> listarIncidencias());
        btnEliminar.addActionListener(e -> eliminarIncidencia());

        cargarDatos();
    }

    //Cargar Datos aquí es donde se carga los datos de la bd en caso de que no aparezcan
    private void cargarDatos() {
        comboAlumno.removeAllItems();
        for (Alumno a : alumnoDAO.listarTodos()) {
            comboAlumno.addItem(a.getDni() + " - " + a.getNombre() + " " + a.getApellido());
        }

        comboReserva.removeAllItems();
        for (Reserva r : reservaDAO.listarTodos()) {
            comboReserva.addItem(r.getId() + " | " + r.getFecha() + " " + r.getHora()
                    + " | Aula " + r.getAula().getNumAula());
        }
    }

    //Guardar Incidencia aquí es donde se pide los datos y se hace las comprobaciones de estos
    private void guardarIncidencia() {
        try {
            String selAlumno = (String) comboAlumno.getSelectedItem();
            String selReserva = (String) comboReserva.getSelectedItem();
            String descripcion = campoDescripcion.getText().trim();

            if (selAlumno == null || selReserva == null) {
                JOptionPane.showMessageDialog(this, "Selecciona alumno y reserva (deben existir previamente).", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (descripcion.isEmpty()) {
                JOptionPane.showMessageDialog(this, "La descripción no puede estar vacía.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String dniAlumno  = selAlumno.split(" - ")[0].trim();
            int idReserva  = Integer.parseInt(selReserva.split(" \\| ")[0].trim());

            Alumno  alumno  = alumnoDAO.buscarPorId(dniAlumno);
            Reserva reserva = reservaDAO.buscarPorId(idReserva);

            boolean alumnoPertenece = false;
            for (Alumno a : reserva.getAlumnos()) {
                if (a.getDni().equals(dniAlumno)) {
                    alumnoPertenece = true;
                    break;
                }
            }

            if (!alumnoPertenece) {
                JOptionPane.showMessageDialog(this,
                        "El alumno seleccionado no forma parte de la reserva #" + idReserva + ".\nNo se puede crear la incidencia.",
                        "Error de Validación",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            Incidencia inc = new Incidencia(alumno, reserva, descripcion);
            incidenciaDAO.insertar(inc);
            JOptionPane.showMessageDialog(this, "Incidencia registrada correctamente.");
            campoDescripcion.setText("");
            listarIncidencias();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Eliminar Incidencia aquí donde se elimina la incidencia
    private void eliminarIncidencia() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una incidencia de la tabla.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (int) modeloTabla.getValueAt(fila, 0);
        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Eliminar la incidencia #" + id + "?", "Confirmar", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            if (incidenciaDAO.eliminar(id)) {
                JOptionPane.showMessageDialog(this, "Incidencia eliminada.");
                listarIncidencias();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo eliminar la incidencia.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    //Listar Incidencias aquí es donde se lista todas las incidencias
    private void listarIncidencias() {
        modeloTabla.setRowCount(0);
        List<Incidencia> lista = incidenciaDAO.listarTodos();
        for (Incidencia i : lista) {
            modeloTabla.addRow(new Object[]{
                    i.getId(),
                    i.getAlumno().getNombre() + " " + i.getAlumno().getApellido(),
                    i.getReserva().getId(),
                    i.getDescripcion()
            });
        }
    }
}