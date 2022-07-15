/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.MarceloDiaz.controller;

import com.jfoenix.controls.JFXDatePicker;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Date; 
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
import org.MarceloDiaz.bean.Administracion;
import org.MarceloDiaz.bean.CuentasPorPagar;
import org.MarceloDiaz.bean.Horarios;
import org.MarceloDiaz.bean.Proveedores;
import org.MarceloDiaz.db.Conexion;
import org.MarceloDiaz.report.GenerarReporte;
import org.MarceloDiaz.system.Principal;
/**
 *
* @author Marcelo Javier Diaz Pineda
* @date 8/07/2021
* @time 09:20:00
 */
public class CuentasporPagarController implements Initializable  {
    private Principal escenarioPrincipal;
    @FXML
    private Button btnNuevo;
    @FXML
    private Button btnModificar;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnReporte;
    @FXML
    private TableView tblCuentasPorPagar;
    @FXML
    private TableColumn colId;
    @FXML
    private TableColumn colNumero;
    @FXML
    private TableColumn colFecha;
    @FXML
    private TableColumn colProveedor;
    @FXML
    private TableColumn colValorNeto;
    @FXML
    private TableColumn colEstado;
    @FXML
    private TableColumn colAdministracion;
    @FXML
    private TextField txtId;
    @FXML
    private TextField txtNumeroFactura;
    @FXML
    private TextField txtValorNeto;
    @FXML
    private TextField txtEstadoPago;
    @FXML
    private ComboBox cmbAdministracion;
    @FXML
    private ComboBox cmbProveedor;
    @FXML
    private ImageView imgNuevo;
    @FXML
    private ImageView imgModificar;
    @FXML
    private ImageView imgEliminar;
    @FXML
    private ImageView imgReporte;
    @FXML
    private JFXDatePicker tpFechaLimite;
    private enum Operaciones {
        GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO
    }
    private Operaciones operacion = Operaciones.NINGUNO;
    
    private ObservableList<CuentasPorPagar> listaCuentasPorPagar;
    private ObservableList<Administracion> listaAdministracion;
    private ObservableList<Proveedores>listaProveedores;
    
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
    private void activarControles() {
        txtId.setEditable(false);
        txtNumeroFactura.setEditable(true);
        tpFechaLimite.setDisable(false);
        txtEstadoPago.setEditable(true);
        txtValorNeto.setEditable(true);
        cmbAdministracion.setDisable(false);
        cmbProveedor.setDisable(false);
    }

    private void desactivarControles() {
        txtId.setEditable(false);
        txtNumeroFactura.setEditable(false);
        tpFechaLimite.setDisable(true);
        txtEstadoPago.setEditable(false);
        txtValorNeto.setEditable(false);
        cmbAdministracion.setDisable(true);
        cmbProveedor.setDisable(true);
    }

    private void limpiarControles() {
        txtId.clear();
        txtNumeroFactura.clear();
        tpFechaLimite.getEditor().clear();
        txtEstadoPago.clear();
        txtValorNeto.clear();
        cmbAdministracion.valueProperty().set(null);
        cmbProveedor.valueProperty().set(null);
    }
    public boolean validarNumeroReal(String numero) {
        String patron = "^[0-9]+([.][0-9]{2})?";
        Pattern pattern = Pattern.compile(patron);
        Matcher matcher = pattern.matcher((numero));
        return matcher.matches();
    }
    public ObservableList<CuentasPorPagar> getCuentasPorPagar() {
        ArrayList<CuentasPorPagar> lista = new ArrayList<>();
        try {
            PreparedStatement pstmt = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarCuentasPorPagar}");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                lista.add(new CuentasPorPagar(rs.getInt("id"),
                        rs.getString("numeroFactura"),
                        rs.getDate("fechaLimitePago"),
                        rs.getString("estadoPago"),
                        rs.getBigDecimal("valorNetoPago"),
                        rs.getInt("codigoAdministracion"),
                        rs.getInt("codigoProveedor")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        listaCuentasPorPagar = FXCollections.observableArrayList(lista);
        return listaCuentasPorPagar;
    }
     public ObservableList<Administracion> getAdministracion() {

        ArrayList<Administracion> listado = new ArrayList<Administracion>();
        try {
            //CallableStatement stmt;
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarAdministracion()}");
            ResultSet resultado = stmt.executeQuery();

            while (resultado.next()) {
                listado.add(new Administracion(
                        resultado.getInt("id"),
                        resultado.getString("direccion"),
                        resultado.getString("telefono")
                )
                );
            }
            resultado.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        listaAdministracion = FXCollections.observableArrayList(listado);
        return listaAdministracion;
    }
     
