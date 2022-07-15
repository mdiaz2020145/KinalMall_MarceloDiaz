/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Marcelo Javier Diaz Pineda
 * @date 5/05/2021
 * @time 11:27:58
 */
package org.MarceloDiaz.system;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.MarceloDiaz.controller.AdministracionController;
import org.MarceloDiaz.controller.AutorController;
import org.MarceloDiaz.controller.CargosController;
import org.MarceloDiaz.controller.ClientesController;
import org.MarceloDiaz.controller.CobrosAdminController;
import org.MarceloDiaz.controller.CuentasPorCobrarController;
import org.MarceloDiaz.controller.CuentasporPagarController;
import org.MarceloDiaz.controller.DepartamentosController;
import org.MarceloDiaz.controller.EmpleadoController;
import org.MarceloDiaz.controller.HorariosController;
import org.MarceloDiaz.controller.LocalesController;
import org.MarceloDiaz.controller.LoginController;
import org.MarceloDiaz.controller.MenuPrincipalController;
import org.MarceloDiaz.controller.ProveedoresController;
import org.MarceloDiaz.controller.TipoClientesController;
import java.util.Base64; 
public class Principal extends Application {

    private final String PAQUETE_VIEW = "/org/MarceloDiaz/view/";
    private final String PAQUETE_IMAGES = "/org/MarceloDiaz/resource/images/";
    private Stage escenarioPrincipal;
    private Scene escena;
    private int rol;

    public static void main(String[] args) {
        launch(args);
    }
    public boolean validar(ArrayList<TextField> listadoCampos, ArrayList<ComboBox> listaComboBox) {
        boolean respuesta = true;

        for(ComboBox comboBox: listaComboBox){
            if(comboBox.getSelectionModel().getSelectedItem() == null)
            return false;
        }

        for (TextField campoTexto : listadoCampos) {
            if (campoTexto.getText().trim().isEmpty()) {
                return false;
            }
        }
        return respuesta;
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.escenarioPrincipal = stage;
         //menuPrincipal();
         login();
         String password = "12345";
        String password64 = Base64.getEncoder().encodeToString(password.getBytes());
        System.out.println("Codificado: "+password64);
    }

    public void menuPrincipal() {
        try {
            MenuPrincipalController menuPrincipal = null;
            menuPrincipal = (MenuPrincipalController) cambiarEscena("MenuPrincipalView.fxml", 564, 322);
            menuPrincipal.setEscenarioPrincipal(this);
        } catch (Exception ex) {
            System.out.println("Se produjo un error.");
            ex.printStackTrace(); 
        }
    }

    public void ventanaAutor() {
        try {
            AutorController datosPersonales = null;
            datosPersonales = (AutorController) cambiarEscena("AutorView.fxml", 512, 263);
            datosPersonales.setEscenarioPrincipal(this);
        } catch (Exception ex) {
            System.out.println("Se produjo un error.");
            ex.printStackTrace(); 
        }
    }
    public void administracion(){
        try{
            AdministracionController adminController; 
            adminController = (AdministracionController) cambiarEscena("AdministracionView.fxml",857,438);
            adminController.setEscenarioPrincipal(this);
        }catch(Exception ex){
            System.out.println("Se produjo un error.");
            ex.printStackTrace();
        }
            
    }
    public void empleado(){
        try{
            EmpleadoController empleadoController;
            empleadoController =(EmpleadoController) cambiarEscena("EmpleadoView.fxml",1483,489);
            empleadoController.setEscenarioPrincipal(this);
        }catch(Exception ex){
            System.out.println("se produjo un error.");
            ex.printStackTrace();
        }
    }
    public void proveedores(){
        try{
            ProveedoresController proveedorController; 
            proveedorController = (ProveedoresController) cambiarEscena("ProveedoresView.fxml",1002,448);
            proveedorController.setEscenarioPrincipal(this);
        }catch(Exception ex){
           System.out.println("se produjo un error.");
           ex.printStackTrace();
        }
                
    }
    public void clientes(){
        try{
            ClientesController clienteController; 
            clienteController = (ClientesController) cambiarEscena("ClientesView.fxml",1062,622);
            clienteController.setEscenarioPrincipal(this);
        }catch(Exception ex){
           System.out.println("se produjo un error.");
           ex.printStackTrace();
        }
        
    }

