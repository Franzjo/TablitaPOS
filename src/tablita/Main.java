package tablita;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        ViewsManager.setStage(primaryStage);


//        ReportesDatos r = new ReportesDatos();
//        Instant ins = Instant.now();
//        System.out.println("r.productosPopulares(3) = " + r.productosPopularesAnual(2016,true).size());
//        Instant ins2 = Instant.now();
//        System.out.println(ins2.toEpochMilli() - ins.toEpochMilli());

        Parent root = FXMLLoader.load(getClass().getResource("views/login.fxml"));
        primaryStage.setTitle("TablitaPOS");
        primaryStage.setScene(new Scene(root, 550, 400));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
        RolesFactory.initialize();

    }
}
