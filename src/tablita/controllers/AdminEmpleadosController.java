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
import tablita.CreadorEntityManager;
import tablita.RolesFactory;
import tablita.ViewsManager;
import tablita.persistencia.JPAControllers.UsuariosJpaController;
import tablita.persistencia.JPAControllers.exceptions.IllegalOrphanException;
import tablita.persistencia.JPAControllers.exceptions.NonexistentEntityException;
import tablita.persistencia.Usuarios;
import javax.persistence.EntityManagerFactory;
import java.io.IOException;
import java.util.Optional;

/**
 * Created by akino on 11-08-15.
 */
public class AdminEmpleadosController {

    EntityManagerFactory emf = CreadorEntityManager.emf();
    UsuariosJpaController ujpa = new UsuariosJpaController(emf);

    @FXML
    TableView viewEmpleados;
    @FXML
    TableColumn codigo;
    @FXML
    TableColumn nombres;
    @FXML
    TableColumn apellidos;
    @FXML
    TableColumn DUI;
    @FXML
    TableColumn NIT;
    @FXML
    TableColumn TEL;
    @FXML
    Button botonNuevo;
    @FXML
    Button botonInicio;
    @FXML
    Button botonActualizar;

    private ObservableList<Usuarios> empleados = FXCollections.observableArrayList(ujpa.findByRole(RolesFactory.empleado()));

    public AdminEmpleadosController() {
    }

    @FXML
    private void initialize(){
        codigo.setCellValueFactory(new PropertyValueFactory<Usuarios,String>("codigo"));
        nombres.setCellValueFactory(new PropertyValueFactory<Usuarios,String>("nombres"));
        apellidos.setCellValueFactory(new PropertyValueFactory<Usuarios,String>("apellidos"));
        DUI.setCellValueFactory(new PropertyValueFactory<Usuarios,Integer>("dui"));
        NIT.setCellValueFactory(new PropertyValueFactory<Usuarios,String>("nit"));
        TEL.setCellValueFactory(new PropertyValueFactory<Usuarios,Integer>("tel"));

        botonNuevo.setOnAction(event -> {
            NuevoEmpleadoDialog dialog = new NuevoEmpleadoDialog();
            Optional<Usuarios> result = dialog.showAndWait();
            final Usuarios[] nuevoEmpleado = {new Usuarios()};
            result.ifPresent(usuarios->{
                nuevoEmpleado[0] = usuarios;
                ujpa.create(nuevoEmpleado[0]);
                empleados = FXCollections.observableArrayList(ujpa.findByRole(RolesFactory.empleado()));
                viewEmpleados.setItems(empleados);
            });
        });

        botonActualizar.setOnAction(event -> {

            Usuarios u = (Usuarios) viewEmpleados.getSelectionModel().getSelectedItem();

            UpdateEmpleadoDialog dialog = new UpdateEmpleadoDialog(
                    u.getNombres(),
                    u.getApellidos(),
                    String.valueOf(u.getDui()),
                    u.getNit(),
                    String.valueOf(u.getTel()));
            Optional<Usuarios> result = dialog.showAndWait();
            final Usuarios[] empleado = {new Usuarios()};
            result.ifPresent(usuario -> {
                empleado[0] = usuario;
                try {
                    System.out.println(empleado[0].getNombres());
                    System.out.println(empleado[0].getApellidos());
                    System.out.println(empleado[0].getNit());
                    int id = ujpa.findByCodigo(empleado[0].getCodigo()).getIdUsuario();
                    empleado[0].setIdUsuario(id);
                    ujpa.edit(empleado[0]);
                } catch (NonexistentEntityException e) {
                    e.printStackTrace();
                } catch (IllegalOrphanException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }


            });
            empleados = FXCollections.observableArrayList(ujpa.findByRole(RolesFactory.empleado()));
            viewEmpleados.getItems().clear();
            viewEmpleados.setItems(empleados);

        });

        botonInicio.setOnAction(event -> {
            try {
                Parent parent = FXMLLoader.load(getClass().getResource("../views/administrador/VistaAdministrador.fxml"));
                ViewsManager.cambiarVentana("TablitaPOS", parent);
            } catch (IOException e) {
                e.printStackTrace();
            }

        });

        viewEmpleados.setItems(empleados);
    }

