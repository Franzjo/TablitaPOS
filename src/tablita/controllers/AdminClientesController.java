package tablita.controllers;


import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import tablita.CreadorEntityManager;
import tablita.persistencia.Clientes;
import tablita.persistencia.JPAControllers.ClientesJpaController;
import tablita.persistencia.JPAControllers.exceptions.IllegalOrphanException;
import tablita.persistencia.JPAControllers.exceptions.NonexistentEntityException;
import javax.persistence.EntityManagerFactory;
import java.util.Optional;

/**
 * Created by Rey on 07/12/2015.
 */
public class AdminClientesController {

    EntityManagerFactory emf = CreadorEntityManager.emf();
    ClientesJpaController cjpa = new ClientesJpaController(emf);

    @FXML
    TableView viewClientes;
    @FXML
    TableColumn idClientes;
    @FXML
    TableColumn nombre;
    @FXML
    TableColumn apellido;
    @FXML
    TableColumn numeroTelefono;
    @FXML
    Button btnnuevo;
    @FXML
    Button btneditar;
    @FXML
    Button btneliminar;
    @FXML
    Button btninicio;

    private ObservableList<Clientes> clientes = FXCollections.observableArrayList(cjpa.findClientesEntities());

    public  AdminClientesController() {
    }

    @FXML
    private void initialize(){
        idClientes.setCellValueFactory( new PropertyValueFactory<Clientes, String>("idClientes"));
        nombre.setCellValueFactory( new PropertyValueFactory<Clientes, String>("nombre"));
        apellido.setCellValueFactory( new PropertyValueFactory<Clientes, String>("apellido"));
        numeroTelefono.setCellValueFactory( new PropertyValueFactory<Clientes, Integer>("numeroTelefono"));
        viewClientes.setItems(clientes);


        btnnuevo.setOnAction(event -> {
            NuevoClienteDialog dialog = new NuevoClienteDialog();
            Optional<Clientes> result = dialog.showAndWait();
            final Clientes[] nuevoCliente = {new Clientes()};
            result.ifPresent(cliente -> {
                nuevoCliente[0] = cliente;
                cjpa.create(nuevoCliente[0]);
                clientes = FXCollections.observableArrayList(cjpa.findClientesEntities());
                viewClientes.setItems(clientes);
            });

        });

        btninicio.setOnAction(event1 -> {
            viewClientes.setItems(clientes);
        });

        btneditar.setOnAction(event -> {

            Clientes cl ;
            if (viewClientes.getSelectionModel().getSelectedIndex() > -1 ) {
                cl = (Clientes) viewClientes.getSelectionModel().getSelectedItem();
            } else {
                return;
            }

            UpdateClienteDialog dialog = new UpdateClienteDialog(
                    cl.getNombre(),
                    cl.getApellido(),
                    String.valueOf(cl.getNumeroTelefono()),
                    cl.getIdClientes());
            Optional<Clientes> result = dialog.showAndWait();
            final Clientes[] Client = {new Clientes()};
            result.ifPresent(client -> {
                Client[0] = client;
                try {
                    System.out.println(Client[0].getNombre());
                    System.out.println(Client[0].getApellido());
                    System.out.println(Client[0].getNumeroTelefono());
                    cjpa.edit(Client[0]);
                } catch (NonexistentEntityException c){
                    c.printStackTrace();
                } catch (IllegalOrphanException c) {
                    c.printStackTrace();
                } catch (Exception c) {
                    c.printStackTrace();
                }

            });
            actualizartabla();
        });

        btneliminar.setOnAction(event -> {
            delete();
        });

    }

    private void delete(){
        int reservas = viewClientes.getSelectionModel().getSelectedIndex();
        viewClientes.getItems().remove(reservas);
    }


    private void actualizartabla() {
        clientes = FXCollections.observableArrayList(cjpa.findClientesEntities());
        viewClientes.getItems().clear();
        viewClientes.setItems(clientes);
    }

    private class UpdateClienteDialog extends NuevoClienteDialog {

        public UpdateClienteDialog (String nombre, String apellido, String numeroTelefono, Integer id){
            super();
            this.nombre.setText(nombre);
            this.apellido.setText(apellido);
            this.numeroTelefono.setText(numeroTelefono);

            this.setResultConverter(dialogButton  -> {
                if (dialogButton == btnCrear) {
                    Clientes c = new Clientes();
                    c.setIdClientes(id);
                    c.setNombre(this.nombre.getText());
                    c.setApellido(this.apellido.getText());
                    c.setNumeroTelefono(Integer.valueOf(this.numeroTelefono.getText()));
                    return c;
                }
                return null;
            });
        }
    }

        private class NuevoClienteDialog extends Dialog<Clientes> {

            ButtonType btnCrear = new ButtonType("Crear", ButtonBar.ButtonData.OK_DONE);
            GridPane grid = new GridPane();

            TextField nombre = new TextField();
            TextField apellido = new TextField();
            TextField numeroTelefono = new TextField();

            public NuevoClienteDialog() {
                this.setTitle("Registrar Cliente");
                this.setHeaderText("Registrar un Cliente nuevo");

                this.getDialogPane().getButtonTypes().addAll(btnCrear, ButtonType.CANCEL);

                grid.setHgap(10);
                grid.setVgap(10);
                grid.setPadding(new Insets(20, 150, 10, 10));

                nombre.setPromptText("Nombre");
                apellido.setPromptText("Apellido");
                numeroTelefono.setPromptText("Teléfono");

                grid.add(new Label("Nombres"), 0, 0);
                grid.add(nombre, 1, 0);

                grid.add(new Label("Apellido"), 0,1);
                grid.add(apellido, 1,1);

                grid.add(new Label("Teléfono"), 0,2);
                grid.add(numeroTelefono, 1,2);

                Node nodeCrear = this.getDialogPane().lookupButton(btnCrear);
                nodeCrear.setDisable(true);

                nombre.textProperty().addListener((observable, oldValue, newValue) -> {
                    nodeCrear.setDisable(newValue.trim().isEmpty());
                });

                apellido.textProperty().addListener((observable, oldValue, newValue) ->  {
                   nodeCrear.setDisable(newValue.trim().isEmpty());
                });

                numeroTelefono.textProperty().addListener((observable, oldValue, newValue) -> {
                    nodeCrear.setDisable(newValue.trim().isEmpty());
                });

                this.getDialogPane().setContent(grid);

                Platform.runLater(() -> nombre.requestFocus());

                this.setResultConverter(dialogButton  -> {
                    if(dialogButton == btnCrear){
                        Clientes cli = new Clientes();
                        cli.setNombre(nombre.getText());
                        cli.setApellido(apellido.getText());
                        cli.setNumeroTelefono(Integer.valueOf(numeroTelefono.getText()));
                        return cli;
                    }
                    return null;
                });
        }
    }
}
