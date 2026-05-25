package com.informatica.dao;

import com.informatica.model.Incidencia;
import com.informatica.util.JPAUtil;
import jakarta.persistence.EntityManager;

import java.util.List;

/**
 * DAO para la entidad Incidencia.
 * La incidencia no tiene entidades que dependan de ella,
 * por lo que se puede eliminar directamente sin verificaciones adicionales.
 */
public class IncidenciaDAO {

    public void insertar(Incidencia incidencia) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(incidencia);
            em.getTransaction().commit();
            System.out.println("✅ Incidencia registrada correctamente");
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.out.println("❌ Error al insertar incidencia: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    public Incidencia buscarPorId(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Incidencia.class, id);
        } finally {
            em.close();
        }
    }

    public List<Incidencia> listarTodos() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT i FROM Incidencia i", Incidencia.class).getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Elimina una incidencia por su ID.
     * No tiene dependencias, se elimina directamente.
     * @return true si se eliminó, false si no se encontró.
     */
    public boolean eliminar(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Incidencia i = em.find(Incidencia.class, id);
            if (i != null) {
                em.remove(i);
                em.getTransaction().commit();
                System.out.println("✅ Incidencia eliminada correctamente");
                return true;
            } else {
                em.getTransaction().rollback();
                System.out.println("⚠ Incidencia no encontrada con ID: " + id);
                return false;
            }
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.out.println("❌ Error al eliminar incidencia: " + e.getMessage());
            return false;
        } finally {
            em.close();
        }
    }
}