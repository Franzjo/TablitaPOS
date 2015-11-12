/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tablita.persistencia;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

/**
 *
 * @author akino
 */
@Entity
@Table(name = "Mesas")
@NamedQueries({
    @NamedQuery(name = "Mesas.findAll", query = "SELECT m FROM Mesas m"),
    @NamedQuery(name = "Mesas.findByIdMesa", query = "SELECT m FROM Mesas m WHERE m.idMesa = :idMesa"),
    @NamedQuery(name = "Mesas.findByArea", query = "SELECT m FROM Mesas m WHERE m.area = :area"),
    @NamedQuery(name = "Mesas.findByDescripcion", query = "SELECT m FROM Mesas m WHERE m.descripcion = :descripcion"),
    @NamedQuery(name = "Mesas.findByNumeroMesa", query = "SELECT m FROM Mesas m WHERE m.numeroMesa = :numeroMesa")})
public class Mesas implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Basic(optional = false)
    @Column(name = "idMesa")
    private Integer idMesa;
    @Column(name = "area")
    private String area;
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @Column(name = "numeroMesa")
    private int numeroMesa;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idMesa")
    private Collection<Ventas> ventasCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mesas")
    private Collection<Reservaciones> reservacionesCollection;

    public Mesas() {
    }

    public Mesas(Integer idMesa) {
        this.idMesa = idMesa;
    }

    public Mesas(Integer idMesa, int numeroMesa) {
        this.idMesa = idMesa;
        this.numeroMesa = numeroMesa;
    }

    public Integer getIdMesa() {
        return idMesa;
    }

    public void setIdMesa(Integer idMesa) {
        this.idMesa = idMesa;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getNumeroMesa() {
        return numeroMesa;
    }

    public void setNumeroMesa(int numeroMesa) {
        this.numeroMesa = numeroMesa;
    }

    public Collection<Ventas> getVentasCollection() {
        return ventasCollection;
    }

    public void setVentasCollection(Collection<Ventas> ventasCollection) {
        this.ventasCollection = ventasCollection;
    }

    public Collection<Reservaciones> getReservacionesCollection() {
        return reservacionesCollection;
    }

    public void setReservacionesCollection(Collection<Reservaciones> reservacionesCollection) {
        this.reservacionesCollection = reservacionesCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idMesa != null ? idMesa.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Mesas)) {
            return false;
        }
        Mesas other = (Mesas) object;
        if ((this.idMesa == null && other.idMesa != null) || (this.idMesa != null && !this.idMesa.equals(other.idMesa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "tablita.persistencia.Mesas[ idMesa=" + idMesa + " ]";
    }
    
}
