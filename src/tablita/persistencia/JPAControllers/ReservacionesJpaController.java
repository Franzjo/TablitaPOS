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
import tablita.persistencia.Salas;
import tablita.persistencia.Mesas;
import tablita.persistencia.Clientes;
import tablita.persistencia.JPAControllers.exceptions.NonexistentEntityException;
import tablita.persistencia.JPAControllers.exceptions.PreexistingEntityException;
import tablita.persistencia.Reservaciones;
import tablita.persistencia.ReservacionesPK;

/**
 *
 * @author akino
 */
public class ReservacionesJpaController implements Serializable {

    public ReservacionesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Reservaciones reservaciones) throws PreexistingEntityException, Exception {
        if (reservaciones.getReservacionesPK() == null) {
            reservaciones.setReservacionesPK(new ReservacionesPK());
        }
        reservaciones.getReservacionesPK().setIdSalas(reservaciones.getSalas().getIdSalas());
        reservaciones.getReservacionesPK().setIdClientes(reservaciones.getClientes().getIdClientes());
        reservaciones.getReservacionesPK().setIdMesa(reservaciones.getMesas().getIdMesa());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Salas salas = reservaciones.getSalas();
            if (salas != null) {
                salas = em.getReference(salas.getClass(), salas.getIdSalas());
                reservaciones.setSalas(salas);
            }
            Mesas mesas = reservaciones.getMesas();
            if (mesas != null) {
                mesas = em.getReference(mesas.getClass(), mesas.getIdMesa());
                reservaciones.setMesas(mesas);
            }
            Clientes clientes = reservaciones.getClientes();
            if (clientes != null) {
                clientes = em.getReference(clientes.getClass(), clientes.getIdClientes());
                reservaciones.setClientes(clientes);
            }
            em.persist(reservaciones);
            if (salas != null) {
                salas.getReservacionesCollection().add(reservaciones);
                salas = em.merge(salas);
            }
            if (mesas != null) {
                mesas.getReservacionesCollection().add(reservaciones);
                mesas = em.merge(mesas);
            }
            if (clientes != null) {
                clientes.getReservacionesCollection().add(reservaciones);
                clientes = em.merge(clientes);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findReservaciones(reservaciones.getReservacionesPK()) != null) {
                throw new PreexistingEntityException("Reservaciones " + reservaciones + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Reservaciones reservaciones) throws NonexistentEntityException, Exception {
        reservaciones.getReservacionesPK().setIdSalas(reservaciones.getSalas().getIdSalas());
        reservaciones.getReservacionesPK().setIdClientes(reservaciones.getClientes().getIdClientes());
        reservaciones.getReservacionesPK().setIdMesa(reservaciones.getMesas().getIdMesa());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Reservaciones persistentReservaciones = em.find(Reservaciones.class, reservaciones.getReservacionesPK());
            Salas salasOld = persistentReservaciones.getSalas();
            Salas salasNew = reservaciones.getSalas();
            Mesas mesasOld = persistentReservaciones.getMesas();
            Mesas mesasNew = reservaciones.getMesas();
            Clientes clientesOld = persistentReservaciones.getClientes();
            Clientes clientesNew = reservaciones.getClientes();
            if (salasNew != null) {
                salasNew = em.getReference(salasNew.getClass(), salasNew.getIdSalas());
                reservaciones.setSalas(salasNew);
            }
            if (mesasNew != null) {
                mesasNew = em.getReference(mesasNew.getClass(), mesasNew.getIdMesa());
                reservaciones.setMesas(mesasNew);
            }
            if (clientesNew != null) {
                clientesNew = em.getReference(clientesNew.getClass(), clientesNew.getIdClientes());
                reservaciones.setClientes(clientesNew);
            }
            reservaciones = em.merge(reservaciones);
            if (salasOld != null && !salasOld.equals(salasNew)) {
                salasOld.getReservacionesCollection().remove(reservaciones);
                salasOld = em.merge(salasOld);
            }
            if (salasNew != null && !salasNew.equals(salasOld)) {
                salasNew.getReservacionesCollection().add(reservaciones);
                salasNew = em.merge(salasNew);
            }
            if (mesasOld != null && !mesasOld.equals(mesasNew)) {
                mesasOld.getReservacionesCollection().remove(reservaciones);
                mesasOld = em.merge(mesasOld);
            }
            if (mesasNew != null && !mesasNew.equals(mesasOld)) {
                mesasNew.getReservacionesCollection().add(reservaciones);
                mesasNew = em.merge(mesasNew);
            }
            if (clientesOld != null && !clientesOld.equals(clientesNew)) {
                clientesOld.getReservacionesCollection().remove(reservaciones);
                clientesOld = em.merge(clientesOld);
            }
            if (clientesNew != null && !clientesNew.equals(clientesOld)) {
                clientesNew.getReservacionesCollection().add(reservaciones);
                clientesNew = em.merge(clientesNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                ReservacionesPK id = reservaciones.getReservacionesPK();
                if (findReservaciones(id) == null) {
                    throw new NonexistentEntityException("The reservaciones with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(ReservacionesPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Reservaciones reservaciones;
            try {
                reservaciones = em.getReference(Reservaciones.class, id);
                reservaciones.getReservacionesPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The reservaciones with id " + id + " no longer exists.", enfe);
            }
            Salas salas = reservaciones.getSalas();
            if (salas != null) {
                salas.getReservacionesCollection().remove(reservaciones);
                salas = em.merge(salas);
            }
            Mesas mesas = reservaciones.getMesas();
            if (mesas != null) {
                mesas.getReservacionesCollection().remove(reservaciones);
                mesas = em.merge(mesas);
            }
            Clientes clientes = reservaciones.getClientes();
            if (clientes != null) {
                clientes.getReservacionesCollection().remove(reservaciones);
                clientes = em.merge(clientes);
            }
            em.remove(reservaciones);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Reservaciones> findReservacionesEntities() {
        return findReservacionesEntities(true, -1, -1);
    }

    public List<Reservaciones> findReservacionesEntities(int maxResults, int firstResult) {
        return findReservacionesEntities(false, maxResults, firstResult);
    }

    private List<Reservaciones> findReservacionesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Reservaciones.class));
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

    public Reservaciones findReservaciones(ReservacionesPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Reservaciones.class, id);
        } finally {
            em.close();
        }
    }

    public int getReservacionesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Reservaciones> rt = cq.from(Reservaciones.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
