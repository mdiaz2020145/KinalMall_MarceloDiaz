/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.MarceloDiaz.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.MarceloDiaz.system.Principal;

/**
 *
* @author Marcelo Javier Diaz Pineda
* @date 14/05/2021
* @time 13:08:40
 */
public class CobrosAdminController implements Initializable{
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
      public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
}
