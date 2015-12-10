package tablita.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import tablita.BotonesMesasCajero;

/**
 * Created by akino on 11-25-15.
 */
public class CajeroMainController {
    @FXML
    VBox mainVBox;
    BotonesMesasCajero bm = new BotonesMesasCajero();

    public CajeroMainController() {
    }

    @FXML
    private void initialize(){
        mainVBox.setSpacing(10);
        mainVBox.getChildren().addAll(bm.gethBoxList());
    }



}
