package tablita;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import tablita.controllers.EmpleadoOrdenController;
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
public class BotonesMesas {

    MesasJpaController mesasJpa = new MesasJpaController(CreadorEntityManager.emf());
    VentasJpaController ventasJpa = new VentasJpaController(CreadorEntityManager.emf());

    List<Mesas> mesas = new ArrayList<>();

    static List<HBox> hBoxList;
    List<Integer> activas;


    public BotonesMesas() {
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
                        b.setPrefWidth(100);
                        b.setPrefHeight(60);

                        final int finalM = m;
                        if(activas.contains(finalM+1)){
                            b.getStyleClass().add("button-primary");
                            b.setText(b.getText()+"*");
                            b.setOnAction(event -> {
                                FXMLLoader loader =
                                        new FXMLLoader(getClass().getResource("/tablita/views/empleados/VistaEmpleadoOrden.fxml"));
                                Parent root = null;
                                try {
                                    root = loader.load();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Ventas v = ventas.stream()
                                        .filter(ventas1 -> ventas1.getIdMesa().getNumeroMesa() == finalM+1)
                                        .findFirst().get();
                                EmpleadoOrdenController eoc = loader.<EmpleadoOrdenController>getController();
                                eoc.initExistente(v);
                                ViewsManager.cambiarVentana("Venta N° "+v.getIdVentas(), root);

                            });
                        } else {
                            b.setOnAction(event -> {
                                FXMLLoader loader =
                                        new FXMLLoader(getClass().getResource("/tablita/views/empleados/VistaEmpleadoOrden.fxml"));
                                Parent root = null;
                                try {
                                    root = loader.load();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                EmpleadoOrdenController eoc = loader.<EmpleadoOrdenController>getController();
                                eoc.initNuevaVenta(finalM+1);
                                ViewsManager.cambiarVentana("Nueva Venta" , root);
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