    public void cobrosAdmin(){
        try{
            CobrosAdminController cobroAdmin; 
            cobroAdmin = (CobrosAdminController) cambiarEscena("CobrosAdministracionView.fxml",600,400);
            cobroAdmin.setEscenarioPrincipal(this);
        }catch(Exception ex){
            System.out.println("Se produjo un error.");
            ex.printStackTrace();
        }
    }
    public void departamentos(){
        try{
            DepartamentosController departamento; 
            departamento = (DepartamentosController) cambiarEscena("DepartamentosView.fxml",710,400);
            departamento.setEscenarioPrincipal(this);
        }catch(Exception ex){
            System.out.println("Se produjo un error.");
            ex.printStackTrace();
        }
    }
    public void tipoCliente(){
        try{
            TipoClientesController tipoClientes;
            tipoClientes = (TipoClientesController)cambiarEscena("TipoClientesView.fxml",700,400); 
            tipoClientes.setEscenarioPrincipal(this);
        }catch(Exception ex){
            System.out.println("Se produjo un error.");
            ex.printStackTrace();
        }
    }
    public void locales(){
        try{
            LocalesController locales;
            locales = (LocalesController)cambiarEscena("LocalesView.fxml",1076,451); 
            locales.setEscenarioPrincipal(this);
        }catch(Exception ex){
            System.out.println("Se produjo un error.");
            ex.printStackTrace();
        }
    }
    public void CuentasPorCobrar(){
        try{
            CuentasPorCobrarController cuentasPorCobrar;
            cuentasPorCobrar = (CuentasPorCobrarController)cambiarEscena("CuentasPorCobrarView.fxml",1076,451); 
            cuentasPorCobrar.setEscenarioPrincipal(this);
    }catch(Exception ex){
    System.out.println("Se produjo un error.");
            ex.printStackTrace();
}
    }
    public void Horarios(){
        try{
            HorariosController horarios; 
            horarios = (HorariosController)cambiarEscena("HorariosView.fxml",1076,451); 
            horarios.setEscenarioPrincipal(this);
        }catch(Exception ex){
             System.out.println("Se produjo un error.");
            ex.printStackTrace();
        }
    }
    
    public void CuentasPorPagar(){
        try{
            CuentasporPagarController cuentasPorPagar; 
            cuentasPorPagar = (CuentasporPagarController)cambiarEscena("CuentasPorPagarView.fxml",1076,451);
            cuentasPorPagar.setEscenarioPrincipal(this);
        }catch(Exception ex){
            System.out.println("Se produjo un error.");
            ex.printStackTrace();
        }
    }
    
    public void cargos(){
        try{
            CargosController cargos; 
            cargos = (CargosController)cambiarEscena("CargosView.fxml",700,400);
            cargos.setEscenarioPrincipal(this);
        }catch(Exception ex){
            System.out.println("Se produjo un error.");
            ex.printStackTrace();
        }
    }
    
    public void login(){
        try{
            LoginController login; 
            login = (LoginController)cambiarEscena("LoginView.fxml",380,361); 
            login.setEscenarioPrincipal(this);

        }catch(Exception e){
            System.out.println("Se produjo un error");
            e.printStackTrace();
        }
    }
    public int getRol() {
        return rol;
    }

    public void setRol(int rol) {
        this.rol = rol;
    }
    

    public Initializable cambiarEscena(String viewFxml, int ancho, int alto) throws Exception {
        Initializable resultado = null;
        FXMLLoader cargadorFXML = new FXMLLoader();
        InputStream archivo = Principal.class.getResourceAsStream(PAQUETE_VIEW + viewFxml);
        cargadorFXML.setBuilderFactory(new JavaFXBuilderFactory());
        cargadorFXML.setLocation(Principal.class.getResource(PAQUETE_VIEW + viewFxml));
        escena = new Scene((AnchorPane) cargadorFXML.load(archivo), ancho, alto);
        escenarioPrincipal.setScene(escena);
        escenarioPrincipal.sizeToScene();
        //escenarioPrincipal.getIcons().add(new Image(PAQUETE_IMAGES + "logo1.PNG"));
        escenarioPrincipal.show();

        resultado = (Initializable) cargadorFXML.getController();
        return resultado;
    }
   
}