    private class UpdateEmpleadoDialog extends NuevoEmpleadoDialog{
        public UpdateEmpleadoDialog(String nombre, String apellidos, String dui, String nit, String tel) {
            super();
            this.nombres.setText(nombre);
            this.apellidos.setText(apellidos);
            this.dui.setText(dui);
            this.nit.setText(nit);
            this.tel.setText(tel);
            //botonCambiarMesa = new ButtonType("Actualizar", ButtonBar.ButtonData.OK_DONE);

            this.setResultConverter(dialogButton -> {
                if(dialogButton == botonCrear){
                    Usuarios u = new Usuarios();
                    //u.setCodigo(this.codigo(this.nombres.getText(),this.apellidos.getText(),this.nit.getText()));
                    u.setCodigo("oo");
                    u.setNombres(this.nombres.getText());
                    u.setApellidos(this.apellidos.getText());
                    u.setDui(Integer.valueOf(this.dui.getText()));
                    u.setNit(this.nit.getText());
                    u.setTel(Integer.valueOf(this.tel.getText()));
                    u.setIdRol(RolesFactory.empleado());
                    return u;
                }
                return null;
            });

        }
    }

    private class NuevoEmpleadoDialog extends Dialog<Usuarios> {

        ButtonType botonCrear = new ButtonType("Crear", ButtonBar.ButtonData.OK_DONE);
        GridPane grid = new GridPane();

        TextField nombres = new TextField();
        TextField apellidos = new TextField();
        TextField dui = new TextField();
        TextField nit = new TextField();
        TextField tel = new TextField();


        public NuevoEmpleadoDialog() {
            this.setTitle("Nuevo Empleado");
            this.setHeaderText("Crear un nuevo empleado");

            this.getDialogPane().getButtonTypes().addAll(botonCrear,ButtonType.CANCEL);

            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20,150,10,10));

            nombres.setPromptText("Nombres");
            apellidos.setPromptText("Apellidos");
            dui.setPromptText("DUI");
            nit.setPromptText("NIT");
            tel.setPromptText("TEL");

            grid.add(new Label("Nombre"),0,0);
            grid.add(nombres,1,0);

            grid.add(new Label("Apellidos"),0,1);
            grid.add(apellidos,1,1);

            grid.add(new Label("DUI"),0,2);
            grid.add(dui,1,2);

            grid.add(new Label("NIT"),0,3);
            grid.add(nit,1,3);

            grid.add(new Label("TEL"),0,4);
            grid.add(tel,1,4);

            Node nodeCrear = this.getDialogPane().lookupButton(botonCrear);
            nodeCrear.setDisable(true);

            nombres.textProperty().addListener((observable, oldValue, newValue) -> {
                nodeCrear.setDisable(newValue.trim().isEmpty());
            });

            apellidos.textProperty().addListener((observable, oldValue, newValue) -> {
                nodeCrear.setDisable(newValue.trim().isEmpty());
            });

            dui.textProperty().addListener((observable, oldValue, newValue) -> {
                nodeCrear.setDisable(newValue.trim().isEmpty());
            });

            nit.textProperty().addListener((observable, oldValue, newValue) -> {
                nodeCrear.setDisable(newValue.trim().isEmpty());
            });

            tel.textProperty().addListener((observable, oldValue, newValue) -> {
                nodeCrear.setDisable(newValue.trim().isEmpty());
            });

            this.getDialogPane().setContent(grid);

            Platform.runLater(()->nombres.requestFocus());

            this.setResultConverter(dialogButton -> {
                if(dialogButton == botonCrear){
                    Usuarios u = new Usuarios();
                    u.setCodigo(codigo(nombres.getText(),apellidos.getText(),nit.getText()));
                    u.setNombres(nombres.getText());
                    u.setApellidos(apellidos.getText());
                    u.setDui(Integer.valueOf(dui.getText()));
                    u.setNit(nit.getText());
                    u.setTel(Integer.valueOf(tel.getText()));
                    u.setIdRol(RolesFactory.empleado());
                    return u;
                }
                return null;
            });
        }

        public String codigo (String nombre, String apellido, String nit){
            String cod = String.valueOf(nombre.charAt(0));
            cod = cod + String.valueOf(apellido.charAt(0));
            cod = cod + nit.substring(4,8);
            return cod;
        }

    }
}
