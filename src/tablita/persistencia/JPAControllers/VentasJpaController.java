/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tablita.persistencia.JPAControllers;

import tablita.persistencia.DetallesVentas;
import tablita.persistencia.JPAControllers.exceptions.IllegalOrphanException;
import tablita.persistencia.JPAControllers.exceptions.NonexistentEntityException;
import tablita.persistencia.Mesas;
import tablita.persistencia.Usuarios;
import tablita.persistencia.Ventas;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.*;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.*;

/**
 *
 * @author akino
 */
public class VentasJpaController implements Serializable {

    public VentasJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Ventas ventas) {
        if (ventas.getDetallesVentasCollection() == null) {
            ventas.setDetallesVentasCollection(new ArrayList<DetallesVentas>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuarios idUsuario = ventas.getIdUsuario();
            if (idUsuario != null) {
                idUsuario = em.getReference(idUsuario.getClass(), idUsuario.getIdUsuario());
                ventas.setIdUsuario(idUsuario);
            }
            Mesas idMesa = ventas.getIdMesa();
            if (idMesa != null) {
                idMesa = em.getReference(idMesa.getClass(), idMesa.getIdMesa());
                ventas.setIdMesa(idMesa);
            }
            Collection<DetallesVentas> attachedDetallesVentasCollection = new ArrayList<DetallesVentas>();
            for (DetallesVentas detallesVentasCollectionDetallesVentasToAttach : ventas.getDetallesVentasCollection()) {
                detallesVentasCollectionDetallesVentasToAttach = em.getReference(detallesVentasCollectionDetallesVentasToAttach.getClass(), detallesVentasCollectionDetallesVentasToAttach.getIdDetallesVentas());
                attachedDetallesVentasCollection.add(detallesVentasCollectionDetallesVentasToAttach);
            }
            ventas.setDetallesVentasCollection(attachedDetallesVentasCollection);
            em.persist(ventas);
            if (idUsuario != null) {
                idUsuario.getVentasCollection().add(ventas);
                idUsuario = em.merge(idUsuario);
            }
            if (idMesa != null) {
                idMesa.getVentasCollection().add(ventas);
                idMesa = em.merge(idMesa);
            }
            for (DetallesVentas detallesVentasCollectionDetallesVentas : ventas.getDetallesVentasCollection()) {
                Ventas oldIdVentasOfDetallesVentasCollectionDetallesVentas = detallesVentasCollectionDetallesVentas.getIdVentas();
                detallesVentasCollectionDetallesVentas.setIdVentas(ventas);
                detallesVentasCollectionDetallesVentas = em.merge(detallesVentasCollectionDetallesVentas);
                if (oldIdVentasOfDetallesVentasCollectionDetallesVentas != null) {
                    oldIdVentasOfDetallesVentasCollectionDetallesVentas.getDetallesVentasCollection().remove(detallesVentasCollectionDetallesVentas);
                    oldIdVentasOfDetallesVentasCollectionDetallesVentas = em.merge(oldIdVentasOfDetallesVentasCollectionDetallesVentas);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Ventas ventas) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ventas persistentVentas = em.find(Ventas.class, ventas.getIdVentas());
            Usuarios idUsuarioOld = persistentVentas.getIdUsuario();
            Usuarios idUsuarioNew = ventas.getIdUsuario();
            Mesas idMesaOld = persistentVentas.getIdMesa();
            Mesas idMesaNew = ventas.getIdMesa();
            Collection<DetallesVentas> detallesVentasCollectionOld = persistentVentas.getDetallesVentasCollection();
            Collection<DetallesVentas> detallesVentasCollectionNew = ventas.getDetallesVentasCollection();
            List<String> illegalOrphanMessages = null;
            for (DetallesVentas detallesVentasCollectionOldDetallesVentas : detallesVentasCollectionOld) {
                if (!detallesVentasCollectionNew.contains(detallesVentasCollectionOldDetallesVentas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DetallesVentas " + detallesVentasCollectionOldDetallesVentas + " since its idVentas field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idUsuarioNew != null) {
                idUsuarioNew = em.getReference(idUsuarioNew.getClass(), idUsuarioNew.getIdUsuario());
                ventas.setIdUsuario(idUsuarioNew);
            }
            if (idMesaNew != null) {
                idMesaNew = em.getReference(idMesaNew.getClass(), idMesaNew.getIdMesa());
                ventas.setIdMesa(idMesaNew);
            }
            Collection<DetallesVentas> attachedDetallesVentasCollectionNew = new ArrayList<DetallesVentas>();
            for (DetallesVentas detallesVentasCollectionNewDetallesVentasToAttach : detallesVentasCollectionNew) {
                detallesVentasCollectionNewDetallesVentasToAttach = em.getReference(detallesVentasCollectionNewDetallesVentasToAttach.getClass(), detallesVentasCollectionNewDetallesVentasToAttach.getIdDetallesVentas());
                attachedDetallesVentasCollectionNew.add(detallesVentasCollectionNewDetallesVentasToAttach);
            }
            detallesVentasCollectionNew = attachedDetallesVentasCollectionNew;
            ventas.setDetallesVentasCollection(detallesVentasCollectionNew);
            ventas = em.merge(ventas);
            if (idUsuarioOld != null && !idUsuarioOld.equals(idUsuarioNew)) {
                idUsuarioOld.getVentasCollection().remove(ventas);
                idUsuarioOld = em.merge(idUsuarioOld);
            }
            if (idUsuarioNew != null && !idUsuarioNew.equals(idUsuarioOld)) {
                idUsuarioNew.getVentasCollection().add(ventas);
                idUsuarioNew = em.merge(idUsuarioNew);
            }
            if (idMesaOld != null && !idMesaOld.equals(idMesaNew)) {
                idMesaOld.getVentasCollection().remove(ventas);
                idMesaOld = em.merge(idMesaOld);
            }
            if (idMesaNew != null && !idMesaNew.equals(idMesaOld)) {
                idMesaNew.getVentasCollection().add(ventas);
                idMesaNew = em.merge(idMesaNew);
            }
            for (DetallesVentas detallesVentasCollectionNewDetallesVentas : detallesVentasCollectionNew) {
                if (!detallesVentasCollectionOld.contains(detallesVentasCollectionNewDetallesVentas)) {
                    Ventas oldIdVentasOfDetallesVentasCollectionNewDetallesVentas = detallesVentasCollectionNewDetallesVentas.getIdVentas();
                    detallesVentasCollectionNewDetallesVentas.setIdVentas(ventas);
                    detallesVentasCollectionNewDetallesVentas = em.merge(detallesVentasCollectionNewDetallesVentas);
                    if (oldIdVentasOfDetallesVentasCollectionNewDetallesVentas != null && !oldIdVentasOfDetallesVentasCollectionNewDetallesVentas.equals(ventas)) {
                        oldIdVentasOfDetallesVentasCollectionNewDetallesVentas.getDetallesVentasCollection().remove(detallesVentasCollectionNewDetallesVentas);
                        oldIdVentasOfDetallesVentasCollectionNewDetallesVentas = em.merge(oldIdVentasOfDetallesVentasCollectionNewDetallesVentas);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = ventas.getIdVentas();
                if (findVentas(id) == null) {
                    throw new NonexistentEntityException("The ventas with id " + id + " no longer exists.");
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
            Ventas ventas;
            try {
                ventas = em.getReference(Ventas.class, id);
                ventas.getIdVentas();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ventas with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<DetallesVentas> detallesVentasCollectionOrphanCheck = ventas.getDetallesVentasCollection();
            for (DetallesVentas detallesVentasCollectionOrphanCheckDetallesVentas : detallesVentasCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Ventas (" + ventas + ") cannot be destroyed since the DetallesVentas " + detallesVentasCollectionOrphanCheckDetallesVentas + " in its detallesVentasCollection field has a non-nullable idVentas field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Usuarios idUsuario = ventas.getIdUsuario();
            if (idUsuario != null) {
                idUsuario.getVentasCollection().remove(ventas);
                idUsuario = em.merge(idUsuario);
            }
            Mesas idMesa = ventas.getIdMesa();
            if (idMesa != null) {
                idMesa.getVentasCollection().remove(ventas);
                idMesa = em.merge(idMesa);
            }
            em.remove(ventas);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Ventas> findVentasEntities() {
        return findVentasEntities(true, -1, -1);
    }

    public List<Ventas> findVentasEntities(int maxResults, int firstResult) {
        return findVentasEntities(false, maxResults, firstResult);
    }

    private List<Ventas> findVentasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Ventas.class));
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

    public Ventas findVentas(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Ventas.class, id);
        } finally {
            em.close();
        }
    }

    public int getVentasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Ventas> rt = cq.from(Ventas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public List<Integer> findMesasActivas(){
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Integer> query = em.createQuery("select m.idMesa from Mesas m " +
                            "join Ventas v on v.idMesa.idMesa = m.idMesa " +
                            "where v.activa = true"
                    ,Integer.class);
            return query.getResultList();
        }finally {
            em.close();
        }
    }

    public List<Integer> findMesasDisponibles(int numMesas){
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Integer> query = em.createQuery("select m.idMesa from Mesas m " +
                            "join Ventas v on v.idMesa.idMesa = m.idMesa " +
                            "where v.activa = false " +
                            "and m.idMesa <= :mm "
                    ,Integer.class);
            query.setParameter("mm", numMesas);
            return query.getResultList();
        }finally {
            em.close();
        }
    }

    public List<Ventas> findActivas(){
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Ventas> query = em.createQuery("select v from Ventas v where v.activa = true",Ventas.class);
            return query.getResultList();
        }finally {
            em.close();
        }
    }

    public List<Ventas> ventasDiarias(){
        EntityManager em = getEntityManager();
        try{
            LocalTime midnight = LocalTime.MIDNIGHT;
            LocalDate today = LocalDate.now(ZoneId.of("America/El_Salvador"));
            LocalDateTime todayMidnight = LocalDateTime.of(today, midnight);
            LocalDateTime tomorrowMidnight = todayMidnight.plusDays(1);

            TypedQuery<Ventas> query = em.createQuery(" select v from Ventas v where v.fechaHora > :fecha and v.fechaHora < :fin", Ventas.class);

            query.setParameter("fecha",new Timestamp(todayMidnight.toInstant(ZoneOffset.ofHours(0)).toEpochMilli()), TemporalType.TIMESTAMP);
            query.setParameter("fin",new Timestamp(tomorrowMidnight.toInstant(ZoneOffset.ofHours(0)).toEpochMilli()) , TemporalType.TIMESTAMP);

            return query.getResultList();


        } finally {
            em.close();
        }
    }

    public List<Ventas> findTotalThisSemana(){
        EntityManager em = getEntityManager();
        try{

            LocalTime midnight = LocalTime.MIDNIGHT;
            LocalDate today = LocalDate.now(ZoneId.of("America/El_Salvador"));
            LocalDateTime todayMidnight = LocalDateTime.of(today, midnight);

            TemporalField diaDeSemana = WeekFields.of(Locale.getDefault()).dayOfWeek();

            LocalDateTime primerDia = todayMidnight.with(diaDeSemana, 1);

            TypedQuery<Ventas> query = em.createQuery(" select v from Ventas v where v.fechaHora > :date", Ventas.class);

            query.setParameter("date", new Timestamp(primerDia.toInstant(ZoneOffset.ofHours(0)).toEpochMilli()),TemporalType.TIMESTAMP);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Ventas> findTotalThisMes(){
        EntityManager em = getEntityManager();
        try{

            LocalTime midnight = LocalTime.MIDNIGHT;
            LocalDate today = LocalDate.now(ZoneId.of("America/El_Salvador"));
            LocalDateTime todayMidnight = LocalDateTime.of(today, midnight);

            LocalDateTime primerDiaMes = todayMidnight.withDayOfMonth(1);

            TypedQuery<Ventas> query = em.createQuery(" select v from Ventas v where v.fechaHora > :date", Ventas.class);

            query.setParameter("date", new Timestamp(primerDiaMes.toInstant(ZoneOffset.ofHours(0)).toEpochMilli()),TemporalType.TIMESTAMP);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Ventas> findByMes(int month){
        EntityManager em = getEntityManager();
        try{
            LocalTime midnight = LocalTime.MIDNIGHT;
            LocalDate today = LocalDate.now(ZoneId.of("America/El_Salvador"));

            LocalDateTime primerDiaMes = LocalDateTime.of(today,midnight).withDayOfMonth(1).withMonth(month);
            LocalDateTime ultimoDiaMes = primerDiaMes.plusDays(primerDiaMes.getMonth().maxLength());

            TypedQuery<Ventas> query = em.createQuery(" select v from Ventas v where v.fechaHora > :fecha and v.fechaHora < :fin", Ventas.class);

            query.setParameter("fecha",new Timestamp(primerDiaMes.toInstant(ZoneOffset.ofHours(0)).toEpochMilli()), TemporalType.TIMESTAMP);
            query.setParameter("fin",new Timestamp(ultimoDiaMes.toInstant(ZoneOffset.ofHours(0)).toEpochMilli()) , TemporalType.TIMESTAMP);

            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Ventas> findTotalByAnio(){
        EntityManager em = getEntityManager();
        try{

            LocalTime midnight = LocalTime.MIDNIGHT;
            LocalDate today = LocalDate.now(ZoneId.of("America/El_Salvador"));
            LocalDateTime todayMidnight = LocalDateTime.of(today, midnight);

            LocalDateTime primerDiaAnio = todayMidnight.withDayOfYear(1);

            TypedQuery<Ventas> query = em.createQuery(" select v from Ventas v where v.fechaHora > :date and " +
                    "v.activa = false", Ventas.class);

            query.setParameter("date", new Timestamp(primerDiaAnio.toInstant(ZoneOffset.ofHours(0)).toEpochMilli()) , TemporalType.TIMESTAMP);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Ventas> findTotalByAnio(int anio){
        EntityManager em = getEntityManager();
        try{

            LocalTime midnight = LocalTime.MIDNIGHT;
            LocalDate today = LocalDate.now(ZoneId.of("America/El_Salvador"));
            LocalDateTime primerDiaAnio = LocalDateTime.of(today, midnight).withDayOfYear(1);
            LocalDateTime ultimoDiaAnio = primerDiaAnio.plusYears(1);



            TypedQuery<Ventas> query = em.createQuery(" select v from Ventas v where v.fechaHora > :fecha and v.fechaHora < :fin", Ventas.class);

            query.setParameter("fecha",new Timestamp(primerDiaAnio.toInstant(ZoneOffset.ofHours(0)).toEpochMilli()), TemporalType.TIMESTAMP);
            query.setParameter("fin",new Timestamp(ultimoDiaAnio.toInstant(ZoneOffset.ofHours(0)).toEpochMilli()) , TemporalType.TIMESTAMP);

//            query.setParameter("date", date);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}
