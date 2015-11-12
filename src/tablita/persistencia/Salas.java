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
@Table(name = "Salas")
@NamedQueries({
    @NamedQuery(name = "Salas.findAll", query = "SELECT s FROM Salas s"),
    @NamedQuery(name = "Salas.findByIdSalas", query = "SELECT s FROM Salas s WHERE s.idSalas = :idSalas"),
    @NamedQuery(name = "Salas.findByNombre", query = "SELECT s FROM Salas s WHERE s.nombre = :nombre")})
public class Salas implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Basic(optional = false)
    @Column(name = "idSalas")
    private Integer idSalas;
    @Column(name = "nombre")
    private String nombre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "salas")
    private Collection<Reservaciones> reservacionesCollection;

    public Salas() {
    }

    public Salas(Integer idSalas) {
        this.idSalas = idSalas;
    }

    public Integer getIdSalas() {
        return idSalas;
    }

    public void setIdSalas(Integer idSalas) {
        this.idSalas = idSalas;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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
        hash += (idSalas != null ? idSalas.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Salas)) {
            return false;
        }
        Salas other = (Salas) object;
        if ((this.idSalas == null && other.idSalas != null) || (this.idSalas != null && !this.idSalas.equals(other.idSalas))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "tablita.persistencia.Salas[ idSalas=" + idSalas + " ]";
    }
    
}
