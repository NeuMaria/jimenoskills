package com.instituto.dao;

import com.instituto.model.Reserva;
import com.instituto.model.Profesor;
import com.instituto.util.JPAUtil;
import jakarta.persistence.EntityManager;

import java.util.List;

public class ReservaDAO {
    //Esta función, usando reserva como input lo guarda en la base de datos
    //Mete el reserva en la base de datos -> Lo guarda en reservas -> Genera Id automaticamente
    public void guardar(Reserva reserva) {

        EntityManager em = JPAUtil.getEntityManager();

        em.getTransaction().begin();
        em.persist(reserva);
        em.getTransaction().commit();

        em.close();
    }

    //Elinamos por la id que le proporcionemos
    public void eliminar(int id) {

        EntityManager em = JPAUtil.getEntityManager();

        em.getTransaction().begin();

        Reserva reserva = em.find(Reserva.class, id);

        if (reserva != null) {
            em.remove(reserva);
        }

        em.getTransaction().commit();
        em.close();
    }

    //Lista todas las reservas de la tabla reserva
    public List<Reserva> listarTodas() {

        EntityManager em = JPAUtil.getEntityManager();

        List<Reserva> lista = em.createQuery(
                "FROM Reserva", Reserva.class
        ).getResultList();

        em.close();

        return lista;
    }
}