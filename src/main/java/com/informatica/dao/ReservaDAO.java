package com.informatica.dao;

import com.informatica.model.Aula;
import com.informatica.model.Profesor;
import com.informatica.model.Reserva;
import com.informatica.util.JPAUtil;
import jakarta.persistence.EntityManager;

import java.util.List;

public class ReservaDAO {
    public void insertar(Reserva reserva) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            // Re-adjuntar profesor y aula al EntityManager actual
            Profesor profesorManaged = em.merge(reserva.getProfesor());
            Aula aulaManaged = em.merge(reserva.getAula());
            reserva.setProfesor(profesorManaged);
            reserva.setAula(aulaManaged);

            em.persist(reserva);
            em.getTransaction().commit();
            System.out.println("✅ Reserva insertada correctamente");
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.out.println("❌ Error al insertar reserva: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    public Reserva buscarPorId(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        Reserva reserva = em.find(Reserva.class, id);
        em.close();
        return reserva;
    }

    public List<Reserva> listarTodos() {
        EntityManager em = JPAUtil.getEntityManager();
        List<Reserva> reservas = em.createQuery("SELECT a FROM Reserva a", Reserva.class).getResultList();
        em.close();
        return reservas;
    }

    public boolean eliminar(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Reserva reserva = em.find(Reserva.class, id);
            if (reserva != null) {
                em.remove(reserva);
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
