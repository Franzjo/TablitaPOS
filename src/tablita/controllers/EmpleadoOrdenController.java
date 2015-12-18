package tablita.controllers;

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
import tablita.ServicioImpresion;
import tablita.Tipos;
import tablita.ViewsManager;
import tablita.persistencia.*;
import tablita.persistencia.JPAControllers.DetallesVentasJpaController;
import tablita.persistencia.JPAControllers.MesasJpaController;
import tablita.persistencia.JPAControllers.ProductosJpaController;
import tablita.persistencia.JPAControllers.VentasJpaController;
import tablita.persistencia.JPAControllers.exceptions.IllegalOrphanException;
import tablita.persistencia.JPAControllers.exceptions.NonexistentEntityException;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Creado por akino on 11-15-15.
 */
public class EmpleadoOrdenController {

    ProductosJpaController prodJpaCont = new ProductosJpaController(CreadorEntityManager.emf());
    VentasJpaController ventasJpaCont = new VentasJpaController(CreadorEntityManager.emf());
    DetallesVentasJpaController detVenJpaCont = new DetallesVentasJpaController(CreadorEntityManager.emf());
    MesasJpaController mesasJpaCont = new MesasJpaController(CreadorEntityManager.emf());


    // TODO: 11-29-15 LLEVAR A INICIO

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
    Button botonEnviar;

    @FXML
    Button botonCero;
    @FXML
    Button botonUno;
    @FXML
    Button botonDos;
    @FXML
    Button botonTres;
    @FXML
    Button botonCuatro;
    @FXML
    Button botonCinco;
    @FXML
    Button botonSeis;
    @FXML
    Button botonSiete;
    @FXML
    Button botonOcho;
    @FXML
    Button botonNueve;
    @FXML
    Button botonBack;
    @FXML
    Button botonClear;

    @FXML
    TextField cantidadText;
    @FXML
    Text productoDesc;
    @FXML
    Text totalText;
    @FXML
    Text idVentaText;
    @FXML
    Text horaText;
    @FXML
    Text mesaText;

    @FXML
    Button botonAdd;
    @FXML
    Button botonEliminar;


    @FXML
    TableView<ProductoVentaDTO> tablaProductos;
    @FXML
    TableColumn<ProductoVentaDTO, String> descripcion;
    @FXML
    TableColumn<ProductoVentaDTO, Integer> cantidad;
    @FXML
    TableColumn<ProductoVentaDTO, BigDecimal> total;



    @FXML
    ListView<String> productosListView;

    private String tipoActual = "";
    ObservableList<ProductoVentaDTO> ventaActual = FXCollections.observableList(new ArrayList<>());
    Ventas venta;
    private List<DetallesVentas> listaDetalles;
    int index = -1;
    boolean existente = false;

    public EmpleadoOrdenController() {}

    public void initNuevaVenta(int mesa){
        venta = new Ventas();

        venta.setIdMesa(new Mesas(mesa));
        venta.setIdUsuario(new Usuarios(1));
        venta.setActiva(true);
        listaDetalles = new ArrayList<>();
        venta.setDetallesVentasCollection(listaDetalles);
        ventasJpaCont.create(venta);
        venta.setFechaHora(Timestamp.valueOf(java.time.LocalDateTime.now()));

        Instant instant = venta.getFechaHora().toInstant();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yy HH:mm").withZone(ZoneId.systemDefault());
        horaText.setText(formatter.format(instant));
        idVentaText.setText(venta.getIdVentas().toString());
        mesaText.setText("Mesa: "+venta.getIdMesa().getNumeroMesa());
    }

    public void initExistente(Ventas v){
        List<DetallesVentas> dv = (List<DetallesVentas>) v.getDetallesVentasCollection();
        this.listaDetalles = dv;
        ventaActual = FXCollections.observableArrayList(
                dv.stream().map(detallesVentas -> new ProductoVentaDTO(
                        detallesVentas.getIdProductos(),
                        detallesVentas.getIdProductos().getItem(),
                        detallesVentas.getCantidad(),
                        detallesVentas.getIdProductos()
                                .getPrecio().multiply(new BigDecimal(detallesVentas.getCantidad())
                        ))).collect(Collectors.toList())
        );
        index = ventaActual.size();
        totalText.setText("Total: $"+total().toString());

        horaText.setText(v.getFechaHora().toString());
        idVentaText.setText(v.getIdVentas().toString());
        mesaText.setText("Mesa: "+v.getIdMesa().getNumeroMesa());

        existente = true;
        this.venta = v;
        tablaProductos.setItems(ventaActual);
    }

