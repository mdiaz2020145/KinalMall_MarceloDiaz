/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.MarceloDiaz.controller;

import com.jfoenix.controls.JFXTimePicker;
import com.mysql.cj.protocol.Resultset;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
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
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import org.MarceloDiaz.bean.Horarios;
import org.MarceloDiaz.db.Conexion;
import org.MarceloDiaz.system.Principal;
import java.sql.Time;
import java.time.LocalTime;
import java.time.format.FormatStyle;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.stage.Stage;
import javafx.util.converter.LocalTimeStringConverter;
import javafx.util.StringConverter;
import javafx.util.converter.LocalTimeStringConverter;
import org.MarceloDiaz.report.GenerarReporte;



/**
 *
* @author Marcelo Javier Diaz Pineda
* @date 7/07/2021
* @time 07:41:58
 */
public class HorariosController implements Initializable {

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
    private TableView tblHorarios;
    @FXML
    private TableColumn cold;
    @FXML
    private TableColumn colHorario;
    @FXML
    private TableColumn colHorarioSalida;
    @FXML
    private TableColumn colLunes;
    @FXML
    private TableColumn colMartes;
    @FXML
    private TableColumn colMiercoles;
    @FXML
    private TableColumn colJueves;
    @FXML
    private TableColumn colViernes;
    @FXML
    private JFXTimePicker tpHorario;
    @FXML
    private CheckBox chkLunes;
    @FXML
    private CheckBox chkMartes;
    @FXML
    private CheckBox chkMiercoles;
    @FXML
    private CheckBox chkJueves;
    @FXML
    private CheckBox chkViernes;
    @FXML
    private ImageView imgNuevo;
    @FXML
    private ImageView imgModificar;
    @FXML
    private ImageView imgEliminar;
    @FXML
    private ImageView imgReporte;
    @FXML
    private TextField txtId;
    @FXML
    private JFXTimePicker tpHorarioSalida;

    private enum Operaciones {
        GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO
    }
    private Operaciones operacion = Operaciones.NINGUNO;

    private ObservableList<Horarios> listahorarios;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        cargarDatos();
        tpHorario.setDisable(true);
        tpHorarioSalida.setDisable(true);
        tpHorario.set24HourView(true);
        tpHorarioSalida.set24HourView(true);
        StringConverter<LocalTime> defaultCoverter = new LocalTimeStringConverter(FormatStyle.SHORT, Locale.UK);
        tpHorario.setConverter(defaultCoverter);
        StringConverter<LocalTime> defaultCoverter2 = new LocalTimeStringConverter(FormatStyle.SHORT, Locale.UK);
        tpHorarioSalida.setConverter(defaultCoverter2);
        Alert alertwarning = new Alert(Alert.AlertType.WARNING);
        alertwarning.setTitle("KINAL MALL");
        alertwarning.setHeaderText("ERROR");
        Stage stage = (Stage) alertwarning.getDialogPane().getScene().getWindow();
        //stage.getIcons().add(new Image("IconoKinal.png"));
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    @FXML
    private void menuPrincipal(MouseEvent event) {
        escenarioPrincipal.empleado();
    }
    private void mostrarvistaEmpleados(){
    }
    
