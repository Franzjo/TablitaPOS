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

import tablita.persistencia.ReservacionesPK;
import tablita.persistencia.Ventas;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import tablita.persistencia.JPAControllers.exceptions.IllegalOrphanException;
import tablita.persistencia.JPAControllers.exceptions.NonexistentEntityException;
import tablita.persistencia.Mesas;
import tablita.persistencia.Reservaciones;

/**
 *
 * @author akino
 */
public class MesasJpaController implements Serializable {

    public MesasJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Mesas mesas) {
        if (mesas.getVentasCollection() == null) {
            mesas.setVentasCollection(new ArrayList<Ventas>());
        }
        if (mesas.getReservacionesCollection() == null) {
            mesas.setReservacionesCollection(new ArrayList<Reservaciones>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Ventas> attachedVentasCollection = new ArrayList<Ventas>();
            for (Ventas ventasCollectionVentasToAttach : mesas.getVentasCollection()) {
                ventasCollectionVentasToAttach = em.getReference(ventasCollectionVentasToAttach.getClass(), ventasCollectionVentasToAttach.getIdVentas());
                attachedVentasCollection.add(ventasCollectionVentasToAttach);
            }
            mesas.setVentasCollection(attachedVentasCollection);
            Collection<Reservaciones> attachedReservacionesCollection = new ArrayList<Reservaciones>();
            for (Reservaciones reservacionesCollectionReservacionesToAttach : mesas.getReservacionesCollection()) {
                reservacionesCollectionReservacionesToAttach = em.getReference(reservacionesCollectionReservacionesToAttach.getClass(), reservacionesCollectionReservacionesToAttach.getReservacionesPK(new ReservacionesPK()));
                attachedReservacionesCollection.add(reservacionesCollectionReservacionesToAttach);
            }
            mesas.setReservacionesCollection(attachedReservacionesCollection);
            em.persist(mesas);
            for (Ventas ventasCollectionVentas : mesas.getVentasCollection()) {
                Mesas oldIdMesaOfVentasCollectionVentas = ventasCollectionVentas.getIdMesa();
                ventasCollectionVentas.setIdMesa(mesas);
                ventasCollectionVentas = em.merge(ventasCollectionVentas);
                if (oldIdMesaOfVentasCollectionVentas != null) {
                    oldIdMesaOfVentasCollectionVentas.getVentasCollection().remove(ventasCollectionVentas);
                    oldIdMesaOfVentasCollectionVentas = em.merge(oldIdMesaOfVentasCollectionVentas);
                }
            }
            for (Reservaciones reservacionesCollectionReservaciones : mesas.getReservacionesCollection()) {
                Mesas oldMesasOfReservacionesCollectionReservaciones = reservacionesCollectionReservaciones.getMesas();
                reservacionesCollectionReservaciones.setMesas(mesas);
                reservacionesCollectionReservaciones = em.merge(reservacionesCollectionReservaciones);
                if (oldMesasOfReservacionesCollectionReservaciones != null) {
                    oldMesasOfReservacionesCollectionReservaciones.getReservacionesCollection().remove(reservacionesCollectionReservaciones);
                    oldMesasOfReservacionesCollectionReservaciones = em.merge(oldMesasOfReservacionesCollectionReservaciones);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Mesas mesas) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Mesas persistentMesas = em.find(Mesas.class, mesas.getIdMesa());
            Collection<Ventas> ventasCollectionOld = persistentMesas.getVentasCollection();
            Collection<Ventas> ventasCollectionNew = mesas.getVentasCollection();
            Collection<Reservaciones> reservacionesCollectionOld = persistentMesas.getReservacionesCollection();
            Collection<Reservaciones> reservacionesCollectionNew = mesas.getReservacionesCollection();
            List<String> illegalOrphanMessages = null;
            for (Ventas ventasCollectionOldVentas : ventasCollectionOld) {
                if (!ventasCollectionNew.contains(ventasCollectionOldVentas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Ventas " + ventasCollectionOldVentas + " since its idMesa field is not nullable.");
                }
            }
            for (Reservaciones reservacionesCollectionOldReservaciones : reservacionesCollectionOld) {
                if (!reservacionesCollectionNew.contains(reservacionesCollectionOldReservaciones)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Reservaciones " + reservacionesCollectionOldReservaciones + " since its mesas field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Ventas> attachedVentasCollectionNew = new ArrayList<Ventas>();
            for (Ventas ventasCollectionNewVentasToAttach : ventasCollectionNew) {
                ventasCollectionNewVentasToAttach = em.getReference(ventasCollectionNewVentasToAttach.getClass(), ventasCollectionNewVentasToAttach.getIdVentas());
                attachedVentasCollectionNew.add(ventasCollectionNewVentasToAttach);
            }
            ventasCollectionNew = attachedVentasCollectionNew;
            mesas.setVentasCollection(ventasCollectionNew);
            Collection<Reservaciones> attachedReservacionesCollectionNew = new ArrayList<Reservaciones>();
            for (Reservaciones reservacionesCollectionNewReservacionesToAttach : reservacionesCollectionNew) {
                reservacionesCollectionNewReservacionesToAttach = em.getReference(reservacionesCollectionNewReservacionesToAttach.getClass(), reservacionesCollectionNewReservacionesToAttach.getReservacionesPK(new ReservacionesPK()));
                attachedReservacionesCollectionNew.add(reservacionesCollectionNewReservacionesToAttach);
            }
            reservacionesCollectionNew = attachedReservacionesCollectionNew;
            mesas.setReservacionesCollection(reservacionesCollectionNew);
            mesas = em.merge(mesas);
            for (Ventas ventasCollectionNewVentas : ventasCollectionNew) {
                if (!ventasCollectionOld.contains(ventasCollectionNewVentas)) {
                    Mesas oldIdMesaOfVentasCollectionNewVentas = ventasCollectionNewVentas.getIdMesa();
                    ventasCollectionNewVentas.setIdMesa(mesas);
                    ventasCollectionNewVentas = em.merge(ventasCollectionNewVentas);
                    if (oldIdMesaOfVentasCollectionNewVentas != null && !oldIdMesaOfVentasCollectionNewVentas.equals(mesas)) {
                        oldIdMesaOfVentasCollectionNewVentas.getVentasCollection().remove(ventasCollectionNewVentas);
                        oldIdMesaOfVentasCollectionNewVentas = em.merge(oldIdMesaOfVentasCollectionNewVentas);
                    }
                }
            }
            for (Reservaciones reservacionesCollectionNewReservaciones : reservacionesCollectionNew) {
                if (!reservacionesCollectionOld.contains(reservacionesCollectionNewReservaciones)) {
                    Mesas oldMesasOfReservacionesCollectionNewReservaciones = reservacionesCollectionNewReservaciones.getMesas();
                    reservacionesCollectionNewReservaciones.setMesas(mesas);
                    reservacionesCollectionNewReservaciones = em.merge(reservacionesCollectionNewReservaciones);
                    if (oldMesasOfReservacionesCollectionNewReservaciones != null && !oldMesasOfReservacionesCollectionNewReservaciones.equals(mesas)) {
                        oldMesasOfReservacionesCollectionNewReservaciones.getReservacionesCollection().remove(reservacionesCollectionNewReservaciones);
                        oldMesasOfReservacionesCollectionNewReservaciones = em.merge(oldMesasOfReservacionesCollectionNewReservaciones);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = mesas.getIdMesa();
                if (findMesas(id) == null) {
                    throw new NonexistentEntityException("The mesas with id " + id + " no longer exists.");
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
            Mesas mesas;
            try {
                mesas = em.getReference(Mesas.class, id);
                mesas.getIdMesa();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The mesas with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Ventas> ventasCollectionOrphanCheck = mesas.getVentasCollection();
            for (Ventas ventasCollectionOrphanCheckVentas : ventasCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Mesas (" + mesas + ") cannot be destroyed since the Ventas " + ventasCollectionOrphanCheckVentas + " in its ventasCollection field has a non-nullable idMesa field.");
            }
            Collection<Reservaciones> reservacionesCollectionOrphanCheck = mesas.getReservacionesCollection();
            for (Reservaciones reservacionesCollectionOrphanCheckReservaciones : reservacionesCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Mesas (" + mesas + ") cannot be destroyed since the Reservaciones " + reservacionesCollectionOrphanCheckReservaciones + " in its reservacionesCollection field has a non-nullable mesas field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(mesas);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Mesas> findMesasEntities() {
        return findMesasEntities(true, -1, -1);
    }

    public List<Mesas> findMesasEntities(int maxResults, int firstResult) {
        return findMesasEntities(false, maxResults, firstResult);
    }

    private List<Mesas> findMesasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Mesas.class));
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

    public Mesas findMesas(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Mesas.class, id);
        } finally {
            em.close();
        }
    }

    public int getMesasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Mesas> rt = cq.from(Mesas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
