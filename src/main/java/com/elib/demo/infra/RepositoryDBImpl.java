package com.elib.demo.infra;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public class RepositoryDBImpl implements Repository{// JPA EntityManager

    public static EntityManager em = PersistenceManager.INSTANCE.getEntityManager();

    @Override
    public int numberOfDocuments() {
        Query query = em.createQuery("SELECT COUNT(d) FROM Document d");
        return (Integer) query.getSingleResult();
    }

    @Override
    public int numberOfUsers() {
        Query query = em.createQuery("SELECT COUNT(d) FROM User d");
        return (Integer) query.getSingleResult();
    }

    @Override
    public int numberOfLoans() {
        Query query = em.createQuery("SELECT COUNT(d) FROM Loan d");
        return (Integer) query.getSingleResult();
    }
}