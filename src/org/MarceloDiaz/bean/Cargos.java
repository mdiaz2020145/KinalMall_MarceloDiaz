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
* @time 17:23:16
 */
public class Cargos {
    private int id; 
    private String nombre; 
    //Constructor nulo 
    public Cargos(){
        
    }
    public Cargos(int id, String nombre){
        this.id=id;
        this.nombre=nombre; 
    }
    //Metodos getter and setter

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "Cargos{" + "id=" + id + ", nombre=" + nombre + '}';
    }
    
}
