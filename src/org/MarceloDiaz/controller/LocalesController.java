/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.MarceloDiaz.controller;

import java.math.BigDecimal;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javax.swing.JOptionPane;
import org.MarceloDiaz.bean.Locales;
import org.MarceloDiaz.db.Conexion;
import org.MarceloDiaz.report.GenerarReporte;
import org.MarceloDiaz.system.Principal;

/**
 *
* @author Marcelo Javier Diaz Pineda
* @date 18/06/2021
* @time 08:44:31
 */
public class LocalesController implements Initializable{
    private Principal escenarioPrincipal;
     private ObservableList<Locales>listaLocales; 
    @FXML
    private Button btnNuevo;
    @FXML
    private Button btnModificar;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnReporte;
    @FXML
    private TableView  tblLocales;
    @FXML
    private TableColumn colId;
    @FXML
    private TableColumn colSaldoFavor;
    @FXML
    private TableColumn  colSaldoContra;
    @FXML
    private TableColumn  colMesesPendiente;
    @FXML
    private TableColumn  colDisponibilidad;
    @FXML
    private TableColumn colValorLocal;
    @FXML
    private TableColumn colValorAdministracion;
    @FXML
    private TextField txtId;
    @FXML
    private TextField txtSaldoFavor;
    @FXML
    private TextField txtContra;
    @FXML
    private TextField txtMeses;
    @FXML
    private TextField txtValorLocal;
    @FXML
    private TextField txtValorAdmin;
    @FXML
    private CheckBox checDisponibilidad;
    @FXML
    private TextField txtSaldoLiquido;
    @FXML
    private ImageView imgNuevo;
    @FXML
    private ImageView imgModificar;
    @FXML
    private ImageView imgEliminar;
    @FXML
    private ImageView imgReporte;
    @FXML
    private TextField txtLocales;
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
    private enum Operaciones{
        GUARDAR,ELIMINAR,ACTUALIZAR,CANCELAR,NINGUNO
    }
    private Operaciones operacion = Operaciones.NINGUNO;
    @FXML
    private void menuPrincipal(MouseEvent event) {
        escenarioPrincipal.administracion();
    }
    
