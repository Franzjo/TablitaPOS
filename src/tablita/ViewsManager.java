package tablita;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by akino on 11-08-15.
 */
public class ViewsManager {
    static Stage stage;

    public static Stage getStage() {
        return stage;
    }

    public static void setStage(Stage stage) {
        ViewsManager.stage = stage;
    }

    public static void cambiarVentana(String titulo, Parent parent){
        stage.setTitle(titulo);
        stage.setScene(new Scene(parent, 1024,768));
        //FlatterFX.style();
        stage.show();
    }
}
