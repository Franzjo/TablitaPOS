package tablita;

import javafx.collections.ObservableList;
import tablita.persistencia.Reservas;

import java.sql.*;

/**
 * Created by Rey on 21/12/2015.
 */
public class Conexion {
    private Connection conexion;
    private String url = "jdbc:mysql://localhost/tablitadb";
    private static String user = "root";
    private static String pass = "root";

    public Conexion(){
        establecerConexion();
    }

    public Connection getConexion(){
        return this.conexion;
    }

    public void establecerConexion(){
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conexion = DriverManager.getConnection(url, user, pass);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void cerrarConexion(){
        try {
            conexion.close();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void llenarTableView(ObservableList<Reservas> list){
        try {
            Statement instruccion = conexion.createStatement();
            ResultSet resultado = instruccion.executeQuery(
                    "SELECT idReservas, "
                            + "nombreCliente, "
                            + "apellidoCliente, "
                            + "numeroPersonas, "
                            + "fecha, "
                            + "hora "
                            + "FROM reservas ");
            while (resultado.next()){
                list.add(
                        new Reservas(
                                resultado.getInt("idReservas"),
                                resultado.getString("nombreCliente"),
                                resultado.getString("apellidoCliente"),
                                resultado.getInt("numeroPersonas"),
                                resultado.getDate("fecha"),
                                resultado.getTime("hora")
                        )
                );
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
