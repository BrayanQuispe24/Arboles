/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Arboles;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

/**
 *
 * 
 */
public class ArbolBinarioBusqueda <K extends Comparable <K>, V> implements IArbolBusqueda<K, V>{

    protected NodoBinario<K,V> raiz;
    
    @Override
    public void insertar(K claveAInsertar, V valorAsociado) {
        if (claveAInsertar == null){
            throw new IllegalArgumentException("Clave a insertar no puede ser " + "vacia");
        }
        if (valorAsociado == null){
            throw new IllegalArgumentException("Valor a insertar no puede ser " + "vacio");
        }
        
        if (this.esArbolVacio()){
            this.raiz = new NodoBinario<>(claveAInsertar, valorAsociado);
            return;
        }
        
        NodoBinario<K,V> nodoAnterior = NodoBinario.nodoVacio();
        NodoBinario<K,V> nodoActual = this.raiz;
        do{
             K claveDelNodoActual = nodoActual.getClave();
             int comparacion = claveAInsertar.compareTo(claveDelNodoActual);
             nodoAnterior = nodoActual;
             if (comparacion < 0 ) {
                 nodoActual = nodoActual.getHijoIzquierdo();
             }else if (comparacion > 0 ){
                 nodoActual = nodoActual.getHijoDerecho();
             }else{
                 nodoActual.setValor(valorAsociado);
                 return;
             }
        } while (!NodoBinario.esNodoVacio(nodoActual));
        
        NodoBinario<K,V> nodoNuevo = new NodoBinario<>(claveAInsertar, valorAsociado);
        if(claveAInsertar.compareTo(nodoAnterior.getClave()) < 0){
            nodoAnterior.setHijoIzquierdo(nodoNuevo);
        } else{
            nodoAnterior.setHijoDerecho(nodoNuevo);
        }   
    }

    @Override
    public V eliminar(K claveAEliminar) {
        if (claveAEliminar == null) {
            throw new IllegalArgumentException("Clave invalida,no se aceptan claves nulas");
	}
	V valorAsociado = buscar(claveAEliminar);
	if (valorAsociado == null) {
            throw new IllegalArgumentException("Valor invalida,no se aceptan valores nulas");        
            //throw new ClaveYValorVaciaException();
	}
	this.raiz = eliminar(this.raiz, claveAEliminar);
	return valorAsociado;      
    }

    private NodoBinario<K, V> eliminar(NodoBinario<K, V> nodoActual, K claveAEliminar) {
            K claveActual = nodoActual.getClave();
            if (claveAEliminar.compareTo(claveActual) < 0) {
                    NodoBinario<K, V> supuestoNuevoHijoIzquierdo = eliminar(nodoActual.getHijoIzquierdo(), claveAEliminar);
                    nodoActual.setHijoIzquierdo(supuestoNuevoHijoIzquierdo);
                    return nodoActual;
            }
            if (claveAEliminar.compareTo(claveActual) > 0) {
                    NodoBinario<K, V> supuestoNuevoHijoDerecho
                                    = eliminar(nodoActual.getHijoDerecho(), claveAEliminar);
                    nodoActual.setHijoDerecho(supuestoNuevoHijoDerecho);
                    return nodoActual;
            }
           
            if (nodoActual.esHoja()) {
                    return NodoBinario.nodoVacio();
            }  
            
            if (nodoActual.esVacioHijoIzquierdo()
                            && !nodoActual.esVacioHijoDerecho()) {
                    return nodoActual.getHijoDerecho();
            }
           
            if (!nodoActual.esVacioHijoIzquierdo()
                            && nodoActual.esVacioHijoDerecho()) {
                    return nodoActual.getHijoIzquierdo();
            }
        
            NodoBinario<K, V> nodoDelSucesor = obtenerNodoDelSucesor(
                            nodoActual.getHijoDerecho());
            NodoBinario<K, V> posibleNuevoHijoDerecho
                            = eliminar(nodoActual.getHijoDerecho(), nodoDelSucesor.getClave());

            nodoActual.setHijoDerecho(posibleNuevoHijoDerecho);
            nodoActual.setClave(nodoDelSucesor.getClave());
            nodoActual.setValor(nodoDelSucesor.getValor());

            return nodoActual;
    }
        
    protected NodoBinario<K, V> obtenerNodoDelSucesor(
                    NodoBinario<K, V> elNodo) {
            NodoBinario<K, V> nodoAnterior;
            do {
                    nodoAnterior = elNodo;
                    elNodo = elNodo.getHijoIzquierdo();
            } while (!NodoBinario.esNodoVacio(elNodo));

            return nodoAnterior;
    }
    

    @Override
    public V buscar(K claveABuscar) {
        if(!this.esArbolVacio()){
            NodoBinario<K,V> nodoActual = this.raiz;
            while(!NodoBinario.esNodoVacio(nodoActual)){
                if(claveABuscar.compareTo(nodoActual.getClave())==0){
                    return nodoActual.getValor();
                }
                if(claveABuscar.compareTo(nodoActual.getClave())<0){
                    nodoActual = nodoActual.getHijoIzquierdo();
                }
                else{
                    nodoActual = nodoActual.getHijoDerecho();
                }
            }
        }
        return null;
    }

