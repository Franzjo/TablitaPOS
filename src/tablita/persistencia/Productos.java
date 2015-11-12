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

/**
 *
 * @author akino
 */
@Entity
@Table(name = "Productos")
@NamedQueries({
    @NamedQuery(name = "Productos.findAll", query = "SELECT p FROM Productos p"),
    @NamedQuery(name = "Productos.findByIdProductos", query = "SELECT p FROM Productos p WHERE p.idProductos = :idProductos"),
    @NamedQuery(name = "Productos.findByItem", query = "SELECT p FROM Productos p WHERE p.item = :item"),
    @NamedQuery(name = "Productos.findByPrecio", query = "SELECT p FROM Productos p WHERE p.precio = :precio"),
    @NamedQuery(name = "Productos.findByExistencia", query = "SELECT p FROM Productos p WHERE p.existencia = :existencia"),
    @NamedQuery(name = "Productos.findByTipo", query = "SELECT p FROM Productos p WHERE p.tipo = :tipo"),
    @NamedQuery(name = "Productos.findByGravado", query = "SELECT p FROM Productos p WHERE p.gravado = :gravado")})
public class Productos implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Basic(optional = false)
    @Column(name = "idProductos")
    private Integer idProductos;
    @Basic(optional = false)
    @Column(name = "item")
    private String item;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "precio")
    private BigDecimal precio;
    @Basic(optional = false)
    @Column(name = "existencia")
    private int existencia;
    @Column(name = "tipo")
    private String tipo;
    @Basic(optional = false)
    @Column(name = "gravado")
    private boolean gravado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idProductos")
    private Collection<DetallesVentas> detallesVentasCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idProductos")
    private Collection<MenuProducto> menuProductoCollection;

    public Productos() {
    }

    public Productos(Integer idProductos) {
        this.idProductos = idProductos;
    }

    public Productos(Integer idProductos, String item, BigDecimal precio, int existencia, boolean gravado) {
        this.idProductos = idProductos;
        this.item = item;
        this.precio = precio;
        this.existencia = existencia;
        this.gravado = gravado;
    }

    public Integer getIdProductos() {
        return idProductos;
    }

    public void setIdProductos(Integer idProductos) {
        this.idProductos = idProductos;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public int getExistencia() {
        return existencia;
    }

    public void setExistencia(int existencia) {
        this.existencia = existencia;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public boolean getGravado() {
        return gravado;
    }

    public void setGravado(boolean gravado) {
        this.gravado = gravado;
    }

    public Collection<DetallesVentas> getDetallesVentasCollection() {
        return detallesVentasCollection;
    }

    public void setDetallesVentasCollection(Collection<DetallesVentas> detallesVentasCollection) {
        this.detallesVentasCollection = detallesVentasCollection;
    }

    public Collection<MenuProducto> getMenuProductoCollection() {
        return menuProductoCollection;
    }

    public void setMenuProductoCollection(Collection<MenuProducto> menuProductoCollection) {
        this.menuProductoCollection = menuProductoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idProductos != null ? idProductos.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Productos)) {
            return false;
        }
        Productos other = (Productos) object;
        if ((this.idProductos == null && other.idProductos != null) || (this.idProductos != null && !this.idProductos.equals(other.idProductos))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "tablita.persistencia.Productos[ idProductos=" + idProductos + " ]";
    }
    
}
