package com.instituto.dao;

import com.instituto.model.Incidencia;
import com.instituto.util.JPAUtil;
import jakarta.persistence.EntityManager;

import java.util.List;

public class IncidenciaDAO {
    //Esta función, usando incidencia como input lo guarda en la base de datos
    //Mete el incidencia en la base de datos -> Lo guarda en incidencias -> Genera Id automaticamente
    public void guardar(Incidencia incidencia) {

        EntityManager em = JPAUtil.getEntityManager();

        em.getTransaction().begin();
        em.persist(incidencia);
        em.getTransaction().commit();

        em.close();
    }
    //Esta función elimina la incidencia que coincida con su id
    public void eliminar(int id) {

        EntityManager em = JPAUtil.getEntityManager();

        em.getTransaction().begin();

        Incidencia inc = em.find(Incidencia.class, id);

        if (inc != null) {
            em.remove(inc);
        }

        em.getTransaction().commit();
        em.close();
    }
    //Esta función muestra todos las incidencias de la tabla incidencias
    public List<Incidencia> listar() {

        EntityManager em = JPAUtil.getEntityManager();

        List<Incidencia> lista = em.createQuery(
                "FROM Incidencia", Incidencia.class
        ).getResultList();

        em.close();

        return lista;
    }
}