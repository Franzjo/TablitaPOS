/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tablita.persistencia.JPAControllers;

import java.io.Serializable;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import tablita.persistencia.Reservaciones;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import tablita.persistencia.Clientes;
import tablita.persistencia.JPAControllers.exceptions.IllegalOrphanException;
import tablita.persistencia.JPAControllers.exceptions.NonexistentEntityException;
import tablita.persistencia.ReservacionesPK;

/**
 *
 * @author akino
 */
public class ClientesJpaController implements Serializable {

    public ClientesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Clientes clientes) {
        if (clientes.getReservacionesCollection() == null) {
            clientes.setReservacionesCollection(new ArrayList<Reservaciones>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Reservaciones> attachedReservacionesCollection = new ArrayList<Reservaciones>();
            for (Reservaciones reservacionesCollectionReservacionesToAttach : clientes.getReservacionesCollection()) {
                reservacionesCollectionReservacionesToAttach = em.getReference(reservacionesCollectionReservacionesToAttach.getClass(), reservacionesCollectionReservacionesToAttach.getReservacionesPK(new ReservacionesPK()));
                attachedReservacionesCollection.add(reservacionesCollectionReservacionesToAttach);
            }
            clientes.setReservacionesCollection(attachedReservacionesCollection);
            em.persist(clientes);
            for (Reservaciones reservacionesCollectionReservaciones : clientes.getReservacionesCollection()) {
                Clientes oldClientesOfReservacionesCollectionReservaciones = reservacionesCollectionReservaciones.getClientes();
                reservacionesCollectionReservaciones.setClientes(clientes);
                reservacionesCollectionReservaciones = em.merge(reservacionesCollectionReservaciones);
                if (oldClientesOfReservacionesCollectionReservaciones != null) {
                    oldClientesOfReservacionesCollectionReservaciones.getReservacionesCollection().remove(reservacionesCollectionReservaciones);
                    oldClientesOfReservacionesCollectionReservaciones = em.merge(oldClientesOfReservacionesCollectionReservaciones);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Clientes clientes) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Clientes persistentClientes = em.find(Clientes.class, clientes.getIdClientes());
            Collection<Reservaciones> reservacionesCollectionOld = persistentClientes.getReservacionesCollection();
            Collection<Reservaciones> reservacionesCollectionNew = clientes.getReservacionesCollection();
            List<String> illegalOrphanMessages = null;
            for (Reservaciones reservacionesCollectionOldReservaciones : reservacionesCollectionOld) {
                if (!reservacionesCollectionNew.contains(reservacionesCollectionOldReservaciones)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Reservaciones " + reservacionesCollectionOldReservaciones + " since its clientes field is not nullable.");
                }
            }

            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Reservaciones> attachedReservacionesCollectionNew = new ArrayList<Reservaciones>();

            //Cambio

            if (reservacionesCollectionNew != null) {
                for (Reservaciones reservacionesCollectionNewReservacionesToAttach : reservacionesCollectionNew) {
                    reservacionesCollectionNewReservacionesToAttach = em.getReference(reservacionesCollectionNewReservacionesToAttach.getClass(), reservacionesCollectionNewReservacionesToAttach.getReservacionesPK(new ReservacionesPK()));
                    attachedReservacionesCollectionNew.add(reservacionesCollectionNewReservacionesToAttach);
                }
            }
            reservacionesCollectionNew = attachedReservacionesCollectionNew;
            clientes.setReservacionesCollection(reservacionesCollectionNew);
            clientes = em.merge(clientes);
            //Cambio

            if (reservacionesCollectionNew != null) {
                for (Reservaciones reservacionesCollectionNewReservaciones : reservacionesCollectionNew) {
                    if (!reservacionesCollectionOld.contains(reservacionesCollectionNewReservaciones)) {
                        Clientes oldClientesOfReservacionesCollectionNewReservaciones = reservacionesCollectionNewReservaciones.getClientes();
                        reservacionesCollectionNewReservaciones.setClientes(clientes);
                        reservacionesCollectionNewReservaciones = em.merge(reservacionesCollectionNewReservaciones);
                        if (oldClientesOfReservacionesCollectionNewReservaciones != null && !oldClientesOfReservacionesCollectionNewReservaciones.equals(clientes)) {
                            oldClientesOfReservacionesCollectionNewReservaciones.getReservacionesCollection().remove(reservacionesCollectionNewReservaciones);
                            oldClientesOfReservacionesCollectionNewReservaciones = em.merge(oldClientesOfReservacionesCollectionNewReservaciones);
                        }
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = clientes.getIdClientes();
                if (findClientes(id) == null) {
                    throw new NonexistentEntityException("The clientes with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void  destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Clientes clientes;
            try {
                clientes = em.getReference(Clientes.class, id);
                clientes.getIdClientes();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The clientes with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Reservaciones> reservacionesCollectionOrphanCheck = clientes.getReservacionesCollection();
            for (Reservaciones reservacionesCollectionOrphanCheckReservaciones : reservacionesCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Clientes (" + clientes + ") cannot be destroyed since the Reservaciones " + reservacionesCollectionOrphanCheckReservaciones + " in its reservacionesCollection field has a non-nullable clientes field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(clientes);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Clientes> findClientesEntities() {
        return findClientesEntities(true, - 1, -1);
    }

    public List<Clientes> findClientesEntities(int maxResults, int firstResult) {
        return findClientesEntities(false, maxResults, firstResult);
    }

    private List<Clientes> findClientesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Clientes.class));
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

    public Clientes findClientes(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Clientes.class, id);
        } finally {
            em.close();
        }
    }

    public int getClientesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Clientes> rt = cq.from(Clientes.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}
