package com.jocata.AMS.config;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.hibernate.query.criteria.JpaCriteriaQuery;
import org.hibernate.query.criteria.JpaPredicate;
import org.hibernate.query.criteria.JpaRoot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.sql.Timestamp;

@Component
public class HibernateConfig {

    private final SessionFactory sessionFactory;

    @Autowired
    public HibernateConfig(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public Session getSession() {
        return this.getSessionFactory().openSession();
    }

    public void closeSession(Session session) {
        if (session != null) {
            session.close();
        }
    }

    public <T> T saveEntity(T entity) {
        Session session = null;
        Transaction tx = null;
        try {
            session = this.getSession();
            tx = session.beginTransaction();
            session.persist(entity);

            tx.commit();
            return entity;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            System.out.println("Error saving entity: " + e.getMessage());
        } finally {
            closeSession(session);
        }
        return null;
    }

    public <T> T saveOrUpdateEntity(T entity) {
        Session session = null;
        Transaction tx = null;
        try {
            session = this.getSession();
            tx = session.beginTransaction();
            T mergeEntity = session.merge(entity);
            tx.commit();
            return mergeEntity;

        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace(); // Log the exception properly
            throw new RuntimeException("Failed to save or update entity", e);
        } finally {
            this.closeSession(session);
        }
//        return null;
    }

    public <T> T findEntityById(Class<T> entityClass, Serializable primaryId) {

        Session session = null;
        try {
            session = this.getSession();
            return session.get(entityClass, primaryId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to find entity by primary id: " + primaryId, e);
        } finally {
            this.closeSession(session);

        }
    }

    public <T> T updateEntity(T entity) {
        Session session = null;
        Transaction tx = null;
        try {
            session = getSession();
            tx = session.beginTransaction();
            session.merge(entity);
            tx.commit();
            return entity;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
        } finally {
            closeSession(session);
        }
        return null;
    }

    public <T> T deleteEntity(T entity, Serializable primaryId) {
        Session session = null;
        Transaction tx = null;
        try {
            session = this.getSession();
            tx = session.beginTransaction();
            Object dataObject = session.get(entity.getClass(), primaryId);
            session.remove(dataObject);
            tx.commit();
            return entity;

        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            this.closeSession(session);
        }
        return null;
    }

    public <T> T softDeleteEntity(T entity) {
        Session session = null;
        Transaction tx = null;
        try {
            session = this.getSession();
            tx = session.beginTransaction();
            session.merge(entity);
            tx.commit();
            return entity;

        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            this.closeSession(session);
        }
        return null;
    }

//    public <T> T findEntityByCriteria(Class<T> entityClass, String primaryPropertyName, Serializable primaryId) {
//
//        Session session = null;
//        try {
//            session = this.getSession();
//            HibernateCriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
//            JpaCriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entityClass);
//            JpaRoot<T> root = criteriaQuery.from(entityClass);
//
//            String[] props = checkIfSplit(primaryPropertyName);
//            if (props.length == 2) {
//                criteriaQuery.select(root).where(criteriaBuilder.equal(root.get(props[0]).get(props[1]), primaryId));
//            } else {
//                criteriaQuery.select(root).where(criteriaBuilder.equal(root.get(primaryPropertyName), primaryId));
//            }
//
//            return session.createQuery(criteriaQuery).getSingleResult();
//        } catch (Exception e) {
//            session.getTransaction().rollback();
//        } finally {
//            this.closeSession(session);
//        }
//        return null;
//    }

    public <T> T findEntityByCriteria(Class<T> entityClass, String primaryPropertyName, Serializable primaryId) {
        // Using try-with-resources to ensure session is closed automatically
        try (Session session = this.getSession()) {

            // Get Hibernate's CriteriaBuilder to construct queries dynamically
            HibernateCriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();

            // Create a CriteriaQuery for the given entity class
            JpaCriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entityClass);

            // Define the root entity (the main table being queried)
            JpaRoot<T> root = criteriaQuery.from(entityClass);

            // Split the primary property name if it's a nested property (e.g., "user.address")
            String[] props = checkIfSplit(primaryPropertyName);

            // If the property name contains a dot (nested field)
            if (props.length == 2) {
                // Query for the nested field (e.g., "user.address" → user.get("address"))
                criteriaQuery.select(root).where(criteriaBuilder.equal(root.get(props[0]).get(props[1]), primaryId));
            } else {
                // Query for a normal field (e.g., "username" → user.get("username"))
                criteriaQuery.select(root).where(criteriaBuilder.equal(root.get(primaryPropertyName), primaryId));
            }

            // Execute the query and get the result list
            List<T> result = session.createQuery(criteriaQuery).getResultList();

            // Return the first result if available, otherwise return null
            return result.isEmpty() ? null : result.get(0);

        } catch (Exception e) {
            // Print stack trace for debugging (should use proper logging in real applications)
            e.printStackTrace();
            // Throw a runtime exception with a meaningful error message
            throw new RuntimeException("Error finding entity", e);
        }
    }

    public <T> List<T> findEntitiesByCriteria(Class<T> entityClass, String primaryPropertyName, Serializable primaryId) {

        Session session = null;
        try {
            session = this.getSession();
            HibernateCriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            JpaCriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entityClass);
            JpaRoot<T> root = criteriaQuery.from(entityClass);

            String[] props = this.checkIfSplit(primaryPropertyName);
            if (props.length == 2) {
                criteriaQuery.select(root).where(criteriaBuilder.equal(root.get(props[0]).get(props[1]), primaryId));
            } else {
                criteriaQuery.select(root).where(criteriaBuilder.equal(root.get(primaryPropertyName), primaryId));
            }

            return session.createQuery(criteriaQuery).getResultList();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            this.closeSession(session);
        }
        return Collections.emptyList();
    }

    public <T> T findEntityByMultipleCriteria(Class<T> entityClass, Map<String, Object> criteriaMap) {
        Session session = null;
        try {
            session = this.getSession();
            HibernateCriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            JpaCriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entityClass);
            JpaRoot<T> root = criteriaQuery.from(entityClass);

            List<JpaPredicate> predicates = new ArrayList<>();
            for (Map.Entry<String, Object> entry : criteriaMap.entrySet()) {
                String propertyName = entry.getKey();
                Object propertyValue = entry.getValue();

                String[] props = checkIfSplit(propertyName);
                if (props.length == 2) {
                    predicates.add(criteriaBuilder.equal(root.get(props[0]).get(props[1]), propertyValue));
                } else {
                    predicates.add(criteriaBuilder.equal(root.get(propertyName), propertyValue));
                }
            }

            criteriaQuery.select(root).where(criteriaBuilder.and(predicates.toArray(new JpaPredicate[0])));
            return session.createQuery(criteriaQuery).getSingleResult();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            this.closeSession(session);
        }
        return null;
    }

    public <T> List<T> loadEntitiesByCriteria(Class<T> entityClass) {
        Session session = null;
        try {
            session = this.getSession();

            HibernateCriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            JpaCriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entityClass);
            JpaRoot<T> root = criteriaQuery.from(entityClass);

            criteriaQuery.select(root);
            Query<T> query = session.createQuery(criteriaQuery);

            return query.getResultList();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            this.closeSession(session);
        }
        return Collections.emptyList();
    }

    private String[] checkIfSplit(String primaryPropertyName) {
        return primaryPropertyName.split("\\.");
    }

}
