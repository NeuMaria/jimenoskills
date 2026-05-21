package com.informatica.dao;

import com.informatica.model.Alumno;
import com.informatica.util.JPAUtil;
import jakarta.persistence.EntityManager;

import java.util.List;

public class AlumnoDAO {
    public void insertar(Alumno alumno) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(alumno);
            em.getTransaction().commit();
            System.out.println("✅ Alumno insertado correctamente");
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.out.println("❌ Error al insertar artículo: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    public Alumno buscarPorId(String id) {
        EntityManager em = JPAUtil.getEntityManager();
        Alumno alumno = em.find(Alumno.class, id);
        em.close();
        return alumno;
    }

    public List<Alumno> listarTodos() {
        EntityManager em = JPAUtil.getEntityManager();
        List<Alumno> alumnos = em.createQuery("SELECT a FROM Alumno a", Alumno.class).getResultList();
        em.close();
        return alumnos;
    }

    public boolean eliminar(String id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Alumno alumno = em.find(Alumno.class, id);
            if (alumno != null) {
                em.remove(alumno);
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
