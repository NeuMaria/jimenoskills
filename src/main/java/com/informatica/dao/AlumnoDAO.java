package com.informatica.dao;

import com.informatica.model.Alumno;
import com.informatica.util.JPAUtil;
import jakarta.persistence.EntityManager;

import java.util.List;

/**
 * DAO para la entidad Alumno.
 * Incluye verificación de claves ajenas antes de eliminar:
 * no se puede borrar un alumno si tiene incidencias o participa en reservas.
 */
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
            System.out.println("❌ Error al insertar alumno: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    public Alumno buscarPorId(String dni) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Alumno.class, dni);
        } finally {
            em.close();
        }
    }

    public List<Alumno> listarTodos() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT a FROM Alumno a", Alumno.class).getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Elimina un alumno solo si no tiene incidencias ni aparece en ninguna reserva.
     * @return true si se eliminó, false si tiene dependencias.
     */
    public boolean eliminar(String dni) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            // Verificar incidencias vinculadas al alumno
            Long incidencias = em.createQuery(
                            "SELECT COUNT(i) FROM Incidencia i WHERE i.alumno.dni = :dni", Long.class)
                    .setParameter("dni", dni)
                    .getSingleResult();

            if (incidencias > 0) {
                System.out.println("⚠ No se puede eliminar: el alumno tiene " + incidencias + " incidencia(s) registrada(s).");
                return false;
            }

            // Verificar si el alumno aparece en alguna reserva (relación ManyToMany)
            Long reservas = em.createQuery(
                            "SELECT COUNT(r) FROM Reserva r WHERE :alumno MEMBER OF r.alumnos", Long.class)
                    .setParameter("alumno", em.find(Alumno.class, dni))
                    .getSingleResult();

            if (reservas > 0) {
                System.out.println("⚠ No se puede eliminar: el alumno participa en " + reservas + " reserva(s).");
                return false;
            }

            em.getTransaction().begin();
            Alumno a = em.find(Alumno.class, dni);
            if (a != null) {
                em.remove(a);
                em.getTransaction().commit();
                System.out.println("✅ Alumno eliminado correctamente");
                return true;
            } else {
                em.getTransaction().rollback();
                System.out.println("⚠ Alumno no encontrado con DNI: " + dni);
                return false;
            }
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.out.println("❌ Error al eliminar alumno: " + e.getMessage());
            return false;
        } finally {
            em.close();
        }
    }
}