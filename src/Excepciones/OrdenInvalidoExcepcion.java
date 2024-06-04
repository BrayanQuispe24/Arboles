/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Excepciones;

/**
 *
 * @author Sergio
 */
public class OrdenInvalidoExcepcion extends Exception{

    public OrdenInvalidoExcepcion() {
        super("Orden del arbol debe ser al menos 3");
    }

    public OrdenInvalidoExcepcion(String message) {
        super(message);
    }
    
}
