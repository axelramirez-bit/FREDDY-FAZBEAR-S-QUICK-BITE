/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Main;

import Config.Conexion;

/**
 *
 * @author HP
 */
public class main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
               Conexion conexion = Conexion.getInstancia();

        if (conexion.estaConectado()) {

            System.out.println("La conexión funciona correctamente.");

        }

        conexion.cerrarConexion();

    }
    }    
