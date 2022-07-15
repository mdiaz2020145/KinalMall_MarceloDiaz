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
* @time 11:40:42
 */
public class Administracion {
    
    private int id; 
    private String direccion; 
    private String telefono; 
    
    //constructor nulo
    public Administracion(){
        
    }
    
    //Constructor con parametros 
    public Administracion(int id, String direccion,String telefono){
        this.id=id;
        this.direccion=direccion;
        this.telefono=telefono;
        
    }
    //Metodo getter and setter 

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @Override
    public String toString() {
         return id + "|" + direccion;
        //return "Administracion{" + "id=" + id + ", direccion=" + direccion + ", telefono=" + telefono + '}';
    }
    
            
}
