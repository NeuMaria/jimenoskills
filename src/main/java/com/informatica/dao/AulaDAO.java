package com.informatica.dao;

import com.informatica.model.Aula;
import com.informatica.util.JPAUtil;
import jakarta.persistence.EntityManager;

import java.util.List;

/**
 * DAO para la entidad Aula.
 * Incluye verificación de claves ajenas antes de eliminar:
 * no se puede borrar un aula si tiene reservas asociadas.
 */
public class AulaDAO {

    public void insertar(Aula aula) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(aula);
            em.getTransaction().commit();
            System.out.println("✅ Aula insertada correctamente");
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.out.println("❌ Error al insertar aula: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    public Aula buscarPorId(int numAula) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Aula.class, numAula);
        } finally {
            em.close();
        }
    }

    public List<Aula> listarTodos() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT a FROM Aula a", Aula.class).getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Elimina un aula solo si no tiene reservas asociadas.
     * @return true si se eliminó, false si tiene dependencias.
     */
    public boolean eliminar(int numAula) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            // Verificar si hay reservas que usen esta aula
            Long reservasAsociadas = em.createQuery(
                            "SELECT COUNT(r) FROM Reserva r WHERE r.aula.numAula = :numAula", Long.class)
                    .setParameter("numAula", numAula)
                    .getSingleResult();

            if (reservasAsociadas > 0) {
                System.out.println("⚠ No se puede eliminar: el aula tiene " + reservasAsociadas + " reserva(s) asociada(s).");
                return false;
            }

            em.getTransaction().begin();
            Aula a = em.find(Aula.class, numAula);
            if (a != null) {
                em.remove(a);
                em.getTransaction().commit();
                System.out.println("✅ Aula eliminada correctamente");
                return true;
            } else {
                em.getTransaction().rollback();
                System.out.println("⚠ Aula no encontrada con número: " + numAula);
                return false;
            }
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.out.println("❌ Error al eliminar aula: " + e.getMessage());
            return false;
        } finally {
            em.close();
        }
    }
}
