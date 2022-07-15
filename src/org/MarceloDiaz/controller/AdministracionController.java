/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.MarceloDiaz.controller;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
 import javafx.fxml.Initializable;
import org.MarceloDiaz.system.Principal;
import java.sql.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.MarceloDiaz.bean.Administracion;
import org.MarceloDiaz.db.Conexion;
import org.MarceloDiaz.report.GenerarReporte;
/**
 *
* @author Marcelo Javier Diaz Pineda
* @date 13/05/2021
* @time 10:35:12
 */
public class AdministracionController implements  Initializable{
    private Principal escenarioPrincipal;
    
    private ObservableList<Administracion>listaAdministracion;
    @FXML
    private ImageView imgNuevo;
    @FXML
    private ImageView imgModificar;
    @FXML
    private ImageView imgEliminar;
    @FXML
    private ImageView imgReporte;
    
    private enum Operaciones {
        NUEVO,GUARDAR,ELIMINAR,ACTUALIZAR,CANCELAR,NINGUNO
    }
    private Operaciones operacion = Operaciones.NINGUNO;
    @FXML
    private Button btnNuevo;

    @FXML
    private Button btnModificar;

    @FXML
    private Button btnEliminar;

    @FXML
    private Button btnReporte;
    @FXML
    private TextField txtId;

    @FXML
    private TextField txtDireccion;

    @FXML
    private TextField txtTelefono;
    
    @FXML
    private TableView tblAdministracion;

    @FXML
    private TableColumn colId;

    @FXML
    private TableColumn  colDireccion;

