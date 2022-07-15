/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.MarceloDiaz.controller;

import com.mysql.cj.protocol.Resultset;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.MarceloDiaz.db.Conexion;
import org.MarceloDiaz.system.Principal;
import java.util.Base64; 

/**
 *
* @author Marcelo Javier Diaz Pineda
* @date 4/08/2021
* @time 11:08:41
 */
public class LoginController implements Initializable{

    @FXML
    private TextField txtUsuario;
    @FXML
    private TextField txtPass;
    @FXML
    private Button btnInicio;
    
    private String userDB; 
    private String passDB; 
    private String rol;
    private Principal escenarioPrincipal;
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    @FXML
    private void validar(ActionEvent event) {
        if (!(txtUsuario.getText().equals("") || txtPass.getText().equals(""))) {
            //if(this.passDB !=null && this.userDB !=null ){
            String user = txtUsuario.getText().trim();
            String pass = txtPass.getText().trim();

            getPassword(user);
            String pass64 = new String(Base64.getDecoder().decode(this.passDB));
            
           
            if ((user.equals(this.userDB)) && (pass.equals(pass64))) {
                escenarioPrincipal.menuPrincipal();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Kinal mall - Login");
                alert.setHeaderText(null);
                alert.setContentText("Bienvenido:  "+ userDB);
                alert.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Kinal mall - Login");
                alert.setHeaderText(null);
                alert.setContentText("Los datos que ingreso son incorrectos");
                alert.show();
            }
            /*}else{
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Kinal mall - Login");
                alert.setHeaderText(null);
                alert.setContentText("Los datos que ingreso son incorrectos");
                alert.show();
            }*/
        }else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Kinal mall - Login");
                alert.setHeaderText(null);
                alert.setContentText("Los datos estan vacios");
                alert.show();
        }
        

        
    }
    
    
    
    
    private void getPassword(String user) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_BuscarUsuario(?)}");
            pstmt.setString(1, user);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                this.userDB = rs.getString("user");
                this.passDB = rs.getString("pass");
                MenuPrincipalController.setRol(rs.getInt("rol"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    
}
