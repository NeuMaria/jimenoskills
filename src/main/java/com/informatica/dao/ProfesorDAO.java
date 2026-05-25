package com.informatica.dao;

import com.informatica.model.Profesor;
import com.informatica.model.Reserva;
import com.informatica.util.JPAUtil;
import jakarta.persistence.EntityManager;

import java.util.List;

/**
 * DAO para la entidad Profesor.
 * Incluye verificación de claves ajenas antes de eliminar:
 * no se puede borrar un profesor si tiene reservas asociadas.
 */
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
            System.out.println("❌ Error al insertar profesor: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    public Profesor buscarPorId(String dni) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Profesor.class, dni);
        } finally {
            em.close();
        }
    }

    public List<Profesor> listarTodos() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT p FROM Profesor p", Profesor.class).getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Elimina un profesor solo si no tiene reservas asociadas.
     * @return true si se eliminó, false si tiene dependencias.
     */
    public boolean eliminar(String dni) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            // Verificar si el profesor tiene reservas vinculadas
            Long reservasAsociadas = em.createQuery(
                            "SELECT COUNT(r) FROM Reserva r WHERE r.profesor.dni = :dni", Long.class)
                    .setParameter("dni", dni)
                    .getSingleResult();

            if (reservasAsociadas > 0) {
                System.out.println("⚠ No se puede eliminar: el profesor tiene " + reservasAsociadas + " reserva(s) asociada(s).");
                return false;
            }

            em.getTransaction().begin();
            Profesor p = em.find(Profesor.class, dni);
            if (p != null) {
                em.remove(p);
                em.getTransaction().commit();
                System.out.println("✅ Profesor eliminado correctamente");
                return true;
            } else {
                em.getTransaction().rollback();
                System.out.println("⚠ Profesor no encontrado con DNI: " + dni);
                return false;
            }
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.out.println("❌ Error al eliminar profesor: " + e.getMessage());
            return false;
        } finally {
            em.close();
        }
    }
}