    @FXML
    private TableColumn  colTelefono;
    private Button btnGuardar;
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        cargarDatos(); 
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    @FXML
      public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    public ObservableList<Administracion> getAdministracion(){
        
        ArrayList<Administracion> listado = new ArrayList<Administracion>();
        try{
            //CallableStatement stmt;
           PreparedStatement stmt;
          stmt = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarAdministracion()}");
          ResultSet resultado = stmt.executeQuery();
          
          while(resultado.next()){
          listado.add(new Administracion(
                  resultado.getInt("id"),
                  resultado.getString("direccion"),
                  resultado.getString("telefono")
                )
            );
          }
          resultado.close(); 
          stmt.close();   
        }catch(SQLException e){
            e.printStackTrace();
        }
        //return FXCollections.observableArrayList(listado);
        listaAdministracion = FXCollections.observableArrayList(listado);
        return listaAdministracion;
    }
    public void cargarDatos(){
        tblAdministracion.setItems(getAdministracion());
        colId.setCellValueFactory(new PropertyValueFactory<Administracion, Integer>("id"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<Administracion, String>("direccion"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<Administracion, String>("telefono"));
    }        

    private void habilitarCampos() {
        txtId.setEditable(false);
        txtDireccion.setEditable(true);
        txtTelefono.setEditable(true);
    }
    @FXML
    public void seleccionarElemento(){
        try{
     txtId.setText(String.valueOf(((Administracion)tblAdministracion.getSelectionModel().getSelectedItem()).getId()));
     txtDireccion.setText((((Administracion) tblAdministracion.getSelectionModel().getSelectedItem()).getDireccion()));
     txtTelefono.setText((((Administracion) tblAdministracion.getSelectionModel().getSelectedItem()).getTelefono()));
        }catch(Exception e){
            System.out.println("La seleccion no es valida");
        }
    }
    private void agregarAdministracion() {
        Administracion registro = new Administracion(); 
        registro.setDireccion(txtDireccion.getText());
        registro.setTelefono(txtTelefono.getText());
        
        try{
            CallableStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarAdministracion(?,?)}");
            stmt.setString(1,registro.getDireccion());
            stmt.setString(2,registro.getTelefono());
            //stmt.executeUpdate();
            stmt.execute();
            cargarDatos();
            limpiarCampos();
            deshabilitarCampos();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    private void editarAdministracion(){
        Administracion registro =(Administracion) tblAdministracion.getSelectionModel().getSelectedItem();
        registro.setId(Integer.parseInt(txtId.getText()));
        registro.setDireccion(txtDireccion.getText());
        registro.setTelefono(txtTelefono.getText());
        try{
            PreparedStatement stmt; 
            stmt = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarAdministracion(?,?,?)}");
            stmt.setInt(1,registro.getId());
            stmt.setString(2,registro.getDireccion());
            stmt.setString(3,registro.getTelefono());
            //stmt.executeUpdate(); 
            stmt.execute(); 
            System.out.println(stmt.toString());
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    private void eliminarAdministracion(){
        try{
            PreparedStatement stmt;
            stmt= Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarAdministracion(?)}");
            stmt.setInt(1,Integer.parseInt(txtId.getText()));
            stmt.execute();
            System.out.println(stmt.toString());
        }catch(Exception e){
            e.printStackTrace(); 
        }
    }
    
    public void limpiarCampos(){
        txtId.setText("");
        txtDireccion.clear(); 
        txtTelefono.clear(); 
    }
    public void deshabilitarCampos(){
        txtId.setEditable(false);
        txtDireccion.setEditable(false);
        txtTelefono.setEditable(false); 
    }
    public boolean validarTelefono(String numero) {
        Pattern pattern = Pattern.compile("^[0-9]{8}$");
        Matcher matcher = pattern.matcher(numero);
        return matcher.matches();

    }
     public boolean existeElementosSeleccionado() {
        if (tblAdministracion.getSelectionModel().getSelectedItem() == null) {
            return false;
        } else {
            return true;
        }
    }
    @FXML
    private void nuevo(ActionEvent event) {
        System.out.println("Operacion"+operacion);
        switch(operacion){
            case NINGUNO:
                habilitarCampos();
                limpiarCampos();
                operacion = Operaciones.GUARDAR;
                btnNuevo.setText("Guardar");
                imgNuevo.setImage(new Image("/org/MarceloDiaz/resource/images/disco-flexible.png"));
                btnModificar.setText("Cancelar");
                imgModificar.setImage(new Image("org/MarceloDiaz/resource/images/cancelar.png"));
                imgReporte.setOpacity(0.15);
                imgEliminar.setOpacity(0.15);
                btnEliminar.setDisable(true);
                btnReporte.setDisable(true);
                break;
            case GUARDAR:
                ArrayList<TextField> listaCampos = new ArrayList<>();
                listaCampos.add(txtDireccion);
                listaCampos.add(txtTelefono);
                ArrayList<ComboBox> listaComboBox = new ArrayList<>();
                 if (escenarioPrincipal.validar(listaCampos, listaComboBox)) {
                if (validarTelefono(txtTelefono.getText())) {
                agregarAdministracion();
                operacion = Operaciones.NINGUNO;
                btnNuevo.setText("Nuevo");
                imgNuevo.setImage(new Image("/org/MarceloDiaz/resource/images/boton-agregar.png"));
                imgEliminar.setOpacity(1);
                btnModificar.setText("Modificar");
                imgModificar.setImage(new Image("org/MarceloDiaz/resource/images/editar.png"));
               imgReporte.setOpacity(1);
                btnEliminar.setDisable(false);
                btnReporte.setDisable(false); 
                      }else {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Advertencia - Administracion");
                        alert.setHeaderText(null);
                        alert.setContentText("El numero de telefono es invalido");
                        alert.show();
                    }
                 }else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Kinal mall - Proveedores");
                    alert.setHeaderText(null);
                    alert.setContentText("Por favor llena todos los datos");
                    alert.show();
                }
                break;
        }
    }

    @FXML
    private void modificar(ActionEvent event) {
        System.out.println("Operacion"+operacion);
        switch(operacion){
            case NINGUNO: 
                if (existeElementosSeleccionado()) {
                habilitarCampos(); 
                btnModificar.setText("Actualizar");
                imgModificar.setImage(new Image("/org/MarceloDiaz/resource/images/actualizar.png"));
                btnEliminar.setText("Cancelar");
                imgEliminar.setImage(new Image("org/MarceloDiaz/resource/images/cancelar.png"));
                btnNuevo.setDisable(true);
                imgNuevo.setOpacity(0.15);
                btnReporte.setDisable(true);
                imgReporte.setOpacity(0.15);
                operacion = Operaciones.ACTUALIZAR;
                }else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Kinal Mall - Administracion");
                    alert.setHeaderText(null);
                    alert.setContentText("Seleccione los datos para modificar");
                    alert.show();
                }
                break; 
            case ACTUALIZAR: 
                ArrayList<TextField> listaCampos = new ArrayList<>();
                listaCampos.add(txtDireccion);
                listaCampos.add(txtTelefono);
                ArrayList<ComboBox> listaComboBox = new ArrayList<>();
                if (escenarioPrincipal.validar(listaCampos, listaComboBox)) {
                    if (validarTelefono(txtTelefono.getText())) {
                editarAdministracion(); 
                limpiarCampos();
                cargarDatos();
                btnModificar.setText("Modificar");
                 imgModificar.setImage(new Image("/org/MarceloDiaz/resource/images/editar.png"));
                btnEliminar.setText("Eliminar");
                imgEliminar.setImage(new Image("/org/MarceloDiaz/resource/images/marca-x.png"));
                imgNuevo.setOpacity(1);
                imgReporte.setOpacity(1);
                btnNuevo.setDisable(false);
                btnReporte.setDisable(false);
                operacion = Operaciones.NINGUNO; 
                    }else{
                             Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Advertencia - Administracion");
                        alert.setHeaderText(null);
                        alert.setContentText("El Numero es invalido");
                        alert.show();
                         }
                }else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Kinal Mall - Administracion");
                    alert.setHeaderText(null);
                    alert.setContentText("Complete todos los campos");
                    alert.show();
                }   
                break; 
            case GUARDAR://Cuando se cancela una insercion     
                limpiarCampos();
                deshabilitarCampos();                
                btnNuevo.setText("Nuevo");
                imgNuevo.setImage(new Image("/org/MarceloDiaz/resource/images/boton-agregar.png"));       
                btnModificar.setText("Modificar");
                imgModificar.setImage(new Image("/org/MarceloDiaz/resource/images/editar.png"));
                imgEliminar.setOpacity(1);
                imgReporte.setOpacity(1);
                btnEliminar.setDisable(false);
                btnReporte.setDisable(false);
                operacion = Operaciones.NINGUNO; 
                break;
        }
          System.out.println("Operacion"+operacion);
    }

    @FXML
    private void eliminar(ActionEvent event) {
         System.out.println("Operacion"+operacion);
         switch(operacion){
             case ACTUALIZAR: //cancelar
                 deshabilitarCampos();
                 limpiarCampos();
                 btnNuevo.setDisable(false);
                 imgNuevo.setOpacity(1);
                 btnReporte.setDisable(false);
                 imgReporte.setOpacity(1);
                 btnModificar.setText("Modificar");
                 imgModificar.setImage(new Image("/org/MarceloDiaz/resource/images/editar.png"));
                 btnEliminar.setText("Eliminar");
                 imgEliminar.setImage(new Image("/org/MarceloDiaz/resource/images/marca-x.png"));
                 operacion = Operaciones.NINGUNO;
                 break;
             case NINGUNO://Eliminar
                 if (existeElementosSeleccionado()) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Kinal mall");
                    alert.setHeaderText(null);
                    alert.setContentText("Estas seguro que quiere eliminar?  ");
                    Optional<ButtonType> respuesta = alert.showAndWait();
                    if (respuesta.get() == ButtonType.OK) {
                        eliminarAdministracion(); 
                        limpiarCampos(); 
                        cargarDatos(); 
                        break;
                    }
                 }else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Kinal MAll");
                    alert.setHeaderText(null);
                    alert.setContentText("Necesitas llenar todos los datos  ");
                    alert.show();
                    break;

                }

    }
    }
    @FXML
    private void reporte(ActionEvent event) {
       Map parametros = new HashMap();

        if (existeElementosSeleccionado()) {
            int codigoAdministracion = ((Administracion) tblAdministracion.getSelectionModel().getSelectedItem()).getId();
            parametros.put("id", codigoAdministracion);
            GenerarReporte.getInstance().mostrarReporte("ReporteAdministracion.jasper", parametros, "Listado de Administracion con Empleados");
        } else {
            GenerarReporte.getInstance().mostrarReporte("ReporteAdministracionNormal.jasper", parametros, "Listado de Administracion");
        }
    }
    
    @FXML
    private void departamentos() {
        escenarioPrincipal.departamentos();
    }
    @FXML
    private void cargos(){
        escenarioPrincipal.cargos();
    }
    @FXML
    private void tipoCliente() {
        escenarioPrincipal.tipoCliente();
    }
    @FXML
    private void locales(){
        escenarioPrincipal.locales();
    }
}
