package tablita.controllers;

import com.sun.deploy.util.FXLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import tablita.ViewsManager;

import java.io.IOException;

/**
 * Created by akino on 11-08-15.
 */
public class AdminMainController {

    private static String TITULO = "TablitaPOS";
    Stage stage;

    @FXML
    Button botonEmpleados;

    @FXML
    Button botonProductos;

    @FXML
    Button botonInicio;

    @FXML
    Button botonClientes;

    @FXML
    Button botonReservaciones;


    public AdminMainController()  {
        System.out.println("constructor");

    }

    @FXML
    private void initialize(){
        System.out.println("initiali");
        botonEmpleados.setOnAction(this::btnEmpleados);

        botonProductos.setOnAction(this::btnProductos);

        botonClientes.setOnAction(this::btnClientes);

        botonReservaciones.setOnAction(this::btnReservas);

    }

    private void btnEmpleados(ActionEvent event){
        try {
            Parent adminEmpleados = FXMLLoader.load(getClass().getResource("../views/administrador/VistaAdministradorEmpleado.fxml"));
            ViewsManager.cambiarVentana("TablitaPOS - Empleados", adminEmpleados);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void btnProductos(ActionEvent event){
        try {
            Parent adminProductos = FXMLLoader.load(getClass().getResource("../views/administrador/VistaAdministradorProductos.fxml"));
            ViewsManager.cambiarVentana("TablitaPOS - Productos", adminProductos);
        } catch (IOException p) {
            p.printStackTrace();
        }
    }

    private void btnClientes(ActionEvent event){
        try {
            Parent adminClientes = FXMLLoader.load(getClass().getResource("../views/administrador/VistaAdministradorClientes.fxml"));
            ViewsManager.cambiarVentana("TablitaPOS - Clientes", adminClientes);
        } catch (IOException cl) {
            cl.printStackTrace();
        }
    }

    private void btnReservas(ActionEvent event) {
       try {
           Parent adminReservas = FXMLLoader.load(getClass().getResource("../views/administrador/VistaAdministradorReserva.fxml"));
           ViewsManager.cambiarVentana("TablitaPOS  - Reservas", adminReservas);
       }catch (IOException r) {
           r.printStackTrace();
       }
    }
}
