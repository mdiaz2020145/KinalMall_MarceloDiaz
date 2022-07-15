/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.MarceloDiaz.report;
import java.io.InputStream;
import java.util.Map;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.MarceloDiaz.db.Conexion;
        
/**
 *
* @author Marcelo Javier Diaz Pineda
* @date 15/07/2021
* @time 10:24:14
 */
public class GenerarReporte {
    
    private static GenerarReporte instancia; 
    private static JasperViewer jasperViewer;
    private GenerarReporte() {
    }
    
    public static GenerarReporte getInstance(){
        if(instancia == null){
            instancia = new GenerarReporte(); 
        }
        return instancia; 
    }
    
    public void mostrarReporte(String nombreReporte, Map parametros, String titulo){
        try{
            parametros.put("LOGO_HEADER",getClass().getResourceAsStream("/org/MarceloDiaz/resource/images/Kinal.png"));
            parametros.put("LOGO_FOOTER",getClass().getResourceAsStream("/org/MarceloDiaz/resource/images/Logo1SinfondoBlanco.png"));
            parametros.put("ADMINISTRACION",getClass().getResourceAsStream("/org/MarceloDiaz/resource/images/presupuesto.png"));
            parametros.put("TIPO_CLIENTE",getClass().getResourceAsStream("/org/MarceloDiaz/resource/images/comportamiento-del-cliente.png"));
            parametros.put("PROVEEDORES",getClass().getResourceAsStream("/org/MarceloDiaz/resource/images/inventario.png"));
            parametros.put("LOCALES",getClass().getResourceAsStream("/org/MarceloDiaz/resource/images/unknown.png"));
            parametros.put("HORARIOS",getClass().getResourceAsStream("/org/MarceloDiaz/resource/images/calendario.png"));
            parametros.put("DEPARTAMENTO",getClass().getResourceAsStream("/org/MarceloDiaz/resource/images/departamento.png"));
            parametros.put("CARGOS",getClass().getResourceAsStream("/org/MarceloDiaz/resource/images/puesto-de-trabajo.png"));
            InputStream reporte = GenerarReporte.class.getResourceAsStream(nombreReporte);
            JasperReport jasperReport =(JasperReport) JRLoader.loadObject(reporte);
            
            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    jasperReport,
                    parametros,
                    Conexion.getInstance().getConexion()
            
            );
            if (jasperViewer == null || !jasperViewer.isVisible()) {
                jasperViewer = new JasperViewer(jasperPrint, false);
                jasperViewer.setTitle(titulo);
                jasperViewer.setVisible(true);
            }
           
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
