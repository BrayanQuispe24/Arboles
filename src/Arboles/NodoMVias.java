/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Arboles;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 
 */
public class NodoMVias <K,V>{
    private List<K> listaDeClaves;
    private List<V> listaDeValores;
    private List<NodoMVias> listaDeHijos;
    
    //CONSTRUCTOR
    public NodoMVias(int orden){
        this.listaDeClaves = new ArrayList<>();
        this.listaDeValores = new ArrayList<>();
        this.listaDeHijos = new ArrayList<>();
        for (int i = 0; i < (orden-1); i++) {
            this.listaDeClaves.add( (K) NodoMVias.datoVacio());
            this.listaDeValores.add((V) NodoMVias.datoVacio());
            this.listaDeHijos.add(NodoMVias.nodoVacio());
        }
        this.listaDeHijos.add(NodoMVias.nodoVacio());
    }
    
    public NodoMVias(int orden,K primerClave, V primerValor){
        this(orden);
        this.listaDeClaves.set(0, primerClave);
        this.listaDeValores.set(0, primerValor);
    }
    
    public static Object datoVacio(){
        return null;
    }
    
    public static NodoMVias nodoVacio(){
        return null;
    }
    
    public static boolean esNodoVacio(NodoMVias elNodo){
        return elNodo == NodoMVias.nodoVacio();
    }
    
    //SETTER Y GETTERS
    public K getClave(int posicion){
        return this.listaDeClaves.get(posicion);
    }
    
    public void setClave(int posicion, K unaClave){
        this.listaDeClaves.set(posicion, unaClave);
    }
    
    public V getValor(int posicion){
        return this.listaDeValores.get(posicion);
    }
    
    public void setValor(int posicion, V unValor){
        this.listaDeValores.set(posicion, unValor);
    }
    
    public NodoMVias getHijo(int posicion){
        return this.listaDeHijos.get(posicion);
    }
    
    public void setHijo(int posicion, NodoMVias unNodo){
        this.listaDeHijos.set(posicion, unNodo);
    }
    
    //METODOS ADICIONALES
    public boolean esHijoVacio(int posicion){
        return NodoMVias.esNodoVacio(this.getHijo(posicion));
    }
    
    public boolean esDatoVacio(int posicion){
        return this.getValor(posicion) == NodoMVias.datoVacio();
    }
    
    public boolean esHoja(){
        for (int i = 0; i < this.listaDeHijos.size(); i++) {
            if(!this.esHijoVacio(i)){
                return false;
            }
        }
        return true;
    }
    
    public int nroDeClavesNoVacias(){
        int cantidad = 0;
        for (int i = 0; i < this.listaDeClaves.size(); i++) {
            if(!this.esDatoVacio(i)){
                cantidad++;
            }
        }
        return cantidad;
    }            
    
    public boolean hayClavesNoVacias(){
        return this.nroDeClavesNoVacias() != 0;
    }
    
    public boolean clavesLLena(){
        return this.nroDeClavesNoVacias() == listaDeClaves.size();
    }
    
    
    
    
    
    
    
}
