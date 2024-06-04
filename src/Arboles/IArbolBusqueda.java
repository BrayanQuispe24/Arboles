/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Arboles;

import java.util.List;

/**
 *
 * 
 */
public interface IArbolBusqueda <K extends Comparable<K>, V>{
    void insertar(K clave, V valor);
    V eliminar(K clave);
    V buscar(K clave);
    boolean contiene(K clave);
    int size();
    int altura();
    void vaciar();
    boolean esArbolVacio();
    int nivel();
    List<K> recorridoEnInOrden();
    List<K> recorridoEnPreOrden();
    List<K> recorridoEnPostOrden();
    List<K> recorridoPorNiveles();
}
