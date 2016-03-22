package tablita.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import tablita.CreadorEntityManager;
import tablita.Passwords;
import tablita.RolesFactory;
import tablita.ViewsManager;
import tablita.persistencia.JPAControllers.UsuariosJpaController;
import tablita.persistencia.Usuarios;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.prefs.Preferences;

/**
 * Creado por akino on 02-24-16.
 */
public class LoginController {
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
    Button botonEntrar;
    @FXML
    TextField usuarioText;
    @FXML
    PasswordField passwordField;

    @FXML
    MenuItem servidorItem;
    @FXML
    MenuItem bdItem;

    TextField textFieldActual;

    UsuariosJpaController usuariosJpa;

    public LoginController() {
        usuariosJpa = new UsuariosJpaController(CreadorEntityManager.emf());
    }

    public void initialize(){
        textFieldActual = usuarioText;

        botonCero.setOnAction(event -> setCantidadText(botonCero));
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

        usuarioText.setOnMouseClicked(event -> setActualText(usuarioText));
        passwordField.setOnMouseClicked(event -> setActualText(passwordField));

        botonEntrar.setOnAction(event -> login());

        servidorItem.setOnAction(e -> serverDialog());

    }

    private boolean login() {
        String userCode = usuarioText.getText();
        String password = passwordField.getText();

        Usuarios u = usuariosJpa.findByCodigo(userCode);
        System.out.printf("before");

        if(u != null){
            if(Passwords.isExpectedPassword(password.toCharArray(), u.getSalt(), u.getPass())){
                try {
                    System.out.printf("logged in");
                    Parent root = null;

                    if(u.getIdRol().getIdRol() == 1){
                        root = FXMLLoader.load(getClass().getResource("/tablita/views/empleados/VistaEmpleado.fxml"));
                        System.out.println("emp: "+RolesFactory.empleado().getIdRol()+"\n");
                        ViewsManager.cambiarVentana("TablitaPOS",root,800,600);
                        return true;
                    }

                    if(u.getIdRol().getIdRol() == 2){
                        root = FXMLLoader.load(getClass().getResource("/tablita/views/cajero/VistaCajeroIncio.fxml"));
                        System.out.println("cajero: "+RolesFactory.cajero().getIdRol()+"\n");
                        ViewsManager.cambiarVentana("TablitaPOS",root);
                        return true;
                    }

                    if(u.getIdRol().getIdRol() == 3){
                        root = FXMLLoader.load(getClass().getResource("/tablita/views/administrador/VistaAdministrador.fxml"));
                        System.out.println("emp: "+RolesFactory.admin().getIdRol()+"\n");
                        ViewsManager.cambiarVentana("Admin - TablitaPOS",root, 1024,768);
                        return true;
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            System.out.print("no logeado");
        }
        return false;
    }

    private void setActualText(TextField textField) {
        textFieldActual = textField;
    }

    private int getCantidad(){
        String cantidad = textFieldActual.getText();
        if(!cantidad.isEmpty()){
            return Integer.valueOf(cantidad);
        }
        return 1;
    }

    private void setCantidadText(Button text){
        String texto = text.getText();
        String actual = textFieldActual.getText();
        textFieldActual.setText(actual+texto);
    }

    private void limpiarCantidad(){
        textFieldActual.setText("");
    }

    private void borrarUltimo(){
        String actual = textFieldActual.getText();
        if(!actual.isEmpty())
            textFieldActual.setText(actual.substring(0,actual.length()-1));
    }

    private void serverDialog(){

        Preferences prefs = Preferences.userNodeForPackage(LoginController.class);
        final String HOST = "HOST";
        final String[] NODE = {"/com/tablita"};
        final String PUERTO = "PORT";
        final String USER = "USER";
        final String PASS = "PASS";

        Map<String, String> actuals = new HashMap<>();
        actuals.put("nombre", prefs.get(HOST,"localhost"));
        actuals.put("puerto", prefs.get(PUERTO,"3303"));
        actuals.put("user", prefs.get(USER, "tablita"));
        actuals.put("pass", prefs.get(PASS, "isss"));

        ServerDiaglog serverDiaglog = new ServerDiaglog(actuals);
        Optional<Map<String, String>> result = serverDiaglog.showAndWait();

        final String[] nombre = {""};
        final String[] port = {""};
        final String[] user = {""};
        final String[] pass = {""};

        Map<String, String>[] opciones = new Map[]{};
        result.ifPresent(stringStringMap -> {
            nombre[0] = stringStringMap.get("nombre");
            port[0] = stringStringMap.get("puerto");
            user[0] = stringStringMap.get("user");
            pass[0] = stringStringMap.get("pass");
        });

        prefs.put(HOST, nombre[0]);
        prefs.put(PUERTO, port[0]);
        prefs.put(USER, user[0]);
        prefs.put(PASS, pass[0]);

    }

    private void DataBaseDialog (){

    }

    private class ServerDiaglog extends Dialog<Map<String,String>>{
        ButtonType btnCrear = new ButtonType("Aceptar", ButtonBar.ButtonData.OK_DONE);
        GridPane grid = new GridPane();

        TextField nombre = new TextField();
        TextField puerto = new TextField();
        TextField user = new TextField();
        PasswordField pass = new PasswordField();

        Preferences prefs = Preferences.userNodeForPackage(LoginController.class);


        public ServerDiaglog(Map<String,String> values) {
            this.setTitle("Configuración sobre el servidor");
            this.setHeaderText("Host servidor");
            this.getDialogPane().getButtonTypes().addAll(btnCrear,ButtonType.CANCEL);

            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20,150,10,10));

            nombre.setPromptText("Nombre del host");
            nombre.setText(values.get("nombre"));
            grid.add(new Label("Host"), 0, 0);
            grid.add(nombre, 1, 0);

            puerto.setPromptText("Puerto");
            puerto.setText(values.get("puerto"));
            grid.add(new Label("Puerto"), 0, 1);
            grid.add(puerto, 1, 1);

            user.setPromptText("Usuario");
            user.setText(values.get("user"));
            grid.add(new Label("Usuario"), 0, 2);
            grid.add(user, 1, 2);

            pass.setPromptText("Contraseña");
            pass.setText(values.get("pass"));
            grid.add(new Label("Contraseña"), 0, 3);
            grid.add(pass, 1, 3);

            Node nodeCrear = this.getDialogPane().lookupButton(btnCrear);
            nodeCrear.setDisable(true);

            nombre.textProperty().addListener(((observable, oldValue, newValue) -> {
                nodeCrear.setDisable(newValue.trim().isEmpty());
            }));

            puerto.textProperty().addListener(((observable, oldValue, newValue) -> {
                nodeCrear.setDisable(newValue.trim().isEmpty());
            }));

            this.getDialogPane().setContent(grid);

            Platform.runLater(() ->nombre.requestFocus());

            this.setResultConverter(dialogButton -> {
                if(dialogButton == btnCrear){
                    Map<String,String> opciones = new HashMap<>();
                    opciones.put("nombre", nombre.getText());
                    opciones.put("puerto", puerto.getText());
                    opciones.put("user", user.getText());
                    opciones.put("pass", pass.getText());
                    return opciones;
                }
                return null;
            });
        }
    }
}
