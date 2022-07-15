/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.MarceloDiaz.bean;

import java.math.BigDecimal;

/**
 *
* @author Marcelo Javier Diaz Pineda
* @date 27/05/2021
* @time 17:44:01
 */
public class Locales {
    private int id;
    private BigDecimal saldoFavor;
    private BigDecimal saldoContra; 
    private int mesesPendiente;
    private boolean disponibilidad; 
    private BigDecimal valorLocal; 
    private BigDecimal valorAdministracion; 
    
    public Locales(){
        
    }
    public Locales(int id, BigDecimal saldoFavor,BigDecimal saldoContra,int mesesPendiente,boolean disponibilidad,BigDecimal valorLocal,
    BigDecimal valorAdministracion){
        this.id=id;
        this.saldoFavor=saldoFavor;
        this.saldoContra=saldoContra; 
        this.mesesPendiente=mesesPendiente;
        this.disponibilidad=disponibilidad; 
        this.valorLocal=valorLocal; 
        this.valorAdministracion=valorAdministracion; 
  
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BigDecimal getSaldoFavor() {
        return saldoFavor;
    }

    public void setSaldoFavor(BigDecimal saldoFavor) {
        this.saldoFavor = saldoFavor;
    }

    public BigDecimal getSaldoContra() {
        return saldoContra;
    }

    public void setSaldoContra(BigDecimal saldoContra) {
        this.saldoContra = saldoContra;
    }

    public int getMesesPendiente() {
        return mesesPendiente;
    }

    public void setMesesPendiente(int mesesPendiente) {
        this.mesesPendiente = mesesPendiente;
    }

    public boolean isDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(boolean disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    public BigDecimal getValorLocal() {
        return valorLocal;
    }

    public void setValorLocal(BigDecimal valorLocal) {
        this.valorLocal = valorLocal;
    }

    public BigDecimal getValorAdministracion() {
        return valorAdministracion;
    }

    public void setValorAdministracion(BigDecimal valorAdministracion) {
        this.valorAdministracion = valorAdministracion;
    }

    @Override
    public String toString() {
        return id +  "|"  + disponibilidad;
        //return "Locales{" + "id=" + id + ", saldoFavor=" + saldoFavor + ", saldoContra=" + saldoContra + ", mesesPendiente=" + mesesPendiente + ", disponibilidad=" + disponibilidad + ", valorLocal=" + valorLocal + ", valorAdministracion=" + valorAdministracion + '}';
    }

   
    
}