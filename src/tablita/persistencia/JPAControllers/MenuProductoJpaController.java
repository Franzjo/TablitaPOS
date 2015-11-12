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
import tablita.persistencia.JPAControllers.exceptions.NonexistentEntityException;
import tablita.persistencia.Productos;
import tablita.persistencia.Menu;
import tablita.persistencia.MenuProducto;

/**
 *
 * @author akino
 */
public class MenuProductoJpaController implements Serializable {

    public MenuProductoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(MenuProducto menuProducto) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Productos idProductos = menuProducto.getIdProductos();
            if (idProductos != null) {
                idProductos = em.getReference(idProductos.getClass(), idProductos.getIdProductos());
                menuProducto.setIdProductos(idProductos);
            }
            Menu idMenu = menuProducto.getIdMenu();
            if (idMenu != null) {
                idMenu = em.getReference(idMenu.getClass(), idMenu.getIdMenu());
                menuProducto.setIdMenu(idMenu);
            }
            em.persist(menuProducto);
            if (idProductos != null) {
                idProductos.getMenuProductoCollection().add(menuProducto);
                idProductos = em.merge(idProductos);
            }
            if (idMenu != null) {
                idMenu.getMenuProductoCollection().add(menuProducto);
                idMenu = em.merge(idMenu);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(MenuProducto menuProducto) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MenuProducto persistentMenuProducto = em.find(MenuProducto.class, menuProducto.getIdMenuProducto());
            Productos idProductosOld = persistentMenuProducto.getIdProductos();
            Productos idProductosNew = menuProducto.getIdProductos();
            Menu idMenuOld = persistentMenuProducto.getIdMenu();
            Menu idMenuNew = menuProducto.getIdMenu();
            if (idProductosNew != null) {
                idProductosNew = em.getReference(idProductosNew.getClass(), idProductosNew.getIdProductos());
                menuProducto.setIdProductos(idProductosNew);
            }
            if (idMenuNew != null) {
                idMenuNew = em.getReference(idMenuNew.getClass(), idMenuNew.getIdMenu());
                menuProducto.setIdMenu(idMenuNew);
            }
            menuProducto = em.merge(menuProducto);
            if (idProductosOld != null && !idProductosOld.equals(idProductosNew)) {
                idProductosOld.getMenuProductoCollection().remove(menuProducto);
                idProductosOld = em.merge(idProductosOld);
            }
            if (idProductosNew != null && !idProductosNew.equals(idProductosOld)) {
                idProductosNew.getMenuProductoCollection().add(menuProducto);
                idProductosNew = em.merge(idProductosNew);
            }
            if (idMenuOld != null && !idMenuOld.equals(idMenuNew)) {
                idMenuOld.getMenuProductoCollection().remove(menuProducto);
                idMenuOld = em.merge(idMenuOld);
            }
            if (idMenuNew != null && !idMenuNew.equals(idMenuOld)) {
                idMenuNew.getMenuProductoCollection().add(menuProducto);
                idMenuNew = em.merge(idMenuNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = menuProducto.getIdMenuProducto();
                if (findMenuProducto(id) == null) {
                    throw new NonexistentEntityException("The menuProducto with id " + id + " no longer exists.");
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
            MenuProducto menuProducto;
            try {
                menuProducto = em.getReference(MenuProducto.class, id);
                menuProducto.getIdMenuProducto();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The menuProducto with id " + id + " no longer exists.", enfe);
            }
            Productos idProductos = menuProducto.getIdProductos();
            if (idProductos != null) {
                idProductos.getMenuProductoCollection().remove(menuProducto);
                idProductos = em.merge(idProductos);
            }
            Menu idMenu = menuProducto.getIdMenu();
            if (idMenu != null) {
                idMenu.getMenuProductoCollection().remove(menuProducto);
                idMenu = em.merge(idMenu);
            }
            em.remove(menuProducto);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<MenuProducto> findMenuProductoEntities() {
        return findMenuProductoEntities(true, -1, -1);
    }

    public List<MenuProducto> findMenuProductoEntities(int maxResults, int firstResult) {
        return findMenuProductoEntities(false, maxResults, firstResult);
    }

    private List<MenuProducto> findMenuProductoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(MenuProducto.class));
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

    public MenuProducto findMenuProducto(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(MenuProducto.class, id);
        } finally {
            em.close();
        }
    }

    public int getMenuProductoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<MenuProducto> rt = cq.from(MenuProducto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
