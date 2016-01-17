package tablita.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import tablita.Conexion;
import tablita.persistencia.Reservas;

import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Created by PC HP on 16/01/2016.
 */
public class AdminReservasController implements Initializable {
    @FXML private TextField txtidReservas;
    @FXML private TextField txtNombreCliente;
    @FXML private TextField txtApellidoCliente;
    @FXML private TextField txtNumeroPersona;
    @FXML private DatePicker dpFecha;
    @FXML private TextField txtHora;

    @FXML private Button btnGuardar;
    @FXML private Button btnModificar;
    @FXML private Button btnEliminar;

    @FXML private TableView<Reservas> viewReservas;
    @FXML private TableColumn<Reservas, Integer> colidReservas;
    @FXML private TableColumn<Reservas, String> colNombreCliente;
    @FXML private TableColumn<Reservas, String> colApellidoCliente;
    @FXML private TableColumn<Reservas, Integer> colNumeroPersonas;
    @FXML private TableColumn<Reservas, Date> colFecha;
    @FXML private TableColumn<Reservas, Time> colHora;

    private ObservableList<Reservas> reservas;

    private Conexion conexion;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        conexion = new Conexion();
        reservas = FXCollections.observableArrayList();

        llenarListas();

        colidReservas.setCellValueFactory(new PropertyValueFactory<Reservas, Integer>("idReservas"));
        colNombreCliente.setCellValueFactory(new PropertyValueFactory<Reservas, String>("nombreCliente"));
        colApellidoCliente.setCellValueFactory(new PropertyValueFactory<Reservas, String>("apellidoCliente"));
        colNumeroPersonas.setCellValueFactory(new PropertyValueFactory<Reservas, Integer>("numeroPersonas"));
        colFecha.setCellValueFactory(new PropertyValueFactory<Reservas, Date>("fecha"));
        colHora.setCellValueFactory(new PropertyValueFactory<Reservas, Time>("hora"));

        viewReservas.setItems(reservas);
        viewReservas.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Reservas>() {
            @Override
            public void changed(ObservableValue<? extends Reservas> observable, Reservas oldValue, Reservas newValue) {
                btnModificar.setDisable(false);
                btnEliminar.setDisable(false);
                if (newValue != null) {
                    btnGuardar.setDisable(true);
                    llenarCcmponentes(newValue);
                }
            }
        });
    }

        public void llenarCcmponentes(Reservas newValue) {
            txtidReservas.setText(String.valueOf(newValue.getIdReservas()));
            txtNombreCliente.setText(newValue.getNombreCliente());
            txtApellidoCliente.setText(newValue.getApellidoCliente());
            dpFecha.setValue(newValue.getFecha().toLocalDate());
            txtHora.setText(String.valueOf(newValue.getFecha().toLocalDate()));
        }

        public void llenarListas() {
            conexion.llenarTableView(reservas);
            conexion.cerrarConexion();
        }

            @FXML
            public void agregarRegistro () {
                Reservas r = new Reservas(
                                Integer.valueOf(txtidReservas.getText()),
                                txtNombreCliente.getText(),
                                txtApellidoCliente.getText(),
                                Integer.valueOf(txtNumeroPersona.getText()),
                                Date.valueOf(dpFecha.getValue()),
                                Time.valueOf(txtHora.getText())
                );
                conexion.establecerConexion();
                int resultado = r.agregarRegistro(conexion);
                //conexion.cerrarConexion();

                if (resultado == 1){
                    reservas.add(r);
                    Alert cuadroDialog = new Alert(Alert.AlertType.INFORMATION);
                    cuadroDialog.setContentText("Reserva guardada con exito");
                    cuadroDialog.setTitle("Registro agregado");
                    cuadroDialog.setHeaderText("Resultado: ");
                    cuadroDialog.showAndWait();
                }
             }

            @FXML
            public void modificarRegistro(){
                Reservas r = new Reservas(
                                Integer.valueOf(txtidReservas.getText()),
                                txtNombreCliente.getText(),
                                txtApellidoCliente.getText(),
                                Integer.valueOf(txtNumeroPersona.getText()),
                                Date.valueOf(dpFecha.getValue()),
                                Time.valueOf(txtHora.getText())
                );
                //comment

//jhgjhg
                conexion.establecerConexion();
                int resultado = r.modificarRegistro(conexion);
                conexion.cerrarConexion();

                if (resultado == 1) {
                    reservas.set(
                            viewReservas.getSelectionModel().getSelectedIndex(),r);
                }
                Alert cuadroDialogo = new Alert(Alert.AlertType.INFORMATION);
                cuadroDialogo.setContentText("Registro modificado con exito");
                cuadroDialogo.setTitle("Registro Actualizado");
                cuadroDialogo.setHeaderText("Resultado: ");
                cuadroDialogo.showAndWait();
            }

            @FXML
            public void eliminarRegistro(){
                Alert cuadroDialogoConfirmacion = new Alert(Alert.AlertType.CONFIRMATION);
                cuadroDialogoConfirmacion.setTitle("Confirmación");
                cuadroDialogoConfirmacion.setHeaderText("Eliminar Regristro");
                cuadroDialogoConfirmacion.setContentText("¿Esta seguro que desea eliminar este registro?");
                Optional<ButtonType> resultado = cuadroDialogoConfirmacion.showAndWait();
                if (resultado.get() == ButtonType.OK){
                    Reservas r = new Reservas();
                    r.setIdReservas(Integer.valueOf(txtidReservas.getText()));
                    int rst = r.eliminarRegistro(conexion);
                    conexion.establecerConexion();
                    if (rst == 1){
                        reservas.remove(viewReservas.getSelectionModel().getSelectedIndex());
                        limpiarComponentes();
                        Alert cuadroDialogo = new Alert(Alert.AlertType.INFORMATION);
                        cuadroDialogo.setContentText("Registro eliminado con exito");
                        cuadroDialogo.setTitle("Registr Eliminado");
                        cuadroDialogo.setHeaderText("Resultado: ");
                        cuadroDialogo.showAndWait();
                    }
                }
            }

    @FXML
    public void limpiarComponentes(){
        viewReservas.getSelectionModel().select(null);
        txtidReservas.setText(null);
        txtNombreCliente.setText(null);
        txtApellidoCliente.setText(null);
        txtNumeroPersona.setText(null);
        dpFecha.setValue(null);
        txtHora.setText(null);
    }

 }