    @FXML
    private void initialize(){

        descripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        cantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        total.setCellValueFactory(new PropertyValueFactory<>("total"));

        botonEntrada.setOnAction(event -> {
            productosListView.setItems(productosList(Tipos.ENTRADA));
            limpiarCantidad();
        });
        botonFrias.setOnAction(event -> {
            productosListView.setItems(productosList(Tipos.BEBIDAS));
            limpiarCantidad();
        });
        botonPlatoFuerte.setOnAction(event -> {
            productosListView.setItems(productosList(Tipos.PLATOFUERTE));
            limpiarCantidad();
        });
        botonPostres.setOnAction(event -> {
            productosListView.setItems(productosList(Tipos.POSTRES));
            limpiarCantidad();
        });
        botonCombos.setOnAction(event -> {
            productosListView.setItems(productosList(Tipos.COMBOS));
            limpiarCantidad();
        });

        botonCero.setOnAction(event -> {
            if(!cantidadText.getText().isEmpty())
                setCantidadText(botonCero);
        });

        botonUno.setOnAction(event -> setCantidadText(botonUno));
        botonDos.setOnAction(event -> setCantidadText(botonDos));
        botonTres.setOnAction(event -> setCantidadText(botonTres));
        botonCuatro.setOnAction(event -> setCantidadText(botonCuatro));
        botonCinco.setOnAction(event -> setCantidadText(botonCinco));
        botonSeis.setOnAction(event -> setCantidadText(botonSeis));
        botonSiete.setOnAction(event -> setCantidadText(botonSiete));
        botonOcho.setOnAction(event -> setCantidadText(botonOcho));
        botonNueve.setOnAction(event -> setCantidadText(botonNueve));
        botonBack.setOnAction(event -> borrarUltimo());
        botonClear.setOnAction(event -> limpiarCantidad());


        productosListView.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            int seleccionado = (Integer) observable.getValue();
            if(seleccionado != -1){
                Productos p = prodJpaCont.getProductosByTipo(tipoActual).get(seleccionado);
                productoDesc.setText("$" + p.getPrecio().toString());
                cantidadText.setText("");
            }
        });

        botonAdd.setOnAction(event -> {
            Productos p = getSelectedProduct();
            if(p!=null){

                ProductoVentaDTO pvDto = new ProductoVentaDTO();
                int cantidad = getCantidad();
                pvDto.setIdProductos(p);
                pvDto.setDescripcion(p.getItem());
                pvDto.setCantidad(cantidad);
                pvDto.setTotal(getTotalProducto(p,cantidad));

                DetallesVentas dv = new DetallesVentas();
                dv.setCantidad(cantidad);
                dv.setIdProductos(p);
                dv.setIdVentas(venta);

                listaDetalles.add(dv);

                ventaActual.add(pvDto);

                System.out.println(total());

                totalText.setText("Total: $"+total().toString());

                venta.setTotal(total());

                cantidadText.setText("");

                tablaProductos.setItems(ventaActual);
            }
        });

        botonEliminar.setOnAction(eventEliminar -> {
            int indice = tablaProductos.getSelectionModel().getSelectedIndex();
            System.out.println(indice +" "+(index-1));
            if (indice != -1 && indice > index-1) {
                ventaActual.remove(indice);
                listaDetalles.remove(indice);
                totalText.setText("Total: $" + total().toString());
                tablaProductos.setItems(ventaActual);
            }
            if (indice <= index-1){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("El producto ya ha sido enviado");
                alert.setContentText("Este producto no se puede eliminar \n" +
                        "porque ya ha sido enviado y registrado");

                alert.showAndWait();
            }
        });

        botonCancelar.setOnAction(cancelarE -> {
            try {
                System.out.println("in");
                if(!existente){
                    ventasJpaCont.destroy(venta.getIdVentas());
                }
                goHome();

            } catch (IllegalOrphanException e) {
                e.printStackTrace();
            } catch (NonexistentEntityException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        botonEnviar.setOnAction(event -> {
            guardarVenta();
        });

        botonMesa.setOnAction(event -> {
            CambioMesaDialog dialog = new CambioMesaDialog();
            Optional<Mesas> result = dialog.showAndWait();

            result.ifPresent(mesas -> {
                venta.setIdMesa(mesas);
                mesaText.setText("Mesa: "+venta.getIdMesa().getNumeroMesa());
                try {
                    ventasJpaCont.edit(venta);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });
    }

    private BigDecimal total(){
        if(listaDetalles.isEmpty())
            return BigDecimal.ZERO;
        return listaDetalles.stream().map(ld -> ld.getIdProductos().getPrecio().multiply(new BigDecimal(ld.getCantidad())))
                .reduce(BigDecimal::add).get();
    }

    private void guardarVenta() {
        if(existente){
            for (DetallesVentas d: listaDetalles.subList(index,listaDetalles.size())) {
                detVenJpaCont.create(d);
            }
            try {
                ServicioImpresion.imprimirCocina(listaDetalles.subList(index,listaDetalles.size()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (!existente){
            for (DetallesVentas detallesVentas: listaDetalles) {
                detVenJpaCont.create(detallesVentas);
            }
            try {
                ServicioImpresion.imprimirCocina(listaDetalles);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        venta.setDetallesVentasCollection(listaDetalles);

        try {
            ventasJpaCont.edit(venta);
            goHome();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private BigDecimal getTotalProducto(Productos p, int cantidad){
        return p.getPrecio().multiply(new BigDecimal(cantidad));
    }

    private int getCantidad(){
        String cantidad = cantidadText.getText();
        if(!cantidad.isEmpty()){
            return Integer.valueOf(cantidad);
        }
        return 1;
    }

    private void setCantidadText(Button text){
        String texto = text.getText();
        String actual = cantidadText.getText();
        cantidadText.setText(actual+texto);
    }

    private void limpiarCantidad(){
        cantidadText.setText("");
        productoDesc.setText("");
    }

    private void borrarUltimo(){
        String actual = cantidadText.getText();
        if(!actual.isEmpty())
            cantidadText.setText(actual.substring(0,actual.length()-1));
    }

    private void setUno(){
        if(cantidadText.getText().isEmpty()||cantidadText.getText().equalsIgnoreCase("1"))
            cantidadText.setText("1");
    }

    private Productos getSelectedProduct(){
        int seleccionado = productosListView.getSelectionModel().getSelectedIndices().get(0);
        if(seleccionado != -1){
            return prodJpaCont.getProductosByTipo(tipoActual).get(seleccionado);
        }
        return null;
    }

    private ObservableList<String> productosList(String tipo){
        tipoActual = tipo;
        List<Productos> productosList = prodJpaCont.getProductosByTipo(tipo);
        return FXCollections.observableArrayList(
                productosList.stream()
                        .map(productos -> productos.getItem())
                        .collect(Collectors.toList()));
    }

    private void goHome() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../views/empleados/VistaEmpleado.fxml"));
        ViewsManager.cambiarVentana("TablitaPOS",root);
    }

    private class CambioMesaDialog extends  Dialog<Mesas>{
        ButtonType botonCambiarMesa = new ButtonType("Crear", ButtonBar.ButtonData.OK_DONE);
        GridPane grid = new GridPane();

        int mesaActual = venta.getIdMesa().getNumeroMesa();
        ListView<String> listaMesas = new ListView<>();
        int count = mesasJpaCont.getMesasCount();
        List<Integer> activas = ventasJpaCont.findMesasActivas();
        List<Integer> disponibles = new ArrayList<>();

        public CambioMesaDialog() {
            for (int i = 1; i <= count; i++) {
                if (!activas.contains(i)) {
                    disponibles.add(i);
                }
            }

            ObservableList<String> mesas = FXCollections.observableArrayList(disponibles.stream()
                    .map(integer -> "Mesa "+String.valueOf(integer)).collect(Collectors.toList()));

            listaMesas.setItems(mesas);

            listaMesas.setPrefSize(150,300);

            this.setTitle("Cambiar Mesa");
            this.setHeaderText("Cambiar Mesa");

            this.getDialogPane().getButtonTypes().addAll(botonCambiarMesa,ButtonType.CANCEL);

            grid.setHgap(10);
            grid.setVgap(10);

            grid.setPadding(new Insets(10,25,10,25));

            grid.add(listaMesas,0,0);

            Node nodeCambiar = this.getDialogPane().lookupButton(botonCambiarMesa);

            // TODO: 12-04-15 CAMBIAR A TRUE
            nodeCambiar.setDisable(false);

            listaMesas.selectionModelProperty().addListener((observable, oldValue, newValue) -> {
                nodeCambiar.setDisable(observable.getValue().getSelectedItems().isEmpty());
            });

            this.getDialogPane().setContent(grid);

            this.setResultConverter(dialogButton -> {
                if(dialogButton == botonCambiarMesa){
                    Mesas m = mesaSeleccionada();
                    if(m != null){
                        return m;
                    }
                }
                return null;
            });
        }

        private Mesas mesaSeleccionada(){
            int selectedMesa = Integer.valueOf(listaMesas.getSelectionModel().getSelectedItem().replaceAll("Mesa ",""));
            if(selectedMesa>-1){
                Mesas m = mesasJpaCont.findMesaByNumero(selectedMesa);
                return m;
            }
            return null;
        }
    }
}
