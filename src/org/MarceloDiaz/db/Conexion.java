/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.MarceloDiaz.db;
import java.sql.Connection; 
import java.sql.DriverManager; 
import java.sql.SQLException;
/**
 *
* @author Marcelo Javier Diaz Pineda
* @date 26/05/2021
* @time 09:32:24
 */
public class Conexion {
    private Connection conexion; 
    final String URL;
    
    private static Conexion instancia; 
    
    private Conexion() {
        //URL = "jdbc:mysql://127.0.0.1:3306/dbKinalMall_MarceloDiaz?user=root&password=admin&serverTimezone=UTC&useSSl=false";
        URL = "jdbc:mysql://127.0.0.1:3306/IN5BM_KinalMall?allowPublicKeyRetrieval=true&serverTimezone=UTC&useSSL=false";
        try{
            //opcion 1
           Class.forName("com.mysql.jdbc.Driver").newInstance(); 
           
           //Opcion 2
           //Class.forName("com.mysql.jdb.Driver");
           
           //Opcion3
           //Class.forName("com.mysql.cj.jdb.Driver");
           
           //Opcion 4
           //A partir del jdk 6, los driver JDBC 4
           //Ya se registran automaticamente
           //conexion = DriverManager.getConnection(URL); 
           conexion = DriverManager.getConnection(URL, "root", "admin");
        }catch(ClassNotFoundException e){
            System.out.println("No se encuentra ninguna definicion para la clase");
            e.printStackTrace();
        }catch(InstantiationException e){
            System.out.println("No se puede crear una instancia del objeto");
            e.printStackTrace();
        }catch(IllegalAccessException e){
            System.out.println("No se tienen permiso para acceder al paquete");
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }  
    }
    
    public static Conexion getInstance(){
        if(instancia == null){
            instancia = new Conexion(); 
        }
        return instancia; 
    }

    public Connection getConexion() {
        return conexion;
    }

    public void setConexion(Connection conexion) {
        this.conexion = conexion;
    }
    
 
}

