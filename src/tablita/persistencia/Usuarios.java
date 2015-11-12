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
@Table(name = "Usuarios")
@NamedQueries({
    @NamedQuery(name = "Usuarios.findAll", query = "SELECT u FROM Usuarios u"),
    @NamedQuery(name = "Usuarios.findByIdUsuario", query = "SELECT u FROM Usuarios u WHERE u.idUsuario = :idUsuario"),
    @NamedQuery(name = "Usuarios.findByCodigo", query = "SELECT u FROM Usuarios u WHERE u.codigo = :codigo"),
    @NamedQuery(name = "Usuarios.findByNombres", query = "SELECT u FROM Usuarios u WHERE u.nombres = :nombres"),
    @NamedQuery(name = "Usuarios.findByApellidos", query = "SELECT u FROM Usuarios u WHERE u.apellidos = :apellidos"),
    @NamedQuery(name = "Usuarios.findByPass", query = "SELECT u FROM Usuarios u WHERE u.pass = :pass"),
    @NamedQuery(name = "Usuarios.findByDui", query = "SELECT u FROM Usuarios u WHERE u.dui = :dui"),
    @NamedQuery(name = "Usuarios.findByNit", query = "SELECT u FROM Usuarios u WHERE u.nit = :nit"),
    @NamedQuery(name = "Usuarios.findByTel", query = "SELECT u FROM Usuarios u WHERE u.tel = :tel")})
public class Usuarios implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Basic(optional = false)
    @Column(name = "idUsuario")
    private Integer idUsuario;
    @Basic(optional = false)
    @Column(name = "codigo")
    private String codigo;
    @Basic(optional = false)
    @Column(name = "nombres")
    private String nombres;
    @Column(name = "apellidos")
    private String apellidos;
    @Basic(optional = false)
    @Column(name = "pass")
    private String pass;
    @Column(name = "DUI")
    private Integer dui;
    @Column(name = "NIT")
    private String nit;
    @Column(name = "TEL")
    private Integer tel;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idUsuario")
    private Collection<Ventas> ventasCollection;
    @JoinColumn(name = "idRol", referencedColumnName = "idRol")
    @ManyToOne(optional = false)
    private Roles idRol;

    public Usuarios() {
    }

    public Usuarios(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Usuarios(Integer idUsuario, String codigo, String nombres, String pass) {
        this.idUsuario = idUsuario;
        this.codigo = codigo;
        this.nombres = nombres;
        this.pass = pass;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public Integer getDui() {
        return dui;
    }

    public void setDui(Integer dui) {
        this.dui = dui;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public Integer getTel() {
        return tel;
    }

    public void setTel(Integer tel) {
        this.tel = tel;
    }

    public Collection<Ventas> getVentasCollection() {
        return ventasCollection;
    }

    public void setVentasCollection(Collection<Ventas> ventasCollection) {
        this.ventasCollection = ventasCollection;
    }

    public Roles getIdRol() {
        return idRol;
    }

    public void setIdRol(Roles idRol) {
        this.idRol = idRol;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idUsuario != null ? idUsuario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Usuarios)) {
            return false;
        }
        Usuarios other = (Usuarios) object;
        if ((this.idUsuario == null && other.idUsuario != null) || (this.idUsuario != null && !this.idUsuario.equals(other.idUsuario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "tablita.persistencia.Usuarios[ idUsuario=" + idUsuario + " ]";
    }
    
}
