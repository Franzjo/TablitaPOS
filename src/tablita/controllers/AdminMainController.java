package tablita.controllers;

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
    Button botonInicio;

    public AdminMainController()  {
        System.out.println("constructor");

    }

    @FXML
    private void initialize(){
        System.out.println("initiali");
        botonEmpleados.setOnAction(this::bntEmpleados);
    }

    private void bntEmpleados(ActionEvent event){
        try {
            Parent adminEmpleados = FXMLLoader.load(getClass().getResource("../views/administrador/VistaAdministrador.fxml"));
            ViewsManager.cambiarVentana("TablitaPOS - Empleados", adminEmpleados);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