    @Override
    public boolean contiene(K claveAContener) {
        return this.buscar(claveAContener) != null;
    }

    @Override
    public int size() {
       return size(raiz);
    }
   
    public int size(NodoBinario nodoActual){
        if(!NodoBinario.esNodoVacio(nodoActual)){
            int sizeIzq = size(nodoActual.getHijoIzquierdo());
            int sizeDer = size(nodoActual.getHijoDerecho());
            return sizeIzq + sizeDer + 1; 
        }
        return 0;
    }

    @Override
    public int altura() {
           return altura(raiz);
    }
    
    public int altura(NodoBinario nodoActual){
        if(NodoBinario.esNodoVacio(nodoActual)){
            return 0;
        }
        int alturaIzq = altura(nodoActual.getHijoIzquierdo());
        int alturaDer = altura(nodoActual.getHijoDerecho());
        if(alturaIzq > alturaDer){
            return alturaIzq +1;
        }
        return alturaDer + 1; 
    }

    @Override
    public void vaciar() {
        this.raiz = NodoBinario.nodoVacio(); 
    }

    @Override
    public boolean esArbolVacio() {
        return NodoBinario.esNodoVacio(raiz);
    }

    @Override
    public int nivel() {
        return this.altura()-1;
    }

    @Override
    public List<K> recorridoEnInOrden() {
        List<K> recorrido = new ArrayList<>();
        recorridoEnInOrden(raiz, recorrido);
        return recorrido;
        
    }
    
    private void recorridoEnInOrden(NodoBinario<K,V> nodoActual, List<K> recorrido){
        if(!NodoBinario.esNodoVacio(nodoActual)){
            recorridoEnInOrden(nodoActual.getHijoIzquierdo(), recorrido);
            recorrido.add(nodoActual.getClave());
            recorridoEnInOrden(nodoActual.getHijoDerecho(), recorrido);
        }
    }
            

    @Override
    public List<K> recorridoEnPreOrden() {
        List<K> recorrido = new ArrayList<>();
        if (!this.esArbolVacio()){
            Stack<NodoBinario<K,V>> pilaDeNodos = new Stack<>();
            pilaDeNodos.push(this.raiz);
            do{
                NodoBinario<K,V> nodoActual = pilaDeNodos.pop();
                recorrido.add(nodoActual.getClave());
                if(!nodoActual.esVacioHijoDerecho()){
                    pilaDeNodos.push(nodoActual.getHijoDerecho());
                } 
                if (!nodoActual.esVacioHijoIzquierdo()){
                    pilaDeNodos.push(nodoActual.getHijoIzquierdo());
                }         
            }while (!pilaDeNodos.isEmpty());
        }
        return recorrido;
    }

    @Override
    public List<K> recorridoEnPostOrden() {
        List<K> recorrido = new ArrayList<>();
        recorridoEnPostOrden(raiz, recorrido);
        return recorrido;
        
    }
    
    private void recorridoEnPostOrden(NodoBinario<K,V> nodoActual, List<K> recorrido){
        if(!NodoBinario.esNodoVacio(nodoActual)){
            recorridoEnPostOrden(nodoActual.getHijoIzquierdo(), recorrido); 
            recorridoEnPostOrden(nodoActual.getHijoDerecho(), recorrido);
            recorrido.add(nodoActual.getClave());
        }
    }

    @Override
    public List<K> recorridoPorNiveles() {
      List<K> recorrido = new ArrayList<>();
        if (!this.esArbolVacio()){
            Queue<NodoBinario<K,V>> colaDeNodos = new LinkedList<>();
            colaDeNodos.offer(this.raiz);
            do{
                NodoBinario<K,V> nodoActual = colaDeNodos.poll();
                recorrido.add(nodoActual.getClave());
                if (!nodoActual.esVacioHijoIzquierdo()){
                     colaDeNodos.offer(nodoActual.getHijoIzquierdo());
                }
                if(!nodoActual.esVacioHijoDerecho()){
                     colaDeNodos.offer(nodoActual.getHijoDerecho());
                } 
                         
            }while (!colaDeNodos.isEmpty());
        }
        return recorrido;
    }

    @Override
    public String toString() {
        
        return ToStrinNodo(raiz, "");
    }

    private String ToStrinNodo(NodoBinario<K,V> nodoActual, String ant) {
        if (estaVacio(nodoActual)) {
            return "";
        }
        String g = "{" + nodoActual.getClave().toString() + "}\n";
        g += ant + "║\n";
        g += ant + "╠═Der-> " + ToStrinNodo(nodoActual.getHijoDerecho(), ant + "║  ") + "\n";
        g += ant + "║\n";
        g += ant + "╚═Izq-> " + ToStrinNodo(nodoActual.getHijoIzquierdo(), ant + "    ");
        return g;
    }
    
     protected boolean estaVacio(NodoBinario<K,V> nodoActual) {
        return nodoActual == null;
    }
}
