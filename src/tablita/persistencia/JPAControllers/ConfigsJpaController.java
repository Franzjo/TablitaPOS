/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tablita.persistencia.JPAControllers;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import tablita.persistencia.Configs;
import tablita.persistencia.JPAControllers.exceptions.NonexistentEntityException;
import tablita.persistencia.JPAControllers.exceptions.PreexistingEntityException;

/**
 *
 * @author akino
 */
public class ConfigsJpaController implements Serializable {

    public ConfigsJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Configs configs) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(configs);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findConfigs(configs.getIdConfigs()) != null) {
                throw new PreexistingEntityException("Configs " + configs + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Configs configs) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            configs = em.merge(configs);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = configs.getIdConfigs();
                if (findConfigs(id) == null) {
                    throw new NonexistentEntityException("The configs with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Configs configs;
            try {
                configs = em.getReference(Configs.class, id);
                configs.getIdConfigs();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The configs with id " + id + " no longer exists.", enfe);
            }
            em.remove(configs);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Configs> findConfigsEntities() {
        return findConfigsEntities(true, -1, -1);
    }

    public List<Configs> findConfigsEntities(int maxResults, int firstResult) {
        return findConfigsEntities(false, maxResults, firstResult);
    }

    private List<Configs> findConfigsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Configs.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Configs findConfigs(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Configs.class, id);
        } finally {
            em.close();
        }
    }

    public int getConfigsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Configs> rt = cq.from(Configs.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
