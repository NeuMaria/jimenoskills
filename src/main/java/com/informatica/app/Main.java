package com.informatica.app;

import com.informatica.dao.*;
import com.informatica.model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Clase principal de la aplicación.
 * Lanza la ventana principal con interfaz gráfica Swing.
 */
public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal();
            ventana.setVisible(true);
        });
    }
}

/**
 * Ventana principal de la aplicación.
 * Contiene un JTabbedPane con una pestaña por cada módulo de gestión.
 */
class VentanaPrincipal extends JFrame {

    public VentanaPrincipal() {
        setTitle("Gestión de Reservas - Aula de Informática");
        setSize(950, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane pestanas = new JTabbedPane();
        pestanas.addTab("Profesores",  new PanelProfesor());
        pestanas.addTab("Aulas",       new PanelAula());
        pestanas.addTab("Alumnos",     new PanelAlumno());
        pestanas.addTab("Reservas",    new PanelReserva());
        pestanas.addTab("Incidencias", new PanelIncidencia());

        add(pestanas);
    }
}

/* =========================================================
 *  PESTAÑA PROFESORES
 * ========================================================= */

/**
 * Panel para gestionar profesores: añadir, listar, buscar y eliminar.
 * Al eliminar se comprueba que el profesor no tenga reservas asociadas.
 */
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
    ) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable tabla = new JTable(modeloTabla);

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
        JButton btnEliminar = new JButton("Eliminar seleccionado");
        JButton btnLimpiar  = new JButton("Limpiar");

        // BOTONES PRINCIPALES
        JPanel botonesAcciones = new JPanel();

        botonesAcciones.add(btnGuardar);
        botonesAcciones.add(btnEliminar);
        botonesAcciones.add(btnLimpiar);

        // BOTONES SECUNDARIOS
        JPanel botonesDatos = new JPanel();

        botonesDatos.setBorder(
                BorderFactory.createTitledBorder("Datos")
        );

        botonesDatos.add(btnListar);

        JPanel sur = new JPanel(new BorderLayout());

        sur.add(botonesAcciones, BorderLayout.CENTER);
        sur.add(botonesDatos, BorderLayout.EAST);

        JPanel norte = new JPanel(new BorderLayout());

        norte.add(formulario, BorderLayout.CENTER);
        norte.add(sur, BorderLayout.SOUTH);

        add(norte, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        btnGuardar.addActionListener(e -> guardarProfesor());
        btnListar.addActionListener(e -> listarProfesores());
        btnEliminar.addActionListener(e -> eliminarProfesor());
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        listarProfesores();
    }

    private void guardarProfesor() {

        try {

            String dni = campoDni.getText().trim();
            String nombre = campoNombre.getText().trim();
            String apellido = campoApellido.getText().trim();
            String dir = campoDirec.getText().trim();
            String telefonoTexto = campoTelef.getText().trim();

            // TODOS VACÍOS
            if (dni.isEmpty() && nombre.isEmpty() && apellido.isEmpty()
                    && dir.isEmpty() && telefonoTexto.isEmpty()) {

                JOptionPane.showMessageDialog(this,
                        "Tienes que meter datos.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // DNI vacío
            if (dni.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "El DNI no puede estar vacío.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // DNI longitud
            if (dni.length() != 9) {
                JOptionPane.showMessageDialog(this,
                        "El DNI debe tener 9 caracteres.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Nombre vacío
            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "El nombre no puede estar vacío.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Apellido vacío
            if (apellido.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "El apellido no puede estar vacío.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Dirección vacía
            if (dir.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "La dirección no puede estar vacía.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Teléfono vacío
            if (telefonoTexto.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "El teléfono no puede estar vacío.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Teléfono longitud
            if (telefonoTexto.length() != 9) {
                JOptionPane.showMessageDialog(this,
                        "El teléfono debe tener 9 números.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Convertir teléfono SOLO después de validar
            long telef = Long.parseLong(telefonoTexto);

            // Comprobar si ya existe el profesor
            if (profesorDAO.buscarPorId(dni) != null) {

                JOptionPane.showMessageDialog(this,
                        "Ya existe un profesor con ese DNI.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);

                return;
            }

            List<String> cursos = Arrays.asList(campoCursos.getText().split(","));

            Profesor p = new Profesor(dni, nombre, apellido, dir, telef, cursos);

            profesorDAO.insertar(p);

            JOptionPane.showMessageDialog(this, "Profesor guardado correctamente.");

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(this,
                    "El teléfono solo puede contener números.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        } finally {
            listarProfesores();
        }
    }

    /**
     * Elimina el profesor seleccionado en la tabla.
     * Muestra un mensaje de advertencia si tiene reservas asociadas.
     */
    private void eliminarProfesor() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un profesor de la tabla primero.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String dni = (String) modeloTabla.getValueAt(fila, 0);
        String nombre = (String) modeloTabla.getValueAt(fila, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Eliminar al profesor " + nombre + " (DNI: " + dni + ")?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) return;

        boolean eliminado = profesorDAO.eliminar(dni);
        if (eliminado) {
            JOptionPane.showMessageDialog(this, "Profesor eliminado correctamente.");
        } else {
            JOptionPane.showMessageDialog(this,
                    "No se puede eliminar: el profesor tiene reservas asociadas.\nElimina primero esas reservas.",
                    "Error de integridad", JOptionPane.ERROR_MESSAGE);
        }
        listarProfesores();
    }

    private void listarProfesores() {
        modeloTabla.setRowCount(0);
        for (Profesor p : profesorDAO.listarTodos()) {
            modeloTabla.addRow(new Object[]{
                    p.getDni(), p.getNombre(), p.getApellido(),
                    p.getDireccion(), p.getTelefono(), p.getCursos()
            });
        }
    }

    private void limpiarFormulario() {
        campoDni.setText(""); campoNombre.setText(""); campoApellido.setText("");
        campoDirec.setText(""); campoTelef.setText(""); campoCursos.setText("");
    }
}

/* =========================================================
 *  PESTAÑA AULAS
 * ========================================================= */

/**
 * Panel para gestionar aulas: añadir, listar y eliminar.
 * Al eliminar se comprueba que el aula no tenga reservas asociadas.
 */
class PanelAula extends JPanel {

    private final AulaDAO aulaDAO = new AulaDAO();

    private final JTextField campoNum       = new JTextField(10);
    private final JTextField campoCapacidad = new JTextField(10);

    private final DefaultTableModel modeloTabla = new DefaultTableModel(
            new String[]{"Número Aula", "Capacidad"}, 0
    ) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable tabla = new JTable(modeloTabla);

    public PanelAula() {
        setLayout(new BorderLayout(10, 10));

        JPanel formulario = new JPanel(new GridLayout(0, 2, 5, 5));
        formulario.setBorder(BorderFactory.createTitledBorder("Nueva Aula"));

        formulario.add(new JLabel("Número de Aula:")); formulario.add(campoNum);
        formulario.add(new JLabel("Capacidad:"));      formulario.add(campoCapacidad);

        JButton btnGuardar  = new JButton("Guardar");
        JButton btnListar   = new JButton("Listar todas");
        JButton btnEliminar = new JButton("Eliminar seleccionada");

        JPanel botonesAcciones = new JPanel();

        botonesAcciones.add(btnGuardar);
        botonesAcciones.add(btnEliminar);

        JPanel botonesDatos = new JPanel();

        botonesDatos.setBorder(
                BorderFactory.createTitledBorder("Datos")
        );

        botonesDatos.add(btnListar);

        JPanel sur = new JPanel(new BorderLayout());

        sur.add(botonesAcciones, BorderLayout.CENTER);
        sur.add(botonesDatos, BorderLayout.EAST);

        JPanel norte = new JPanel(new BorderLayout());

        norte.add(formulario, BorderLayout.CENTER);
        norte.add(sur, BorderLayout.SOUTH);

        add(norte, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        btnGuardar.addActionListener(e -> guardarAula());
        btnListar.addActionListener(e -> listarAulas());
        btnEliminar.addActionListener(e -> eliminarAula());

        listarAulas();
    }

    private void guardarAula() {
        try {
            int num = Integer.parseInt(campoNum.getText().trim());
            int cap = Integer.parseInt(campoCapacidad.getText().trim());

            if (cap <= 0) {
                JOptionPane.showMessageDialog(this, "La capacidad debe ser mayor que 0.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            aulaDAO.insertar(new Aula(num, cap));
            JOptionPane.showMessageDialog(this, "Aula guardada correctamente.");
            campoNum.setText(""); campoCapacidad.setText("");
            listarAulas();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Los campos deben ser números.", "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            listarAulas();
        }
    }

    /**
     * Elimina el aula seleccionada en la tabla.
     * Muestra un mensaje de advertencia si tiene reservas asociadas.
     */
    private void eliminarAula() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un aula de la tabla primero.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Integer numAula = (Integer) modeloTabla.getValueAt(fila, 0);

        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Eliminar el aula número " + numAula + "?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) return;

        boolean eliminado = aulaDAO.eliminar(numAula);
        if (eliminado) {
            JOptionPane.showMessageDialog(this, "Aula eliminada correctamente.");
        } else {
            JOptionPane.showMessageDialog(this,
                    "No se puede eliminar: el aula tiene reservas asociadas.\nElimina primero esas reservas.",
                    "Error de integridad", JOptionPane.ERROR_MESSAGE);
        }
        listarAulas();
    }

    private void listarAulas() {
        modeloTabla.setRowCount(0);
        for (Aula a : aulaDAO.listarTodos()) {
            modeloTabla.addRow(new Object[]{a.getNumAula(), a.getCapacidad()});
        }
    }
}

/* =========================================================
 *  PESTAÑA ALUMNOS
 * ========================================================= */

/**
 * Panel para gestionar alumnos: añadir, listar y eliminar.
 * Al eliminar se comprueba que el alumno no tenga incidencias ni reservas.
 */
class PanelAlumno extends JPanel {

    private final AlumnoDAO alumnoDAO = new AlumnoDAO();

    private final JTextField campoDni         = new JTextField(10);
    private final JTextField campoNombre      = new JTextField(15);
    private final JTextField campoApellido    = new JTextField(15);
    private final JTextField campoDirec       = new JTextField(20);
    private final JTextField campoTelef       = new JTextField(10);
    private final JTextField campoCurso       = new JTextField(10);
    private final JTextField campoAsignaturas = new JTextField(20);

    private final DefaultTableModel modeloTabla = new DefaultTableModel(
            new String[]{"DNI", "Nombre", "Apellido", "Dirección", "Teléfono", "Curso", "Asignaturas"}, 0
    ) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable tabla = new JTable(modeloTabla);

    public PanelAlumno() {
        setLayout(new BorderLayout(10, 10));

        JPanel formulario = new JPanel(new GridLayout(0, 2, 5, 5));
        formulario.setBorder(BorderFactory.createTitledBorder("Nuevo Alumno"));

        formulario.add(new JLabel("DNI:"));              formulario.add(campoDni);
        formulario.add(new JLabel("Nombre:"));           formulario.add(campoNombre);
        formulario.add(new JLabel("Apellido:"));         formulario.add(campoApellido);
        formulario.add(new JLabel("Dirección:"));        formulario.add(campoDirec);
        formulario.add(new JLabel("Teléfono:"));         formulario.add(campoTelef);
        formulario.add(new JLabel("Curso:"));            formulario.add(campoCurso);
        formulario.add(new JLabel("Asignaturas (,):"));  formulario.add(campoAsignaturas);

        JButton btnGuardar  = new JButton("Guardar");
        JButton btnListar   = new JButton("Listar todos");
        JButton btnEliminar = new JButton("Eliminar seleccionado");
        JButton btnLimpiar  = new JButton("Limpiar");

        JPanel botonesAcciones = new JPanel();

        botonesAcciones.add(btnGuardar);
        botonesAcciones.add(btnEliminar);
        botonesAcciones.add(btnLimpiar);

        JPanel botonesDatos = new JPanel();

        botonesDatos.setBorder(
                BorderFactory.createTitledBorder("Datos")
        );

        botonesDatos.add(btnListar);

        JPanel sur = new JPanel(new BorderLayout());

        sur.add(botonesAcciones, BorderLayout.CENTER);
        sur.add(botonesDatos, BorderLayout.EAST);

        JPanel norte = new JPanel(new BorderLayout());

        norte.add(formulario, BorderLayout.CENTER);
        norte.add(sur, BorderLayout.SOUTH);

        add(norte, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        btnGuardar.addActionListener(e -> guardarAlumno());
        btnListar.addActionListener(e -> listarAlumnos());
        btnEliminar.addActionListener(e -> eliminarAlumno());
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        listarAlumnos();
    }

    private void guardarAlumno() {

        try {

            String dni = campoDni.getText().trim();
            String nombre = campoNombre.getText().trim();
            String apellido = campoApellido.getText().trim();
            String dir = campoDirec.getText().trim();
            String telefonoTexto = campoTelef.getText().trim();
            String curso = campoCurso.getText().trim();

            List<String> asignaturas =
                    Arrays.asList(campoAsignaturas.getText().split(","));

            // TODOS VACÍOS
            if (dni.isEmpty() && nombre.isEmpty() && apellido.isEmpty()
                    && dir.isEmpty() && telefonoTexto.isEmpty()
                    && curso.isEmpty()) {

                JOptionPane.showMessageDialog(this,
                        "Tienes que meter datos.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // DNI vacío
            if (dni.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "El DNI no puede estar vacío.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // DNI longitud
            if (dni.length() != 9) {
                JOptionPane.showMessageDialog(this,
                        "El DNI debe tener 9 caracteres.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Nombre vacío
            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "El nombre no puede estar vacío.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Apellido vacío
            if (apellido.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "El apellido no puede estar vacío.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Dirección vacía
            if (dir.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "La dirección no puede estar vacía.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Curso vacío
            if (curso.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "El curso no puede estar vacío.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Teléfono vacío
            if (telefonoTexto.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "El teléfono no puede estar vacío.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Teléfono longitud
            if (telefonoTexto.length() != 9) {
                JOptionPane.showMessageDialog(this,
                        "El teléfono debe tener 9 números.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Convertir después de validar
            long telef = Long.parseLong(telefonoTexto);

            if (alumnoDAO.buscarPorId(dni) != null) {

                JOptionPane.showMessageDialog(this,
                        "Ya existe un alumno con ese DNI.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);

                return;
            }

            alumnoDAO.insertar(
                    new Alumno(
                            dni,
                            nombre,
                            apellido,
                            dir,
                            telef,
                            curso,
                            asignaturas
                    )
            );

            JOptionPane.showMessageDialog(this,
                    "Alumno guardado correctamente.");

            limpiarFormulario();
            listarAlumnos();

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(this,
                    "El teléfono solo puede contener números.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        } finally {

            listarAlumnos();
        }
    }

    /**
     * Elimina el alumno seleccionado en la tabla.
     * Muestra un mensaje de advertencia si tiene incidencias o reservas asociadas.
     */
    private void eliminarAlumno() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un alumno de la tabla primero.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String dni = (String) modeloTabla.getValueAt(fila, 0);
        String nombre = (String) modeloTabla.getValueAt(fila, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Eliminar al alumno " + nombre + " (DNI: " + dni + ")?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) return;

        boolean eliminado = alumnoDAO.eliminar(dni);
        if (eliminado) {
            JOptionPane.showMessageDialog(this, "Alumno eliminado correctamente.");
        } else {
            JOptionPane.showMessageDialog(this,
                    "No se puede eliminar: el alumno tiene incidencias o reservas asociadas.\nElimínalas primero.",
                    "Error de integridad", JOptionPane.ERROR_MESSAGE);
        }
        listarAlumnos();
    }

    private void listarAlumnos() {
        modeloTabla.setRowCount(0);
        for (Alumno a : alumnoDAO.listarTodos()) {
            modeloTabla.addRow(new Object[]{
                    a.getDni(), a.getNombre(), a.getApellido(),
                    a.getDireccion(), a.getTelefono(), a.getCurso(), a.getAsignaturas()
            });
        }
    }

    private void limpiarFormulario() {
        campoDni.setText(""); campoNombre.setText(""); campoApellido.setText("");
        campoDirec.setText(""); campoTelef.setText(""); campoCurso.setText(""); campoAsignaturas.setText("");
    }
}

/* =========================================================
 *  PESTAÑA RESERVAS
 * ========================================================= */

/**
 * Panel para gestionar reservas: crear, listar y eliminar.
 * Detecta conflictos de horario y verifica incidencias antes de eliminar.
 */
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
    ) {
        @Override
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };

    private final JTable tabla = new JTable(modeloTabla);

    // Panel de Reserva donde se crea lo visual
    public PanelReserva() {

        setLayout(new BorderLayout(10, 10));

        // --- NUEVO DISEÑO DIVIDIDO PARA EL FORMULARIO ---
        JPanel formulario = new JPanel(new BorderLayout(15, 10));
        formulario.setBorder(BorderFactory.createTitledBorder("Nueva Reserva"));

        // Panel izquierdo
        JPanel panelCampos = new JPanel(new GridLayout(4, 2, 5, 5));

        panelCampos.add(new JLabel("Profesor:"));
        panelCampos.add(comboProfesor);

        panelCampos.add(new JLabel("Aula:"));
        panelCampos.add(comboAula);

        panelCampos.add(new JLabel("Fecha (YYYY-MM-DD):"));
        panelCampos.add(campoFecha);

        panelCampos.add(new JLabel("Hora (HH:MM):"));
        panelCampos.add(campoHora);

        // Panel derecho
        JPanel panelLista = new JPanel(new BorderLayout(0, 5));

        panelLista.add(new JLabel("Alumnos (Ctrl+clic):"), BorderLayout.NORTH);

        listaAlumnos.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        JScrollPane scrollLista = new JScrollPane(listaAlumnos);
        scrollLista.setPreferredSize(new Dimension(250, 100));

        panelLista.add(scrollLista, BorderLayout.CENTER);

        // Unir paneles
        formulario.add(panelCampos, BorderLayout.CENTER);
        formulario.add(panelLista, BorderLayout.EAST);

        // Botones
        JButton btnCargar   = new JButton("Cargar datos");
        JButton btnGuardar  = new JButton("Guardar reserva");
        JButton btnListar   = new JButton("Listar reservas");
        JButton btnEliminar = new JButton("Eliminar seleccionada");

        JPanel botonesAcciones = new JPanel();

        botonesAcciones.add(btnGuardar);
        botonesAcciones.add(btnEliminar);

        JPanel botonesDatos = new JPanel();

        botonesDatos.setBorder(
                BorderFactory.createTitledBorder("Datos")
        );

        botonesDatos.add(btnCargar);
        botonesDatos.add(btnListar);

        JPanel sur = new JPanel(new BorderLayout());

        sur.add(botonesAcciones, BorderLayout.CENTER);
        sur.add(botonesDatos, BorderLayout.EAST);

        JPanel norte = new JPanel(new BorderLayout());

        norte.add(formulario, BorderLayout.CENTER);
        norte.add(sur, BorderLayout.SOUTH);

        add(norte, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // Eventos
        btnCargar.addActionListener(e -> cargarDatos());
        btnGuardar.addActionListener(e -> guardarReserva());
        btnListar.addActionListener(e -> listarReservas());
        btnEliminar.addActionListener(e -> eliminarReserva());

        cargarDatos();
        listarReservas();
    }

    // Cargar datos de la BD
    private void cargarDatos() {

        comboProfesor.removeAllItems();

        for (Profesor p : profesorDAO.listarTodos()) {
            comboProfesor.addItem(
                    p.getDni() + " - " + p.getNombre() + " " + p.getApellido()
            );
        }

        comboAula.removeAllItems();

        for (Aula a : aulaDAO.listarTodos()) {
            comboAula.addItem(
                    a.getNumAula() + " (cap: " + a.getCapacidad() + ")"
            );
        }

        modeloAlumnos.clear();

        for (Alumno a : alumnoDAO.listarTodos()) {
            modeloAlumnos.addElement(
                    a.getDni() + " - " + a.getNombre() + " " + a.getApellido()
            );
        }
    }

    // Guardar reserva
    private void guardarReserva() {

        try {

            String selProfesor = (String) comboProfesor.getSelectedItem();

            if (selProfesor == null) {
                JOptionPane.showMessageDialog(this, "Selecciona un profesor.");
                return;
            }

            String dniProfesor =
                    selProfesor.split(" - ")[0].trim();

            String selAula = (String) comboAula.getSelectedItem();

            if (selAula == null) {
                JOptionPane.showMessageDialog(this, "Selecciona un aula.");
                return;
            }

            int numAula = Integer.parseInt(
                    selAula.split(" ")[0].trim()
            );

            LocalDate fecha = LocalDate.parse(
                    campoFecha.getText().trim()
            );

            String hora = campoHora.getText().trim();

            // Validar alumnos seleccionados
            List<String> seleccionados = listaAlumnos.getSelectedValuesList();

            if (seleccionados.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Debes seleccionar al menos un alumno.",
                        "Advertencia",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            // Comprobar conflicto de horario
            for (Reserva r : reservaDAO.listarTodos()) {

                if (r.getAula().getNumAula() == numAula
                        && r.getFecha().equals(fecha)
                        && r.getHora().equals(hora)) {

                    JOptionPane.showMessageDialog(
                            this,
                            "⚠ Conflicto: el aula " + numAula
                                    + " ya está reservada el "
                                    + fecha + " a las " + hora,
                            "Conflicto de horario",
                            JOptionPane.WARNING_MESSAGE
                    );

                    return;
                }
            }

            // Crear lista de alumnos
            List<Alumno> alumnosReserva = new ArrayList<>();

            for (String s : seleccionados) {

                String dni =
                        s.split(" - ")[0].trim();

                Alumno a = alumnoDAO.buscarPorId(dni);

                if (a != null) {
                    alumnosReserva.add(a);
                }
            }

            Profesor profesor = profesorDAO.buscarPorId(dniProfesor);
            Aula aula         = aulaDAO.buscarPorId(numAula);

            Reserva reserva = new Reserva(
                    profesor,
                    aula,
                    alumnosReserva,
                    fecha,
                    hora
            );

            reservaDAO.insertar(reserva);

            JOptionPane.showMessageDialog(
                    this,
                    "Reserva guardada correctamente."
            );

            listarReservas();

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        } finally {
            cargarDatos();
            listarReservas();
        }
    }

    // Eliminar reserva
    private void eliminarReserva() {

        int fila = tabla.getSelectedRow();

        if (fila == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Selecciona una reserva de la tabla.",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int id = (int) modeloTabla.getValueAt(fila, 0);

        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Eliminar la reserva #" + id + "?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacion == JOptionPane.YES_OPTION) {

            if (reservaDAO.eliminar(id)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Reserva eliminada."
                );

                listarReservas();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "No se pudo eliminar la reserva.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    // Listar reservas
    private void listarReservas() {

        modeloTabla.setRowCount(0);

        List<Reserva> lista = reservaDAO.listarTodos();

        for (Reserva r : lista) {

            modeloTabla.addRow(new Object[] {
                    r.getId(),
                    r.getProfesor().getNombre() + " "
                            + r.getProfesor().getApellido(),
                    r.getAula().getNumAula(),
                    r.getFecha(),
                    r.getHora(),
                    r.getAlumnos().size() + " alumno(s)"
            });
        }
    }
}

/* =========================================================
 *  PESTAÑA INCIDENCIAS
 * ========================================================= */

/**
 * Panel para registrar, listar y eliminar incidencias.
 * La incidencia no tiene dependencias, se puede eliminar directamente.
 */
class PanelIncidencia extends JPanel {

    private final IncidenciaDAO incidenciaDAO = new IncidenciaDAO();
    private final AlumnoDAO     alumnoDAO     = new AlumnoDAO();
    private final ReservaDAO    reservaDAO    = new ReservaDAO();

    private final JComboBox<String> comboAlumno  = new JComboBox<>();
    private final JComboBox<String> comboReserva = new JComboBox<>();
    private final JTextArea campoDescripcion     = new JTextArea(3, 30);

    private final DefaultTableModel modeloTabla = new DefaultTableModel(
            new String[]{"ID", "Alumno", "Reserva ID", "Descripción"}, 0
    ) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable tabla = new JTable(modeloTabla);

    public PanelIncidencia() {
        setLayout(new BorderLayout(10, 10));

        JPanel formulario = new JPanel(new GridLayout(0, 2, 5, 5));
        formulario.setBorder(BorderFactory.createTitledBorder("Nueva Incidencia"));

        formulario.add(new JLabel("Alumno:"));       formulario.add(comboAlumno);
        formulario.add(new JLabel("Reserva:"));      formulario.add(comboReserva);
        formulario.add(new JLabel("Descripción:"));  formulario.add(new JScrollPane(campoDescripcion));

        JButton btnCargar   = new JButton("Cargar datos");
        JButton btnGuardar  = new JButton("Registrar incidencia");
        JButton btnListar   = new JButton("Listar incidencias");
        JButton btnEliminar = new JButton("Eliminar seleccionada");

        JPanel botonesAcciones = new JPanel();

        botonesAcciones.add(btnGuardar);
        botonesAcciones.add(btnEliminar);

        JPanel botonesDatos = new JPanel();

        botonesDatos.setBorder(
                BorderFactory.createTitledBorder("Datos")
        );

        botonesDatos.add(btnCargar);
        botonesDatos.add(btnListar);

        JPanel sur = new JPanel(new BorderLayout());

        sur.add(botonesAcciones, BorderLayout.CENTER);
        sur.add(botonesDatos, BorderLayout.EAST);

        JPanel norte = new JPanel(new BorderLayout());

        norte.add(formulario, BorderLayout.CENTER);
        norte.add(sur, BorderLayout.SOUTH);

        add(norte, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        btnCargar.addActionListener(e -> cargarDatos());
        btnGuardar.addActionListener(e -> guardarIncidencia());
        btnListar.addActionListener(e -> listarIncidencias());
        btnEliminar.addActionListener(e -> eliminarIncidencia());

        cargarDatos();
        listarIncidencias();
    }

    private void cargarDatos() {
        comboAlumno.removeAllItems();
        for (Alumno a : alumnoDAO.listarTodos())
            comboAlumno.addItem(a.getDni() + " - " + a.getNombre() + " " + a.getApellido());

        comboReserva.removeAllItems();
        for (Reserva r : reservaDAO.listarTodos())
            comboReserva.addItem(r.getId() + " | " + r.getFecha() + " " + r.getHora()
                    + " | Aula " + r.getAula().getNumAula());
    }

    private void guardarIncidencia() {
        try {
            String selAlumno  = (String) comboAlumno.getSelectedItem();
            String selReserva = (String) comboReserva.getSelectedItem();
            String descripcion = campoDescripcion.getText().trim();

            if (selAlumno == null || selReserva == null) {
                JOptionPane.showMessageDialog(this, "Selecciona alumno y reserva.");
                return;
            }
            if (descripcion.isEmpty()) {
                JOptionPane.showMessageDialog(this, "La descripción no puede estar vacía.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String dniAlumno = selAlumno.split(" - ")[0].trim();
            int idReserva = Integer.parseInt(selReserva.split(" \\| ")[0].trim());

            Alumno  alumno  = alumnoDAO.buscarPorId(dniAlumno);
            Reserva reserva = reservaDAO.buscarPorId(idReserva);

            incidenciaDAO.insertar(new Incidencia(alumno, reserva, descripcion));
            JOptionPane.showMessageDialog(this, "Incidencia registrada correctamente.");
            campoDescripcion.setText("");
            listarIncidencias();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            cargarDatos();
            listarIncidencias();
        }
    }

    /**
     * Elimina la incidencia seleccionada en la tabla directamente,
     * sin verificaciones adicionales (no hay entidades que dependan de ella).
     */
    private void eliminarIncidencia() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una incidencia de la tabla primero.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) modeloTabla.getValueAt(fila, 0);

        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Eliminar la incidencia con ID " + id + "?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) return;

        boolean eliminado = incidenciaDAO.eliminar(id);
        if (eliminado) {
            JOptionPane.showMessageDialog(this, "Incidencia eliminada correctamente.");
        } else {
            JOptionPane.showMessageDialog(this, "No se encontró la incidencia.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
        listarIncidencias();
    }

    private void listarIncidencias() {
        modeloTabla.setRowCount(0);
        for (Incidencia i : incidenciaDAO.listarTodos()) {
            modeloTabla.addRow(new Object[]{
                    i.getId(),
                    i.getAlumno().getNombre() + " " + i.getAlumno().getApellido(),
                    i.getReserva().getId(),
                    i.getDescripcion()
            });
        }
    }
}