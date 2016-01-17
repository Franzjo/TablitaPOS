package tablita;

import javafx.application.Application;
import javafx.stage.Stage;
import tablita.persistencia.JPAControllers.VentasJpaController;
import tablita.persistencia.Ventas;

import java.util.List;

public class Main extends Application {




    @Override
    public void start(Stage primaryStage) throws Exception{
        ViewsManager.setStage(primaryStage);
        FlatterFX.style();
        Parent root = FXMLLoader.load(getClass().getResource("views/administrador/VistaAdministrador.fxml"));
       // Parent root = FXMLLoader.load(getClass().getResource("views/empleados/VistaEmpleadoOrden.fxml"));
        VentasJpaController vjpa = new VentasJpaController(CreadorEntityManager.emf());
        //Parent root = FXMLLoader.load(getClass().getResource("views/administrador/VistaAdministrador.fxml"));
        //Parent root = FXMLLoader.load(getClass().getResource("views/cajero/VistaCajeroIncio.fxml"));

        //Parent root = FXMLLoader.load(getClass().getResource("views/empleados/VistaEmpleado.fxml"));
        //ViewsManager.cambiarVentana("TablitaPOS",root);


        List<Ventas> ventas = vjpa.findByAnio();

        GeneradorJson.generarJson(ventas);


//        primaryStage.setTitle("TablitaPOS");
//        primaryStage.setScene(new Scene(root, 1024, 768));
//        primaryStage.show();

    }




    public static void main(String[] args) {
        launch(args);
        //RolesFactory.initialize();

    }
}
