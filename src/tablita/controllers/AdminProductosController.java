package tablita.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import tablita.CreadorEntityManager;
import tablita.Tipos;
import tablita.persistencia.JPAControllers.ProductosJpaController;
import tablita.persistencia.JPAControllers.exceptions.IllegalOrphanException;
import tablita.persistencia.JPAControllers.exceptions.NonexistentEntityException;
import tablita.persistencia.Productos;
import javax.persistence.EntityManagerFactory;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Created by china on 18/11/2015.
 */
public class AdminProductosController {

    EntityManagerFactory emf = CreadorEntityManager.emf();
    ProductosJpaController ujpa = new ProductosJpaController(emf);


    @FXML
    TableView viewProductos;
    @FXML
    TableColumn item;
    @FXML
    TableColumn precio;
    @FXML
    TableColumn tipo;
    @FXML
    TableColumn existencia;
    @FXML
    TableColumn gravado;
    @FXML
    Button btnnuevo;
    @FXML
    Button btneditar;
    @FXML
    Button btneliminar;
    @FXML
    Button btninicio;


    private ObservableList<Productos> productos = FXCollections.observableArrayList(ujpa.findProductosEntities());

    public AdminProductosController(){
    }

    @FXML
    private void initialize(){
        item.setCellValueFactory(new PropertyValueFactory<Productos,String>("item"));
        precio.setCellValueFactory( new PropertyValueFactory<Productos, DecimalFormat>("precio"));
        tipo.setCellValueFactory( new PropertyValueFactory<Productos, String>("tipo"));
        existencia.setCellValueFactory( new PropertyValueFactory<Productos, Integer>("existencia"));
        gravado.setCellValueFactory( new PropertyValueFactory<Productos,String>("gravado"));

        viewProductos.setItems(productos);

        btnnuevo.setOnAction(event ->  {
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

        btneditar.setOnAction(event -> {
            Productos pr;
            if(viewProductos.getSelectionModel().getSelectedIndex()> -1 ) {
                 pr = (Productos) viewProductos.getSelectionModel().getSelectedItem();
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
                } catch (NonexistentEntityException p) {
                    p.printStackTrace();
                } catch (IllegalOrphanException p) {
                    p.printStackTrace();
                } catch (Exception p) {
                    p.printStackTrace();
                }

            });

            actualizarTabla();

        });

        btneliminar.setOnAction( event -> {
            int producto = viewProductos.getSelectionModel().getSelectedIndex();
            Productos pr= (Productos) viewProductos.getSelectionModel().getSelectedItem();
            productos.remove(producto);
        });


    }

    private void actualizarTabla() {
        productos = FXCollections.observableArrayList(ujpa.findProductosEntities());
        viewProductos.getItems().clear();
        viewProductos.setItems(productos);
    }

    private class UpdateProductoDialog extends  NuevoProductoDialog{

            public UpdateProductoDialog(String item, BigDecimal precio,  String tipo, Boolean gravado, Integer id) {
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
            existencia.textProperty().addListener(((observable, oldValue, newValue) -> {
                nodeCrear.setDisable(newValue.trim().isEmpty());
            }));

            this.getDialogPane().setContent(grid);

            Platform.runLater(() ->item.requestFocus());

            this.setResultConverter(dialogButton -> {
                if(dialogButton == btnCrear){
                    Productos p = new Productos();
                    p.setItem(item.getText());
                    p.setPrecio(BigDecimal.valueOf(Long.parseLong(precio.getText())));
                    p.setGravado(gravado.isSelected());
                    p.setTipo(tipo.getSelectionModel().getSelectedItem());
                    return p;
               }
                return null;
            });
        }

    }


}