    public ObservableList<Locales>getLocales(){
        ArrayList<Locales>listado = new  ArrayList<Locales>();
        try{
             PreparedStatement stmt;
          stmt = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarLocales()}");
          ResultSet resultado = stmt.executeQuery();
         while(resultado.next()){
                Locales registro = new Locales();
                registro.setId(resultado.getInt("id"));
                registro.setSaldoFavor(resultado.getBigDecimal("saldofavor"));
                registro.setSaldoContra(resultado.getBigDecimal("saldocontra"));
                registro.setMesesPendiente(resultado.getInt("mesesPendientes"));
                registro.setDisponibilidad(resultado.getBoolean("disponibilidad"));
                registro.setValorLocal(resultado.getBigDecimal("ValorLocal"));
                registro.setValorAdministracion(resultado.getBigDecimal("ValorAdministracion"));
                listado.add(registro);
                
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        listaLocales=FXCollections.observableArrayList(listado);
        return listaLocales;
    }
    public void cargarDatos() {
        tblLocales.setItems(getLocales());
        colId.setCellValueFactory(new PropertyValueFactory<Locales, Integer>("id"));
        colSaldoFavor.setCellValueFactory(new PropertyValueFactory<Locales, String>("saldoFavor"));
        colSaldoContra.setCellValueFactory(new PropertyValueFactory<Locales, String>("saldoContra"));
        colMesesPendiente.setCellValueFactory(new PropertyValueFactory<Locales, Integer>("mesesPendiente"));
        colDisponibilidad.setCellValueFactory(new PropertyValueFactory<Locales, String>("disponibilidad"));
        colValorLocal.setCellValueFactory(new PropertyValueFactory<Locales, String>("valorLocal"));
        colValorAdministracion.setCellValueFactory(new PropertyValueFactory<Locales, String>("valorAdministracion"));
    }
    public void HabilitarControles() {
        txtId.setEditable(false);
        txtLocales.setEditable(false);
        txtSaldoFavor.setEditable(true);
        txtContra.setEditable(true);
        txtMeses.setEditable(false);
        txtValorAdmin.setEditable(true);
        txtValorLocal.setEditable(true);
        txtSaldoLiquido.setEditable(false);
        checDisponibilidad.setDisable(false);

    }
    public void DeshabilitarControles() {
        txtId.setEditable(false);
        txtLocales.setEditable(false);
        txtMeses.setEditable(false);
        txtValorAdmin.setEditable(false);
        txtContra.setEditable(false);
        txtSaldoFavor.setEditable(false);
        txtValorLocal.setEditable(false);
         txtSaldoLiquido.setEditable(false);
        checDisponibilidad.setDisable(true);
    }

    public void limpiarControladores() {
        txtId.clear();
        txtLocales.clear();
        txtMeses.clear();
        txtValorAdmin.clear();
        txtContra.clear();
        txtSaldoFavor.clear();
        txtValorLocal.clear();
        checDisponibilidad.setSelected(false);
    }
    public boolean existeElementosSeleccionado(){
         if (tblLocales.getSelectionModel().getSelectedItem() == null) {
            return false;
        } else {
            return true;
        }
    }
    @FXML
  public void seleccionarElemento() {
        if (existeElementosSeleccionado()) {
            txtId.setText(String.valueOf(((Locales) tblLocales.getSelectionModel().getSelectedItem()).getId()));
            txtContra.setText(String.valueOf(((Locales) tblLocales.getSelectionModel().getSelectedItem()).getSaldoContra()));
            txtSaldoFavor.setText(String.valueOf(((Locales) tblLocales.getSelectionModel().getSelectedItem()).getSaldoFavor()));
            txtMeses.setText(String.valueOf(((Locales) tblLocales.getSelectionModel().getSelectedItem()).getMesesPendiente()));
            checDisponibilidad.setSelected(((Locales) tblLocales.getSelectionModel().getSelectedItem()).isDisponibilidad());
            txtValorLocal.setText(String.valueOf(((Locales) tblLocales.getSelectionModel().getSelectedItem()).getValorLocal()));
            txtValorAdmin.setText(String.valueOf(((Locales) tblLocales.getSelectionModel().getSelectedItem()).getValorAdministracion()));

            BigDecimal op1 = ((Locales) tblLocales.getSelectionModel().getSelectedItem()).getSaldoFavor();
            BigDecimal op2 = ((Locales) tblLocales.getSelectionModel().getSelectedItem()).getSaldoContra();
            BigDecimal resultado = op1.subtract(op2);
            txtSaldoLiquido.setText(String.valueOf(resultado));
        }
    }
    public void agregarLocales() {
        Locales locales = new Locales();
        locales.setSaldoFavor(BigDecimal.valueOf(Double.valueOf(txtSaldoFavor.getText())));
        locales.setSaldoContra(BigDecimal.valueOf(Double.valueOf(txtContra.getText())));
        //locales.setMesesPendiente(Integer.parseInt(txtMeses.getText()));
        locales.setDisponibilidad(checDisponibilidad.isSelected());
        locales.setValorLocal(BigDecimal.valueOf(Double.valueOf(txtValorLocal.getText())));
        locales.setValorAdministracion(BigDecimal.valueOf(Double.valueOf(txtSaldoFavor.getText())));
        try {
            PreparedStatement stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_AgregarLocales(?,?,?,?,?,?)}");
            stmt.setBigDecimal(1, locales.getSaldoFavor());
            stmt.setBigDecimal(2, locales.getSaldoContra());
            stmt.setNull(3, locales.getMesesPendiente());
            stmt.setBoolean(4, locales.isDisponibilidad());
            stmt.setBigDecimal(5, locales.getValorLocal());
            stmt.setBigDecimal(6, locales.getValorAdministracion());
            stmt.execute();
            cargarDatos();
            limpiarControladores();
            DeshabilitarControles();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void editarLocales() {
        Locales local = new Locales();
        local.setId(Integer.parseInt(txtId.getText()));
        local.setSaldoFavor(BigDecimal.valueOf(Double.valueOf(txtSaldoFavor.getText())));
        local.setSaldoContra(BigDecimal.valueOf(Double.valueOf(txtContra.getText())));
        local.setMesesPendiente(Integer.parseInt(txtMeses.getText()));
        local.setDisponibilidad(checDisponibilidad.isSelected());
        local.setValorLocal(BigDecimal.valueOf(Double.valueOf(txtValorLocal.getText())));
        local.setValorAdministracion(BigDecimal.valueOf(Double.valueOf(txtSaldoFavor.getText())));

        try {
            PreparedStatement stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EditarLocales(?,?,?,?,?,?,?)}");
            stmt.setInt(1, local.getId());
            stmt.setBigDecimal(2, local.getSaldoFavor());
            stmt.setBigDecimal(3, local.getSaldoContra());
            stmt.setInt(4, local.getMesesPendiente());
            stmt.setBoolean(5, local.isDisponibilidad());
            stmt.setBigDecimal(6, local.getValorLocal());
            stmt.setBigDecimal(7, local.getValorAdministracion());

            stmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void eliminarLocales(){
        if (existeElementosSeleccionado()) {
            Locales local = ((Locales) tblLocales.getSelectionModel().getSelectedItem());
            System.out.println(local);
            try {
                PreparedStatement pstmt = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarLocales(?)}");
                pstmt.setInt(1, local.getId());

                System.out.println(pstmt.toString());
                pstmt.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public void contarLocales(){
        try{
            PreparedStatement pstmt =Conexion.getInstance().getConexion().prepareCall("{call sp_ConteoDeLocales}");
            ResultSet resultado = pstmt.executeQuery();
             while(resultado.next()){
                 txtLocales.setText(String.valueOf(resultado.getInt("Cantidad")));
             }
             resultado.close();
             pstmt.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void nuevo(ActionEvent event) {
         System.out.println("Operacion: " + operacion);
          switch (operacion) {
            case NINGUNO:
                HabilitarControles();
                limpiarControladores();
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
                listaCampos.add(txtSaldoFavor);
                listaCampos.add(txtContra);
                //listaCampos.add(txtMeses);
                listaCampos.add(txtValorLocal);
                listaCampos.add(txtValorAdmin);
                 ArrayList<ComboBox> listaComboBox = new ArrayList<>();
                 if (escenarioPrincipal.validar(listaCampos,listaComboBox)){
                     agregarLocales();
                     cargarDatos();
                     limpiarControladores();
                     DeshabilitarControles();
                     contarLocales();
                 operacion = Operaciones.NINGUNO;
                btnNuevo.setText("Nuevo");
                imgNuevo.setImage(new Image("/org/MarceloDiaz/resource/images/boton-agregar.png"));
                btnModificar.setText("Modificar"); 
                imgModificar.setImage(new Image("org/MarceloDiaz/resource/images/editar.png"));
                btnEliminar.setDisable(false);
                btnReporte.setDisable(false); 
                imgEliminar.setOpacity(1);
                imgReporte.setOpacity(1);   
                 }else{
                     Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Kinal mall - Locales");
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
               HabilitarControles(); 
                btnModificar.setText("Actualizar");
                imgModificar.setImage(new Image("/org/MarceloDiaz/resource/images/actualizar.png"));
                btnEliminar.setText("Cancelar");
                imgEliminar.setImage(new Image("org/MarceloDiaz/resource/images/cancelar.png"));
                contarLocales();
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
                listaCampos.add(txtSaldoFavor);
                listaCampos.add(txtContra);
                //listaCampos.add(txtMeses);
                listaCampos.add(txtValorLocal);
                listaCampos.add(txtValorAdmin);
                 ArrayList<ComboBox> listaComboBox = new ArrayList<>();
                 if (escenarioPrincipal.validar(listaCampos,listaComboBox)){
                     contarLocales();
                     editarLocales(); 
                limpiarControladores();
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
                 }else{
                     //JOptionPane.showMessageDialog(null, "Por favor llena todos los datos");
                     Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Kinal mall - Locales");
                    alert.setHeaderText(null);
                    alert.setContentText("Por favor llena todos los datos");
                    alert.show();
                 }
                 break;
           case GUARDAR:
               limpiarControladores();
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
                 DeshabilitarControles();
                 limpiarControladores();
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
             case NINGUNO:
                if (existeElementosSeleccionado()) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Kinal mall");
                    alert.setHeaderText(null);
                    alert.setContentText("Estas seguro que quiere eliminar?  ");
                    Optional<ButtonType> respuesta = alert.showAndWait();
                    if (respuesta.get() == ButtonType.OK) {
                 eliminarLocales(); 
                 limpiarControladores(); 
                 cargarDatos();
                    }
                }else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Kinal MAll");
                    alert.setHeaderText(null);
                    alert.setContentText("Necesitas llenar todos los datos  ");
                    alert.show();
                    break;
                  }  
                 break;
                    
              
         }
    }

    @FXML
    private void reporte(ActionEvent event) {
         Map parametros = new HashMap();
        GenerarReporte.getInstance().mostrarReporte("ReporteLocales.jasper", parametros, "Listado de Locales");
    }
}
