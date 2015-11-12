package tablita;

import com.guigarage.flatterfx.FlatterFX;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class Main extends Application {




    @Override
    public void start(Stage primaryStage) throws Exception{
        ViewsManager.setStage(primaryStage);
        FlatterFX.style();
        Parent root = FXMLLoader.load(getClass().getResource("views/administrador/VistaAdministrador.fxml"));

        System.out.println("3");

        ViewsManager.cambiarVentana("TablitaPOS",root);

//        primaryStage.setTitle("TablitaPOS");
//        primaryStage.setScene(new Scene(root, 1024, 768));
//        primaryStage.show();

    }




    public static void main(String[] args) {
        launch(args);
    }
}
