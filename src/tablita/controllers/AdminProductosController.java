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
import javafx.scene.layout.HBox;
import tablita.CreadorEntityManager;
import tablita.Tipos;
import tablita.ViewsManager;
import tablita.persistencia.JPAControllers.ProductosJpaController;
import tablita.persistencia.JPAControllers.exceptions.IllegalOrphanException;
import tablita.persistencia.JPAControllers.exceptions.NonexistentEntityException;
import tablita.persistencia.Productos;

import javax.persistence.EntityManagerFactory;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class AdminProductosController {


    EntityManagerFactory emf = CreadorEntityManager.emf();
    ProductosJpaController ujpa = new ProductosJpaController(emf);

    @FXML
    private HBox contentHbox;

    @FXML
    TableView<Productos> viewProductos;
    @FXML
    TableColumn<Productos, String> item;
    @FXML
    TableColumn<Productos, DecimalFormat> precio;
    @FXML
    TableColumn<Productos, String> tipo;
//    @FXML
//    TableColumn<Productos, Integer> existencia;
    @FXML
    TableColumn<Productos, String> gravado;

    @FXML
    private Button nuevoProductoBtn;

    @FXML
    private Button editarProductoBtn;

    @FXML
    private Button eliminarProductoBtn;

    @FXML
    private Button botonProductos;

    @FXML
    private Button botonClientes;

    @FXML
    private Button botonInicio;

    @FXML
    private Button botonEmpleados;

    @FXML
    private ToggleButton productosTgl;

//    @FXML
//    private ToggleGroup productos;

    @FXML
    private ToggleButton estadisticasTgl;




    private ObservableList<Productos> productos = FXCollections.observableArrayList(ujpa.findProductosEntities());

    public AdminProductosController(){
    }

    @FXML
    private void initialize(){
        item.setCellValueFactory(new PropertyValueFactory<>("item"));
        precio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        tipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
//        existencia.setCellValueFactory(new PropertyValueFactory<>("existencia"));
        gravado.setCellValueFactory(new PropertyValueFactory<>("gravado"));

        viewProductos.setItems(productos);

        nuevoProductoBtn.setOnAction(event ->  {
            NuevoProductoDialog dialog = new NuevoProductoDialog();
            Optional<Productos> result = dialog.showAndWait();
            final Productos[] nuevoProducto = {new Productos()};
            result.ifPresent(productos ->{
                nuevoProducto[0] = productos;
                ujpa.create(nuevoProducto[0]);



            });
            productos = FXCollections.observableArrayList(ujpa.findProductosEntities());
            viewProductos.setItems(productos);
        });

        editarProductoBtn.setOnAction(event -> {
            Productos pr;
            if(viewProductos.getSelectionModel().getSelectedIndex()> -1 ) {
                pr = viewProductos.getSelectionModel().getSelectedItem();
            } else {
                return;
            }

            UpdateProductoDialog dialog = new UpdateProductoDialog(
                    pr.getItem(),
                    pr.getPrecio(),
                    pr.getTipo(),
                    pr.getGravado(),
                    pr.getIdProductos());
            Optional<Productos> result = dialog.showAndWait();
            final Productos[] productos = {new Productos()};
            result.ifPresent(producto -> {
                productos[0] = producto;
                try {
                    System.out.println(productos[0].getItem());
                    System.out.println(productos[0].getPrecio());
                    System.out.println(productos[0].getGravado());
                    System.out.println(productos[0].getTipo());
                    ujpa.edit(productos[0]);
                } catch (NonexistentEntityException | IllegalOrphanException p) {
                    p.printStackTrace();
                } catch (Exception p) {
                    p.printStackTrace();
                }

            });

            actualizarTabla();

        });

        eliminarProductoBtn.setOnAction( event -> {
            int producto = viewProductos.getSelectionModel().getSelectedIndex();
//            Productos pr= viewProductos.getSelectionModel().getSelectedItem();
            productos.remove(producto);
        });

        botonClientes.setOnAction(e -> irAClientes());
        botonEmpleados.setOnAction(e -> irAEmpleados());
        botonInicio.setOnAction(e -> irAInicio());

    }

    private void actualizarTabla() {
        productos = FXCollections.observableArrayList(ujpa.findProductosEntities());
        viewProductos.getItems().clear();
        viewProductos.setItems(productos);
    }

    private class UpdateProductoDialog extends  NuevoProductoDialog{

        public UpdateProductoDialog(String item, BigDecimal precio, String tipo, Boolean gravado, Integer id) {
            super();
            this.item.setText(item);
            this.precio.setText(precio.toString());
            this.tipo.setValue(tipo);
            this.gravado.setSelected(gravado);

            this.setResultConverter(dialogButton  -> {
                if(dialogButton == btnCrear){
                    Productos pro = new Productos();
                    pro.setIdProductos(id);
                    pro.setItem(this.item.getText());
                    pro.setPrecio( new BigDecimal(this.precio.getText()));
                    pro.setGravado(this.gravado.isSelected());
                    pro.setTipo(this.tipo.getSelectionModel().getSelectedItem());
                    return pro;
                }
                return null;
            });
        }
    }

    private class NuevoProductoDialog extends Dialog<Productos> {

        ButtonType btnCrear = new ButtonType("Crear", ButtonBar.ButtonData.OK_DONE);
        GridPane grid = new GridPane();

        TextField item = new TextField();
        TextField precio = new TextField();

        ComboBox<String> tipo = new ComboBox<>();
        CheckBox gravado = new CheckBox();



        public NuevoProductoDialog() {
            this.setTitle("Nuevo Producto");
            this.setHeaderText("Registrar un nuevo producto");

            this.getDialogPane().getButtonTypes().addAll(btnCrear,ButtonType.CANCEL);

            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20,150,10,10));

            item.setPromptText("Item");
            precio.setPromptText("Precio");
            gravado.setSelected(true);
            tipo.setPromptText("Tipo");

            grid.add(new Label("Item"), 0, 0);
            grid.add(item, 1, 0);

            grid.add(new Label("Precio"), 0, 1);
            grid.add(precio, 1, 1);

            grid.add(new Label("Gravado"), 0, 2);
            grid.add(gravado, 1, 2);

            grid.add(new Label("Tipo"), 0, 4);
            grid.add(tipo, 1, 4);

            List<String> tipos =
                    Arrays.asList(Tipos.BEBIDAS, Tipos.COMBOS, Tipos.ENTRADA,
                            Tipos.PLATOFUERTE, Tipos.BEBIDAS, Tipos.POSTRES);

            tipo.setItems(FXCollections.observableList(tipos));

            Node nodeCrear = this.getDialogPane().lookupButton(btnCrear);
            nodeCrear.setDisable(true);

            item.textProperty().addListener(((observable, oldValue, newValue) -> {
                nodeCrear.setDisable(newValue.trim().isEmpty());
            }));

            precio.textProperty().addListener((observable, oldValue, newValue) -> {
                nodeCrear.setDisable(newValue.trim().isEmpty());
            });

            gravado.textProperty().addListener(((observable, oldValue, newValue) -> {
                nodeCrear.setDisable(newValue.trim().isEmpty());
            }));
//            existencia.textProperty().addListener(((observable, oldValue, newValue) -> {
//                nodeCrear.setDisable(newValue.trim().isEmpty());
//            }));

            this.getDialogPane().setContent(grid);

            Platform.runLater(() ->item.requestFocus());

            this.setResultConverter(dialogButton -> {
                if(dialogButton == btnCrear){
                    Productos p = new Productos();
                    p.setItem(item.getText());
                    p.setPrecio(BigDecimal.valueOf(Double.valueOf(precio.getText())));
                    p.setGravado(gravado.isSelected());
                    p.setTipo(tipo.getSelectionModel().getSelectedItem());
                    return p;
                }
                return null;
            });
        }

    }

    private void irAClientes() {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/tablita/views/administrador/VistaAdministradorReserva.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        ViewsManager.cambiarVentana("TablitaPOS - Clientes",root);
    }

    private void irAInicio(){
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/tablita/views/administrador/VistaAdministradorReserva.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        ViewsManager.cambiarVentana("TablitaPOS",root);
    }

    private void irAEmpleados(){
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/tablita/views/administrador/VistaAdministradorEmpleado.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        ViewsManager.cambiarVentana("TablitaPOS - Empleados",root);
    }
}
