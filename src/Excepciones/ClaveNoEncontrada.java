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
public class ClaveNoEncontrada extends Exception{
    public ClaveNoEncontrada(String tipoArbol) {
        super(("!!!La clave no se encontro en el " + 
                tipoArbol + "¡¡¡").toUpperCase());
    }
}
