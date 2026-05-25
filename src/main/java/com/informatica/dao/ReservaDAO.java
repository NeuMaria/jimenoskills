package com.informatica.dao;

import com.informatica.model.Aula;
import com.informatica.model.Profesor;
import com.informatica.model.Reserva;
import com.informatica.util.JPAUtil;
import jakarta.persistence.EntityManager;

import java.util.List;

/**
 * DAO para la entidad Reserva.
 * Incluye verificación de claves ajenas antes de eliminar:
 * no se puede borrar una reserva si tiene incidencias asociadas.
 */
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
        try {
            return em.find(Reserva.class, id);
        } finally {
            em.close();
        }
    }

    public List<Reserva> listarTodos() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT r FROM Reserva r", Reserva.class).getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Elimina una reserva solo si no tiene incidencias asociadas.
     * @return true si se eliminó, false si tiene dependencias.
     */
    public boolean eliminar(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            // Verificar si la reserva tiene incidencias vinculadas
            Long incidencias = em.createQuery(
                            "SELECT COUNT(i) FROM Incidencia i WHERE i.reserva.id = :id", Long.class)
                    .setParameter("id", id)
                    .getSingleResult();

            if (incidencias > 0) {
                System.out.println("⚠ No se puede eliminar: la reserva tiene " + incidencias + " incidencia(s) registrada(s).");
                return false;
            }

            em.getTransaction().begin();
            Reserva r = em.find(Reserva.class, id);
            if (r != null) {
                em.remove(r);
                em.getTransaction().commit();
                System.out.println("✅ Reserva eliminada correctamente");
                return true;
            } else {
                em.getTransaction().rollback();
                System.out.println("⚠ Reserva no encontrada con ID: " + id);
                return false;
            }
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.out.println("❌ Error al eliminar reserva: " + e.getMessage());
            return false;
        } finally {
            em.close();
        }
    }
}
