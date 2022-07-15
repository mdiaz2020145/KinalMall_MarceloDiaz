/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.MarceloDiaz.bean;

/**
 *
* @author Marcelo Javier Diaz Pineda
* @date 27/05/2021
* @time 17:38:17
 */
public class TipoCliente {
    private int id; 
    private String descripcion; 
    
    public TipoCliente(){
        
    }
    public TipoCliente(int id, String descripcion){
        this.id=id; 
        this.descripcion=descripcion; 
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return id + " | " + descripcion;
        //return "TipoCliente{" + "id=" + id + ", descripcion=" + descripcion + '}';
    }
    
}
