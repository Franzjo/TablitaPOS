/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tablita.persistencia;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

/**
 *
 * @author akino
 */
@Entity
@Table(name = "Ventas")
@NamedQueries({
    @NamedQuery(name = "Ventas.findAll", query = "SELECT v FROM Ventas v"),
    @NamedQuery(name = "Ventas.findByIdVentas", query = "SELECT v FROM Ventas v WHERE v.idVentas = :idVentas"),
    @NamedQuery(name = "Ventas.findByFechaHora", query = "SELECT v FROM Ventas v WHERE v.fechaHora = :fechaHora"),
    @NamedQuery(name = "Ventas.findByTotal", query = "SELECT v FROM Ventas v WHERE v.total = :total"),
    @NamedQuery(name = "Ventas.findByImporte", query = "SELECT v FROM Ventas v WHERE v.importe = :importe"),
    @NamedQuery(name = "Ventas.findByCambio", query = "SELECT v FROM Ventas v WHERE v.cambio = :cambio")})
public class Ventas implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Basic(optional = false)
    @Column(name = "idVentas")
    private Integer idVentas;
    @Column(name = "fechaHora")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHora;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "total")
    private BigDecimal total;
    @Column(name = "importe")
    private BigDecimal importe;
    @Column(name = "cambio")
    private BigDecimal cambio;
    @JoinColumn(name = "idUsuario", referencedColumnName = "idUsuario")
    @ManyToOne(optional = false)
    private Usuarios idUsuario;
    @JoinColumn(name = "idMesa", referencedColumnName = "idMesa")
    @ManyToOne(optional = false)
    private Mesas idMesa;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idVentas")
    private Collection<DetallesVentas> detallesVentasCollection;

    public Ventas() {
    }

    public Ventas(Integer idVentas) {
        this.idVentas = idVentas;
    }

    public Integer getIdVentas() {
        return idVentas;
    }

    public void setIdVentas(Integer idVentas) {
        this.idVentas = idVentas;
    }

    public Date getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getImporte() {
        return importe;
    }

    public void setImporte(BigDecimal importe) {
        this.importe = importe;
    }

    public BigDecimal getCambio() {
        return cambio;
    }

    public void setCambio(BigDecimal cambio) {
        this.cambio = cambio;
    }

    public Usuarios getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Usuarios idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Mesas getIdMesa() {
        return idMesa;
    }

    public void setIdMesa(Mesas idMesa) {
        this.idMesa = idMesa;
    }

    public Collection<DetallesVentas> getDetallesVentasCollection() {
        return detallesVentasCollection;
    }

    public void setDetallesVentasCollection(Collection<DetallesVentas> detallesVentasCollection) {
        this.detallesVentasCollection = detallesVentasCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idVentas != null ? idVentas.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Ventas)) {
            return false;
        }
        Ventas other = (Ventas) object;
        if ((this.idVentas == null && other.idVentas != null) || (this.idVentas != null && !this.idVentas.equals(other.idVentas))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "tablita.persistencia.Ventas[ idVentas=" + idVentas + " ]";
    }
    
}
