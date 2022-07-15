/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.MarceloDiaz.controller;

import com.jfoenix.controls.JFXDatePicker;
import com.mysql.cj.jdbc.CallableStatement;
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
import org.MarceloDiaz.bean.Administracion;
import org.MarceloDiaz.bean.Cargos;
import org.MarceloDiaz.bean.Departamentos;
import org.MarceloDiaz.bean.Empleados;
import org.MarceloDiaz.bean.Horarios;
import org.MarceloDiaz.db.Conexion;
import org.MarceloDiaz.report.GenerarReporte;
import org.MarceloDiaz.system.Principal;

/**
 *
* @author Marcelo Javier Diaz Pineda
* @date 13/05/2021
* @time 22:15:58
 */
public class EmpleadoController implements Initializable {
    private Principal escenarioPrincipal; 
    private ObservableList<Empleados>listaEmpleados;
    private ObservableList<Departamentos>listaDepartamentos;
    private ObservableList<Cargos>listaCargos;
    private ObservableList<Horarios>listaHorarios; 
    private ObservableList<Administracion>listaAdministracion;
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
    private TableView tblEmpleados;
    @FXML
    private TableColumn colId;
    @FXML
    private TableColumn colNombres;
    @FXML
    private TableColumn colApellidos;
    @FXML
    private TableColumn colEmail;
    @FXML
    private TableColumn colTelefono;
    @FXML
    private TableColumn colFechaDeContratacion;
    @FXML
    private TableColumn colSueldo;
    @FXML
    private TableColumn colCodigoDepartamento;
    @FXML
    private TableColumn colCodigoCargo;
    @FXML
    private TableColumn colCodigoHorario;
    @FXML
    private TableColumn colCodigoAdministracion;
    @FXML
    private TextField txtId;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtApellidos;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtTelefono;
    @FXML
    private TextField txtSueldo;
    @FXML
    private ComboBox cmbCodigoDepartamento;
    @FXML
    private JFXDatePicker dpFechaDeContratacion;
    @FXML
    private ComboBox cmbCodigoCargo;
    @FXML
    private ComboBox cmbCodigoHorario;
    @FXML
    private ComboBox cmbCodigoAdministracion;

    private enum Operaciones{
        GUARDAR,ELIMINAR,ACTUALIZAR,CANCELAR,NINGUNO
    }
    
    private Operaciones operacion = Operaciones.NINGUNO;
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
     public boolean existeElementoSeleccionado() {
        if (tblEmpleados.getSelectionModel().getSelectedItem() == null) {
            return false;
        } else {
            return true;
        }
    }
    public ObservableList<Administracion> getAdministracion() {
        ArrayList<Administracion> listado = new ArrayList<Administracion>();
        try {
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
        } catch (SQLException e ) {
            e.printStackTrace();
        }
        listaAdministracion = FXCollections.observableArrayList(listado);
        return listaAdministracion;
    } 
     
