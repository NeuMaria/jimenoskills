package com.instituto.view;

import com.instituto.dao.AulaDAO;
import com.instituto.model.Aula;
import com.instituto.util.JPAUtil;
import jakarta.persistence.EntityManager;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class RemAulasView extends JFrame {

    //Creamos listas e inicializamos aulaDAO
    private JList<Aula> listaAulas;
    private DefaultListModel<Aula> model;

    private AulaDAO aulaDAO = new AulaDAO();

    //Parte visual
    public RemAulasView() {

        //Inicializamos la ventana con el titulo y medidas
        setTitle("Gestión de Aulas");
        setSize(400, 300);
        setLocationRelativeTo(null);

        model = new DefaultListModel<>();
        listaAulas = new JList<>(model);

        cargarAulas();

        JButton btnEliminar = new JButton("Eliminar Aula");

        //Eliminar aulas
        btnEliminar.addActionListener(e -> eliminarAula());

        setLayout(new BorderLayout());

        add(new JScrollPane(listaAulas), BorderLayout.CENTER);
        add(btnEliminar, BorderLayout.SOUTH);

        setVisible(true);
    }

    //Cargamos las aulas existentes
    private void cargarAulas() {

        EntityManager em = JPAUtil.getEntityManager();

        List<Aula> aulas = em.createQuery("FROM Aula", Aula.class).getResultList();

        model.clear();
        for (Aula a : aulas) {
            model.addElement(a);
        }

        em.close();
    }

    //Eliminamos
    private void eliminarAula() {

        //Guardamos lo seleccionado
        Aula seleccionada = listaAulas.getSelectedValue();

        //Verificamos que haya un aula seleccionada
        if (seleccionada == null) {
            JOptionPane.showMessageDialog(this,
                    "Selecciona un aula");
            return;
        }

        aulaDAO.eliminar(seleccionada.getId());

        //Mostramos que el aula se elimino
        JOptionPane.showMessageDialog(this,
                "Aula eliminada");

        cargarAulas();
    }
}