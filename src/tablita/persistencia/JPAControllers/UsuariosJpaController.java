/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tablita.persistencia.JPAControllers;

import tablita.persistencia.JPAControllers.exceptions.IllegalOrphanException;
import tablita.persistencia.JPAControllers.exceptions.NonexistentEntityException;
import tablita.persistencia.Roles;
import tablita.persistencia.Usuarios;
import tablita.persistencia.Ventas;

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
public class UsuariosJpaController implements Serializable {

    public UsuariosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuarios usuarios) {
        if (usuarios.getVentasCollection() == null) {
            usuarios.setVentasCollection(new ArrayList<Ventas>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Roles idRol = usuarios.getIdRol();
            if (idRol != null) {
                idRol = em.getReference(idRol.getClass(), idRol.getIdRol());
                usuarios.setIdRol(idRol);
            }
            Collection<Ventas> attachedVentasCollection = new ArrayList<Ventas>();
            for (Ventas ventasCollectionVentasToAttach : usuarios.getVentasCollection()) {
                ventasCollectionVentasToAttach = em.getReference(ventasCollectionVentasToAttach.getClass(), ventasCollectionVentasToAttach.getIdVentas());
                attachedVentasCollection.add(ventasCollectionVentasToAttach);
            }
            usuarios.setVentasCollection(attachedVentasCollection);
            em.persist(usuarios);
            if (idRol != null) {
                idRol.getUsuariosCollection().add(usuarios);
                idRol = em.merge(idRol);
            }
            for (Ventas ventasCollectionVentas : usuarios.getVentasCollection()) {
                Usuarios oldIdUsuarioOfVentasCollectionVentas = ventasCollectionVentas.getIdUsuario();
                ventasCollectionVentas.setIdUsuario(usuarios);
                ventasCollectionVentas = em.merge(ventasCollectionVentas);
                if (oldIdUsuarioOfVentasCollectionVentas != null) {
                    oldIdUsuarioOfVentasCollectionVentas.getVentasCollection().remove(ventasCollectionVentas);
                    oldIdUsuarioOfVentasCollectionVentas = em.merge(oldIdUsuarioOfVentasCollectionVentas);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuarios usuarios) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuarios persistentUsuarios = em.find(Usuarios.class, usuarios.getIdUsuario());
            Roles idRolOld = persistentUsuarios.getIdRol();
            Roles idRolNew = usuarios.getIdRol();
            Collection<Ventas> ventasCollectionOld = persistentUsuarios.getVentasCollection();
            Collection<Ventas> ventasCollectionNew = usuarios.getVentasCollection();
            List<String> illegalOrphanMessages = null;
            for (Ventas ventasCollectionOldVentas : ventasCollectionOld) {
                if (!ventasCollectionNew.contains(ventasCollectionOldVentas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Ventas " + ventasCollectionOldVentas + " since its idUsuario field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idRolNew != null) {
                idRolNew = em.getReference(idRolNew.getClass(), idRolNew.getIdRol());
                usuarios.setIdRol(idRolNew);
            }
            Collection<Ventas> attachedVentasCollectionNew = new ArrayList<Ventas>();
            if(ventasCollectionNew != null){


                for (Ventas ventasCollectionNewVentasToAttach : ventasCollectionNew) {
                    ventasCollectionNewVentasToAttach = em.getReference(ventasCollectionNewVentasToAttach.getClass(), ventasCollectionNewVentasToAttach.getIdVentas());
                    attachedVentasCollectionNew.add(ventasCollectionNewVentasToAttach);
                }
                ventasCollectionNew = attachedVentasCollectionNew;
                usuarios.setVentasCollection(ventasCollectionNew);
            }
            usuarios = em.merge(usuarios);
            if (idRolOld != null && !idRolOld.equals(idRolNew)) {
                idRolOld.getUsuariosCollection().remove(usuarios);
                idRolOld = em.merge(idRolOld);
            }
            if (idRolNew != null && !idRolNew.equals(idRolOld)) {
                idRolNew.getUsuariosCollection().add(usuarios);
                idRolNew = em.merge(idRolNew);
            }
            if(ventasCollectionNew != null){

                for (Ventas ventasCollectionNewVentas : ventasCollectionNew) {
                    if (!ventasCollectionOld.contains(ventasCollectionNewVentas)) {
                        Usuarios oldIdUsuarioOfVentasCollectionNewVentas = ventasCollectionNewVentas.getIdUsuario();
                        ventasCollectionNewVentas.setIdUsuario(usuarios);
                        ventasCollectionNewVentas = em.merge(ventasCollectionNewVentas);
                        if (oldIdUsuarioOfVentasCollectionNewVentas != null && !oldIdUsuarioOfVentasCollectionNewVentas.equals(usuarios)) {
                            oldIdUsuarioOfVentasCollectionNewVentas.getVentasCollection().remove(ventasCollectionNewVentas);
                            oldIdUsuarioOfVentasCollectionNewVentas = em.merge(oldIdUsuarioOfVentasCollectionNewVentas);
                        }
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = usuarios.getIdUsuario();
                if (findUsuarios(id) == null) {
                    throw new NonexistentEntityException("The usuarios with id " + id + " no longer exists.");
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
            Usuarios usuarios;
            try {
                usuarios = em.getReference(Usuarios.class, id);
                usuarios.getIdUsuario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuarios with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Ventas> ventasCollectionOrphanCheck = usuarios.getVentasCollection();
            for (Ventas ventasCollectionOrphanCheckVentas : ventasCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuarios (" + usuarios + ") cannot be destroyed since the Ventas " + ventasCollectionOrphanCheckVentas + " in its ventasCollection field has a non-nullable idUsuario field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Roles idRol = usuarios.getIdRol();
            if (idRol != null) {
                idRol.getUsuariosCollection().remove(usuarios);
                idRol = em.merge(idRol);
            }
            em.remove(usuarios);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuarios> findUsuariosEntities() {
        return findUsuariosEntities(true, -1, -1);
    }

    public List<Usuarios> findUsuariosEntities(int maxResults, int firstResult) {
        return findUsuariosEntities(false, maxResults, firstResult);
    }

    private List<Usuarios> findUsuariosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuarios.class));
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

    public Usuarios findUsuarios(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuarios.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuariosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuarios> rt = cq.from(Usuarios.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public List<Usuarios> findByRole(Roles rol){
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Usuarios> empleados = em.createQuery("select u from Usuarios u where u.idRol = :rol",Usuarios.class);
            empleados.setParameter("rol", rol);
            return empleados.getResultList();
        } finally {
            em.close();
        }
    }

    public Usuarios findByCodigo(String codigo){
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Usuarios> query = em.createNamedQuery("Usuarios.findByCodigo",Usuarios.class);
            query.setParameter("codigo", codigo);
            return query.getSingleResult();
        }finally {
            em.close();
        }
    }

    public int getLastRegister(){
        EntityManager em = getEntityManager();
        try{
            TypedQuery<Usuarios> query = em.createQuery("select u from Usuarios u order by u.idUsuario desc", Usuarios.class);
            Usuarios u = query.setMaxResults(1).getResultList().get(0);
            return u.getIdUsuario() >= 0 ? u.getIdUsuario() : 0;
        } finally {
            em.close();
        }

    }
    
}
