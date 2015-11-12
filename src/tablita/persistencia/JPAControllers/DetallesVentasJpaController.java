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
import tablita.persistencia.DetallesVentas;
import tablita.persistencia.JPAControllers.exceptions.NonexistentEntityException;
import tablita.persistencia.Productos;
import tablita.persistencia.Ventas;

/**
 *
 * @author akino
 */
public class DetallesVentasJpaController implements Serializable {

    public DetallesVentasJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DetallesVentas detallesVentas) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Productos idProductos = detallesVentas.getIdProductos();
            if (idProductos != null) {
                idProductos = em.getReference(idProductos.getClass(), idProductos.getIdProductos());
                detallesVentas.setIdProductos(idProductos);
            }
            Ventas idVentas = detallesVentas.getIdVentas();
            if (idVentas != null) {
                idVentas = em.getReference(idVentas.getClass(), idVentas.getIdVentas());
                detallesVentas.setIdVentas(idVentas);
            }
            em.persist(detallesVentas);
            if (idProductos != null) {
                idProductos.getDetallesVentasCollection().add(detallesVentas);
                idProductos = em.merge(idProductos);
            }
            if (idVentas != null) {
                idVentas.getDetallesVentasCollection().add(detallesVentas);
                idVentas = em.merge(idVentas);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(DetallesVentas detallesVentas) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DetallesVentas persistentDetallesVentas = em.find(DetallesVentas.class, detallesVentas.getIdDetallesVentas());
            Productos idProductosOld = persistentDetallesVentas.getIdProductos();
            Productos idProductosNew = detallesVentas.getIdProductos();
            Ventas idVentasOld = persistentDetallesVentas.getIdVentas();
            Ventas idVentasNew = detallesVentas.getIdVentas();
            if (idProductosNew != null) {
                idProductosNew = em.getReference(idProductosNew.getClass(), idProductosNew.getIdProductos());
                detallesVentas.setIdProductos(idProductosNew);
            }
            if (idVentasNew != null) {
                idVentasNew = em.getReference(idVentasNew.getClass(), idVentasNew.getIdVentas());
                detallesVentas.setIdVentas(idVentasNew);
            }
            detallesVentas = em.merge(detallesVentas);
            if (idProductosOld != null && !idProductosOld.equals(idProductosNew)) {
                idProductosOld.getDetallesVentasCollection().remove(detallesVentas);
                idProductosOld = em.merge(idProductosOld);
            }
            if (idProductosNew != null && !idProductosNew.equals(idProductosOld)) {
                idProductosNew.getDetallesVentasCollection().add(detallesVentas);
                idProductosNew = em.merge(idProductosNew);
            }
            if (idVentasOld != null && !idVentasOld.equals(idVentasNew)) {
                idVentasOld.getDetallesVentasCollection().remove(detallesVentas);
                idVentasOld = em.merge(idVentasOld);
            }
            if (idVentasNew != null && !idVentasNew.equals(idVentasOld)) {
                idVentasNew.getDetallesVentasCollection().add(detallesVentas);
                idVentasNew = em.merge(idVentasNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = detallesVentas.getIdDetallesVentas();
                if (findDetallesVentas(id) == null) {
                    throw new NonexistentEntityException("The detallesVentas with id " + id + " no longer exists.");
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
            DetallesVentas detallesVentas;
            try {
                detallesVentas = em.getReference(DetallesVentas.class, id);
                detallesVentas.getIdDetallesVentas();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The detallesVentas with id " + id + " no longer exists.", enfe);
            }
            Productos idProductos = detallesVentas.getIdProductos();
            if (idProductos != null) {
                idProductos.getDetallesVentasCollection().remove(detallesVentas);
                idProductos = em.merge(idProductos);
            }
            Ventas idVentas = detallesVentas.getIdVentas();
            if (idVentas != null) {
                idVentas.getDetallesVentasCollection().remove(detallesVentas);
                idVentas = em.merge(idVentas);
            }
            em.remove(detallesVentas);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<DetallesVentas> findDetallesVentasEntities() {
        return findDetallesVentasEntities(true, -1, -1);
    }

    public List<DetallesVentas> findDetallesVentasEntities(int maxResults, int firstResult) {
        return findDetallesVentasEntities(false, maxResults, firstResult);
    }

    private List<DetallesVentas> findDetallesVentasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DetallesVentas.class));
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

    public DetallesVentas findDetallesVentas(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DetallesVentas.class, id);
        } finally {
            em.close();
        }
    }

    public int getDetallesVentasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DetallesVentas> rt = cq.from(DetallesVentas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
