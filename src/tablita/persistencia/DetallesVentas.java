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
@Table(name = "DetallesVentas")
@NamedQueries({
    @NamedQuery(name = "DetallesVentas.findAll", query = "SELECT d FROM DetallesVentas d"),
    @NamedQuery(name = "DetallesVentas.findByIdDetallesVentas", query = "SELECT d FROM DetallesVentas d WHERE d.idDetallesVentas = :idDetallesVentas"),
    @NamedQuery(name = "DetallesVentas.findByCantidad", query = "SELECT d FROM DetallesVentas d WHERE d.cantidad = :cantidad")})
public class DetallesVentas implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Basic(optional = false)
    @Column(name = "idDetallesVentas")
    private Integer idDetallesVentas;
    @Basic(optional = false)
    @Column(name = "cantidad")
    private int cantidad;
    @JoinColumn(name = "idProductos", referencedColumnName = "idProductos")
    @ManyToOne(optional = false)
    private Productos idProductos;
    @JoinColumn(name = "idVentas", referencedColumnName = "idVentas")
    @ManyToOne(optional = false)
    private Ventas idVentas;

    public DetallesVentas() {
    }

    public DetallesVentas(Integer idDetallesVentas) {
        this.idDetallesVentas = idDetallesVentas;
    }

    public DetallesVentas(Integer idDetallesVentas, int cantidad) {
        this.idDetallesVentas = idDetallesVentas;
        this.cantidad = cantidad;
    }

    public Integer getIdDetallesVentas() {
        return idDetallesVentas;
    }

    public void setIdDetallesVentas(Integer idDetallesVentas) {
        this.idDetallesVentas = idDetallesVentas;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Productos getIdProductos() {
        return idProductos;
    }

    public void setIdProductos(Productos idProductos) {
        this.idProductos = idProductos;
    }

    public Ventas getIdVentas() {
        return idVentas;
    }

    public void setIdVentas(Ventas idVentas) {
        this.idVentas = idVentas;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDetallesVentas != null ? idDetallesVentas.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetallesVentas)) {
            return false;
        }
        DetallesVentas other = (DetallesVentas) object;
        if ((this.idDetallesVentas == null && other.idDetallesVentas != null) || (this.idDetallesVentas != null && !this.idDetallesVentas.equals(other.idDetallesVentas))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "tablita.persistencia.DetallesVentas[ idDetallesVentas=" + idDetallesVentas + " ]";
    }
    
}
