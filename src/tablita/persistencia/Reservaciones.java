/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tablita.persistencia;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author akino
 */
@Entity
@Table(name = "Reservaciones")
@NamedQueries({
    @NamedQuery(name = "Reservaciones.findAll", query = "SELECT r FROM Reservaciones r"),
    @NamedQuery(name = "Reservaciones.findByIdReservas", query = "SELECT r FROM Reservaciones r WHERE r.reservacionesPK.idReservas = :idReservas"),
    @NamedQuery(name = "Reservaciones.findByhora", query = "SELECT r FROM Reservaciones r WHERE r.hora = :hora"),
    @NamedQuery(name = "Reservaciones.findByIdClientes", query = "SELECT r FROM Reservaciones r WHERE r.reservacionesPK.idClientes = :idClientes"),
    @NamedQuery(name = "Reservaciones.findByIdMesa", query = "SELECT r FROM Reservaciones r WHERE r.reservacionesPK.idMesa = :idMesa"),
    @NamedQuery(name = "Reservaciones.findByIdSalas", query = "SELECT r FROM Reservaciones r WHERE r.reservacionesPK.idSalas = :idSalas")})
public class Reservaciones implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ReservacionesPK reservacionesPK;
//    @Basic(optional = false)
    @Column(name = "hora")
    @Temporal(TemporalType.TIMESTAMP)
    private Date hora;
//    @JoinColumn(name = "idSalas", referencedColumnName = "idSalas", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Salas salas;
    @JoinColumn(name = "idMesa", referencedColumnName = "idMesa", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Mesas mesas;
    @JoinColumn(name = "idClientes", referencedColumnName = "idClientes", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Clientes clientes;

    public Reservaciones() {
    }

    public Reservaciones(ReservacionesPK reservacionesPK) {
        this.reservacionesPK = reservacionesPK;
    }

    public Reservaciones(ReservacionesPK reservacionesPK, Date hora) {
        this.reservacionesPK = reservacionesPK;
        this.hora = hora;
    }

    public Reservaciones(int idReservas, int idClientes, int idMesa, int idSalas) {
        this.reservacionesPK = new ReservacionesPK(idReservas, idClientes, idMesa, idSalas);
    }

    public ReservacionesPK getReservacionesPK(ReservacionesPK reservacionesPK) {
        return this.reservacionesPK;
    }

    public void setReservacionesPK(ReservacionesPK reservacionesPK) {
        this.reservacionesPK = reservacionesPK;
    }

    public Date gethora() {
        return hora;
    }

    public void sethora(Date hora) {
        this.hora = hora;
    }

    public Salas getSalas() {
        return salas;
    }

    public void setSalas(Salas salas) {
        this.salas = salas;
    }

    public Mesas getMesas() {
        return mesas;
    }

    public void setMesas(Mesas mesas) {
        this.mesas = mesas;
    }

    public Clientes getClientes() {
        return clientes;
    }

    public void setClientes(Clientes clientes) {
        this.clientes = clientes;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (reservacionesPK != null ? reservacionesPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Reservaciones)) {
            return false;
        }
        Reservaciones other = (Reservaciones) object;
        if ((this.reservacionesPK == null && other.reservacionesPK != null) || (this.reservacionesPK != null && !this.reservacionesPK.equals(other.reservacionesPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "tablita.persistencia.Reservaciones[ reservacionesPK=" + reservacionesPK + " ]";
    }

}
