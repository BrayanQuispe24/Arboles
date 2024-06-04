/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Arboles;

import Excepciones.OrdenInvalidoExcepcion;
import Excepciones.ClaveNoEncontrada;
/**
 *
 *
 */
public class TestMVias {

    /**
     * @param args the command line arguments
     */
    //throw OrdenInvalidoExcepcion
    public static void main(String[] args){
        //EL NODO ES DE 2 CLAVES Y 3 
        IArbolBusqueda<Integer, String> arbolMV = new ArbolMViasBusqueda<>();
        arbolMV.insertar(100, "Eunice");
        arbolMV.insertar(200, "Juan");
        arbolMV.insertar(70, "Brayan");
        arbolMV.insertar(60, "Saturnino"); 
        arbolMV.insertar(55, "Joaquin");
        System.out.println(arbolMV);
        System.out.println("--------------------------------");
        
        IArbolBusqueda<Integer, String> arbolB = new ArbolB<>();
        arbolB.insertar(100, "Eunice");
        arbolB.insertar(200, "Juan");
        arbolB.insertar(70, "Brayan");
        arbolB.insertar(60, "Saturnino"); 
        arbolB.insertar(55, "Joaquin");   
        System.out.println(arbolB);
//        System.out.println(arbol.eliminar(100));
//        System.out.println(arbol.toString());
//        System.out.println(" "+ arbol.recorridoEnPreOrden());
//        System.out.println(" "+ arbol.recorridoPorNiveles());
//        System.out.println(arbol);
//        System.out.println("--------------------------");
//        System.out.println("ELIMINAR "+arbol.eliminar(47));
//        System.out.println(arbol);
    }
    
}
