/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tablita.persistencia;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 *
 * @author akino
 */
@Embeddable
public class ReservacionesPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "idReservas")
    private int idReservas;
    @Basic(optional = false)
    @Column(name = "idClientes")
    private int idClientes;
    @Basic(optional = false)
    @Column(name = "idMesa")
    private int idMesa;
    @Basic(optional = false)
    @Column(name = "idSalas")
    private int idSalas;

    public ReservacionesPK() {
    }

    public ReservacionesPK(int idReservas, int idClientes, int idMesa, int idSalas) {
        this.idReservas = idReservas;
        this.idClientes = idClientes;
        this.idMesa = idMesa;
        this.idSalas = idSalas;
    }

    public int getIdReservas() {
        return idReservas;
    }

    public void setIdReservas(int idReservas) {
        this.idReservas = idReservas;
    }

    public int getIdClientes() {
        return idClientes;
    }

    public void setIdClientes(int idClientes) {
        this.idClientes = idClientes;
    }

    public int getIdMesa() {
        return idMesa;
    }

    public void setIdMesa(int idMesa) {
        this.idMesa = idMesa;
    }

    public int getIdSalas() {
        return idSalas;
    }

    public void setIdSalas(int idSalas) {
        this.idSalas = idSalas;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idReservas;
        hash += (int) idClientes;
        hash += (int) idMesa;
        hash += (int) idSalas;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ReservacionesPK)) {
            return false;
        }
        ReservacionesPK other = (ReservacionesPK) object;
        if (this.idReservas != other.idReservas) {
            return false;
        }
        if (this.idClientes != other.idClientes) {
            return false;
        }
        if (this.idMesa != other.idMesa) {
            return false;
        }
        if (this.idSalas != other.idSalas) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "tablita.persistencia.ReservacionesPK[ idReservas=" + idReservas + ", idClientes=" + idClientes + ", idMesa=" + idMesa + ", idSalas=" + idSalas + " ]";
    }
    
}
