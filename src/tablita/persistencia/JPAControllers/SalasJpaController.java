/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tablita.persistencia.JPAControllers;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import tablita.persistencia.Reservaciones;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import tablita.persistencia.JPAControllers.exceptions.IllegalOrphanException;
import tablita.persistencia.JPAControllers.exceptions.NonexistentEntityException;
import tablita.persistencia.ReservacionesPK;
import tablita.persistencia.Salas;

/**
 *
 * @author akino
 */
public class SalasJpaController implements Serializable {

    public SalasJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Salas salas) {
        if (salas.getReservacionesCollection() == null) {
            salas.setReservacionesCollection(new ArrayList<Reservaciones>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Reservaciones> attachedReservacionesCollection = new ArrayList<Reservaciones>();
            for (Reservaciones reservacionesCollectionReservacionesToAttach : salas.getReservacionesCollection()) {
                reservacionesCollectionReservacionesToAttach = em.getReference(reservacionesCollectionReservacionesToAttach.getClass(), reservacionesCollectionReservacionesToAttach.getReservacionesPK(new ReservacionesPK()));
                attachedReservacionesCollection.add(reservacionesCollectionReservacionesToAttach);
            }
            salas.setReservacionesCollection(attachedReservacionesCollection);
            em.persist(salas);
            for (Reservaciones reservacionesCollectionReservaciones : salas.getReservacionesCollection()) {
                Salas oldSalasOfReservacionesCollectionReservaciones = reservacionesCollectionReservaciones.getSalas();
                reservacionesCollectionReservaciones.setSalas(salas);
                reservacionesCollectionReservaciones = em.merge(reservacionesCollectionReservaciones);
                if (oldSalasOfReservacionesCollectionReservaciones != null) {
                    oldSalasOfReservacionesCollectionReservaciones.getReservacionesCollection().remove(reservacionesCollectionReservaciones);
                    oldSalasOfReservacionesCollectionReservaciones = em.merge(oldSalasOfReservacionesCollectionReservaciones);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Salas salas) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Salas persistentSalas = em.find(Salas.class, salas.getIdSalas());
            Collection<Reservaciones> reservacionesCollectionOld = persistentSalas.getReservacionesCollection();
            Collection<Reservaciones> reservacionesCollectionNew = salas.getReservacionesCollection();
            List<String> illegalOrphanMessages = null;
            for (Reservaciones reservacionesCollectionOldReservaciones : reservacionesCollectionOld) {
                if (!reservacionesCollectionNew.contains(reservacionesCollectionOldReservaciones)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Reservaciones " + reservacionesCollectionOldReservaciones + " since its salas field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Reservaciones> attachedReservacionesCollectionNew = new ArrayList<Reservaciones>();
            for (Reservaciones reservacionesCollectionNewReservacionesToAttach : reservacionesCollectionNew) {
                reservacionesCollectionNewReservacionesToAttach = em.getReference(reservacionesCollectionNewReservacionesToAttach.getClass(), reservacionesCollectionNewReservacionesToAttach.getReservacionesPK(new ReservacionesPK()));
                attachedReservacionesCollectionNew.add(reservacionesCollectionNewReservacionesToAttach);
            }
            reservacionesCollectionNew = attachedReservacionesCollectionNew;
            salas.setReservacionesCollection(reservacionesCollectionNew);
            salas = em.merge(salas);
            for (Reservaciones reservacionesCollectionNewReservaciones : reservacionesCollectionNew) {
                if (!reservacionesCollectionOld.contains(reservacionesCollectionNewReservaciones)) {
                    Salas oldSalasOfReservacionesCollectionNewReservaciones = reservacionesCollectionNewReservaciones.getSalas();
                    reservacionesCollectionNewReservaciones.setSalas(salas);
                    reservacionesCollectionNewReservaciones = em.merge(reservacionesCollectionNewReservaciones);
                    if (oldSalasOfReservacionesCollectionNewReservaciones != null && !oldSalasOfReservacionesCollectionNewReservaciones.equals(salas)) {
                        oldSalasOfReservacionesCollectionNewReservaciones.getReservacionesCollection().remove(reservacionesCollectionNewReservaciones);
                        oldSalasOfReservacionesCollectionNewReservaciones = em.merge(oldSalasOfReservacionesCollectionNewReservaciones);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = salas.getIdSalas();
                if (findSalas(id) == null) {
                    throw new NonexistentEntityException("The salas with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Salas salas;
            try {
                salas = em.getReference(Salas.class, id);
                salas.getIdSalas();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The salas with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Reservaciones> reservacionesCollectionOrphanCheck = salas.getReservacionesCollection();
            for (Reservaciones reservacionesCollectionOrphanCheckReservaciones : reservacionesCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Salas (" + salas + ") cannot be destroyed since the Reservaciones " + reservacionesCollectionOrphanCheckReservaciones + " in its reservacionesCollection field has a non-nullable salas field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(salas);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Salas> findSalasEntities() {
        return findSalasEntities(true, -1, -1);
    }

    public List<Salas> findSalasEntities(int maxResults, int firstResult) {
        return findSalasEntities(false, maxResults, firstResult);
    }

    private List<Salas> findSalasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Salas.class));
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

    public Salas findSalas(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Salas.class, id);
        } finally {
            em.close();
        }
    }

    public int getSalasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Salas> rt = cq.from(Salas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
