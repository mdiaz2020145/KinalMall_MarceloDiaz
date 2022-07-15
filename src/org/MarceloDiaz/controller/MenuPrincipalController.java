package org.MarceloDiaz.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import org.MarceloDiaz.system.Principal;

/**
 *
* @author Marcelo Javier Diaz Pineda
* @date 5/05/2021
* @time 11:42:59
 */
public class MenuPrincipalController implements Initializable {
    private Principal escenarioPrincipal; 
    private static int rol; 
    @FXML
    private MenuItem menuAdministracion;
    @FXML
    private MenuBar menuBar;
    @FXML
    private Button btnCerrarSesion;
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        if(rol!=1){
            menuAdministracion.setDisable(true);
        }
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    @FXML
    public void ventanaAutor(){
        escenarioPrincipal.ventanaAutor(); 
    }
    @FXML
    public void administracion(){
        escenarioPrincipal.administracion();
    }
    @FXML
    public void empleado(){
        escenarioPrincipal.empleado();
    }
    @FXML
    public void proveedores(){
        escenarioPrincipal.proveedores();
    }
    @FXML
    public void clientes(){
        escenarioPrincipal.clientes();
    }

    public static int getRol() {
        return rol;
    }

    public static void setRol(int rol) {
        MenuPrincipalController.rol = rol;
    }

    @FXML
    private void cerrarSesion(ActionEvent event) {
        setRol(0);
        this.escenarioPrincipal.login();
    }
    

}
