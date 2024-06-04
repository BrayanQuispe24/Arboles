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
public class AVL <K extends Comparable<K>,V> extends ArbolBinarioBusqueda<K, V>{
    
    public static final byte RANGO_SUPERIOR = 1;
    public static final byte RANGO_INFERIOR  = - 1;
    
    public AVL(){}
    
    @Override
    public void insertar(K claveAInsertar, V valorAsociado){
        if(claveAInsertar == null){
            throw new IllegalArgumentException("Clave a insertar no puede ser vacio");
        }
        if(valorAsociado ==  null){
            throw new IllegalArgumentException("Valor a insertar no puede ser vacio");
        }
        this.raiz = insertar(this.raiz,claveAInsertar, valorAsociado);
    }
    
    private NodoBinario<K,V> insertar(NodoBinario<K,V> nodoActual,
                                      K claveAInsertar, V valorAsociado){
        if(NodoBinario.esNodoVacio(nodoActual)){
            NodoBinario<K,V> nodoNuevo = new NodoBinario<>(claveAInsertar, valorAsociado);
            return nodoNuevo;
        }
        
        K claveDelNodoActual =  nodoActual.getClave();
        if(claveAInsertar.compareTo(claveDelNodoActual) < 0 ){
            NodoBinario<K,V> supuestoNuevoHI = insertar(nodoActual.getHijoIzquierdo(), claveAInsertar, valorAsociado);
            nodoActual.setHijoIzquierdo(supuestoNuevoHI);
            return balancear(nodoActual);
        }
        if(claveAInsertar.compareTo(claveDelNodoActual) > 0){
            NodoBinario<K,V> supuestoNuevoHD = insertar(nodoActual.getHijoDerecho(), claveAInsertar, valorAsociado);
            nodoActual.setHijoDerecho(supuestoNuevoHD);
            return balancear(nodoActual);
        }
        
        nodoActual.setValor(valorAsociado);
        return nodoActual;  
    }
    
    private NodoBinario<K,V> balancear(NodoBinario<K,V> nodoActual){
        int alturaPorIzquierda = super.altura(nodoActual.getHijoIzquierdo());
        int alturaPorDerecha = super.altura(nodoActual.getHijoDerecho());
        int diferenciaDeAltura = alturaPorIzquierda - alturaPorDerecha;
        
        if(diferenciaDeAltura > AVL.RANGO_SUPERIOR){
            NodoBinario<K,V> hijoIzquierdoDelAct = nodoActual.getHijoIzquierdo();
            alturaPorIzquierda = super.altura(hijoIzquierdoDelAct.getHijoIzquierdo());
            alturaPorDerecha = super.altura(hijoIzquierdoDelAct.getHijoDerecho());
            if(alturaPorDerecha > alturaPorIzquierda){
                return rotacionDobleADerecha(nodoActual);
            }
            else{
                return rotacionSimpleADerecha(nodoActual);
            }
        } 
        else if(diferenciaDeAltura < AVL.RANGO_INFERIOR){
            NodoBinario<K,V> hijoDerechoAct = nodoActual.getHijoDerecho();
            alturaPorIzquierda = super.altura(hijoDerechoAct.getHijoIzquierdo());
            alturaPorDerecha = super.altura(hijoDerechoAct.getHijoDerecho());
            if(alturaPorIzquierda > alturaPorDerecha){
                return this.rotacionDobleAIzquierda(nodoActual);
            }
            else{
                return this.rotacionSimpleAIzquierda(nodoActual);
            }
        }
        return nodoActual;
    }
    
    private NodoBinario<K,V> rotacionSimpleADerecha(NodoBinario<K,V> nodoActual){
        NodoBinario<K,V> nodoARetornar =  nodoActual.getHijoIzquierdo();
        nodoActual.setHijoIzquierdo(nodoARetornar.getHijoDerecho()); 
        nodoARetornar.setHijoDerecho(nodoActual);
        return nodoARetornar;
    }
    
    private NodoBinario<K,V> rotacionSimpleAIzquierda(NodoBinario<K,V> nodoActual){
        NodoBinario<K,V> nodoARetornar = nodoActual.getHijoDerecho();
        nodoActual.setHijoDerecho(nodoARetornar.getHijoIzquierdo());
        nodoARetornar.setHijoIzquierdo(nodoActual);      
        return nodoARetornar;
    }
    
    private NodoBinario<K,V> rotacionDobleADerecha(NodoBinario<K,V> nodoActual){
        NodoBinario<K,V> nodoQueRotaAIzq = rotacionSimpleAIzquierda(nodoActual.getHijoIzquierdo());
        nodoActual.setHijoIzquierdo(nodoQueRotaAIzq);
        return this.rotacionSimpleADerecha(nodoActual);
    }
    
    private NodoBinario<K,V> rotacionDobleAIzquierda(NodoBinario<K,V> nodoActual){
        NodoBinario<K,V> nodoQueRotaADer = rotacionSimpleADerecha(nodoActual.getHijoDerecho());
        nodoActual.setHijoDerecho(nodoQueRotaADer);
        return this.rotacionSimpleAIzquierda(nodoActual);
    }
    
    @Override
    public V eliminar(K claveAEliminar){
        if(claveAEliminar == null){
            throw new IllegalArgumentException("Clave a eliminar no puede ser vacío");
        }
        V valorAsociado = super.buscar(claveAEliminar);
        super.raiz = eliminar(super.raiz, claveAEliminar);
        return valorAsociado;
    }
    
    private NodoBinario<K,V> eliminar(NodoBinario<K,V> nodoActual, K claveAEliminar){
        if(claveAEliminar.compareTo(nodoActual.getClave()) < 0){
            NodoBinario<K,V>supuestoNuevoHI = eliminar(nodoActual.getHijoIzquierdo(), claveAEliminar);
            nodoActual.setHijoIzquierdo(supuestoNuevoHI);
            return balancear(nodoActual);
        }
        if(claveAEliminar.compareTo(nodoActual.getClave()) > 0){
            NodoBinario<K,V> supuestoNuevoHD = eliminar(nodoActual.getHijoDerecho(), claveAEliminar);
            nodoActual.setHijoDerecho(supuestoNuevoHD);
            return balancear(nodoActual);
        }
        //CASO 1
        if(nodoActual.esHoja()){
            return NodoBinario.nodoVacio();
        }
        
        //CASO 2
        if(!nodoActual.esVacioHijoIzquierdo() && nodoActual.esVacioHijoDerecho()){
            return nodoActual.getHijoIzquierdo();
        }
        if(nodoActual.esVacioHijoIzquierdo() && !nodoActual.esVacioHijoDerecho()){
            return nodoActual.getHijoDerecho();
        }
        
        //CASO 3
        NodoBinario<K,V> nodoSucesorInOrden = super.obtenerNodoDelSucesor(nodoActual.getHijoDerecho());
        NodoBinario<K,V> posibleNuevoHD=eliminar(nodoActual.getHijoDerecho(), nodoSucesorInOrden.getClave());
        nodoActual.setHijoDerecho(posibleNuevoHD);
        nodoActual.setClave(nodoSucesorInOrden.getClave());
        nodoActual.setValor(nodoSucesorInOrden.getValor());
        return nodoActual;
    }
    
}
