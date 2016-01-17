package tablita;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import tablita.controllers.CajeroOrdenController;
import tablita.persistencia.JPAControllers.MesasJpaController;
import tablita.persistencia.JPAControllers.VentasJpaController;
import tablita.persistencia.Mesas;
import tablita.persistencia.Ventas;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by akino on 11-21-15.
 */
public class BotonesMesasCajero {

    MesasJpaController mesasJpa = new MesasJpaController(CreadorEntityManager.emf());
    VentasJpaController ventasJpa = new VentasJpaController(CreadorEntityManager.emf());

    List<Mesas> mesas = new ArrayList<>();

    static List<HBox> hBoxList;
    List<Integer> activas;


    public BotonesMesasCajero() {
        mesas = mesasJpa.findMesasEntities();
        activas = ventasJpa.findMesasActivas();
        List<Ventas> ventas = ventasJpa.findActivas();
        int totalH = mesas.size() / 5;
        hBoxList = new ArrayList<>(totalH);
        int m = 0;

        for(int i = 0; i <= totalH; i++){
            if(m<mesas.size()){
                HBox hbox = new HBox(10);
                hbox.setAlignment(Pos.CENTER);
                for (int j = 0; j<=4; j++){
                    if(m<mesas.size()){
                        Button b = new Button("Mesa "+mesas.get(m).getNumeroMesa());
                        b.setId("boton"+m);
                        b.setPrefWidth(140);
                        b.setPrefHeight(80);

                        final int finalM = m;
                        if(activas.contains(finalM+1)){
                            b.getStyleClass().add("button-primary");
                            b.setText(b.getText()+"*");
                            b.setOnAction(event -> {
                                // TODO: 11-25-15 remove
                                final int u;
                                FXMLLoader loader =
                                        new FXMLLoader(getClass().getResource("../tablita/views/cajero/VistaCajeroOrdenesActivas.fxml"));
                                Parent root = null;
                                try {
                                    root = loader.load();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Ventas v = ventas.stream()
                                        .filter(ventas1 -> ventas1.getIdMesa().getNumeroMesa() == finalM+1)
                                        .findFirst().get();

                                CajeroOrdenController coc = loader.<CajeroOrdenController>getController();
                                coc.init(v);
                                ViewsManager.cambiarVentana("Venta"+v.getIdVentas(), root);
                            });
                        }
                        hbox.getChildren().add(b);
                        m++;
                    } else {
                        break;
                    }
                }
                hBoxList.add(hbox);
            } else {
                break;
            }
        }
    }

    public  static List<HBox> gethBoxList(){
        return hBoxList;
    }

}
