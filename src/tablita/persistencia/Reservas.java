package tablita.persistencia;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import tablita.Conexion;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;


/**
 * Created by Rey on 21/12/2015.
 */
public class Reservas {
    private IntegerProperty idReservas;
    private StringProperty nombreCliente;
    private StringProperty apellidoCliente;
    private IntegerProperty numeroPersonas;
    private Date fecha;
    private Time hora;


    public Reservas(int idReservas, String nombreCliente, String apellidoCliente, int numeroPersonas, Date fecha, Time hora){
        this.idReservas = new SimpleIntegerProperty(idReservas);
        this.nombreCliente = new SimpleStringProperty(nombreCliente);
        this.apellidoCliente = new SimpleStringProperty(apellidoCliente);
        this.numeroPersonas = new SimpleIntegerProperty(numeroPersonas);
        this.fecha = fecha;
        this.hora = hora;
    }

    public Reservas(){

    }

    public int getIdReservas() {
        return idReservas.get();
    }

    public IntegerProperty idReservasProperty() {
        return idReservas;
    }

    public String getNombreCliente() {
        return nombreCliente.get();
    }

    public StringProperty nombreClienteProperty() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente.set(nombreCliente);
    }

    public void setIdReservas(int idReservas) {
        this.idReservas.set(idReservas);
    }

    public String getApellidoCliente() {
        return apellidoCliente.get();
    }

    public StringProperty apellidoClienteProperty() {
        return apellidoCliente;
    }

    public void setApellidoCliente(String apellidoCliente) {
        this.apellidoCliente.set(apellidoCliente);
    }

    public int getNumeroPersonas() {
        return numeroPersonas.get();
    }

    public IntegerProperty numeroPersonasProperty() {
        return numeroPersonas;
    }

    public void setNumeroPersonas(int numeroPersonas) {
        this.numeroPersonas.set(numeroPersonas);
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Time getHora() {
        return hora;
    }

    public void setHora(Time hora) {
        this.hora = hora;
    }

    @Override
    public String toString(){
        return idReservas.get() + "\t\t"
                + nombreCliente.get() + "\t\t"
                + apellidoCliente.get() + "\t\t"
                + numeroPersonas.get() + "\t\t"
                + fecha.toString() + "\t\t"
                + hora.toString();
    }

    public int agregarRegistro(Conexion conexion){
        try {
            PreparedStatement ps = conexion.getConexion().prepareStatement(
                    "INSERT INTO reservas (idReservas, " +
                            "nombreCliente, " +
                            "apellidoCliente, " +
                            "numeroPersonas, " +
                            "fecha," +
                            "hora" +
                            ") VALUES (?, ?, ?, ?, ?, ?)"
            );
            ps.setString(1, String.valueOf(idReservas.get()));
            ps.setString(2, nombreCliente.get());
            ps.setString(3, apellidoCliente.get());
            ps.setString(4, String.valueOf(numeroPersonas.get()));
            ps.setDate(5,fecha);
            ps.setTime(6, hora);
            return (ps.executeUpdate());
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int modificarRegistro(Conexion conexion){
        try {
            PreparedStatement ps = conexion.getConexion().prepareStatement(
                  "UPDATE reservas " +
                          "SET idReservas = ?, " +
                               "nombreCliente = ?, " +
                               "apellidoCliente = ?, " +
                               "numeroPersonas = ?, " +
                               "fecha = ?, " +
                               "hora = ? " +
                          "WHERE idReservas = ?"
            );
            ps.setString(1, String.valueOf(idReservas.get()));
            ps.setString(2, nombreCliente.get());
            ps.setString(3, apellidoCliente.get());
            ps.setString(4, String.valueOf(numeroPersonas.get()));
            ps.setDate(5, fecha);
            ps.setTime(6, hora);
            ps.setInt(7, idReservas.get());
            return (ps.executeUpdate());
        }catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int eliminarRegistro(Conexion conexion){
        try {
            PreparedStatement ps = conexion.getConexion().prepareStatement(
                    "DELETE FROM reservas WHERE idReservas = ?"
            );
            ps.setInt(1, idReservas.get());
            return ps.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();{
                return 0;
            }
        }
    }
}
