/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.MarceloDiaz.controller;

import java.io.InputStream;
import java.net.URL;
import java.sql.CallableStatement;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import org.MarceloDiaz.bean.Clientes;
import org.MarceloDiaz.bean.TipoCliente;
import org.MarceloDiaz.system.Principal;
import java.sql.PreparedStatement;
import org.MarceloDiaz.db.Conexion;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import org.MarceloDiaz.bean.Administracion;
import org.MarceloDiaz.bean.Locales;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.MarceloDiaz.report.GenerarReporte;
/**
 *
* @author Marcelo Javier Diaz Pineda
* @date 14/05/2021
* @time 17:18:20
 */
public class ClientesController implements Initializable {
    private Principal escenarioPrincipal; 
    private ObservableList<Clientes>listaClientes; 
    private ObservableList<TipoCliente>listaTipoCliente; 
    private ObservableList<Locales>listaLocales; 
    private ObservableList<Administracion>listaAdministracion; 
    
    @FXML
    private TableColumn colId;
    @FXML
    private TableColumn colNombre;
    @FXML
    private TableColumn colApellido;
    @FXML
    private TableColumn colTelefono;
    @FXML
    private TableColumn colDireccion;
    @FXML
    private TableColumn colEmail;
    @FXML
    private TableColumn colCodigoCliente;
    @FXML
    private TextField txtId;
    @FXML
    private TextField txtTelefono;
    @FXML
    private TextField txtDireccion;
    @FXML
    private TextField txtApellidos;
    @FXML
    private TextField txtEmail;
    @FXML
    private ComboBox cmbTipoCliente;
    @FXML
    private TextField txtNombres;
    @FXML
    private TableView tblClientes;
    @FXML
    private ImageView imgNuevo;
    @FXML
    private ImageView imgModificar;
    @FXML
    private ImageView imgEliminar;
    @FXML
    private ImageView imgReporte;
    private enum Operaciones{
        GUARDAR,ELIMINAR,ACTUALIZAR,CANCELAR,NINGUNO
    }
    private Operaciones operacion = Operaciones.NINGUNO;
    @FXML
    private Button btnNuevo;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnActualizar;
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        cargarDatos();
    }
    public ObservableList<Locales>getLocales(){
        ArrayList<Locales>lista=new ArrayList<>(); 
        try{
            PreparedStatement pstmt = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarLocales}");
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                Locales registro = new Locales();
                registro.setId(rs.getInt("id"));
                registro.setSaldoFavor(rs.getBigDecimal("saldofavor"));
                registro.setSaldoContra(rs.getBigDecimal("saldocontra"));
                registro.setMesesPendiente(rs.getInt("mesespendiente"));
                registro.setDisponibilidad(rs.getBoolean("disponibilidad"));
                registro.setValorLocal(rs.getBigDecimal("ValorLocal"));
                registro.setValorAdministracion(rs.getBigDecimal("ValorAdministracion"));
                lista.add(registro);
                
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        listaLocales=FXCollections.observableArrayList(lista);
        return listaLocales;
    }
    public ObservableList<Clientes>getClientes(){
        ArrayList<Clientes>lista = new ArrayList<>();
        try{
            PreparedStatement pstmt = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarClientes}");
           ResultSet rs = pstmt.executeQuery();
           while(rs.next()){
               lista.add(new Clientes(
                       rs.getInt("id"),
                       rs.getString("nombres"),
                       rs.getString("apellidos"),
                       rs.getString("telefono"),
                       rs.getString("direccion"),
                       rs.getString("email"),
                       rs.getInt("codigoTipoCliente")));
                       
           }
        }catch(Exception e){
            e.printStackTrace();
        }
        listaClientes=FXCollections.observableArrayList(lista);
        return listaClientes;
        
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
        listaAdministracion = FXCollections.observableArrayList(listado);
        return listaAdministracion;
    }
    public ObservableList<TipoCliente>getTipoCliente(){
        ArrayList<TipoCliente> lista = new ArrayList<>();
        try{
            PreparedStatement pstmt = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarTipoCliente}");
             ResultSet rs = pstmt.executeQuery();
             while(rs.next()){
                 lista.add(new TipoCliente(rs.getInt("id"),rs.getString("descripcion")));
             }
             rs.close();
             pstmt.close();
                     
        }catch(Exception e){
            e.printStackTrace();
        }
        listaTipoCliente = FXCollections.observableArrayList(lista);
        return listaTipoCliente;
    }
    public void cargarDatos(){
        tblClientes.setItems(getClientes());
        colId.setCellValueFactory(new PropertyValueFactory<Clientes,Integer>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Clientes,String>("nombres"));
        colApellido.setCellValueFactory(new PropertyValueFactory<Clientes,String>("apellidos"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<Clientes,String>("telefono"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<Clientes,String>("direccion"));
        colEmail.setCellValueFactory(new PropertyValueFactory<Clientes,String>("email"));
        colCodigoCliente.setCellValueFactory(new PropertyValueFactory<Clientes,Integer>("codigoTipoCliente"));
        

        cmbTipoCliente.setItems(getTipoCliente());
    }
    public TipoCliente buscarTipoCliente(int id){
        TipoCliente registro = null; 
        
        try{
            PreparedStatement pstmt = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarTipoCliente(?)}");
            pstmt.setInt(1,id);
          ResultSet rs =  pstmt.executeQuery(); 
          
          while(rs.next()){
              registro = new TipoCliente(rs.getInt("id"),rs.getString("descripcion"));
          }
            rs.close();
            pstmt.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return registro;
    }

    
    public void ActivarControles(){
        txtId.setEditable(false);
        txtNombres.setEditable(true); 
        txtApellidos.setEditable(true);
        txtTelefono.setEditable(true);
        txtDireccion.setEditable(true);
        txtEmail.setEditable(true);
        cmbTipoCliente.setDisable(false);
        
    }
    
    public void desactivarControles(){
        txtId.setEditable(false);
        txtNombres.setEditable(false);
        txtApellidos.setEditable(false);
        txtTelefono.setEditable(false);
        txtDireccion.setEditable(false);
        txtEmail.setEditable(false);
        cmbTipoCliente.setDisable(true);
    }
    
    public void limpiarControles(){
        txtId.clear();
        txtNombres.clear();
        txtApellidos.clear();
        txtTelefono.clear();
        txtDireccion.clear();
        txtEmail.clear();
        cmbTipoCliente.getSelectionModel().clearSelection();
    }
    public boolean existeElementosSeleccionado(){
         if (tblClientes.getSelectionModel().getSelectedItem() == null) {
            return false;
        } else {
            return true;
        }
    }
   public boolean validarTelefono(String numero) {
        Pattern pattern = Pattern.compile("^[0-9]{8}$");
        Matcher matcher = pattern.matcher(numero);
        return matcher.matches();

    }
     public boolean validarEmail(String email){
      Pattern pattern = Pattern.compile("^[\\w-]+(\\.[\\w-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    @FXML
    public void seleccionarElemento(){
        try{
  txtId.setText(String.valueOf(((Clientes)tblClientes.getSelectionModel().getSelectedItem()).getId()));
  txtNombres.setText(String.valueOf(((Clientes)tblClientes.getSelectionModel().getSelectedItem()).getNombres()));
  txtApellidos.setText(String.valueOf(((Clientes)tblClientes.getSelectionModel().getSelectedItem()).getApellidos()));
  txtTelefono.setText(String.valueOf(((Clientes)tblClientes.getSelectionModel().getSelectedItem()).getTelefono()));
  txtDireccion.setText(String.valueOf(((Clientes)tblClientes.getSelectionModel().getSelectedItem()).getDireccion()));
  txtEmail.setText(String.valueOf(((Clientes)tblClientes.getSelectionModel().getSelectedItem()).getEmail()));
  cmbTipoCliente.getSelectionModel().select( buscarTipoCliente(((Clientes)tblClientes.getSelectionModel().getSelectedItem()).getCodigoTipoCliente()));
        }catch(Exception e){
            System.out.println("Existe un error");
        } 
    } 
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
     private void agregarCliente(){

            Clientes cliente = new Clientes();
            cliente.setNombres(txtNombres.getText());
            cliente.setApellidos(txtApellidos.getText());
            cliente.setTelefono(txtTelefono.getText());
            cliente.setDireccion(txtDireccion.getText());
            cliente.setEmail(txtEmail.getText());

            if (cmbTipoCliente.getSelectionModel().getSelectedItem() != null) {
                cliente.setCodigoTipoCliente(((TipoCliente) cmbTipoCliente.getSelectionModel().getSelectedItem()).getId());
            }

            try {
                PreparedStatement stmt;
                stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_AgregarClientes(?,?,?,?,?,?)}");
                stmt.setString(1, cliente.getNombres());
                stmt.setString(2, cliente.getApellidos());
                stmt.setString(3, cliente.getTelefono());
                stmt.setString(4, cliente.getDireccion());
                stmt.setString(5, cliente.getEmail());
                stmt.setInt(6, cliente.getCodigoTipoCliente());

                stmt.execute();
                listaClientes.add(cliente);
                cargarDatos();
                limpiarControles();

            } catch (SQLException e) {
                if (e.getErrorCode() == 1452) {
                    //JOptionPane.showMessageDialog(null, "Debe seleccionar un tipo de Cliente");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Kinal Mall - ClIENTES");
                alert.setHeaderText(null);
                alert.setContentText("Seleccione un tipo cliente");
                alert.show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
    
    public void editarCliente() {
        
            Clientes cliente = new Clientes();
            cliente.setId(Integer.parseInt(txtId.getText()));
            cliente.setNombres(txtNombres.getText());
            cliente.setApellidos(txtApellidos.getText());
            cliente.setTelefono(txtTelefono.getText());
            cliente.setDireccion(txtDireccion.getText());
            cliente.setEmail(txtEmail.getText());
            cliente.setCodigoTipoCliente(((TipoCliente) cmbTipoCliente.getSelectionModel().getSelectedItem()).getId());

            try {
                PreparedStatement pstmt;
                pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EditarClientes(?,?,?,?,?,?,?)}");
                pstmt.setInt(1, cliente.getId());
                pstmt.setString(2, cliente.getNombres());
                pstmt.setString(3, cliente.getApellidos());
                pstmt.setString(4, cliente.getTelefono());
                pstmt.setString(5, cliente.getDireccion());
                pstmt.setString(6, cliente.getEmail());
                pstmt.setInt(7, cliente.getCodigoTipoCliente());
                
                System.out.println(pstmt.toString());
                
                pstmt.execute();
                
            } catch (Exception e) {
                e.printStackTrace();

            }
    }
    
    public void eliminarCliente(){
         Clientes cliente = ((Clientes) tblClientes.getSelectionModel().getSelectedItem());
        System.out.println(cliente);
        try{
            PreparedStatement pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EliminarClientes(?)}");
            pstmt.setInt(1, cliente.getId());
            
            System.out.println(pstmt.toString());
            pstmt.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    public void menuPrincipal(MouseEvent event) {
        escenarioPrincipal.menuPrincipal();
    }
    @FXML
    private void nuevo(ActionEvent event) {
        System.out.println("Operacion: " + operacion);
        switch (operacion) {
            case NINGUNO:
                ActivarControles();
                limpiarControles();
                operacion = ClientesController.Operaciones.GUARDAR;
                btnNuevo.setText("Guardar");
                imgNuevo.setImage(new Image("/org/MarceloDiaz/resource/images/disco-flexible.png"));
                btnGuardar.setText("Cancelar");
                imgModificar.setImage(new Image("org/MarceloDiaz/resource/images/cancelar.png"));
                imgReporte.setOpacity(0.15);
                imgEliminar.setOpacity(0.15);
                btnEliminar.setDisable(true);
                btnActualizar.setDisable(true);
                break;
            case GUARDAR:
                ArrayList<TextField> listaCampos = new ArrayList<>();
                listaCampos.add(txtNombres);
                listaCampos.add(txtApellidos);
                listaCampos.add(txtTelefono);
                listaCampos.add(txtDireccion);
                listaCampos.add(txtEmail);
                ArrayList<ComboBox> listaComboBox = new ArrayList<>();
                //listaComboBox.add(cmbTipoCliente);

                if (escenarioPrincipal.validar(listaCampos, listaComboBox)) {
                    if (validarTelefono(txtTelefono.getText())) {
                        if (validarEmail(txtEmail.getText())) {
                            agregarCliente();
                            cargarDatos();
                            limpiarControles();
                            desactivarControles();
                            operacion = Operaciones.NINGUNO;
                            btnNuevo.setText("Nuevo");
                            imgNuevo.setImage(new Image("/org/MarceloDiaz/resource/images/boton-agregar.png"));
                            btnGuardar.setText("Modificar");
                            imgModificar.setImage(new Image("org/MarceloDiaz/resource/images/editar.png"));
                            imgEliminar.setOpacity(1);
                            imgReporte.setOpacity(1);
                            btnEliminar.setDisable(false);
                            btnActualizar.setDisable(false);
                        } else {
                            //JOptionPane.showMessageDialog(null, "Este correo no es valido.");
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Kinal mall - Clientes");
                            alert.setHeaderText(null);
                            alert.setContentText("Este correo no es valido.");
                            alert.show();
                        }
                    } else {
                        //JOptionPane.showMessageDialog(null, "Este numero no es valido.");
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Kinal mall - Clientes");
                        alert.setHeaderText(null);
                        alert.setContentText("Este numero no es valido.");
                        alert.show();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Kinal mall - Clientes");
                    alert.setHeaderText(null);
                    alert.setContentText("Por favor llena todos los datos");
                    alert.show();
                }
                break;
        }
    }
    @FXML
    private void guardar(ActionEvent event) {
        System.out.println("Operacion: " + operacion);
        switch(operacion){
           case NINGUNO: 
                if (existeElementosSeleccionado()) {
               ActivarControles(); 
                btnGuardar.setText("Actualizar");
                imgModificar.setImage(new Image("/org/MarceloDiaz/resource/images/actualizar.png"));
                btnEliminar.setText("Cancelar");
                imgEliminar.setImage(new Image("org/MarceloDiaz/resource/images/cancelar.png"));
                btnNuevo.setDisable(true);
                imgNuevo.setOpacity(0.15);
                btnActualizar.setDisable(true);
                imgReporte.setOpacity(0.15);
                operacion = Operaciones.ACTUALIZAR;
                }else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Kinal Mall - Proveedores");
                    alert.setHeaderText(null);
                    alert.setContentText("Seleccione los datos para modificar");
                    alert.show();
                }
                break; 
           case ACTUALIZAR:
                ArrayList<TextField> listaCampos = new ArrayList<>();
                listaCampos.add(txtId);
                listaCampos.add(txtNombres);
                listaCampos.add(txtApellidos);
                listaCampos.add(txtTelefono);
                listaCampos.add(txtDireccion);
                listaCampos.add(txtEmail);
                ArrayList<ComboBox> listaComboBox = new ArrayList<>();
                listaComboBox.add(cmbTipoCliente);
                if (escenarioPrincipal.validar(listaCampos, listaComboBox)) {
                    if (validarTelefono(txtTelefono.getText())) {
                        if (validarEmail(txtEmail.getText())) {
                            editarCliente();
                            limpiarControles();
                            cargarDatos();
                            btnGuardar.setText("Modificar");
                            imgModificar.setImage(new Image("/org/MarceloDiaz/resource/images/editar.png"));
                            btnEliminar.setText("Eliminar");
                            imgEliminar.setImage(new Image("org/MarceloDiaz/resource/images/marca-x.png"));
                            btnNuevo.setDisable(false);
                            imgNuevo.setOpacity(1);
                            btnActualizar.setDisable(false);
                            imgReporte.setOpacity(1);
                            operacion = Operaciones.NINGUNO;
                            desactivarControles();
                        } else {
                            //JOptionPane.showMessageDialog(null, "Correo no valido");
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Kinal mall - Clientes");
                            alert.setHeaderText(null);
                            alert.setContentText("Correo no valido");
                            alert.show();
                        }
                    } else {
                        //JOptionPane.showMessageDialog(null, "Numero no valido");
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Kinal mall - Clientes");
                            alert.setHeaderText(null);
                            alert.setContentText("Numero no valido");
                            alert.show();
                    }
                } else {
                    //JOptionPane.showMessageDialog(null, "Por favor llena todos los datos");
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Kinal mall - Clientes");
                            alert.setHeaderText(null);
                            alert.setContentText("Porfavor llena todos los datos");
                            alert.show();
                }
                break;
           case GUARDAR:
               limpiarControles();
                btnNuevo.setText("Nuevo");
                imgNuevo.setImage(new Image("/org/MarceloDiaz/resource/images/boton-agregar.png"));   
                btnGuardar.setText("Modificar");
                imgModificar.setImage(new Image("/org/MarceloDiaz/resource/images/editar.png"));
                btnEliminar.setDisable(false);
                btnActualizar.setDisable(false);
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
                 desactivarControles();
                 btnNuevo.setDisable(false);
                 btnActualizar.setDisable(false);
                 btnGuardar.setText("Modificar");
                 imgModificar.setImage(new Image("/org/MarceloDiaz/resource/images/editar.png"));
                 btnEliminar.setText("Eliminar");
                 imgEliminar.setImage(new Image("/org/MarceloDiaz/resource/images/marca-x.png"));
                 imgNuevo.setOpacity(1);
                 imgReporte.setOpacity(1);
                 operacion = Operaciones.NINGUNO;
                 limpiarControles();
                 break;
             case NINGUNO://Eliminar
                 if(existeElementosSeleccionado()) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Kinal mall");
                    alert.setHeaderText(null);
                    alert.setContentText("Estas seguro que quiere eliminar?  ");
                    Optional<ButtonType> respuesta = alert.showAndWait();
                    if (respuesta.get() == ButtonType.OK) {
                 eliminarCliente(); 
                 limpiarControles(); 
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
    private void actualizar(ActionEvent event) {
         Map parametros = new HashMap();
        GenerarReporte.getInstance().mostrarReporte("ReporteClientes.jasper", parametros, "Listado de Clientes");
    }

        @FXML
    private void CuentasPorCobrar(){
        escenarioPrincipal.CuentasPorCobrar();
    }
}
