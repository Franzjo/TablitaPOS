package tablita.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import tablita.CreadorEntityManager;
import tablita.Tipos;
import tablita.persistencia.JPAControllers.ProductosJpaController;
import tablita.persistencia.Productos;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by akino on 11-15-15.
 */
public class EmpleadoOrden {

    ProductosJpaController prodJpaCont = new ProductosJpaController(CreadorEntityManager.emf());

    @FXML
    Button botonInicio;
    @FXML
    Button botonMesa;
    @FXML
    Button botonCancelar;
    @FXML
    Button botonEntrada;
    @FXML
    Button botonPlatoFuerte;
    @FXML
    Button botonCombos;
    @FXML
    Button botonFrias;
    @FXML
    Button botonPostres;
    @FXML
    TableView tablaProductos;
    @FXML
    TableColumn descripcion;
    @FXML
    TableColumn cantidad;
    @FXML
    TableColumn importe;

    @FXML
    ListView<String> productosListView;

    public EmpleadoOrden() {
        System.out.println("construc");
    }

    @FXML
    private void initialize(){
        botonEntrada.setOnAction(event -> {
            productosListView.setItems(productosList(Tipos.ENTRADA));
        });
        botonFrias.setOnAction(event -> {
                productosListView.setItems(productosList(Tipos.BEBIDAS));
        });
        botonPlatoFuerte.setOnAction(event -> {
            productosListView.setItems(productosList(Tipos.PLATOFUERTE));
        });
        botonPostres.setOnAction(event -> {
            productosListView.setItems(productosList(Tipos.POSTRES));
        });
        botonCombos.setOnAction(event -> {
            productosListView.setItems(productosList(Tipos.COMBOS));
        });
    }

    private ObservableList<String> productosList(String tipo){

        List<Productos> productosList = prodJpaCont.getProductosByTipo(tipo);
        return FXCollections.observableArrayList(
                productosList.stream()
                        .map(productos -> productos.getItem())
                        .collect(Collectors.toList()));
    }
}
