package com.instituto.dao;

import com.instituto.model.Aula;
import com.instituto.util.*;
import jakarta.persistence.EntityManager;

import java.util.List;

public class AulaDAO {
    //Esta función, usando aula como input lo guarda en la base de datos
    //Mete el aula en la base de datos -> Lo guarda en aulas -> Genera Id automaticamente
    public void guardar(Aula aula) {
        EntityManager em = JPAUtil.getEntityManager();
        em.getTransaction().begin();

        em.persist(aula);

        em.getTransaction().commit();
        em.close();
    }
    //Esta función muestra todos los aulas de la tabla aulas
    public List<Aula> listar() {
        EntityManager em = JPAUtil.getEntityManager();

        List<Aula> lista =
                em.createQuery("FROM Aula", Aula.class)
                        .getResultList();

        em.close();
        return lista;
    }
    //Elimina el aula según su id
    public void eliminar(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        em.getTransaction().begin();

        Aula aula = em.find(Aula.class, id);
        if (aula != null) {
            em.remove(aula);
        }

        em.getTransaction().commit();
        em.close();
    }
}