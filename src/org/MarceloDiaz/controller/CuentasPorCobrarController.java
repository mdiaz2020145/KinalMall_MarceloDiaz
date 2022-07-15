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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javax.swing.JOptionPane;
import org.MarceloDiaz.bean.Administracion;
import org.MarceloDiaz.bean.Clientes;
import org.MarceloDiaz.bean.CuentasPorCobrar;
import org.MarceloDiaz.bean.Locales;
import org.MarceloDiaz.db.Conexion;
import org.MarceloDiaz.report.GenerarReporte;
import org.MarceloDiaz.system.Principal;

/**
 *
* @author Marcelo Javier Diaz Pineda
* @date 30/06/2021
* @time 10:15:43
 */
public class CuentasPorCobrarController implements Initializable{
    private Principal escenarioPrincipal;
    private ObservableList<CuentasPorCobrar> listaCuentasPorCobrar;
    private ObservableList<Clientes> listaClientes;
    private ObservableList<Locales> listaLocales;
    private ObservableList<Administracion> listaAdministracion;
    @FXML
    private ImageView imgNuevo;
    @FXML
    private ImageView imgModificar;
    @FXML
    private ImageView imgEliminar;
    @FXML
    private ImageView imgReporte;
    @FXML
    private TableView tblCuentasPorCobrar;
    @FXML
    private TableColumn colMes;
    @FXML
    private TextField txtId;
    @FXML
    private ComboBox cmbEstadoPago;

    private enum Operaciones {
        GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO
    }
    private Operaciones operacion = Operaciones.NINGUNO;
    @FXML
    private HBox hBoxNuevo;
    @FXML
    private Button btnNuevo;
    @FXML
    private HBox hBoxModificar;
    @FXML
    private Button btnModificar;
    @FXML
    private HBox hBoxEliminar;
    @FXML
    private Button btnEliminar;
    @FXML
    private HBox hBoxReporte;
    @FXML
    private Button btnReporte;
    private TableView tblLocales;
    @FXML
    private TableColumn colId;
    @FXML
    private TableColumn colFactura;
    @FXML
    private TableColumn colAño;
    @FXML
    private TableColumn colValorNeto;
    @FXML
    private TableColumn colEstado;
    @FXML
    private TableColumn colAdministracion;
    @FXML
    private TableColumn colCliente;
    @FXML
    private TableColumn colLocal;

    @FXML
    private Spinner<Integer> spnAño;
    private SpinnerValueFactory<Integer> valueFactoryAño;

