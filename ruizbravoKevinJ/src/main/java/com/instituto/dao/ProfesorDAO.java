package com.instituto.dao;

import com.instituto.model.Profesor;
import com.instituto.util.JPAUtil;
import jakarta.persistence.EntityManager;

import java.util.List;

public class ProfesorDAO {
    //Esta función, usando profesor como input lo guarda en la base de datos
    //Mete el profesor en la base de datos -> Lo guarda en profesores -> Genera Id automaticamente
    public void guardar(Profesor profesor) {
        EntityManager em = JPAUtil.getEntityManager();
        em.getTransaction().begin();

        em.persist(profesor);

        em.getTransaction().commit();
        em.close();
    }
    //Es como buscar por id, pero este busca por el nombre
    public Profesor buscarPorNombre(String nombre) {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            return em.createQuery(
                            "FROM Profesor p WHERE p.nombre = :nombre",
                            Profesor.class)
                    .setParameter("nombre", nombre)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

        } finally {
            em.close();
        }
    }
    //Esta función muestra todos los profesores de la tabla profesores
    public List<Profesor> listar() {
        EntityManager em = JPAUtil.getEntityManager();

        List<Profesor> lista =
                em.createQuery("FROM Profesor", Profesor.class)
                        .getResultList();

        em.close();
        return lista;
    }


}
