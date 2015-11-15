package tablita.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * Created by akino on 11-15-15.
 */
public class EmpleadoOrden {
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

    public EmpleadoOrden() {
    }

}
