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
@Table(name = "MenuProducto")
@NamedQueries({
    @NamedQuery(name = "MenuProducto.findAll", query = "SELECT m FROM MenuProducto m"),
    @NamedQuery(name = "MenuProducto.findByIdMenuProducto", query = "SELECT m FROM MenuProducto m WHERE m.idMenuProducto = :idMenuProducto")})
public class MenuProducto implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Basic(optional = true)
    @Column(name = "idMenuProducto")
    private Integer idMenuProducto;
    @JoinColumn(name = "idProductos", referencedColumnName = "idProductos")
    @ManyToOne(optional = false)
    private Productos idProductos;
    @JoinColumn(name = "idMenu", referencedColumnName = "idMenu")
    @ManyToOne(optional = false)
    private Menu idMenu;

    public MenuProducto() {
    }

    public MenuProducto(Integer idMenuProducto) {
        this.idMenuProducto = idMenuProducto;
    }

    public Integer getIdMenuProducto() {
        return idMenuProducto;
    }

    public void setIdMenuProducto(Integer idMenuProducto) {
        this.idMenuProducto = idMenuProducto;
    }

    public Productos getIdProductos() {
        return idProductos;
    }

    public void setIdProductos(Productos idProductos) {
        this.idProductos = idProductos;
    }

    public Menu getIdMenu() {
        return idMenu;
    }

    public void setIdMenu(Menu idMenu) {
        this.idMenu = idMenu;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idMenuProducto != null ? idMenuProducto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MenuProducto)) {
            return false;
        }
        MenuProducto other = (MenuProducto) object;
        if ((this.idMenuProducto == null && other.idMenuProducto != null) || (this.idMenuProducto != null && !this.idMenuProducto.equals(other.idMenuProducto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "tablita.persistencia.MenuProducto[ idMenuProducto=" + idMenuProducto + " ]";
    }
    
}
