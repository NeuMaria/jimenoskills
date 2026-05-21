package com.instituto.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAUtil {
    //Establecemos la conexión
    private static final EntityManagerFactory emf =
            //Lee persistence.xml y carga la configuracion de hibernate
            Persistence.createEntityManagerFactory("institutoPU");

    public static EntityManager getEntityManager() {
        //Esto crea el gestor de base de datos.
        return emf.createEntityManager();
    }
}