    public ObservableList<Empleados> getEmpleados() {
        ArrayList<Empleados> listado = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarEmpleados}");
            rs = pstmt.executeQuery();
            while (rs.next()) {
                listado.add(new Empleados(
                        rs.getInt("id"),
                        rs.getString("nombres"),
                        rs.getString("apellidos"),
                        rs.getString("email"),
                        rs.getString("telefono"),
                        rs.getDate("fechaContratacion"),
                        rs.getBigDecimal("sueldo"),
                        rs.getInt("codigoDepartamento"),
                        rs.getInt("codigoCargo"),
                        rs.getInt("codigoHorario"),
                        rs.getInt("codigoAdministracion")
                ));
            }
            listaEmpleados = FXCollections.observableArrayList(listado);

        } catch (SQLException e) {
            System.err.println("Se produjo un error al listar Empleados");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                pstmt.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return listaEmpleados;
    }
    
    public ObservableList<Departamentos> getDepartamentos() {
        ArrayList<Departamentos> lista = new ArrayList<Departamentos>();

        try {
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarDepartamentos()}");
            ResultSet resultado = stmt.executeQuery();

            while (resultado.next()) {
                lista.add(new Departamentos(
                        resultado.getInt("id"),
                        resultado.getString("nombre")
                )
                );
            }
            resultado.close();
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        listaDepartamentos = FXCollections.observableArrayList(lista);
        return listaDepartamentos;
    }
    
    public ObservableList<Cargos> getCargos() {
        ArrayList<Cargos> lista = new ArrayList<Cargos>();
        try {
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarCargos()}");
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                lista.add(new Cargos(
                        resultado.getInt("id"),
                        resultado.getString("nombre")
                )
                );
            }
            resultado.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        listaCargos = FXCollections.observableArrayList(lista);
        return listaCargos;
    }

    public ObservableList<Horarios> getHorarios() {
        ArrayList<Horarios> listado = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarHorarios}");
            rs = pstmt.executeQuery();
            while (rs.next()) {
                listado.add(new Horarios(
                        rs.getInt("id"),
                        rs.getTime("horarioEntrada"),
                        rs.getTime("horarioSalida"),
                        rs.getBoolean("lunes"),
                        rs.getBoolean("martes"),
                        rs.getBoolean("miercoles"),
                        rs.getBoolean("jueves"),
                        rs.getBoolean("viernes")));
            }
            listaHorarios = FXCollections.observableArrayList(listado);

        } catch (SQLException e) {
            System.err.println("Se produjo un error al listar Horarios");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                pstmt.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return listaHorarios;
    }
    public void cargarDatos(){
        tblEmpleados.setItems(getEmpleados());
        colId.setCellValueFactory(new PropertyValueFactory<Administracion, Integer>("id"));
        colNombres.setCellValueFactory(new PropertyValueFactory<Administracion, String>("nombres"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<Administracion, String>("apellidos"));
        colEmail.setCellValueFactory(new PropertyValueFactory<Administracion, String>("email"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<Administracion, String>("telefono"));
        colFechaDeContratacion.setCellValueFactory(new PropertyValueFactory<Administracion, Date>("fechaContratacion"));
        colSueldo.setCellValueFactory(new PropertyValueFactory<Administracion, BigDecimal>("sueldo"));
        colCodigoDepartamento.setCellValueFactory(new PropertyValueFactory<Administracion, Integer>("codigoDepartamento"));
        colCodigoCargo.setCellValueFactory(new PropertyValueFactory<Administracion, Integer>("codigoCargo"));
        colCodigoHorario.setCellValueFactory(new PropertyValueFactory<Administracion, Integer>("codigoHorario"));
        colCodigoAdministracion.setCellValueFactory(new PropertyValueFactory<Administracion, Integer>("codigoAdministracion"));
        cmbCodigoDepartamento.setItems(getDepartamentos());
        cmbCodigoCargo.setItems(getCargos());
        cmbCodigoHorario.setItems(getHorarios());
        cmbCodigoAdministracion.setItems(getAdministracion());
    }
    public Administracion buscarAdministracion(int id) {
        Administracion registro = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_BuscarAdministracion(?)}");
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            while (rs.next()) {
                registro = new Administracion(
                        rs.getInt("id"),
                        rs.getString("direccion"),
                        rs.getString("telefono")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                stmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return registro;
    }
    
    public Cargos buscarCargos(int id) {
        Cargos registro = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_BuscarCargos(?)}");
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            while (rs.next()) {
                registro = new Cargos(
                        rs.getInt("id"),
                        rs.getString("nombre")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                stmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return registro;
    }
    
    public Horarios buscarHorarios(int id) {
        Horarios registro = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_BuscarHorarios(?)}");
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            while (rs.next()) {
                registro = new Horarios(
                        rs.getInt("id"),
                        rs.getTime("horarioEntrada"),
                        rs.getTime("horarioSalida"),
                        rs.getBoolean("lunes"),
                        rs.getBoolean("martes"),
                        rs.getBoolean("miercoles"),
                        rs.getBoolean("jueves"),
                        rs.getBoolean("viernes")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                stmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return registro;
    }
    
    
    public Departamentos buscarDepartamentos(int id) {
        Departamentos registro = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_BuscarDepartamentos(?)}");
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            while (rs.next()) {
                registro = new Departamentos(
                        rs.getInt("id"),
                        rs.getString("nombre")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                stmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return registro;
    }
    
    public void seleccionarElemento() {
        if (existeElementoSeleccionado()) {
            txtId.setText(String.valueOf(((Empleados) tblEmpleados.getSelectionModel().getSelectedItem()).getId()));
            txtNombre.setText(((Empleados) tblEmpleados.getSelectionModel().getSelectedItem()).getNombres());
            txtApellidos.setText(((Empleados) tblEmpleados.getSelectionModel().getSelectedItem()).getApellidos());
            txtEmail.setText(((Empleados) tblEmpleados.getSelectionModel().getSelectedItem()).getEmail());
            txtTelefono.setText(((Empleados) tblEmpleados.getSelectionModel().getSelectedItem()).getTelefono());
            dpFechaDeContratacion.setValue(((Empleados) tblEmpleados.getSelectionModel().getSelectedItem()).getFechaContratacion().toLocalDate());
            txtSueldo.setText(String.valueOf(((Empleados) tblEmpleados.getSelectionModel().getSelectedItem()).getSueldo()));
            cmbCodigoDepartamento.getSelectionModel().select(buscarDepartamentos(((Empleados) tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoDepartamento()));
            cmbCodigoCargo.getSelectionModel().select(buscarCargos(((Empleados) tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoCargo()));
            cmbCodigoHorario.getSelectionModel().select(buscarHorarios(((Empleados) tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoHorario()));
            cmbCodigoAdministracion.getSelectionModel().select(buscarAdministracion(((Empleados) tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoAdministracion()));
        }
    }
    private void ActivarControles() {
        txtId.setEditable(false);
        txtNombre.setEditable(true);
        txtApellidos.setEditable(true);
        txtEmail.setEditable(true);
        dpFechaDeContratacion.setDisable(false);
        txtSueldo.setEditable(true);
        txtTelefono.setEditable(true);
        cmbCodigoAdministracion.setDisable(false);
        cmbCodigoCargo.setDisable(false);
        cmbCodigoDepartamento.setDisable(false);
        cmbCodigoHorario.setDisable(false);


    }

    private void limpiarControles() {
          txtId.clear();
        txtNombre.clear();
        txtApellidos.clear();
        txtEmail.clear();
        txtTelefono.clear();
        txtSueldo.clear();
        cmbCodigoAdministracion.valueProperty().set(null);
        cmbCodigoCargo.valueProperty().set(null);
        cmbCodigoDepartamento.valueProperty().set(null);
        cmbCodigoHorario.valueProperty().set(null);
    }

    private void desactivarControles() {
        txtId.setEditable(false);
        txtNombre.setEditable(false);
        txtApellidos.setEditable(false);
        txtEmail.setEditable(false);
        txtSueldo.setEditable(false);
        txtTelefono.setEditable(false);
        dpFechaDeContratacion.setDisable(true);
        cmbCodigoAdministracion.setDisable(true);
        cmbCodigoCargo.setDisable(true);
        cmbCodigoDepartamento.setDisable(true);
        cmbCodigoHorario.setDisable(true);
    }
    
    public void agregarEmpleados() {
        Empleados empleado = new Empleados();
        empleado.setNombres(txtNombre.getText());
        empleado.setApellidos(txtApellidos.getText());
        empleado.setEmail(txtEmail.getText());
        empleado.setTelefono(txtTelefono.getText());
        empleado.setFechaContratacion(Date.valueOf(dpFechaDeContratacion.getValue()));
        empleado.setSueldo(new BigDecimal(txtSueldo.getText()));
        empleado.setCodigoDepartamento(((Departamentos) cmbCodigoDepartamento.getSelectionModel().getSelectedItem()).getId());
        empleado.setCodigoCargo(((Cargos) cmbCodigoCargo.getSelectionModel().getSelectedItem()).getId());
        empleado.setCodigoHorario(((Horarios) cmbCodigoHorario.getSelectionModel().getSelectedItem()).getId());
        empleado.setCodigoAdministracion(((Administracion) cmbCodigoAdministracion.getSelectionModel().getSelectedItem()).getId());

        PreparedStatement pstmt = null;

        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarEmpleados(?,?,?,?,?,?,?,?,?,?)}");
            pstmt.setString(1, empleado.getNombres());
            pstmt.setString(2, empleado.getApellidos());
            pstmt.setString(3, empleado.getEmail());
            pstmt.setString(4, empleado.getTelefono());
            pstmt.setDate(5, (Date) empleado.getFechaContratacion());
            pstmt.setBigDecimal(6, empleado.getSueldo());
            pstmt.setInt(7, empleado.getCodigoDepartamento());
            pstmt.setInt(8, empleado.getCodigoCargo());
            pstmt.setInt(9, empleado.getCodigoHorario());
            pstmt.setInt(10, empleado.getCodigoAdministracion());

            System.out.println(pstmt.toString());
            pstmt.execute();

        } catch (SQLException e) {
            System.err.println("se produjo un error al agregar Empleados");
            e.printStackTrace();
        } finally {
            try {
                pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
    
    public void editarEmpleados() {
        Empleados empleado = new Empleados();
        empleado.setId(Integer.parseInt(txtId.getText()));
        empleado.setNombres(txtNombre.getText());
        empleado.setApellidos(txtApellidos.getText());
        empleado.setEmail(txtEmail.getText());
        empleado.setTelefono(txtTelefono.getText());
        empleado.setFechaContratacion(Date.valueOf(dpFechaDeContratacion.getValue()));
        empleado.setSueldo(new BigDecimal(txtSueldo.getText()));
        empleado.setCodigoDepartamento(((Departamentos) cmbCodigoDepartamento.getSelectionModel().getSelectedItem()).getId());
        empleado.setCodigoCargo(((Cargos) cmbCodigoCargo.getSelectionModel().getSelectedItem()).getId());
        empleado.setCodigoHorario(((Horarios) cmbCodigoHorario.getSelectionModel().getSelectedItem()).getId());
        empleado.setCodigoAdministracion(((Administracion) cmbCodigoAdministracion.getSelectionModel().getSelectedItem()).getId());

        PreparedStatement pstmt = null;

        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarEmpleados(?,?,?,?,?,?,?,?,?,?,?)}");
            pstmt.setInt(1, empleado.getId());
            pstmt.setString(2, empleado.getNombres());
            pstmt.setString(3, empleado.getApellidos());
            pstmt.setString(4, empleado.getEmail());
            pstmt.setString(5, empleado.getTelefono());
            pstmt.setDate(6, (Date) empleado.getFechaContratacion());
            pstmt.setBigDecimal(7, empleado.getSueldo());
            pstmt.setInt(8, empleado.getCodigoDepartamento());
            pstmt.setInt(9, empleado.getCodigoCargo());
            pstmt.setInt(10, empleado.getCodigoHorario());
            pstmt.setInt(11, empleado.getCodigoAdministracion());

            System.out.println(pstmt.toString());
            pstmt.execute();

        } catch (SQLException e) {
            System.err.println("se produjo un error al editar empleados");
            e.printStackTrace();
        } finally {
            try {
                pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public void eliminarEmpleados() {

        Empleados empleado = null;
        if (existeElementoSeleccionado()) {
            empleado = ((Empleados) tblEmpleados.getSelectionModel().getSelectedItem());

            System.out.println(empleado);

            try {
                PreparedStatement pstmt = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarEmpleados(?)}");
                pstmt.setInt(1, empleado.getId());

                System.out.println(pstmt.toString());
                pstmt.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public boolean validarTelefono(String numero) {
        Pattern pattern1 = Pattern.compile("^[0-9]{8}$");
        Matcher matcher1 = pattern1.matcher(numero);
        return matcher1.matches();
    }
    
    public boolean validarEmail(String email) {
        Pattern pattern = Pattern.compile("^[\\w-]+(\\.[\\w-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    
     public boolean validarNumeroReal(String numero) {
        String patron = "^[0-9]+([.][0-9]{2})?";
        Pattern pattern = Pattern.compile(patron);
        Matcher matcher = pattern.matcher((numero));
        return matcher.matches();
    }
     
     
    @FXML
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    
    @FXML
    private void nuevo(ActionEvent event) {
        System.out.println("Operacion: " + operacion);
        switch (operacion) {
            case NINGUNO:
                ActivarControles();
                limpiarControles();
                operacion = Operaciones.GUARDAR;
                btnNuevo.setText("Guardar");
                imgNuevo.setImage(new Image("/org/MarceloDiaz/resource/images/disco-flexible.png"));
                btnModificar.setText("Cancelar");
                imgModificar.setImage(new Image("org/MarceloDiaz/resource/images/cancelar.png"));
                btnEliminar.setDisable(true);
                imgEliminar.setOpacity(0.15);
                btnReporte.setDisable(true);
                imgReporte.setOpacity(0.15);
                break;
            case GUARDAR:
                ArrayList<TextField> listaCampos = new ArrayList<>();
                listaCampos.add(txtNombre);
                listaCampos.add(txtApellidos);
                listaCampos.add(txtEmail);
                listaCampos.add(txtSueldo);
                listaCampos.add(txtTelefono);

                ArrayList<ComboBox> listaComboBox = new ArrayList<>();
                listaComboBox.add(cmbCodigoAdministracion);
                listaComboBox.add(cmbCodigoCargo);
                listaComboBox.add(cmbCodigoDepartamento);
                listaComboBox.add(cmbCodigoHorario);
                if (escenarioPrincipal.validar(listaCampos, listaComboBox)) {
                    if (validarNumeroReal(txtSueldo.getText())) {
                        if (validarTelefono(txtTelefono.getText())) {
                            if (validarEmail(txtEmail.getText())) {
                                agregarEmpleados();
                                cargarDatos();
                                limpiarControles();
                                desactivarControles();
                                operacion = Operaciones.NINGUNO;
                                btnNuevo.setText("Nuevo");
                                imgNuevo.setImage(new Image("/org/MarceloDiaz/resource/images/boton-agregar.png"));
                                btnModificar.setText("Modificar");
                                imgModificar.setImage(new Image("org/MarceloDiaz/resource/images/editar.png"));
                                btnEliminar.setDisable(false);
                                imgEliminar.setOpacity(1);
                                btnReporte.setDisable(false);
                                imgReporte.setOpacity(1);
                            } else {
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("Kinal mall - Empleado");
                                alert.setHeaderText(null);
                                alert.setContentText("El email es invalido");
                                alert.show();
                            }
                        } else {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Kinal mall - Empleado");
                            alert.setHeaderText(null);
                            alert.setContentText("Numero de telefono invalido");
                            alert.show();
                        }
                    } else {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Kinal mall - Empleado");
                        alert.setHeaderText(null);
                        alert.setContentText("Los valores del sueldo no son validos");
                        alert.show();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Kinal mall - Empleado");
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
        switch (operacion) {
            case NINGUNO:
                if (existeElementoSeleccionado()) {
                    ActivarControles();
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
                listaCampos.add(txtNombre);
                listaCampos.add(txtApellidos);
                listaCampos.add(txtEmail);
                listaCampos.add(txtSueldo);
                listaCampos.add(txtTelefono);
                ArrayList<ComboBox> listaComboBox = new ArrayList<>();
                listaComboBox.add(cmbCodigoAdministracion);
                listaComboBox.add(cmbCodigoCargo);
                listaComboBox.add(cmbCodigoDepartamento);
                listaComboBox.add(cmbCodigoHorario);
                if (escenarioPrincipal.validar(listaCampos, listaComboBox)) {
                    if (validarNumeroReal(txtSueldo.getText())) {
                        if (validarTelefono(txtTelefono.getText())) {
                            if (validarEmail(txtEmail.getText())) {
                    editarEmpleados();
                    limpiarControles();
                    cargarDatos();
                    btnModificar.setText("Modificar");
                    imgNuevo.setOpacity(1);
                    imgModificar.setImage(new Image("/org/MarceloDiaz/resource/images/editar.png"));
                    btnEliminar.setText("Eliminar");
                    imgReporte.setOpacity(1);
                    imgEliminar.setImage(new Image("/org/MarceloDiaz/resource/images/marca-x.png"));
                    btnNuevo.setDisable(false);
                    btnReporte.setDisable(false);
                    operacion = Operaciones.NINGUNO;
                    desactivarControles();
                    
                            }else{
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Kinal mall - Empleado");
                            alert.setHeaderText(null);
                            alert.setContentText("Numero de email invalido");
                            alert.show();
                            }
                    
                        }else{
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Kinal mall - Empleado");
                            alert.setHeaderText(null);
                            alert.setContentText("Numero de telefono invalido");
                            alert.show();
                        
                        }
                    }else{
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Kinal mall - Empleado");
                        alert.setHeaderText(null);
                        alert.setContentText("Los valores del sueldo no son validos");
                        alert.show();
       
                    }
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
                btnEliminar.setDisable(false);
                imgEliminar.setOpacity(1);
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
                btnReporte.setDisable(false);
                imgReporte.setOpacity(1);
                btnModificar.setText("Modificar");
                imgModificar.setImage(new Image("/org/MarceloDiaz/resource/images/editar.png"));
                btnEliminar.setText("Eliminar");
                imgEliminar.setImage(new Image("/org/MarceloDiaz/resource/images/marca-x.png"));
                operacion = Operaciones.NINGUNO;
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
                        eliminarEmpleados();
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
        GenerarReporte.getInstance().mostrarReporte("ReporteEmpleados.jasper", parametros, "Listado de Cuentas por cobrar");
    }
    
    @FXML
    private void horarios(){
        escenarioPrincipal.Horarios();
    }
}
