package tablita.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import tablita.CreadorEntityManager;
import tablita.Dtos.ProductoVentaDTO;
import tablita.Preferencias;
import tablita.ServicioImpresion;
import tablita.ViewsManager;
import tablita.persistencia.DetallesVentas;
import tablita.persistencia.JPAControllers.VentasJpaController;
import tablita.persistencia.Ventas;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by akino on 11-25-15.
 */
public class CajeroOrdenController {

    VentasJpaController ventasJpa = new VentasJpaController(CreadorEntityManager.emf());

    @FXML
    TableView<ProductoVentaDTO> listaProductos;
    @FXML
    TableColumn<ProductoVentaDTO, String> descripcion;
    @FXML
    TableColumn<ProductoVentaDTO, Integer> cantidad;
    @FXML
    TableColumn<ProductoVentaDTO, BigDecimal> unitario;
    @FXML
    TableColumn<ProductoVentaDTO, BigDecimal> total_actual;

    @FXML
    TableView<Ventas> mesasActivas;
    @FXML
    TableColumn<Ventas, String> mesa;
    @FXML
    TableColumn total;

    @FXML
    Button botonInicio;
    @FXML
    Button botonReservas;
    @FXML
    Button botonClientes;
    @FXML
    Button botonCobrar;
    @FXML
    Button botonImprimir;
    @FXML
    Button botonFormaPago;
    @FXML
    Text textTotal;
    @FXML
    Text subTotal;
    @FXML
    Text textPropina;
    @FXML
    Slider sliderTipo;


    private List<DetallesVentas> listaDetalles;
    private ObservableList<ProductoVentaDTO> ventaActual;
    private Ventas venta;

    @FXML
    private void initialize(){
        descripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        cantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        unitario.setCellValueFactory(new PropertyValueFactory<>("unitario"));
        total_actual.setCellValueFactory(new PropertyValueFactory<>("total"));

        //mesa.setCellValueFactory(new PropertyValueFactory<>("mesa"));
        //cantidad.setCellValueFactory(new PropertyValueFactory<Ventas, BigDecimal>("subTotal"));

        botonInicio.setOnAction(event -> { try { goHome(); } catch (IOException e) {e.printStackTrace(); }});
        botonCobrar.setOnAction(event -> cobrar());
        botonImprimir.setOnAction(event -> {
            try {
                ServicioImpresion.imprimirTicket(listaDetalles,venta);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void init(Ventas v) {
        List<DetallesVentas> dv = (List<DetallesVentas>) v.getDetallesVentasCollection();
        this.listaDetalles = dv;
        ventaActual = FXCollections.observableArrayList(
                dv.stream().map(detallesVentas -> new ProductoVentaDTO(
                        detallesVentas.getIdProductos(),
                        detallesVentas.getIdProductos().getItem(),
                        detallesVentas.getCantidad(),
                        detallesVentas.getIdProductos()
                                .getPrecio().multiply(new BigDecimal(detallesVentas.getCantidad())),
                        detallesVentas.getIdProductos().getPrecio()
                        )).collect(Collectors.toList()));

        //  TODO: 11-25-15 HORA MESA IDVENTA
        //horaText.setText(v.getFechaHora().toString());
        //idVentaText.setText(v.getIdVentas().toString());
        //mesaText.setText("Mesa: "+v.getIdMesa().getNumeroMesa());

        BigDecimal stot = subTotal();
        double val = (double) Preferencias.PROPINA/100;
        BigDecimal prop = stot.multiply(new BigDecimal(val)).setScale(2,RoundingMode.HALF_DOWN);
        BigDecimal tot = stot.add(prop).setScale(2, RoundingMode.HALF_DOWN);
        subTotal.setText("$ "+ stot.toString());
        textTotal.setText("$ " + tot.toString());
        textPropina.setText("$ " + prop.toString());

        v.setSubtotal(stot);
        v.setTotal(tot);
        v.setPropina(prop);

        this.venta = v;
        listaProductos.setItems(ventaActual);

        //ObservableList<Ventas> ventasActivas = FXCollections.observableArrayList(ventasJpa.findActivas());
        //mesasActivas.setItems(ventasActivas);
    }

    private BigDecimal subTotal() {
        if(listaDetalles.isEmpty())
            return BigDecimal.ZERO;
        return listaDetalles.stream().map(ld -> ld.getIdProductos().getPrecio().multiply(new BigDecimal(ld.getCantidad())))
                .reduce(BigDecimal::add).get();
    }

    private void cobrar(){
        venta.setActiva(false);
        venta.setFin(Timestamp.valueOf(java.time.LocalDateTime.now()));
        CobrarDialog cobrarDialog = new CobrarDialog(venta);
        Optional<Ventas> result = cobrarDialog.showAndWait();
        venta = result.get();
        if(sliderTipo.getValue() == 0){
            venta.setTipo("Efectivo");
        }else {
            venta.setTipo("Tarjeta");
        }
        try {
            ventasJpa.edit(venta);
        } catch (Exception e) {
            venta.setActiva(true);
            e.printStackTrace();
        }
    }

    private void goHome() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/tablita/views/cajero/VistaCajeroIncio.fxml"));
        ViewsManager.cambiarVentana("TablitaPOS",root);
    }

    private class CobrarDialog extends Dialog<Ventas>{
        ButtonType botonCobrar = new ButtonType("Cobrar", ButtonBar.ButtonData.OK_DONE);
        GridPane grid = new GridPane();

        TextField importe = new TextField();
        TextField subTotal, propina,total;
        Label cambio = new Label("Cambio: $");


        public CobrarDialog(Ventas v) {

            subTotal = new TextField(v.getSubtotal().toString());
            subTotal.setDisable(true);

            propina = new TextField(v.getPropina().toString());
            propina.setDisable(true);

            total = new TextField(v.getTotal().toString());
            total.setDisable(true);

            this.setTitle("Cobrar");
            this.setHeaderText("Introduzca el importe para esta venta");

            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(10,25,10,25));

            this.getDialogPane().getButtonTypes().addAll(botonCobrar, ButtonType.CANCEL);

            importe.setPromptText("Importe");

            grid.add(new Label("SubTotal"),0,0);
            grid.add(subTotal,1,0);

            grid.add(new Label("Propina"),0,1);
            grid.add(propina,1,1);

            grid.add(new Label("Total"),0,2);
            grid.add(total,1,2);

            grid.add(new Label("Importe"),0,3);
            grid.add(importe,1,3);

            grid.add(cambio,0,4);

            Node nodeCrear = this.getDialogPane().lookupButton(botonCobrar);
            nodeCrear.setDisable(true);

            importe.textProperty().addListener((observable, oldValue, newValue) -> {
                nodeCrear.setDisable(newValue.trim().isEmpty());
                cambio.setText("Cambio: $ "+cambio(v.getTotal(),new BigDecimal(importe.getText())).toString());
            });

            this.getDialogPane().setContent(grid);

            Platform.runLater(()->importe.requestFocus());


            this.setResultConverter(param -> {
                if(param == botonCobrar){
                    v.setImporte(getImporte(importe.getText()));
                    v.setCambio(cambio(v.getTotal(),v.getImporte()));
                    return v;
                }
                return null;
            });

        }


        BigDecimal getImporte(String importe){
            return new BigDecimal(importe);
        }

        BigDecimal cambio (BigDecimal total, BigDecimal importe){
            return importe.compareTo(total) == 1 ?
                    importe.subtract(total) :
                    BigDecimal.ZERO;
        }
    }
}
