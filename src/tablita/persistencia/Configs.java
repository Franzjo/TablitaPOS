/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tablita.persistencia;

import javax.persistence.*;
import java.io.Serializable;

/**
 *
 * @author akino
 */
@Entity
@Table(name = "Configs")
@NamedQueries({
    @NamedQuery(name = "Configs.findAll", query = "SELECT c FROM Configs c"),
    @NamedQuery(name = "Configs.findByIdConfigs", query = "SELECT c FROM Configs c WHERE c.idConfigs = :idConfigs"),
    @NamedQuery(name = "Configs.findByReservamax", query = "SELECT c FROM Configs c WHERE c.reservamax = :reservamax")})
public class Configs implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
//    @Basic(optional = false)
    @Column(name = "idConfigs")
    private Integer idConfigs;
    @Column(name = "Reserva_max")
    private Integer reservamax;

    public Configs() {
    }

    public Configs(Integer idConfigs) {
        this.idConfigs = idConfigs;
    }

    public Integer getIdConfigs() {
        return idConfigs;
    }

    public void setIdConfigs(Integer idConfigs) {
        this.idConfigs = idConfigs;
    }

    public Integer getReservamax() {
        return reservamax;
    }

    public void setReservamax(Integer reservamax) {
        this.reservamax = reservamax;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idConfigs != null ? idConfigs.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Configs)) {
            return false;
        }
        Configs other = (Configs) object;
        if ((this.idConfigs == null && other.idConfigs != null) || (this.idConfigs != null && !this.idConfigs.equals(other.idConfigs))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "tablita.persistencia.Configs[ idConfigs=" + idConfigs + " ]";
    }
    
}
