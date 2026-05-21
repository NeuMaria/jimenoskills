package com.informatica.dao;

import com.informatica.model.Alumno;
import com.informatica.model.Aula;
import com.informatica.util.JPAUtil;
import jakarta.persistence.EntityManager;

import java.util.List;

public class AulaDAO {
    public void insertar(Aula aula) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(aula);
            em.getTransaction().commit();
            System.out.println("✅ Aula insertado correctamente");
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.out.println("❌ Error al insertar artículo: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    public Aula buscarPorId(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        Aula aula = em.find(Aula.class, id);
        em.close();
        return aula;
    }

    public List<Aula> listarTodos() {
        EntityManager em = JPAUtil.getEntityManager();
        List<Aula> aulas = em.createQuery("SELECT a FROM Aula a", Aula.class).getResultList();
        em.close();
        return aulas;
    }
}
