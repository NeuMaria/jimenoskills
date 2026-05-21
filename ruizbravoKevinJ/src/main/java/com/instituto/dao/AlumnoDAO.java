package com.instituto.dao;

import com.instituto.model.Alumno;
import com.instituto.util.JPAUtil;
import jakarta.persistence.EntityManager;

import java.util.List;

public class AlumnoDAO {
    //Esta función, usando alumno como input lo guarda en la base de datos
    //Mete el alumno en la base de datos -> Lo guarda en alumnos -> Genera Id automaticamente
    public void guardar(Alumno alumno) {

        EntityManager em = JPAUtil.getEntityManager();

        em.getTransaction().begin();
        em.persist(alumno);
        em.getTransaction().commit();

        em.close();
    }

    //Esta función muestra todos los alumnos de la tabla alumnos
    public List<Alumno> listar() {

        EntityManager em = JPAUtil.getEntityManager();

        List<Alumno> lista = em.createQuery(
                "FROM Alumno", Alumno.class
        ).getResultList();

        em.close();

        return lista;
    }

    //Elimina a los alumnos segun el id que le des.
    public void eliminar(int id) {

        EntityManager em = JPAUtil.getEntityManager();

        em.getTransaction().begin();

        Alumno alumno = em.find(Alumno.class, id);

        if (alumno != null) {
            em.remove(alumno);
        }

        em.getTransaction().commit();
        em.close();
    }
}