    public ObservableList<Horarios> getHorarios() {
        ArrayList<Horarios> lista = new ArrayList<>();

        try {
            PreparedStatement pstmt = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarHorarios}");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                lista.add(new Horarios(rs.getInt("id"),
                        rs.getTime("horarioEntrada"),
                        rs.getTime("horarioSalida"),
                        rs.getBoolean("lunes"),
                        rs.getBoolean("martes"),
                        rs.getBoolean("miercoles"),
                        rs.getBoolean("jueves"),
                        rs.getBoolean("Viernes")));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        listahorarios = FXCollections.observableArrayList(lista);
        return listahorarios;

    }
    
    public void cargarDatos() {
        tblHorarios.setItems(getHorarios());
        cold.setCellValueFactory(new PropertyValueFactory<Horarios, Integer>("id"));
        colHorario.setCellValueFactory(new PropertyValueFactory<Horarios, Time>("horarioEntrada"));
        colHorarioSalida.setCellValueFactory(new PropertyValueFactory<Horarios, Time>("horarioSalida"));
        colLunes.setCellValueFactory(new PropertyValueFactory<Horarios, Boolean>("lunes"));
        colMartes.setCellValueFactory(new PropertyValueFactory<Horarios, Boolean>("martes"));
        colMiercoles.setCellValueFactory(new PropertyValueFactory<Horarios, Boolean>("miercoles"));
        colJueves.setCellValueFactory(new PropertyValueFactory<Horarios, Boolean>("jueves"));
        colViernes.setCellValueFactory(new PropertyValueFactory<Horarios, Boolean>("viernes"));
    }
    public void agregarHorarios() {
        Horarios horario = new Horarios();
        horario.setHorarioEntrada(Time.valueOf(tpHorario.getValue()));
        horario.setHorarioSalida(Time.valueOf(tpHorarioSalida.getValue()));
        horario.setLunes(chkLunes.isSelected());
        horario.setMartes(chkMartes.isSelected());
        horario.setMiercoles(chkMiercoles.isSelected());
        horario.setJueves(chkJueves.isSelected());
        horario.setViernes(chkViernes.isSelected());
        PreparedStatement pstmt = null;
        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarHorarios(?,?,?,?,?,?,?)}");
            pstmt.setTime(1, horario.getHorarioEntrada());
            pstmt.setTime(2, horario.getHorarioSalida());
            pstmt.setBoolean(3, horario.isLunes());
            pstmt.setBoolean(4, horario.isMartes());
            pstmt.setBoolean(5, horario.isMiercoles());
            pstmt.setBoolean(6, horario.isJueves());
            pstmt.setBoolean(7, horario.isViernes());

            pstmt.execute();
        } catch (Exception e) {
            System.out.println("se produjo un error al intentar agregar un horario en la base da datos");

        } finally {
            try {
                pstmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public void editarHorario() {
        Horarios horario = new Horarios();
        horario.setId(Integer.parseInt(txtId.getText()));
        horario.setHorarioEntrada(Time.valueOf(tpHorario.getValue()));
        horario.setHorarioSalida(Time.valueOf(tpHorarioSalida.getValue()));
        horario.setLunes(chkLunes.isSelected());
        horario.setMartes(chkMartes.isSelected());
        horario.setJueves(chkJueves.isSelected());
        horario.setViernes(chkViernes.isSelected());

        PreparedStatement pstmt = null;
        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarHorarios(?,?,?,?,?,?,?,?)}");
            pstmt.setInt(1, horario.getId());
            pstmt.setTime(2, horario.getHorarioEntrada());
            pstmt.setTime(3, horario.getHorarioSalida());
            pstmt.setBoolean(4, horario.isLunes());
            pstmt.setBoolean(5, horario.isMartes());
            pstmt.setBoolean(6, horario.isMiercoles());
            pstmt.setBoolean(7, horario.isJueves());
            pstmt.setBoolean(8, horario.isViernes());
            pstmt.execute();
        } catch (SQLException e) {
            System.out.println("Se produjo un error al editar horarios en la base de datos");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void eliminarHorarios() {
        if (existeElementosSeleccionado()) {
            Horarios local = ((Horarios) tblHorarios.getSelectionModel().getSelectedItem());
            System.out.println(local);

            try {
                PreparedStatement pstmt = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarHorarios(?)}");
                pstmt.setInt(1, local.getId());

                System.out.println(pstmt.toString());
                pstmt.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public boolean existeElementosSeleccionado() {
        if (tblHorarios.getSelectionModel().getSelectedItem() == null) {
            return false;
        } else {
            return true;
        }
    }
    
     public void seleccionarElemento() {
        if (existeElementosSeleccionado()) {
            txtId.setText(String.valueOf(((Horarios) tblHorarios.getSelectionModel().getSelectedItem()).getId()));
            tpHorario.setValue(((Horarios) tblHorarios.getSelectionModel().getSelectedItem()).getHorarioEntrada().toLocalTime());
            tpHorarioSalida.setValue(((Horarios) tblHorarios.getSelectionModel().getSelectedItem()).getHorarioEntrada().toLocalTime());
            chkLunes.setSelected(((Horarios) tblHorarios.getSelectionModel().getSelectedItem()).isLunes());
            chkMartes.setSelected(((Horarios) tblHorarios.getSelectionModel().getSelectedItem()).isMartes());
            chkMiercoles.setSelected(((Horarios) tblHorarios.getSelectionModel().getSelectedItem()).isMiercoles());
            chkJueves.setSelected(((Horarios) tblHorarios.getSelectionModel().getSelectedItem()).isJueves());
            chkViernes.setSelected(((Horarios) tblHorarios.getSelectionModel().getSelectedItem()).isViernes());
        }
    }
    
    public boolean validarFecha(String date){
        String patron = "[0-9]{4}[-/.](0[0-9]|1[012])[-/.](0[0-9]|1[0-9]|2[0-9]|3[01])";
        Pattern pattern = Pattern.compile(patron);
        Matcher matcher = pattern.matcher(date);
        System.out.println(matcher.matches());
        return matcher.matches();
    }
    
    public boolean validarTiempo(String time) {
        String patron = "([01][0-9]|[2][0123]):([0-5][0-9]):([0-5][0-9])";
        Pattern pattern = Pattern.compile(patron);
        Matcher matcher = pattern.matcher(time);
        System.out.println(matcher.matches());
        return matcher.matches();
    }
    public void activarControles(){
        txtId.setEditable(false);
        txtId.setEditable(false);
        tpHorario.setDisable(false);
        tpHorarioSalida.setDisable(false);
        chkLunes.setDisable(false);
        chkMartes.setDisable(false);
        chkMiercoles.setDisable(false);
        chkJueves.setDisable(false);
        chkViernes.setDisable(false);
    }
    
    public void limpiarControles(){
        txtId.clear();
        tpHorario.getEditor().clear();
        tpHorarioSalida.getEditor().clear();
        chkLunes.setSelected(false);
        chkMartes.setSelected(false);
        chkMiercoles.setSelected(false);
        chkJueves.setSelected(false);
        chkViernes.setSelected(false);
    }
    
    public void desactivarControles() {
        txtId.setEditable(false);
        txtId.setDisable(true);
        tpHorario.setDisable(true);
        tpHorarioSalida.setDisable(true);
        chkLunes.setDisable(true);
        chkMartes.setDisable(true);
        chkMiercoles.setDisable(true);
        chkJueves.setDisable(true);
        chkViernes.setDisable(true);
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
                operacion = HorariosController.Operaciones.GUARDAR;
                break;
            case GUARDAR:
                if ((tpHorario.getValue() != null) && (tpHorarioSalida.getValue() != null)) {
                    agregarHorarios();
                    cargarDatos();
                    limpiarControles();
                    desactivarControles();
                    operacion = HorariosController.Operaciones.NINGUNO;
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
                listaCampos.add(txtId);
                ArrayList<ComboBox> listaComboBox = new ArrayList<>();  
                 if (escenarioPrincipal.validar(listaCampos, listaComboBox)) {
                      if ((tpHorario.getValue() != null) && (tpHorarioSalida.getValue() != null)) {
                       editarHorario();
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
                 }else{
                     Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Kinal Mall - Horarios");
                    alert.setHeaderText(null);
                    alert.setContentText("Porfavor llene todos los campos");
                    alert.show();  
                 }
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
                operacion = HorariosController.Operaciones.NINGUNO;
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
                        eliminarHorarios();
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
        GenerarReporte.getInstance().mostrarReporte("ReporteHorarios.jasper", parametros, "Listado de Horarios");
    }

    
}
