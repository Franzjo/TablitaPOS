package tablita.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import tablita.*;
import tablita.persistencia.JPAControllers.VentasJpaController;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

public class AdminMainController {


    private VentasJpaController ventasJpa;

    private ReportesDatos reportesDatos;


    @FXML
    private Pane root;

    @FXML
    private ToggleButton semanaBtn;

    @FXML
    private ToggleGroup diario;

    @FXML
    private ToggleButton mensualBtn;

    @FXML
    private CheckBox esteMesChk;

    @FXML
    private ComboBox<String> mesesDropDown;

    @FXML
    private Label hoyText;

    @FXML
    private Label propHoy;

    @FXML
    private Label semanaText;

    @FXML
    private Label mesText;

    @FXML
    private Label anioText;

    @FXML
    private Button botonInicio;

    @FXML
    private Button botonEmpleados;

    @FXML
    private Button botonProductos;

    @FXML
    private Button botonClientes;

    @FXML
    private CheckBox esteAnioChk;

    @FXML
    private TextField anioTextFiel;

    @FXML
    private Button dibujarBtn;

    @FXML
    private HBox graficoBox;

    @FXML
    private ToggleButton mesBtn;

    private Grafico grafico;

    private ArrayList<String> mesesStr = new ArrayList<>(Arrays.asList("Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul","Ago","Sep","Oct","Nov","Dic"));

    private ObservableList<String> meses = FXCollections.observableList(mesesStr);

    public AdminMainController() {
        ventasJpa = new VentasJpaController(CreadorEntityManager.emf());
        reportesDatos = new ReportesDatos();
    }

    public void initialize() {
        System.out.println("reportesDatos = " + reportesDatos.totalDiario());
        hoyText.setText("$"+String.valueOf(reportesDatos.totalDiario()));
        propHoy.setText("("+ reportesDatos.propinaTotalDiaria().toString()+")");
        semanaText.setText("$" + reportesDatos.totalSemana());
        mesText.setText("$" + reportesDatos.totalMes());
        anioText.setText("$" + reportesDatos.totalAnual());

        generarDatos("diarioSemanal");

        semanaBtn.setOnAction(e->generarDatos("diarioSemanal"));

        mensualBtn.setOnAction(e->generarDatos("diarioMensual"));

        mesesDropDown.setItems(meses);

        esteMesChk.setDisable(true);
        mesesDropDown.setDisable(true);
        esteAnioChk.setDisable(true);

        esteMesChk.setOnAction(e -> dibujarMeses());

        esteAnioChk.setOnAction(e -> dibujarAnios());

        mesesDropDown.setOnAction(e -> generarDatos("diarioMensual"));

        mesBtn.setOnAction( e -> generarDatos("mensual"));

        dibujarBtn.setOnAction(e -> generarDatos("mensual"));


        /**
         * Botones menu izquierdo
         */

        botonClientes.setOnAction(e -> {
            try {
                irAClientes();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        botonEmpleados.setOnAction(e -> {
            try {
                irAEmpleados();
            } catch (IOException e1) {


            }
        });

        botonProductos.setOnAction(e -> {
            try {
                irAProductos();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });


    }


    /*******************
     * GRAFICOS VENTAS *
     *******************/

    //region Graficos Ventas

    private void dibujarAnios() {
        if(esteAnioChk.isSelected()){
            setDisabledAnios(true);
            generarDatos("mensual");
        } else {
            setDisabledAnios(false);
        }
    }

    private void dibujarMeses() {
        if(esteMesChk.isSelected()){
            mesesDropDown.setDisable(true);
            generarDatos("diarioMensual");
        } else {
            mesesDropDown.setDisable(false);
        }
    }


    private void generarDatos(String tipo){
        switch (tipo){
            case "diarioSemanal":
                diarioSemanal();
                break;
            case "diarioMensual":
                diarioMensual(esteMesChk.isSelected());
                break;
            case "mensual":
                mensualAnual(esteAnioChk.isSelected());
                break;
        }
    }

    private void mensualAnual(boolean esteAnio) {
        setDisableMeses(true);
        esteAnioChk.setDisable(false);
        int anio = LocalDateTime.now().getYear();

        if(esteAnio){
            grafico = new Grafico(reportesDatos.totalMensualAnual(),TipoGrafico.MENSUAL);
        } else if(!esteAnio & !anioTextFiel.getText().isEmpty()) {
            try {
                anio = Integer.parseInt(anioTextFiel.getText());
            } catch (NumberFormatException ex){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("Año incorrecto");
                alert.setContentText("Por favor introduzca un año valido");
                setCss(alert.getDialogPane());
                alert.showAndWait();
            } finally {
                grafico = new Grafico(reportesDatos.totalMensualAnual(anio), TipoGrafico.MENSUAL);
            }
        }
        grafico.setLabelX("Días");
        grafico.setLabelY("Cantidad vendida");
        grafico.setTitulo("Ventas totales mensuales para el año "+anio);
        grafico.setNombreSerie("Ventas");
        actualizarChart(grafico.getLineas());

    }

    private void diarioMensual(boolean esteMes) {
        esteMesChk.setDisable(false);
        setDisabledAnios(true);

        if(esteMes) {
            grafico = new Grafico(reportesDatos.totalDiarioMensual(0, true), TipoGrafico.DIARIO_MES);
        } else  {

            grafico = new Grafico(reportesDatos.totalDiarioMensual(mesesDropDown.getSelectionModel().getSelectedIndex() + 1, false), TipoGrafico.DIARIO_MES);
        }
        grafico.setLabelX("Días");
        grafico.setLabelY("Cantidad vendida");
        grafico.setTitulo("Ventas totales diaras para este mes");
        grafico.setNombreSerie("Ventas");
        actualizarChart(grafico.getLineas());
    }

    private void diarioSemanal() {
        setDisableMeses(true);
        setDisabledAnios(true);
        grafico = new Grafico(reportesDatos.totalDiarioSemana(),TipoGrafico.SEMANAL);
        grafico.setLabelX("Días");
        grafico.setLabelY("Cantidad vendida");
        grafico.setTitulo("Ventas totales diaras para esta semana");
        grafico.setNombreSerie("Ventas");
        actualizarChart(grafico.getLineas());
    }

    private void actualizarChart(LineChart lineChart){
        graficoBox.getChildren().clear();
        graficoBox.getChildren().addAll(lineChart);
    }

    private void setDisableMeses(boolean estado){
        esteMesChk.setDisable(estado);
        mesesDropDown.setDisable(estado);
    }

    private void setDisabledAnios (boolean estado){
        esteAnioChk.setDisable(estado   );
        anioTextFiel.setDisable(estado);
        dibujarBtn.setDisable(estado);

    }
    //endregion





    private void irAEmpleados() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/tablita/views/administrador/VistaAdministradorEmpleado.fxml"));
        ViewsManager.cambiarVentana("TablitaPOS - Empleados",root);
    }
    private void irAProductos() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/tablita/views/administrador/VistaAdministradorProductos.fxml"));
        ViewsManager.cambiarVentana("TablitaPOS - Productos",root);
    }
    private void irAClientes() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/tablita/views/administrador/VistaAdministradorReserva.fxml"));
        ViewsManager.cambiarVentana("TablitaPOS - Clientes",root);
    }

    private void setCss(DialogPane dialogPane){
        DialogPane pane = dialogPane;
        pane.getStylesheets().add(
                getClass().getResource("/tablita/views/administrador/iconos/estilo.css").toExternalForm());
        pane.getStyleClass().add("myDialog");
    }
}
