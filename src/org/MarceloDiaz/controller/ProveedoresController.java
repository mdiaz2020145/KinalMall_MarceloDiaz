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
import org.MarceloDiaz.bean.Proveedores;
import org.MarceloDiaz.db.Conexion;
import org.MarceloDiaz.report.GenerarReporte;
import org.MarceloDiaz.system.Principal;

/**
 *
* @author Marcelo Javier Diaz Pineda
* @date 13/05/2021
* @time 22:40:00
 */
public class ProveedoresController implements Initializable  {
    private Principal escenarioPrincipal;
    private ObservableList<Proveedores>listaProveedores;
    @FXML
    private Button btnNuevo;
    @FXML
    private Button btnModificar;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnReporte;
    @FXML
    private TableColumn colId;
    @FXML
    private TableColumn colNit;
    @FXML
    private TableColumn colServicio;
    @FXML
    private TableColumn colTelefono;
    @FXML
    private TableColumn colDireccion;
    @FXML
    private TableColumn colSaldoFavor;
    @FXML
    private TableColumn colSaldoContra;
    @FXML
    private TextField txtId;
    @FXML
    private TextField txtNit;
    @FXML
    private TextField txtServicio;
    @FXML
    private TextField txtTelefono;
    @FXML
    private TextField txtDireccion;
    @FXML
    private TextField txtSaldoFavor;
    @FXML
    private TextField txtSaldoContra;
    @FXML
    private TextField txtSaldoLiquido;
    @FXML
    private TableView tblProveedores;
    @FXML
    private ImageView imgNuevo;
    @FXML
    private ImageView imgModificar;
    @FXML
    private ImageView imgEliminar;
    @FXML
    private ImageView imgReporte;
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        cargarDatos();
        String nit = "2365489-9";
        if(validarNIT(nit)){
            nit = nit.replace("-","");
        }
    }

    public Principal getEscenrioaPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenaPrincipal) {
        this.escenarioPrincipal = escenaPrincipal;
    }
    @FXML
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
     private enum Operaciones{
        GUARDAR,ELIMINAR,ACTUALIZAR,CANCELAR,NINGUNO
    }
    private Operaciones operacion = Operaciones.NINGUNO;
    
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
        tblProveedores.setItems(getProveedores());
        colId.setCellValueFactory(new PropertyValueFactory<Proveedores, Integer>("id"));
        colNit.setCellValueFactory(new PropertyValueFactory<Proveedores, String>("nit"));
        colServicio.setCellValueFactory(new PropertyValueFactory<Proveedores, String>("servicioPrestado"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<Proveedores, String>("telefono"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<Proveedores, String>("direccion"));
        colSaldoFavor.setCellValueFactory(new PropertyValueFactory<Proveedores, BigDecimal>("saldoFavor"));
        colSaldoContra.setCellValueFactory(new PropertyValueFactory<Proveedores, BigDecimal>("saldoContra"));
    }
    
    public boolean existeElementosSeleccionado() {
        if (tblProveedores.getSelectionModel().getSelectedItem() == null) {
            return false;
        } else {
            return true;
        }
    }
    
    @FXML
    public void seleccionarElemento() {
        if (existeElementosSeleccionado()) {
            txtId.setText(String.valueOf(((Proveedores) tblProveedores.getSelectionModel().getSelectedItem()).getId()));
            txtNit.setText(String.valueOf(((Proveedores) tblProveedores.getSelectionModel().getSelectedItem()).getNit()));
            txtServicio.setText(String.valueOf(((Proveedores) tblProveedores.getSelectionModel().getSelectedItem()).getServicioPrestado()));
            txtTelefono.setText(String.valueOf(((Proveedores) tblProveedores.getSelectionModel().getSelectedItem()).getTelefono()));
            txtDireccion.setText(String.valueOf(((Proveedores) tblProveedores.getSelectionModel().getSelectedItem()).getDireccion()));
            txtSaldoFavor.setText(String.valueOf(((Proveedores) tblProveedores.getSelectionModel().getSelectedItem()).getSaldoFavor()));
            txtSaldoContra.setText(String.valueOf(((Proveedores) tblProveedores.getSelectionModel().getSelectedItem()).getSaldoContra()));

            BigDecimal op1 = ((Proveedores) tblProveedores.getSelectionModel().getSelectedItem()).getSaldoFavor();
            BigDecimal op2 = ((Proveedores) tblProveedores.getSelectionModel().getSelectedItem()).getSaldoContra();
            BigDecimal resultado = op1.subtract(op2);
            txtSaldoLiquido.setText(String.valueOf(resultado));
        }
    }
    public void habilitarControles() {
        txtId.setEditable(false);
        txtNit.setEditable(true);
        txtDireccion.setEditable(true);
        txtServicio.setEditable(true);
        txtTelefono.setEditable(true);
        txtSaldoLiquido.setEditable(false);
        txtSaldoFavor.setEditable(true);
        txtSaldoContra.setEditable(true);
    }

    public void deshabilitarControles() {
        txtId.setEditable(false);
        txtNit.setEditable(false);
        txtDireccion.setEditable(false);
        txtServicio.setEditable(false);
        txtTelefono.setEditable(false);
        txtSaldoLiquido.setEditable(false);
        txtSaldoFavor.setEditable(false);
        txtSaldoContra.setEditable(false);
    }

    public void limpiarControles() {
        txtId.clear();
        txtNit.clear();
        txtDireccion.clear();
        txtServicio.clear();
        txtTelefono.clear();
        txtSaldoLiquido.clear();
        txtSaldoContra.clear();
    }
    
    public boolean validarTelefono(String numero) {
        Pattern pattern = Pattern.compile("^[0-9]{8}$");
        Matcher matcher = pattern.matcher(numero);
        return matcher.matches();

    }
    public boolean validarNIT(String nit){
        String patron = "^[0-9]{6,}[-]?[0-9]{1}$";
        
        Pattern pattern = Pattern.compile(patron);
        Matcher marcher = pattern.matcher(nit);
        System.out.println(marcher.matches());
        return marcher.matches();
    }
    public void agregarProveedores() {
        Proveedores proveedores = new Proveedores();
        proveedores.setNit(txtNit.getText());
        proveedores.setServicioPrestado(txtServicio.getText());
        proveedores.setTelefono(txtTelefono.getText());
        proveedores.setDireccion(txtDireccion.getText());
        proveedores.setSaldoFavor(BigDecimal.valueOf(Double.valueOf(txtSaldoFavor.getText())));
        proveedores.setSaldoContra(BigDecimal.valueOf(Double.valueOf(txtSaldoContra.getText())));

        PreparedStatement stmt = null;

        try {
            stmt = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarProveedores(?,?,?,?,?,?)}");
            stmt.setString(1, proveedores.getNit());
            stmt.setString(2, proveedores.getServicioPrestado());
            stmt.setString(3, proveedores.getTelefono());
            stmt.setString(4, proveedores.getTelefono());
            stmt.setBigDecimal(5, proveedores.getSaldoFavor());
            stmt.setBigDecimal(6, proveedores.getSaldoContra());
            stmt.execute();
            cargarDatos();
            limpiarControles();
            deshabilitarControles();
        } catch (SQLException e) {
            System.err.println("Se produjo un error al agregar Proveedores");
            e.printStackTrace();
        } finally {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public void editarProveedores() {
        Proveedores proveedores = new Proveedores();
        proveedores.setId(Integer.parseInt(txtId.getText()));
        proveedores.setNit(txtNit.getText());
        proveedores.setServicioPrestado(txtServicio.getText());
        proveedores.setTelefono(txtTelefono.getText());
        proveedores.setDireccion(txtDireccion.getText());
        proveedores.setSaldoFavor(BigDecimal.valueOf(Double.valueOf(txtSaldoFavor.getText())));
        proveedores.setSaldoContra(BigDecimal.valueOf(Double.valueOf(txtSaldoContra.getText())));

        PreparedStatement pstmt = null;
        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarProveedores(?,?,?,?,?,?,?)}");
            pstmt.setInt(1, proveedores.getId());
            pstmt.setString(2, proveedores.getNit());
            pstmt.setString(3, proveedores.getServicioPrestado());
            pstmt.setString(4, proveedores.getTelefono());
            pstmt.setString(5, proveedores.getDireccion());
            pstmt.setBigDecimal(6, proveedores.getSaldoFavor());
            pstmt.setBigDecimal(7, proveedores.getSaldoContra());
            System.out.println(pstmt.toString());
            pstmt.execute();
        } catch (SQLException e) {
            System.err.println("Se produjo un error al editar proveedores.");
            e.printStackTrace();
        } finally {
            try {
                pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
    
    public void eliminarProveedores() {
        Proveedores proveedores = null;
        if (existeElementosSeleccionado()) {
            proveedores = ((Proveedores) tblProveedores.getSelectionModel().getSelectedItem());
            System.out.println(proveedores);
            try {
                PreparedStatement pstmt = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarProveedores(?)}");
                pstmt.setInt(1, proveedores.getId());
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
                habilitarControles();
                limpiarControles();
                btnNuevo.setText("Guardar");
                imgNuevo.setImage(new Image("/org/MarceloDiaz/resource/images/disco-flexible.png"));
                btnModificar.setText("Cancelar");
                imgModificar.setImage(new Image("org/MarceloDiaz/resource/images/cancelar.png"));
                imgReporte.setOpacity(0.15);
                imgEliminar.setOpacity(0.15);
                btnEliminar.setDisable(true);
                btnReporte.setDisable(true);
                operacion = ProveedoresController.Operaciones.GUARDAR;
                break;
            case GUARDAR:
                ArrayList<TextField> listaCampos = new ArrayList<>();
                listaCampos.add(txtNit);
                listaCampos.add(txtServicio);
                listaCampos.add(txtTelefono);
                listaCampos.add(txtDireccion);
                listaCampos.add(txtSaldoFavor);
                listaCampos.add(txtSaldoContra);
                ArrayList<ComboBox> listaComboBox = new ArrayList<>();
                if (escenarioPrincipal.validar(listaCampos, listaComboBox)) {
                    if (validarTelefono(txtTelefono.getText())) {
                        if(validarNIT(txtNit.getText())){
                        agregarProveedores();
                        cargarDatos();
                        limpiarControles();
                        deshabilitarControles();
                        operacion = Operaciones.NINGUNO;
                        btnNuevo.setText("Nuevo");
                        imgNuevo.setImage(new Image("/org/MarceloDiaz/resource/images/boton-agregar.png"));
                        imgEliminar.setOpacity(1);
                        btnModificar.setText("Modificar");
                        imgModificar.setImage(new Image("org/MarceloDiaz/resource/images/editar.png"));
                        imgReporte.setOpacity(1);
                        btnEliminar.setDisable(false);
                        btnReporte.setDisable(false);
                        
                        }else{
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Advertencia - Proveedores");
                        alert.setHeaderText(null);
                        alert.setContentText("El NIT es invalido");
                        alert.show();
                        }
                        

                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Advertencia - Proveedores");
                        alert.setHeaderText(null);
                        alert.setContentText("El numero de telefono es invalido");
                        alert.show();
                    }
                } else {
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
        System.out.println("Operacion" + operacion);
        switch (operacion) {
            case NINGUNO:
                if (existeElementosSeleccionado()) {
                    habilitarControles();
                    btnModificar.setText("Actualizar");
                    imgModificar.setImage(new Image("/org/MarceloDiaz/resource/images/actualizar.png"));
                    btnEliminar.setText("Cancelar");
                    imgEliminar.setImage(new Image("org/MarceloDiaz/resource/images/cancelar.png"));
                    btnNuevo.setDisable(true);
                    imgNuevo.setOpacity(0.15);
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
                listaCampos.add(txtId);
                listaCampos.add(txtNit);
                listaCampos.add(txtServicio);
                listaCampos.add(txtTelefono);
                listaCampos.add(txtDireccion);
                listaCampos.add(txtSaldoFavor);
                listaCampos.add(txtSaldoContra);
                ArrayList<ComboBox> listaComboBox = new ArrayList<>();
                if (escenarioPrincipal.validar(listaCampos, listaComboBox)) {
                    if (validarTelefono(txtTelefono.getText())) {
                         if(validarNIT(txtNit.getText())){
                        editarProveedores();
                        limpiarControles();
                        cargarDatos();
                        btnModificar.setText("Modificar");
                        imgModificar.setImage(new Image("/org/MarceloDiaz/resource/images/editar.png"));
                        btnEliminar.setText("Cancelar");
                        imgEliminar.setImage(new Image("org/MarceloDiaz/resource/images/marca-x.png"));
                        imgReporte.setOpacity(1);
                        imgNuevo.setOpacity(1);
                        btnNuevo.setDisable(false);
                        btnReporte.setDisable(false);
                        operacion = Operaciones.NINGUNO;
                        deshabilitarControles();
                         }else{
                             Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Advertencia - Proveedores");
                        alert.setHeaderText(null);
                        alert.setContentText("El NIT es invalido");
                        alert.show();
                         }
                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Advertencia - Proveedores");
                        alert.setHeaderText(null);
                        alert.setContentText("El numero de telefono es invalido");
                        alert.show();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Kinal Mall - Proveedores");
                    alert.setHeaderText(null);
                    alert.setContentText("Complete todos los campos");
                    alert.show();
                }
                break;
            case GUARDAR:
                btnNuevo.setDisable(false);
                btnModificar.setText("Modificar");
                imgNuevo.setImage(new Image("/org/MarceloDiaz/resource/images/boton-agregar.png"));
                btnModificar.setText("Modificar");
                imgModificar.setImage(new Image("/org/MarceloDiaz/resource/images/editar.png"));
                btnEliminar.setDisable(false);
                imgEliminar.setOpacity(1);
                btnReporte.setDisable(false);
                imgReporte.setOpacity(1);
                operacion = Operaciones.NINGUNO;
                deshabilitarControles();
                limpiarControles();
                break;
        }
    }

    @FXML
    private void eliminar(ActionEvent event) {
        System.out.println("Operacion" + operacion);
        switch (operacion) {
            case ACTUALIZAR: //cancelar
                deshabilitarControles();
                limpiarControles();
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
            case NINGUNO://eliminar
                if (existeElementosSeleccionado()) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Kinal mall");
                    alert.setHeaderText(null);
                    alert.setContentText("Estas seguro que quiere eliminar?  ");
                    Optional<ButtonType> respuesta = alert.showAndWait();
                    if (respuesta.get() == ButtonType.OK) {
                        eliminarProveedores();
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
        GenerarReporte.getInstance().mostrarReporte("ReporteProovedores.jasper", parametros, "Listado de Proveedores");
    }
        @FXML
    private void cuentasPorPagar(){
        escenarioPrincipal.CuentasPorPagar();
    }
}
