/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.MarceloDiaz.controller;

import java.net.URL;
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
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import org.MarceloDiaz.bean.Cargos;
import org.MarceloDiaz.db.Conexion;
import org.MarceloDiaz.report.GenerarReporte;
import org.MarceloDiaz.system.Principal;

/**
 *
* @author Marcelo Javier Diaz Pineda
* @date 12/07/2021
* @time 17:56:11
 */
public class CargosController implements Initializable {
    private Principal escenarioPrincipal; 
    @FXML
    private Button btnNuevo;
    @FXML
    private ImageView imgNuevo;
    @FXML
    private Button btnModificar;
    @FXML
    private ImageView imgModificar;
    @FXML
    private Button btnEliminar;
    @FXML
    private ImageView imgEliminar;
    @FXML
    private Button btnReporte;
    @FXML
    private ImageView imgReporte;
    @FXML
    private TableView tblCargos;
    @FXML
    private TableColumn colId;
    @FXML
    private TableColumn colNombre;
    @FXML
    private TextField txtId;
    @FXML
    private TextField txtNombre;
    
    private enum Operaciones {
        GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO
    }
    private Operaciones operacion = Operaciones.NINGUNO;

    private ObservableList<Cargos> listaCargos;
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
    private void menuPrincipal(MouseEvent event) {
        escenarioPrincipal.administracion();
    }
    
    public ObservableList<Cargos> getCargos(){
        ArrayList<Cargos> lista = new ArrayList<>();
        try{
            PreparedStatement pstmt = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarCargos}");
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                lista.add(new Cargos(rs.getInt("id"),
                        rs.getString("nombre")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
       listaCargos = FXCollections.observableArrayList(lista);
       return listaCargos;
    }
    
    public void cargarDatos(){
        tblCargos.setItems(getCargos());
        colId.setCellValueFactory(new PropertyValueFactory<Cargos, Integer>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Cargos, String>("nombre"));
    }
    
    public void agregarCargos() {
        Cargos cargos = new Cargos();
        cargos.setNombre(txtNombre.getText());
        PreparedStatement pstmt = null;
        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarCargos(?)}");
            pstmt.setString(1, cargos.getNombre());
            pstmt.execute();
        } catch (SQLException e) {
            System.out.println("se produjo un error al intentar agregar un cargo en la base da datos");
        } finally {
            try {
                pstmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public void editarCargos() {
        Cargos cargos = new Cargos();
        cargos.setId(Integer.parseInt(txtId.getText()));
        cargos.setNombre(txtNombre.getText());
        PreparedStatement pstmt = null;
        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarCargos(?,?)}");
            pstmt.setInt(1, cargos.getId());
            pstmt.setString(2, cargos.getNombre());
            pstmt.execute();
        } catch (SQLException e) {
            System.out.println("Se produjo un error al editar Cargos en la base de datos");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public boolean existeElementosSeleccionado() {
        if (tblCargos.getSelectionModel().getSelectedItem() == null) {
            return false;
        } else {
            return true;
        }
    }
    public void eliminarCargos() {
        if (existeElementosSeleccionado()) {
            Cargos local = ((Cargos) tblCargos.getSelectionModel().getSelectedItem());
            System.out.println(local);
            try {
                PreparedStatement pstmt = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarCargos(?)}");
                pstmt.setInt(1, local.getId());
                System.out.println(pstmt.toString());
                pstmt.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
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
    
    public void habilitarCampos(){
        txtId.setEditable(false);
        txtNombre.setEditable(true);
    }
    
    public void seleccionarElemento(){
         if (existeElementosSeleccionado()) {
            txtId.setText(String.valueOf(((Cargos) tblCargos.getSelectionModel().getSelectedItem()).getId()));
            txtNombre.setText(String.valueOf(((Cargos)tblCargos.getSelectionModel().getSelectedItem()).getNombre()));

         }
    }
    @FXML
    private void nuevo(ActionEvent event) {
        System.out.println("Operacion" + operacion);
        switch (operacion) {
            case NINGUNO:
                habilitarCampos();
                limpiarCampos();
                operacion = Operaciones.GUARDAR;
                btnNuevo.setText("Guardar");
                imgNuevo.setImage(new Image("/org/MarceloDiaz/resource/images/disco-flexible.png"));
                btnModificar.setText("Cancelar");
                imgModificar.setImage(new Image("org/MarceloDiaz/resource/images/cancelar.png"));
                btnEliminar.setDisable(true);
                btnReporte.setDisable(true);
                imgReporte.setOpacity(0.15);
                imgEliminar.setOpacity(0.15);
                break;
            case GUARDAR:
                ArrayList<TextField> listaCampos = new ArrayList<>();
                listaCampos.add(txtNombre);
                ArrayList<ComboBox> listaComboBox = new ArrayList<>();
                if (escenarioPrincipal.validar(listaCampos, listaComboBox)) {
                agregarCargos();
                cargarDatos();
                limpiarCampos();
                deshabilitarCampos();
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
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Kinal mall - Cargos");
                    alert.setHeaderText(null);
                    alert.setContentText("Por favor llena todos los datos");
                    alert.show();
                }
                break;
        }
        
    }

    @FXML
    private void modificar(ActionEvent event) {
        System.out.println("Operacion: " + operacion);
        switch(operacion){
           case NINGUNO: 
               if (existeElementosSeleccionado()) {
               habilitarCampos(); 
                btnModificar.setText("Actualizar");
                imgModificar.setImage(new Image ("/org/MarceloDiaz/resource/images/actualizar.png"));
                btnEliminar.setText("Cancelar");
                imgEliminar.setImage(new Image("org/MarceloDiaz/resource/images/cancelar.png"));
                btnNuevo.setDisable(true);
                imgNuevo.setOpacity(0.15);
                btnReporte.setDisable(true);
                 imgReporte.setOpacity(0.15);
                operacion = Operaciones.ACTUALIZAR;
               }else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Kinal Mall - Tipo clientes");
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
                editarCargos(); 
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
                deshabilitarCampos();     
                }else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Kinal Mall - Cargos");
                    alert.setHeaderText(null);
                    alert.setContentText("Complete todos los campos");
                    alert.show();
                }
               break;
           case GUARDAR:
               deshabilitarCampos();
               limpiarCampos();
                btnNuevo.setText("Nuevo");
                imgNuevo.setImage(new Image ("/org/MarceloDiaz/resource/images/boton-agregar.png"));
                btnModificar.setText("Modificar");
                imgModificar.setImage(new Image("/org/MarceloDiaz/resource/images/editar.png"));
                btnEliminar.setDisable(false);
                btnReporte.setDisable(false);
                imgEliminar.setOpacity(1);
                imgReporte.setOpacity(1);
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
                 btnReporte.setDisable(false);
                 btnModificar.setText("Modificar");
                 imgModificar.setImage(new Image("/org/MarceloDiaz/resource/images/editar.png"));
                 btnEliminar.setText("Eliminar");
                 imgEliminar.setImage(new Image("/org/MarceloDiaz/resource/images/marca-x.png"));
                 imgNuevo.setOpacity(1);
                 imgReporte.setOpacity(1);
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
                        eliminarCargos(); 
                        limpiarCampos(); 
                        cargarDatos();
                         break;
                    }   
                    }else{
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
        GenerarReporte.getInstance().mostrarReporte("ReporteCargos.jasper", parametros, "Listado de Cargos");
    }

}
