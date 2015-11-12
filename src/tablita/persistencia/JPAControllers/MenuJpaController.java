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
import tablita.persistencia.MenuProducto;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import tablita.persistencia.JPAControllers.exceptions.IllegalOrphanException;
import tablita.persistencia.JPAControllers.exceptions.NonexistentEntityException;
import tablita.persistencia.Menu;

/**
 *
 * @author akino
 */
public class MenuJpaController implements Serializable {

    public MenuJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Menu menu) {
        if (menu.getMenuProductoCollection() == null) {
            menu.setMenuProductoCollection(new ArrayList<MenuProducto>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<MenuProducto> attachedMenuProductoCollection = new ArrayList<MenuProducto>();
            for (MenuProducto menuProductoCollectionMenuProductoToAttach : menu.getMenuProductoCollection()) {
                menuProductoCollectionMenuProductoToAttach = em.getReference(menuProductoCollectionMenuProductoToAttach.getClass(), menuProductoCollectionMenuProductoToAttach.getIdMenuProducto());
                attachedMenuProductoCollection.add(menuProductoCollectionMenuProductoToAttach);
            }
            menu.setMenuProductoCollection(attachedMenuProductoCollection);
            em.persist(menu);
            for (MenuProducto menuProductoCollectionMenuProducto : menu.getMenuProductoCollection()) {
                Menu oldIdMenuOfMenuProductoCollectionMenuProducto = menuProductoCollectionMenuProducto.getIdMenu();
                menuProductoCollectionMenuProducto.setIdMenu(menu);
                menuProductoCollectionMenuProducto = em.merge(menuProductoCollectionMenuProducto);
                if (oldIdMenuOfMenuProductoCollectionMenuProducto != null) {
                    oldIdMenuOfMenuProductoCollectionMenuProducto.getMenuProductoCollection().remove(menuProductoCollectionMenuProducto);
                    oldIdMenuOfMenuProductoCollectionMenuProducto = em.merge(oldIdMenuOfMenuProductoCollectionMenuProducto);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Menu menu) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Menu persistentMenu = em.find(Menu.class, menu.getIdMenu());
            Collection<MenuProducto> menuProductoCollectionOld = persistentMenu.getMenuProductoCollection();
            Collection<MenuProducto> menuProductoCollectionNew = menu.getMenuProductoCollection();
            List<String> illegalOrphanMessages = null;
            for (MenuProducto menuProductoCollectionOldMenuProducto : menuProductoCollectionOld) {
                if (!menuProductoCollectionNew.contains(menuProductoCollectionOldMenuProducto)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain MenuProducto " + menuProductoCollectionOldMenuProducto + " since its idMenu field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<MenuProducto> attachedMenuProductoCollectionNew = new ArrayList<MenuProducto>();
            for (MenuProducto menuProductoCollectionNewMenuProductoToAttach : menuProductoCollectionNew) {
                menuProductoCollectionNewMenuProductoToAttach = em.getReference(menuProductoCollectionNewMenuProductoToAttach.getClass(), menuProductoCollectionNewMenuProductoToAttach.getIdMenuProducto());
                attachedMenuProductoCollectionNew.add(menuProductoCollectionNewMenuProductoToAttach);
            }
            menuProductoCollectionNew = attachedMenuProductoCollectionNew;
            menu.setMenuProductoCollection(menuProductoCollectionNew);
            menu = em.merge(menu);
            for (MenuProducto menuProductoCollectionNewMenuProducto : menuProductoCollectionNew) {
                if (!menuProductoCollectionOld.contains(menuProductoCollectionNewMenuProducto)) {
                    Menu oldIdMenuOfMenuProductoCollectionNewMenuProducto = menuProductoCollectionNewMenuProducto.getIdMenu();
                    menuProductoCollectionNewMenuProducto.setIdMenu(menu);
                    menuProductoCollectionNewMenuProducto = em.merge(menuProductoCollectionNewMenuProducto);
                    if (oldIdMenuOfMenuProductoCollectionNewMenuProducto != null && !oldIdMenuOfMenuProductoCollectionNewMenuProducto.equals(menu)) {
                        oldIdMenuOfMenuProductoCollectionNewMenuProducto.getMenuProductoCollection().remove(menuProductoCollectionNewMenuProducto);
                        oldIdMenuOfMenuProductoCollectionNewMenuProducto = em.merge(oldIdMenuOfMenuProductoCollectionNewMenuProducto);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = menu.getIdMenu();
                if (findMenu(id) == null) {
                    throw new NonexistentEntityException("The menu with id " + id + " no longer exists.");
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
            Menu menu;
            try {
                menu = em.getReference(Menu.class, id);
                menu.getIdMenu();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The menu with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<MenuProducto> menuProductoCollectionOrphanCheck = menu.getMenuProductoCollection();
            for (MenuProducto menuProductoCollectionOrphanCheckMenuProducto : menuProductoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Menu (" + menu + ") cannot be destroyed since the MenuProducto " + menuProductoCollectionOrphanCheckMenuProducto + " in its menuProductoCollection field has a non-nullable idMenu field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(menu);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Menu> findMenuEntities() {
        return findMenuEntities(true, -1, -1);
    }

    public List<Menu> findMenuEntities(int maxResults, int firstResult) {
        return findMenuEntities(false, maxResults, firstResult);
    }

    private List<Menu> findMenuEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Menu.class));
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

    public Menu findMenu(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Menu.class, id);
        } finally {
            em.close();
        }
    }

    public int getMenuCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Menu> rt = cq.from(Menu.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
