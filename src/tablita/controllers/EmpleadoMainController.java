package tablita.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import tablita.BotonesMesas;

/**
 * Created by akino on 11-21-15.
 */


public class EmpleadoMainController {

    @FXML
    VBox mainVBox;
    BotonesMesas bm = new BotonesMesas();

    public EmpleadoMainController() {
    }

    @FXML
    public void initialize(){
        mainVBox.setSpacing(10);
        mainVBox.getChildren().addAll(bm.gethBoxList());
    }
}