    @FXML
    private Spinner<Integer> spnMes;
    private SpinnerValueFactory<Integer> valueFactoryMes;
    @FXML
    private TextField txtFactura;
    @FXML
    private TextField txtValorNeto;
    @FXML
    private ComboBox cmbAdministracion;
    @FXML
    private ComboBox cmbCliente;
    @FXML
    private ComboBox cmbLocal;
    private TextField txtEstado;
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        valueFactoryAño = new SpinnerValueFactory.IntegerSpinnerValueFactory(2020, 2050, 2021);
        valueFactoryMes = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 12, 6);
        spnAño.setValueFactory(valueFactoryAño);
        spnMes.setValueFactory(valueFactoryMes);
        cargarDatos();
        
        ObservableList<String> listaOpciones = FXCollections.observableArrayList("PENDIENTE","PAGADO");
        cmbEstadoPago.getItems().addAll(listaOpciones);
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    public boolean existeElementoSeleccionado() {
        if (tblCuentasPorCobrar.getSelectionModel().getSelectedItem() == null) {
            return false;
        } else {
            return true;
        }
    }
    
    @FXML
    private void menuPrincipal(MouseEvent event) {
        escenarioPrincipal.clientes();
    }

    public void ActivarControles() {
        txtId.setEditable(false);
        txtFactura.setEditable(true);
        txtValorNeto.setEditable(true);
        //txtEstado.setEditable(true);

        spnAño.setDisable(false);
        spnMes.setDisable(false);

        cmbAdministracion.setDisable(false);
        cmbCliente.setDisable(false);
        cmbLocal.setDisable(false);
        cmbEstadoPago.setDisable(false);

    }

    public void limpiarControles() {
        txtId.clear();
        txtFactura.clear();
        txtValorNeto.clear();

        spnAño.getValueFactory().setValue(2021);
        spnMes.getValueFactory().setValue(1);

        cmbAdministracion.valueProperty().set(null);
        cmbCliente.valueProperty().set(null);
        cmbLocal.valueProperty().set(null);
        cmbEstadoPago.valueProperty().set(null);
    }

    public void desactivarControles() {
        spnAño.setDisable(false);
        spnMes.setDisable(true);
        cmbAdministracion.setDisable(true);
        cmbLocal.setDisable(true);
        cmbCliente.setDisable(true);
        cmbEstadoPago.setDisable(true);
        txtId.setEditable(false);
        txtFactura.setEditable(false);
        txtValorNeto.setEditable(false);
    }
    
    public ObservableList<CuentasPorCobrar> getCuentasPorCobrar() {
        ArrayList<CuentasPorCobrar> listado = new ArrayList<>();
        try {
            PreparedStatement pstmt = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarCuentasPorCobrar}");
            System.out.println(pstmt);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                listado.add(new CuentasPorCobrar(
                        rs.getInt("id"),
                        rs.getString("numeroFactura"),
                        rs.getInt("anio"),
                        rs.getInt("mes"),
                        rs.getBigDecimal("valorNetoPago"),
                        rs.getString("estadoPago"),
                        rs.getInt("codigoAdministracion"),
                        rs.getInt("codigoCliente"),
                        rs.getInt("codigoLocal")
                )
                );
            }
            listaCuentasPorCobrar = FXCollections.observableArrayList(listado);
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("Se produjo un error al consultar la tabla cuentas por cobrar en la base de datos");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaCuentasPorCobrar;
    }
    
    public ObservableList<Locales> getLocales() {
        ArrayList<Locales> listado = new ArrayList<Locales>();
        try {
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarLocales()}");
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
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
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        listaLocales = FXCollections.observableArrayList(listado);
        return listaLocales;
    }
    
       public ObservableList<Clientes> getClientes() {
        ArrayList<Clientes> lista = new ArrayList<>();
        try {
            PreparedStatement pstmt = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarClientes}");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                lista.add(new Clientes(
                        rs.getInt("id"),
                        rs.getString("nombres"),
                        rs.getString("apellidos"),
                        rs.getString("telefono"),
                        rs.getString("direccion"),
                        rs.getString("email"),
                        rs.getInt("codigoTipoCliente")));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        listaClientes = FXCollections.observableArrayList(lista);
        return listaClientes;

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
    public void cargarDatos() {
        tblCuentasPorCobrar.setItems(getCuentasPorCobrar());
        colId.setCellValueFactory(new PropertyValueFactory<CuentasPorCobrar, Integer>("id"));
        colFactura.setCellValueFactory(new PropertyValueFactory<CuentasPorCobrar, String>("numeroFactura"));
        colAño.setCellValueFactory(new PropertyValueFactory<CuentasPorCobrar, Integer>("anio"));
        colMes.setCellValueFactory(new PropertyValueFactory<CuentasPorCobrar, Integer>("mes"));
        colValorNeto.setCellValueFactory(new PropertyValueFactory<CuentasPorCobrar, BigDecimal>("valorNetoPago"));
        colEstado.setCellValueFactory(new PropertyValueFactory<CuentasPorCobrar, String>("estadoPago"));
        colAdministracion.setCellValueFactory(new PropertyValueFactory<CuentasPorCobrar, Integer>("codigoAdministracion"));
        colCliente.setCellValueFactory(new PropertyValueFactory<CuentasPorCobrar, Integer>("codigoCliente"));
        colLocal.setCellValueFactory(new PropertyValueFactory<CuentasPorCobrar, Integer>("codigoLocal"));
        cmbAdministracion.setItems(getAdministracion());
        cmbCliente.setItems(getClientes());
        cmbLocal.setItems(getLocales());

    }

    public Administracion buscarAdministracion(int id) {
        Administracion registro = null;
        try {
            PreparedStatement pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_BuscarAdministracion(?)}");
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
    public Clientes buscarClientes(int id) {
        Clientes registro = null;
        try {
            PreparedStatement pstmt = Conexion.getInstance().getConexion().prepareCall("{Call sp_BuscarClientes(?)}");
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                registro = new Clientes(
                        rs.getInt("id"),
                        rs.getString("nombres"),
                        rs.getString("apellidos"),
                        rs.getString("telefono"),
                        rs.getString("direccion"),
                        rs.getString("email"),
                        rs.getInt("codigoTipoCliente")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return registro;
    }
    
    public Locales buscarLocales(int id) {
        Locales registro = null;
        try {
            PreparedStatement pstmt = Conexion.getInstance().getConexion().prepareCall("{Call sp_BuscarLocales(?)}");
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                registro = new Locales(
                        rs.getInt("id"),
                        rs.getBigDecimal("SaldoFavor"),
                        rs.getBigDecimal("SaldoContra"),
                        rs.getInt("mesesPendientes"),
                        rs.getBoolean("disponibilidad"),
                        rs.getBigDecimal("valorLocal"),
                        rs.getBigDecimal("valorAdministracion")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return registro;
    }
    
    @FXML
    public void seleccionarElemento() {
        if (existeElementoSeleccionado()) {
            txtId.setText(String.valueOf(((CuentasPorCobrar) tblCuentasPorCobrar.getSelectionModel().getSelectedItem()).getId()));
            txtFactura.setText(String.valueOf(((CuentasPorCobrar) tblCuentasPorCobrar.getSelectionModel().getSelectedItem()).getNumeroFactura()));
            spnAño.getValueFactory().setValue(((CuentasPorCobrar) tblCuentasPorCobrar.getSelectionModel().getSelectedItem()).getAnio());
            spnMes.getValueFactory().setValue(((CuentasPorCobrar) tblCuentasPorCobrar.getSelectionModel().getSelectedItem()).getMes());
            txtValorNeto.setText(String.valueOf(((CuentasPorCobrar) tblCuentasPorCobrar.getSelectionModel().getSelectedItem()).getValorNetoPago()));
            //txtEstado.setText(((CuentasPorCobrar) tblCuentasPorCobrar.getSelectionModel().getSelectedItem()).getEstadoPago());
            cmbEstadoPago.setValue(((CuentasPorCobrar) tblCuentasPorCobrar.getSelectionModel().getSelectedItem()).getEstadoPago());
            cmbAdministracion.getSelectionModel().select(buscarAdministracion(((CuentasPorCobrar) tblCuentasPorCobrar.getSelectionModel().getSelectedItem()).getCodigoAdministracion()));
            cmbCliente.getSelectionModel().select(buscarClientes(((CuentasPorCobrar) tblCuentasPorCobrar.getSelectionModel().getSelectedItem()).getCodigoCliente()));
            cmbLocal.getSelectionModel().select(buscarLocales(((CuentasPorCobrar) tblCuentasPorCobrar.getSelectionModel().getSelectedItem()).getCodigoLocal()));
        }
    }
    public void agregarCuentasPorCobrar() {
        CuentasPorCobrar cuentaCobrar = new CuentasPorCobrar();
        cuentaCobrar.setNumeroFactura(txtFactura.getText());
        cuentaCobrar.setAnio(spnAño.getValue());
        cuentaCobrar.setMes(spnMes.getValue());
        cuentaCobrar.setValorNetoPago(new BigDecimal(txtValorNeto.getText()));
        //cuentaCobrar.setEstadoPago(txtEstado.getText());
        cuentaCobrar.setEstadoPago(cmbEstadoPago.getValue().toString());
        cuentaCobrar.setCodigoAdministracion(((Administracion) cmbAdministracion.getSelectionModel().getSelectedItem()).getId());
        cuentaCobrar.setCodigoCliente(((Clientes) cmbCliente.getSelectionModel().getSelectedItem()).getId());
        cuentaCobrar.setCodigoLocal(((Locales) cmbLocal.getSelectionModel().getSelectedItem()).getId());

        System.out.println(cuentaCobrar);
        PreparedStatement pstmt = null;

        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarCuentasPorCobrar(?,?,?,?,?,?,?,?)}");
            pstmt.setString(1, cuentaCobrar.getNumeroFactura());
            pstmt.setInt(2, cuentaCobrar.getAnio());
            pstmt.setInt(3, cuentaCobrar.getMes());
            pstmt.setBigDecimal(4, cuentaCobrar.getValorNetoPago());
            pstmt.setString(5, cuentaCobrar.getEstadoPago());
            pstmt.setInt(6, cuentaCobrar.getCodigoAdministracion());
            pstmt.setInt(7, cuentaCobrar.getCodigoCliente());
            pstmt.setInt(8, cuentaCobrar.getCodigoLocal());
            pstmt.execute();
            listaCuentasPorCobrar.add(cuentaCobrar);
        } catch (SQLException e) {
            System.out.println("Se produjo un erro al intentar agregar una nueva cuenta por cobrar");
            e.printStackTrace();
        }
    }
    public void editarCuentasPorCobrar() {
        CuentasPorCobrar cuentasCobrar = new CuentasPorCobrar();
        cuentasCobrar.setNumeroFactura(txtFactura.getText());
        cuentasCobrar.setId(Integer.parseInt(txtId.getText()));
        cuentasCobrar.setAnio(spnAño.getValue());
        cuentasCobrar.setMes(spnMes.getValue());
        cuentasCobrar.setValorNetoPago(new BigDecimal(txtValorNeto.getText()));
        //cuentasCobrar.setEstadoPago(txtEstado.getText());
        cuentasCobrar.setEstadoPago(cmbEstadoPago.getValue().toString());
        cuentasCobrar.setCodigoAdministracion(((Administracion) cmbAdministracion.getSelectionModel().getSelectedItem()).getId());
        cuentasCobrar.setCodigoCliente(((Clientes) cmbCliente.getSelectionModel().getSelectedItem()).getId());
        cuentasCobrar.setCodigoLocal(((Locales) cmbLocal.getSelectionModel().getSelectedItem()).getId());

        try {
            PreparedStatement pstmt;
            pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EditarCuentasPorCobrar(?,?,?,?,?,?,?,?,?)}");
            pstmt.setInt(1, cuentasCobrar.getId());
            pstmt.setString(2, cuentasCobrar.getNumeroFactura());
            pstmt.setInt(3, cuentasCobrar.getAnio());
            pstmt.setInt(4, cuentasCobrar.getMes());
            pstmt.setBigDecimal(5, cuentasCobrar.getValorNetoPago());
            pstmt.setString(6, cuentasCobrar.getEstadoPago());
            pstmt.setInt(7, cuentasCobrar.getCodigoAdministracion());
            pstmt.setInt(8, cuentasCobrar.getCodigoCliente());
            pstmt.setInt(9, cuentasCobrar.getCodigoLocal());

            System.out.println(pstmt.toString());

            pstmt.execute();

        } catch (SQLException e) {
            System.err.println("Se produjo un error al intentar editar Cuenta por Cobrar");
            e.printStackTrace();
        }

    }
    public void eliminarCuentasPorCobrar() {

        CuentasPorCobrar cuentasCobrar = null;
        if (existeElementoSeleccionado()) {
            cuentasCobrar = ((CuentasPorCobrar) tblCuentasPorCobrar.getSelectionModel().getSelectedItem());

            System.out.println(cuentasCobrar);

            try {
                PreparedStatement pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EliminarCuentasPorCobrar(?)}");
                pstmt.setInt(1, cuentasCobrar.getId());

                System.out.println(pstmt.toString());
                pstmt.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    @FXML
    private void nuevo(ActionEvent event) {
        System.out.println("Operacion: " + operacion);
        switch (operacion) {
            case NINGUNO:
                ActivarControles();
                limpiarControles();
                operacion = CuentasPorCobrarController.Operaciones.GUARDAR;
                btnNuevo.setText("Guardar");
                imgNuevo.setImage(new Image("/org/MarceloDiaz/resource/images/disco-flexible.png"));
                btnModificar.setText("Cancelar");
                imgModificar.setImage(new Image("org/MarceloDiaz/resource/images/cancelar.png"));
                hBoxEliminar.setDisable(true);
                btnEliminar.setDisable(true);
                imgEliminar.setOpacity(0.15);
                hBoxReporte.setDisable(true);
                btnReporte.setDisable(true);
                imgReporte.setOpacity(0.15);
                break;
            case GUARDAR:
                ArrayList<TextField> listaCampos = new ArrayList<>();
                listaCampos.add(txtFactura);
                //listaCampos.add(txtEstado);
                listaCampos.add(txtValorNeto);

                ArrayList<ComboBox> listaComboBox = new ArrayList<>();
                listaComboBox.add(cmbAdministracion);
                listaComboBox.add(cmbCliente);
                listaComboBox.add(cmbLocal);
                listaComboBox.add(cmbEstadoPago);
                if (escenarioPrincipal.validar(listaCampos, listaComboBox)) {
                    agregarCuentasPorCobrar();
                    cargarDatos();
                    limpiarControles();
                    desactivarControles();
                    operacion = CuentasPorCobrarController.Operaciones.NINGUNO;
                    btnNuevo.setText("Nuevo");
                    imgNuevo.setImage(new Image("/org/MarceloDiaz/resource/images/boton-agregar.png"));
                    btnModificar.setText("Modificar");
                    imgModificar.setImage(new Image("org/MarceloDiaz/resource/images/editar.png"));
                    hBoxEliminar.setDisable(false);
                    btnEliminar.setDisable(false);
                    imgEliminar.setOpacity(1);
                    hBoxReporte.setDisable(false);
                    btnReporte.setDisable(false);
                    imgReporte.setOpacity(1);
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Kinal mall - Cuentas por cobrar");
                    alert.setHeaderText(null);
                    alert.setContentText("Por favor llena todos los datos");
                    alert.show();
                }
        }
    }

    @FXML
    private void modificar(ActionEvent event) {
        System.out.println("Operacion: " + operacion);
        switch (operacion) {
            case NINGUNO:
                if (existeElementoSeleccionado()) {
                    ActivarControles();
                    btnModificar.setText("Actualizar");
                    imgModificar.setImage(new Image("/org/MarceloDiaz/resource/images/actualizar.png"));
                    btnEliminar.setText("Cancelar");
                    imgEliminar.setImage(new Image("org/MarceloDiaz/resource/images/cancelar.png"));
                    hBoxNuevo.setDisable(true);
                    btnNuevo.setDisable(true);
                    imgNuevo.setOpacity(0.15);
                    hBoxReporte.setDisable(true);
                    btnReporte.setDisable(true);
                    imgReporte.setOpacity(0.15);
                    operacion = Operaciones.ACTUALIZAR;
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Kinal Mall - Proveedores");
                    alert.setHeaderText(null);
                    alert.setContentText("Seleccione los datos para modificar");
                    alert.show();
                }
                break;
            case ACTUALIZAR:
                ArrayList<TextField> listaCampos = new ArrayList<>();
                listaCampos.add(txtFactura);
                listaCampos.add(txtValorNeto);
                ArrayList<ComboBox> listaComboBox = new ArrayList<>();
                listaComboBox.add(cmbAdministracion);
                listaComboBox.add(cmbCliente);
                listaComboBox.add(cmbLocal);
                 listaComboBox.add(cmbEstadoPago);
                if (escenarioPrincipal.validar(listaCampos, listaComboBox)) {
                    editarCuentasPorCobrar();
                    limpiarControles();
                    cargarDatos();
                    btnModificar.setText("Modificar");
                    imgNuevo.setOpacity(1);
                    imgModificar.setImage(new Image("/org/MarceloDiaz/resource/images/editar.png"));
                    btnEliminar.setText("Eliminar");
                    imgReporte.setOpacity(1);
                    imgEliminar.setImage(new Image("/org/MarceloDiaz/resource/images/marca-x.png"));
                    hBoxNuevo.setDisable(false);
                    btnNuevo.setDisable(false);
                    hBoxReporte.setDisable(false);
                    btnReporte.setDisable(false);
                    operacion = Operaciones.NINGUNO;
                    desactivarControles();
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Kinal mall - Cuentas por cobrar");
                    alert.setHeaderText(null);
                    alert.setContentText("Porfavor llena todos los datos");
                    alert.show();
                }
                break;
            case GUARDAR:
                limpiarControles();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                imgNuevo.setImage(new Image("/org/MarceloDiaz/resource/images/boton-agregar.png"));
                btnModificar.setText("Modificar");
                imgModificar.setImage(new Image("/org/MarceloDiaz/resource/images/editar.png"));
                hBoxEliminar.setDisable(false);
                btnEliminar.setDisable(false);
                imgEliminar.setOpacity(1);
                hBoxReporte.setDisable(false);
                btnReporte.setDisable(false);
                imgReporte.setOpacity(1);
                operacion = Operaciones.NINGUNO;
        }
        System.out.println("Operacion" + operacion);
    }

    @FXML
    private void eliminar(ActionEvent event) {
        System.out.println("Operacion" + operacion);
        switch (operacion) {
            case ACTUALIZAR: //cancelar
                desactivarControles();
                btnNuevo.setDisable(false);
                imgNuevo.setOpacity(1);
                hBoxNuevo.setDisable(false);
                hBoxReporte.setDisable(false);
                btnReporte.setDisable(false);
                imgReporte.setOpacity(1);
                btnModificar.setText("Modificar");
                imgModificar.setImage(new Image("/org/MarceloDiaz/resource/images/editar.png"));
                btnEliminar.setText("Eliminar");
                imgEliminar.setImage(new Image("/org/MarceloDiaz/resource/images/marca-x.png"));
                operacion = CuentasPorCobrarController.Operaciones.NINGUNO;
                limpiarControles();
                break;
            case NINGUNO://Eliminar
                if (existeElementoSeleccionado()) {
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
        GenerarReporte.getInstance().mostrarReporte("ReporteCuentasPorCobrar.jasper", parametros, "Listado de Cuentas por cobrar");
    }

}
