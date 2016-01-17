/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tablita.persistencia.JPAControllers;

import tablita.persistencia.DetallesVentas;
import tablita.persistencia.JPAControllers.exceptions.IllegalOrphanException;
import tablita.persistencia.JPAControllers.exceptions.NonexistentEntityException;
import tablita.persistencia.MenuProducto;
import tablita.persistencia.Productos;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author akino
 */
public class ProductosJpaController implements Serializable {

    public ProductosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Productos productos) {
        if (productos.getDetallesVentasCollection() == null) {
            productos.setDetallesVentasCollection(new ArrayList<DetallesVentas>());
        }
        if (productos.getMenuProductoCollection() == null) {
            productos.setMenuProductoCollection(new ArrayList<MenuProducto>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<DetallesVentas> attachedDetallesVentasCollection = new ArrayList<DetallesVentas>();
            for (DetallesVentas detallesVentasCollectionDetallesVentasToAttach : productos.getDetallesVentasCollection()) {
                detallesVentasCollectionDetallesVentasToAttach = em.getReference(detallesVentasCollectionDetallesVentasToAttach.getClass(), detallesVentasCollectionDetallesVentasToAttach.getIdDetallesVentas());
                attachedDetallesVentasCollection.add(detallesVentasCollectionDetallesVentasToAttach);
            }
            productos.setDetallesVentasCollection(attachedDetallesVentasCollection);
            Collection<MenuProducto> attachedMenuProductoCollection = new ArrayList<MenuProducto>();
            for (MenuProducto menuProductoCollectionMenuProductoToAttach : productos.getMenuProductoCollection()) {
                menuProductoCollectionMenuProductoToAttach = em.getReference(menuProductoCollectionMenuProductoToAttach.getClass(), menuProductoCollectionMenuProductoToAttach.getIdMenuProducto());
                attachedMenuProductoCollection.add(menuProductoCollectionMenuProductoToAttach);
            }
            productos.setMenuProductoCollection(attachedMenuProductoCollection);
            em.persist(productos);
            for (DetallesVentas detallesVentasCollectionDetallesVentas : productos.getDetallesVentasCollection()) {
                Productos oldIdProductosOfDetallesVentasCollectionDetallesVentas = detallesVentasCollectionDetallesVentas.getIdProductos();
                detallesVentasCollectionDetallesVentas.setIdProductos(productos);
                detallesVentasCollectionDetallesVentas = em.merge(detallesVentasCollectionDetallesVentas);
                if (oldIdProductosOfDetallesVentasCollectionDetallesVentas != null) {
                    oldIdProductosOfDetallesVentasCollectionDetallesVentas.getDetallesVentasCollection().remove(detallesVentasCollectionDetallesVentas);
                    oldIdProductosOfDetallesVentasCollectionDetallesVentas = em.merge(oldIdProductosOfDetallesVentasCollectionDetallesVentas);
                }
            }
            for (MenuProducto menuProductoCollectionMenuProducto : productos.getMenuProductoCollection()) {
                Productos oldIdProductosOfMenuProductoCollectionMenuProducto = menuProductoCollectionMenuProducto.getIdProductos();
                menuProductoCollectionMenuProducto.setIdProductos(productos);
                menuProductoCollectionMenuProducto = em.merge(menuProductoCollectionMenuProducto);
                if (oldIdProductosOfMenuProductoCollectionMenuProducto != null) {
                    oldIdProductosOfMenuProductoCollectionMenuProducto.getMenuProductoCollection().remove(menuProductoCollectionMenuProducto);
                    oldIdProductosOfMenuProductoCollectionMenuProducto = em.merge(oldIdProductosOfMenuProductoCollectionMenuProducto);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Productos productos) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Productos persistentProductos = em.find(Productos.class, productos.getIdProductos());
            Collection<DetallesVentas> detallesVentasCollectionOld = persistentProductos.getDetallesVentasCollection();
            Collection<DetallesVentas> detallesVentasCollectionNew = productos.getDetallesVentasCollection();
            Collection<MenuProducto> menuProductoCollectionOld = persistentProductos.getMenuProductoCollection();
            Collection<MenuProducto> menuProductoCollectionNew = productos.getMenuProductoCollection();
            List<String> illegalOrphanMessages = null;
            for (DetallesVentas detallesVentasCollectionOldDetallesVentas : detallesVentasCollectionOld) {
                if (!detallesVentasCollectionNew.contains(detallesVentasCollectionOldDetallesVentas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DetallesVentas " + detallesVentasCollectionOldDetallesVentas + " since its idProductos field is not nullable.");
                }
            }
            for (MenuProducto menuProductoCollectionOldMenuProducto : menuProductoCollectionOld) {
                if (!menuProductoCollectionNew.contains(menuProductoCollectionOldMenuProducto)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain MenuProducto " + menuProductoCollectionOldMenuProducto + " since its idProductos field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<DetallesVentas> attachedDetallesVentasCollectionNew = new ArrayList<DetallesVentas>();

            //CAMBIO AQUII
            if(detallesVentasCollectionNew != null){
                for (DetallesVentas detallesVentasCollectionNewDetallesVentasToAttach : detallesVentasCollectionNew) {
                    detallesVentasCollectionNewDetallesVentasToAttach = em.getReference(detallesVentasCollectionNewDetallesVentasToAttach.getClass(), detallesVentasCollectionNewDetallesVentasToAttach.getIdDetallesVentas());
                    attachedDetallesVentasCollectionNew.add(detallesVentasCollectionNewDetallesVentasToAttach);
                }
            }
            detallesVentasCollectionNew = attachedDetallesVentasCollectionNew;
            productos.setDetallesVentasCollection(detallesVentasCollectionNew);
            Collection<MenuProducto> attachedMenuProductoCollectionNew = new ArrayList<MenuProducto>();
            //aqui
            if(menuProductoCollectionNew != null){
                for (MenuProducto menuProductoCollectionNewMenuProductoToAttach : menuProductoCollectionNew) {
                    menuProductoCollectionNewMenuProductoToAttach = em.getReference(menuProductoCollectionNewMenuProductoToAttach.getClass(), menuProductoCollectionNewMenuProductoToAttach.getIdMenuProducto());
                    attachedMenuProductoCollectionNew.add(menuProductoCollectionNewMenuProductoToAttach);
                }
            }
            menuProductoCollectionNew = attachedMenuProductoCollectionNew;
            productos.setMenuProductoCollection(menuProductoCollectionNew);
            productos = em.merge(productos);

            if(detallesVentasCollectionNew !=null && menuProductoCollectionNew != null){


                for (DetallesVentas detallesVentasCollectionNewDetallesVentas : detallesVentasCollectionNew) {
                    if (!detallesVentasCollectionOld.contains(detallesVentasCollectionNewDetallesVentas)) {
                        Productos oldIdProductosOfDetallesVentasCollectionNewDetallesVentas = detallesVentasCollectionNewDetallesVentas.getIdProductos();
                        detallesVentasCollectionNewDetallesVentas.setIdProductos(productos);
                        detallesVentasCollectionNewDetallesVentas = em.merge(detallesVentasCollectionNewDetallesVentas);
                        if (oldIdProductosOfDetallesVentasCollectionNewDetallesVentas != null && !oldIdProductosOfDetallesVentasCollectionNewDetallesVentas.equals(productos)) {
                            oldIdProductosOfDetallesVentasCollectionNewDetallesVentas.getDetallesVentasCollection().remove(detallesVentasCollectionNewDetallesVentas);
                            oldIdProductosOfDetallesVentasCollectionNewDetallesVentas = em.merge(oldIdProductosOfDetallesVentasCollectionNewDetallesVentas);
                        }
                    }
                }
                for (MenuProducto menuProductoCollectionNewMenuProducto : menuProductoCollectionNew) {
                    if (!menuProductoCollectionOld.contains(menuProductoCollectionNewMenuProducto)) {
                        Productos oldIdProductosOfMenuProductoCollectionNewMenuProducto = menuProductoCollectionNewMenuProducto.getIdProductos();
                        menuProductoCollectionNewMenuProducto.setIdProductos(productos);
                        menuProductoCollectionNewMenuProducto = em.merge(menuProductoCollectionNewMenuProducto);
                        if (oldIdProductosOfMenuProductoCollectionNewMenuProducto != null && !oldIdProductosOfMenuProductoCollectionNewMenuProducto.equals(productos)) {
                            oldIdProductosOfMenuProductoCollectionNewMenuProducto.getMenuProductoCollection().remove(menuProductoCollectionNewMenuProducto);
                            oldIdProductosOfMenuProductoCollectionNewMenuProducto = em.merge(oldIdProductosOfMenuProductoCollectionNewMenuProducto);
                        }
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = productos.getIdProductos();
                if (findProductos(id) == null) {
                    throw new NonexistentEntityException("The productos with id " + id + " no longer exists.");
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
            Productos productos;
            try {
                productos = em.getReference(Productos.class, id);
                productos.getIdProductos();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The productos with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<DetallesVentas> detallesVentasCollectionOrphanCheck = productos.getDetallesVentasCollection();
            for (DetallesVentas detallesVentasCollectionOrphanCheckDetallesVentas : detallesVentasCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Productos (" + productos + ") cannot be destroyed since the DetallesVentas " + detallesVentasCollectionOrphanCheckDetallesVentas + " in its detallesVentasCollection field has a non-nullable idProductos field.");
            }
            Collection<MenuProducto> menuProductoCollectionOrphanCheck = productos.getMenuProductoCollection();
            for (MenuProducto menuProductoCollectionOrphanCheckMenuProducto : menuProductoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Productos (" + productos + ") cannot be destroyed since the MenuProducto " + menuProductoCollectionOrphanCheckMenuProducto + " in its menuProductoCollection field has a non-nullable idProductos field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(productos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Productos> findProductosEntities() {
        return findProductosEntities(true, -1, -1);
    }

    public List<Productos> findProductosEntities(int maxResults, int firstResult) {
        return findProductosEntities(false, maxResults, firstResult);
    }

    private List<Productos> findProductosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Productos.class));
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

    public Productos findProductos(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Productos.class, id);
        } finally {
            em.close();
        }
    }

    public int getProductosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Productos> rt = cq.from(Productos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public List<Productos> getProductosByTipo(String tipo){
        EntityManager em = getEntityManager();
        try{
            TypedQuery<Productos> query = em.createNamedQuery("Productos.findByTipo",Productos.class);
            query.setParameter("tipo",tipo);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

}
