package com.informatica.dao;

import com.informatica.model.Aula;
import com.informatica.model.Persona;
import com.informatica.model.Profesor;
import com.informatica.util.JPAUtil;
import jakarta.persistence.EntityManager;

import java.util.List;

public class ProfesorDAO {
    public void insertar(Profesor profesor) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(profesor);
            em.getTransaction().commit();
            System.out.println("✅ Profesor insertado correctamente");
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.out.println("❌ Error al insertar artículo: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    public Profesor buscarPorId(String id) {
        EntityManager em = JPAUtil.getEntityManager();
        Profesor profesor = em.find(Profesor.class, id);
        em.close();
        return profesor;
    }

    public List<Profesor> listarTodos() {
        EntityManager em = JPAUtil.getEntityManager();
        List<Profesor> profesores = em.createQuery("SELECT a FROM Profesor a", Profesor.class).getResultList();
        em.close();
        return profesores;
    }

    public boolean eliminar(String id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Profesor profesor = em.find(Profesor.class, id);
            if (profesor != null) {
                em.remove(profesor);
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
