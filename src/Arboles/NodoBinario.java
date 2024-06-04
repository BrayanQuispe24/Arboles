/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Arboles;

/**
 *
 * 
 */
public class NodoBinario <K ,V>{
    
    private K clave; 
    private V valor;
    private NodoBinario<K,V> hijoIzquierdo;
    private NodoBinario<K,V> hijoDerecho;
    
   
    public NodoBinario(K clave, V valor){
        this.clave = clave;
        this.valor = valor;
    }
    
    public NodoBinario(){}

    
    public void setClave(K clave){
        this.clave = clave;
    }
    
    public K getClave(){
        return this.clave;
    }
    
    public void setValor(V valor){
        this.valor = valor;
    }
    
    public V getValor(){
        return this.valor;
    }
    
    public void setHijoIzquierdo(NodoBinario<K,V> hijoIzquierdo){
        this.hijoIzquierdo = hijoIzquierdo;
    }
    
    public NodoBinario<K,V> getHijoIzquierdo(){
        return this.hijoIzquierdo;
    }
    
    public void setHijoDerecho(NodoBinario<K,V> hijoDerecho){
        this.hijoDerecho = hijoDerecho;
    }
    
    public NodoBinario<K,V> getHijoDerecho(){
        return this.hijoDerecho;
    }
    
    
    public static NodoBinario nodoVacio() {
        return null;
    }
    
    public static boolean esNodoVacio(NodoBinario nodo){
        return nodo == NodoBinario.nodoVacio();
    }
    
    public boolean esVacioHijoIzquierdo(){
        return NodoBinario.esNodoVacio(this.getHijoIzquierdo());
    }
    
    public boolean esVacioHijoDerecho(){
        return NodoBinario.esNodoVacio(this.getHijoDerecho());
    }
    
    public boolean esHoja(){
        return this.esVacioHijoIzquierdo() && this.esVacioHijoDerecho();
    }

    
}
