/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.MarceloDiaz.controller;

import java.net.URL;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
import org.MarceloDiaz.bean.Departamentos;
import org.MarceloDiaz.db.Conexion;
import org.MarceloDiaz.report.GenerarReporte;
import org.MarceloDiaz.system.Principal;

/**
 *
* @author Marcelo Javier Diaz Pineda
* @date 10/06/2021
* @time 18:20:21
 */
public class DepartamentosController implements Initializable{
    private Principal escenarioPrincipal; 
    
    private ObservableList<Departamentos>listaDepartamentos;
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
    private TableView tblDepartamentos;
    @FXML
    private TableColumn colId;
    @FXML
    private TableColumn colNombre;
    @FXML
    private TextField txtId;
    @FXML
    private TextField txtNombre;
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
        escenarioPrincipal.administracion();
    }
      
    public ObservableList<Departamentos>getDepartamentos(){
        ArrayList<Departamentos> listado = new ArrayList<Departamentos>();
        try{
             PreparedStatement stmt;
             stmt = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarDepartamentos()}");
             ResultSet resultado = stmt.executeQuery();
             while(resultado.next()){
              listado.add(new Departamentos(
                      resultado.getInt("id"),
                      resultado.getString("Nombre")
              )
              );
          }
          resultado.close(); 
          stmt.close();
        }catch(SQLException e){
            e.printStackTrace();
        }
        listaDepartamentos = FXCollections.observableArrayList(listado);
        return listaDepartamentos;
    }  
    
    public void cargarDatos(){
        tblDepartamentos.setItems(getDepartamentos());
        colId.setCellValueFactory(new PropertyValueFactory<Departamentos,Integer>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Departamentos,String>("nombre") );
    }
    public void habilitarCampos(){
         txtId.setEditable(false);
         txtNombre.setEditable(true);
    }
    @FXML
    public void seleccionarElementos(){
        try{
        txtId.setText(String.valueOf(((Departamentos)tblDepartamentos.getSelectionModel().getSelectedItem()).getId()));
        txtNombre.setText(String.valueOf(((Departamentos)tblDepartamentos.getSelectionModel().getSelectedItem()).getNombre()));    
        }catch(Exception e){
            System.out.println("La seleccion no es valida");
        }
    }
    private void agregarDepartamentos(){
        Departamentos registro=new Departamentos();
        registro.setNombre(txtNombre.getText());
         try{
             CallableStatement stmt;
              stmt = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarDepartamentos(?)}");
              stmt.setString(1,registro.getNombre());
             stmt.execute();
            cargarDatos();
            limpiarCampos();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    private void editarDepartamentos(){
        Departamentos registro = (Departamentos) tblDepartamentos.getSelectionModel().getSelectedItem();
        registro.setId(Integer.parseInt(txtId.getText()));
        registro.setNombre(txtNombre.getText());
        try{
            PreparedStatement stmt; 
            stmt = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarDepartamentos(?,?)}");
            stmt.setInt(1,registro.getId());
            stmt.setString(2,registro.getNombre());
            stmt.execute(); 
           System.out.println(stmt.toString());
        }catch(SQLException e){
             e.printStackTrace();
        }
    }
     private void eliminarDepartamentos(){
        try{
            PreparedStatement stmt;
            stmt= Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarDepartamentos(?)}");
            stmt.setInt(1,Integer.parseInt(txtId.getText()));
            stmt.execute();
            System.out.println(stmt.toString());
        }catch(Exception e){
            e.printStackTrace(); 
        }
    }
     public void limpiarCampos(){
        txtId.setText("");
        txtNombre.clear(); 
    } 
     public void deshabilitarCampos(){
        txtId.setEditable(false);
        txtNombre.setEditable(false);
    } 
      public boolean existeElementosSeleccionado() {
        if (tblDepartamentos.getSelectionModel().getSelectedItem() == null) {
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
                listaCampos.add(txtNombre);
                ArrayList<ComboBox> listaComboBox = new ArrayList<>();  
                 if (escenarioPrincipal.validar(listaCampos, listaComboBox)) {
                    agregarDepartamentos();
                    operacion = Operaciones.NINGUNO;
                    btnNuevo.setText("Nuevo");
                    imgNuevo.setImage(new Image("/org/MarceloDiaz/resource/images/boton-agregar.png"));
                    btnModificar.setText("Modificar");
                     imgModificar.setImage(new Image("org/MarceloDiaz/resource/images/editar.png"));
                    btnEliminar.setDisable(false);
                    btnReporte.setDisable(false);
                    imgEliminar.setOpacity(1);
                    imgReporte.setOpacity(1);
                 }else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Kinal mall - Departamentos");
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
                btnReporte.setDisable(true);
                imgNuevo.setOpacity(0.15);
                imgReporte.setOpacity(0.15);
                operacion = Operaciones.ACTUALIZAR;
                }else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Kinal Mall - Departamentos");
                    alert.setHeaderText(null);
                    alert.setContentText("Seleccione los datos para modificar");
                    alert.show();
                }
                break; 
            case ACTUALIZAR:
                 ArrayList<TextField> listaCampos = new ArrayList<>();
                listaCampos.add(txtNombre);
                ArrayList<ComboBox> listaComboBox = new ArrayList<>();  
                 if (escenarioPrincipal.validar(listaCampos, listaComboBox)) {
                editarDepartamentos(); 
                limpiarCampos();
                cargarDatos();
                btnModificar.setText("Modificar");
                 imgModificar.setImage(new Image("/org/MarceloDiaz/resource/images/editar.png"));
                btnEliminar.setText("Eliminar");
                imgEliminar.setImage(new Image("org/MarceloDiaz/resource/images/marca-x.png"));
                btnNuevo.setDisable(false);
                btnReporte.setDisable(false);
                imgNuevo.setOpacity(1);
                imgReporte.setOpacity(1);
                operacion = Operaciones.NINGUNO; 
                 }else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Kinal Mall - Departamentos");
                    alert.setHeaderText(null);
                    alert.setContentText("Complete todos los campos");
                    alert.show();
                }
               break;
            case GUARDAR:
                limpiarCampos();
                btnNuevo.setText("Nuevo");
                imgNuevo.setImage(new Image("/org/MarceloDiaz/resource/images/boton-agregar.png"));       
                btnModificar.setText("Modificar");
                 imgModificar.setImage(new Image("/org/MarceloDiaz/resource/images/editar.png"));
                btnEliminar.setDisable(false);
                btnReporte.setDisable(false);
                imgEliminar.setOpacity(1);
                imgReporte.setOpacity(1);
                operacion = Operaciones.NINGUNO; 
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
                 btnReporte.setDisable(false);
                 btnModificar.setText("Modificar");
                imgModificar.setImage(new Image("/org/MarceloDiaz/resource/images/editar.png"));
                 btnEliminar.setText("Eliminar");
                 imgEliminar.setImage(new Image("/org/MarceloDiaz/resource/images/marca-x.png"));
                 imgReporte.setOpacity(1);
                 imgNuevo.setOpacity(1);
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
                         eliminarDepartamentos(); 
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
        GenerarReporte.getInstance().mostrarReporte("ReporteDepartamento.jasper", parametros, "Listado de Departamentos");
    }
    
}
