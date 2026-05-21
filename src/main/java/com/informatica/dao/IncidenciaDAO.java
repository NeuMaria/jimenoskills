package com.informatica.dao;

import com.informatica.model.Incidencia;
import com.informatica.util.JPAUtil;
import jakarta.persistence.EntityManager;

import java.util.List;

public class IncidenciaDAO {
    public void insertar(Incidencia incidencia) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(incidencia);
            em.getTransaction().commit();
            System.out.println("✅ Incidencia insertado correctamente");
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.out.println("❌ Error al insertar artículo: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    public Incidencia buscarPorId(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        Incidencia incidencia = em.find(Incidencia.class, id);
        em.close();
        return incidencia;
    }

    public List<Incidencia> listarTodos() {
        EntityManager em = JPAUtil.getEntityManager();
        List<Incidencia> incidencias = em.createQuery("SELECT a FROM Incidencia a", Incidencia.class).getResultList();
        em.close();
        return incidencias;
    }

    public boolean eliminar(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Incidencia incidencia = em.find(Incidencia.class, id);
            if (incidencia != null) {
                em.remove(incidencia);
            }
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.out.println("❌ Error al eliminar: " + e.getMessage());
            return false;
        } finally {
            em.close();
        }
    }
}