     public ObservableList<Proveedores> getProveedores() {
        ArrayList<Proveedores> listado = new ArrayList<Proveedores>();
        try {
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarProveedores()}");
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                listado.add(new Proveedores(
                        resultado.getInt("id"),
                        resultado.getString("nit"),
                        resultado.getString("servicioPrestado"),
                        resultado.getString("telefono"),
                        resultado.getString("direccion"),
                        resultado.getBigDecimal("saldoFavor"),
                        resultado.getBigDecimal("saldoContra")
                )
                );
            }
            resultado.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        listaProveedores = FXCollections.observableArrayList(listado);
        return listaProveedores;
    }
         
     public void cargarDatos() {
        tblCuentasPorPagar.setItems(getCuentasPorPagar());
        colId.setCellValueFactory(new PropertyValueFactory<CuentasPorPagar, Integer>("id"));
        colNumero.setCellValueFactory(new PropertyValueFactory<CuentasPorPagar, String>("numeroFactura"));
        colFecha.setCellValueFactory(new PropertyValueFactory<CuentasPorPagar, Date>("fechaLimitePago"));
        colEstado.setCellValueFactory(new PropertyValueFactory<CuentasPorPagar, String>("estadoPago"));
        colValorNeto.setCellValueFactory(new PropertyValueFactory<CuentasPorPagar, BigDecimal>("valorNetoPago"));
        colAdministracion.setCellValueFactory(new PropertyValueFactory<CuentasPorPagar, Integer>("codigoAdministracion"));
        colProveedor.setCellValueFactory(new PropertyValueFactory<CuentasPorPagar, Integer>("codigoProveedor"));
        cmbAdministracion.setItems(getAdministracion());
        cmbProveedor.setItems(getProveedores());
    }
     
    public Administracion buscarAdministracion(int id) {
        Administracion registro = null;
        try {
            PreparedStatement pstmt = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarAdministracion(?)}");
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                registro = new Administracion(
                        rs.getInt("id"),
                        rs.getString("direccion"),
                        rs.getString("telefono")
                );
            }
            rs.close();
            pstmt.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return registro;
    }
    
    public Proveedores buscarProveedores(int id) {
        Proveedores proveedor = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarProveedores(?)");
            pstmt.setInt(1, id);
            System.out.println(pstmt.toString());
            rs = pstmt.executeQuery();

            while (rs.next()) {
                proveedor = new Proveedores(
                        rs.getInt("id"),
                        rs.getString("nit"),
                        rs.getString("servicioPrestado"),
                        rs.getString("telefono"),
                        rs.getString("direccion"),
                        rs.getBigDecimal("saldoFavor"),
                        rs.getBigDecimal("saldoContra"));
            }
        } catch (SQLException e) {
            System.err.println("Se produjo un erro al buscar el proveedor");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                pstmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return proveedor;
    }
    
    public boolean existeElementosSeleccionado() {
        if (tblCuentasPorPagar.getSelectionModel().getSelectedItem() == null) {
            return false;
        } else {
            return true;
        }
    }
    @FXML
    public void seleccionarElemento() {
        if (existeElementosSeleccionado()) {
            txtId.setText(String.valueOf(((CuentasPorPagar) tblCuentasPorPagar.getSelectionModel().getSelectedItem()).getId()));
            txtEstadoPago.setText(String.valueOf(((CuentasPorPagar) tblCuentasPorPagar.getSelectionModel().getSelectedItem()).getEstadoPago()));
            txtValorNeto.setText(String.valueOf(((CuentasPorPagar) tblCuentasPorPagar.getSelectionModel().getSelectedItem()).getValorNetoPago()));
            txtNumeroFactura.setText(String.valueOf(((CuentasPorPagar) tblCuentasPorPagar.getSelectionModel().getSelectedItem()).getNumeroFactura()));
            cmbAdministracion.getSelectionModel().select(buscarAdministracion(((CuentasPorPagar) tblCuentasPorPagar.getSelectionModel().getSelectedItem()).getCodigoAdministracion()));
            cmbProveedor.getSelectionModel().select(buscarProveedores(((CuentasPorPagar) tblCuentasPorPagar.getSelectionModel().getSelectedItem()).getCodigoProveedor()));
            tpFechaLimite.setValue(((CuentasPorPagar) tblCuentasPorPagar.getSelectionModel().getSelectedItem()).getFechaLimitePago().toLocalDate());
        }
    }
    
    public void agregarCuentasPorPagar() {
        CuentasPorPagar cuentasPorPagar = new CuentasPorPagar();
        cuentasPorPagar.setNumeroFactura(txtNumeroFactura.getText());
        cuentasPorPagar.setFechaLimitePago(Date.valueOf(tpFechaLimite.getValue()));
        cuentasPorPagar.setEstadoPago(txtEstadoPago.getText());
        cuentasPorPagar.setValorNetoPago(new BigDecimal(txtValorNeto.getText()));
        cuentasPorPagar.setCodigoAdministracion(((Administracion) cmbAdministracion.getSelectionModel().getSelectedItem()).getId());
        cuentasPorPagar.setCodigoProveedor(((Proveedores) cmbProveedor.getSelectionModel().getSelectedItem()).getId());
        PreparedStatement pstmt = null;
        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarCuentasPorPagar(?,?,?,?,?,?)}");
            pstmt.setString(1, cuentasPorPagar.getNumeroFactura());
            pstmt.setDate(2, cuentasPorPagar.getFechaLimitePago());
            pstmt.setString(3, cuentasPorPagar.getEstadoPago());
            pstmt.setBigDecimal(4, cuentasPorPagar.getValorNetoPago());
            pstmt.setInt(5, cuentasPorPagar.getCodigoAdministracion());
            pstmt.setInt(6, cuentasPorPagar.getCodigoProveedor());
            System.out.println(pstmt.toString());
            pstmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                pstmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void editarCuentasPorPagar() {
        CuentasPorPagar cuentasPorPagar = new CuentasPorPagar();
        cuentasPorPagar.setId(Integer.parseInt(txtId.getText()));
        cuentasPorPagar.setNumeroFactura(txtNumeroFactura.getText());
        cuentasPorPagar.setFechaLimitePago(Date.valueOf(tpFechaLimite.getValue()));
        cuentasPorPagar.setEstadoPago(txtEstadoPago.getText());
        cuentasPorPagar.setValorNetoPago(new BigDecimal(txtValorNeto.getText()));
        cuentasPorPagar.setCodigoAdministracion(((Administracion) cmbAdministracion.getSelectionModel().getSelectedItem()).getId());
        cuentasPorPagar.setCodigoProveedor(((Proveedores) cmbProveedor.getSelectionModel().getSelectedItem()).getId());
        PreparedStatement pstmt = null;
        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarCuentasPorPagar(?, ?, ?, ?, ?, ?, ?)}");

            pstmt.setInt(1, cuentasPorPagar.getId());
            pstmt.setString(2, cuentasPorPagar.getNumeroFactura());
            pstmt.setDate(3, cuentasPorPagar.getFechaLimitePago());
            pstmt.setString(4, cuentasPorPagar.getEstadoPago());
            pstmt.setBigDecimal(5, cuentasPorPagar.getValorNetoPago());
            pstmt.setInt(6, cuentasPorPagar.getCodigoAdministracion());
            pstmt.setInt(7, cuentasPorPagar.getCodigoProveedor());

            System.out.println(pstmt);

            pstmt.execute();
            cargarDatos();
            limpiarControles();
            desactivarControles();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void eliminarCuentasPorCobrar() {
        CuentasPorPagar cuentasPagar = null;
        if(existeElementosSeleccionado()){
            cuentasPagar = ((CuentasPorPagar) tblCuentasPorPagar.getSelectionModel().getSelectedItem());
            System.out.println(cuentasPagar);
            try{
                 PreparedStatement pstmt = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarCuentasPorPagar(?)}");
                pstmt.setInt(1, cuentasPagar.getId());

                System.out.println(pstmt.toString());
                pstmt.execute();
            }catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
    @FXML
    private void nuevo(ActionEvent event) {
        System.out.println("Operacion: " + operacion);
        switch (operacion) {
            case NINGUNO:
                activarControles();
                limpiarControles();
                btnNuevo.setText("Guardar");
                imgNuevo.setImage(new Image("/org/MarceloDiaz/resource/images/disco-flexible.png"));
                btnModificar.setText("Cancelar");
                imgModificar.setImage(new Image("org/MarceloDiaz/resource/images/cancelar.png"));
                imgReporte.setOpacity(0.15);
                imgEliminar.setOpacity(0.15);
                btnEliminar.setDisable(true);
                btnReporte.setDisable(true);
                operacion = CuentasporPagarController.Operaciones.GUARDAR;
                break;
            case GUARDAR:
                ArrayList<TextField> listaCampos = new ArrayList<>();
                listaCampos.add(txtNumeroFactura);
                listaCampos.add(txtValorNeto);
                listaCampos.add(txtEstadoPago);
                ArrayList<ComboBox> listaComboBox = new ArrayList<>();  
                listaComboBox.add(cmbAdministracion);
                listaComboBox.add(cmbProveedor);
                if (escenarioPrincipal.validar(listaCampos, listaComboBox)&& 
                        tpFechaLimite.getValue() != null && validarNumeroReal(txtValorNeto.getText())) {
                    agregarCuentasPorPagar();
                    cargarDatos();
                    limpiarControles();
                    desactivarControles();
                    operacion = CuentasporPagarController.Operaciones.NINGUNO;
                    btnNuevo.setText("Nuevo");
                    imgNuevo.setImage(new Image("/org/MarceloDiaz/resource/images/boton-agregar.png"));
                    imgEliminar.setOpacity(1);
                    btnModificar.setText("Modificar");
                    imgModificar.setImage(new Image("org/MarceloDiaz/resource/images/editar.png"));
                    imgReporte.setOpacity(1);
                    btnEliminar.setDisable(false);
                    btnReporte.setDisable(false);
                
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Kinal mall - Horarios");
                    alert.setHeaderText(null);
                    alert.setContentText("Por favor llena todos los datos");
                    alert.show();
                }
                break; 
        }
    }

    @FXML
    private void modificar(ActionEvent event) {
         System.out.println("Operacion" + operacion);
        switch (operacion) {
            case NINGUNO:
                 if (existeElementosSeleccionado()) {
                     activarControles();
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
                    alert.setTitle("Kinal Mall - Horarios");
                    alert.setHeaderText(null);
                    alert.setContentText("Seleccione los datos para modificar");
                    alert.show();
                 }
                 break;
            case ACTUALIZAR:
                ArrayList<TextField> listaCampos = new ArrayList<>();
                listaCampos.add(txtNumeroFactura);
                listaCampos.add(txtValorNeto);
                listaCampos.add(txtEstadoPago);
                ArrayList<ComboBox> listaComboBox = new ArrayList<>();  
                listaComboBox.add(cmbAdministracion);
                listaComboBox.add(cmbProveedor);
                if (escenarioPrincipal.validar(listaCampos, listaComboBox)&& 
                        tpFechaLimite.getValue() != null && validarNumeroReal(txtValorNeto.getText())) {
                       editarCuentasPorPagar();
                       limpiarControles();
                       cargarDatos();
                       btnModificar.setText("Modificar");
                       imgModificar.setImage(new Image ("/org/MarceloDiaz/resource/images/editar.png"));
                       btnEliminar.setText("Cancelar");
                       imgEliminar.setImage(new Image("org/MarceloDiaz/resource/images/marca-x.png"));
                       imgReporte.setOpacity(1);
                       imgNuevo.setOpacity(1);
                       btnNuevo.setDisable(false);
                       btnReporte.setDisable(false);
                       operacion = Operaciones.NINGUNO;
                       desactivarControles(); 
                  
                 }else{
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Kinal Mall - Horarios");
                    alert.setHeaderText(null);
                    alert.setContentText("Complete todos los campos");
                    alert.show();  
                }
                       /*
                 }else{
                     Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Kinal Mall - Horarios");
                    alert.setHeaderText(null);
                    alert.setContentText("Porfavor llene todos los campos");
                    alert.show();  
                 }*/
                break;
                case GUARDAR:
                    btnNuevo.setDisable(false);
                    btnModificar.setText("Modificar");
                    imgNuevo.setImage(new Image ("/org/MarceloDiaz/resource/images/boton-agregar.png"));
                    btnModificar.setText("Modificar");
                    imgModificar.setImage(new Image("/org/MarceloDiaz/resource/images/editar.png"));
                    btnEliminar.setDisable(false);
                    imgEliminar.setOpacity(1);
                    btnReporte.setDisable(false);
                    imgReporte.setOpacity(1);
                    operacion = Operaciones.NINGUNO; 
                    desactivarControles();
                    limpiarControles();
                    break;
        }
    }

    @FXML
    private void eliminar(ActionEvent event) {
         System.out.println("Operacion" + operacion);
         switch (operacion) {
             case ACTUALIZAR: //cancelar
                 desactivarControles();
                 btnNuevo.setDisable(false); 
                 imgNuevo.setOpacity(1);
                 btnReporte.setDisable(false);
                imgReporte.setOpacity(1);
                 btnModificar.setText("Modificar");
                 imgModificar.setImage(new Image("/org/MarceloDiaz/resource/images/editar.png"));
                 btnEliminar.setText("Eliminar");
                 imgEliminar.setImage(new Image("/org/MarceloDiaz/resource/images/marca-x.png"));
                operacion = CuentasporPagarController.Operaciones.NINGUNO;
                    limpiarControles();
                 break;
            case NINGUNO://eliminar
                if (existeElementosSeleccionado()) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Kinal mall");
                    alert.setHeaderText(null);
                    alert.setContentText("Estas seguro que quiere eliminar?  ");
                    Optional<ButtonType> respuesta = alert.showAndWait();
                    if (respuesta.get() == ButtonType.OK) {
                        eliminarCuentasPorCobrar();
                        limpiarControles();
                        cargarDatos();
                        break;
                    }
                } else {
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
        GenerarReporte.getInstance().mostrarReporte("ReporteCuentasPorPagar.jasper", parametros, "Listado de Cuentas por cobrar");
    }

    @FXML
    private void menuPrincipal(MouseEvent event) {
        escenarioPrincipal.proveedores();
    }

    
}
