/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Arboles;

import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

import Excepciones.OrdenInvalidoExcepcion;
import Excepciones.ClaveNoEncontrada;

        
/**
 *
 * 
 */
public class ArbolB <K extends Comparable<K>, V> extends ArbolMViasBusqueda<K, V> {
    private final int NRO_MINIMO_HIJOS;
    private final int NRO_MINIMO_DATOS;
    private final int NRO_MAXIMO_HIJOS;
    private final int NRO_MAXIMO_DATOS;
    private final int POSICION_INVALIDA = -1;
    private final int VAL_MEDIO = (orden - 1) / 2;

    public ArbolB () {
        super();
        NRO_MAXIMO_HIJOS = 3;
        this.NRO_MAXIMO_DATOS = 2;
        this.NRO_MINIMO_DATOS = 1;
        this.NRO_MINIMO_HIJOS = 2;
    }
    
    public ArbolB (int orden) throws OrdenInvalidoExcepcion {
        super(orden); 
        this.NRO_MAXIMO_HIJOS = orden;
        this.NRO_MAXIMO_DATOS = orden - 1;
        this.NRO_MINIMO_DATOS = NRO_MAXIMO_DATOS / 2;
        this.NRO_MINIMO_HIJOS = NRO_MINIMO_DATOS + 1;
    }


    @Override
    public void insertar(K claveAInsertar, V valorAInsertar) {
        if (super.esArbolVacio()) {
            super.raiz = new NodoMVias<>(super.orden + 1, claveAInsertar, valorAInsertar);
            return;
        }
        Stack<NodoMVias<K, V>> ramaHastaHoja = new Stack<>();
        NodoMVias<K, V> nodoActual = this.raiz;
        int posABajar;
        while (!NodoMVias.esNodoVacio(nodoActual)) {
            ramaHastaHoja.add(nodoActual);
            posABajar = buscarPosicionPorDondeBajar(nodoActual, claveAInsertar);
            if (NodoMVias.esNodoVacio(nodoActual) && claveAInsertar.compareTo(nodoActual.getClave(posABajar)) == 0) {
                nodoActual.setValor(posABajar, valorAInsertar);
                return;
            }
            nodoActual = nodoActual.getHijo(posABajar);
        }
        insertarDatoNodoOrdenadamente(ramaHastaHoja.peek(), claveAInsertar, valorAInsertar, NodoMVias.nodoVacio());
        dividir(ramaHastaHoja);
    }


    private void insertarDatoNodoOrdenadamente(NodoMVias<K, V> nodoNVias,
            K clave, V valor, NodoMVias<K, V> HijoDerecho) {
        int i = 0;
        //identidico donde voy a insertar el nodo en el nodo (posicion)
        while (nodoNVias.getClave(i) != (K) NodoMVias.datoVacio()
                && clave.compareTo(nodoNVias.getClave(i)) > 0) {
            i++;
        }
        //Empujo los valores y hijos hacia adelante 
        int l = nodoNVias.nroDeClavesNoVacias()- 1;
        for (int j = l; j >= i; j--) {
            nodoNVias.setClave(j + 1, nodoNVias.getClave(j));
            nodoNVias.setValor(j + 1, nodoNVias.getValor(j));
        }
        for (int j = l + 1; j >= i + 1; j--) {
            nodoNVias.setHijo(j + 1, nodoNVias.getHijo(j));
        }

        //al fin inserto los valores
        nodoNVias.setClave(i, clave);
        nodoNVias.setValor(i, valor);
        nodoNVias.setHijo(i + 1, HijoDerecho);
    }

    
    private void eliminarDatoDeNodos(NodoMVias<K, V> nodoActual, int posDatoAEliminar) {
        int i;
        for (i = posDatoAEliminar; i < nodoActual.nroDeClavesNoVacias(); i++) {
            nodoActual.setClave(i, nodoActual.getClave(i + 1));
            nodoActual.setValor(i, nodoActual.getValor(i + 1));
            nodoActual.setHijo(i, nodoActual.getHijo(i + 1));
        }
        nodoActual.setClave(i, (K) NodoMVias.datoVacio());
        nodoActual.setValor(i, (V) NodoMVias.datoVacio());
        nodoActual.setHijo(i, nodoActual.getHijo(i + 1));
        nodoActual.setHijo(i + 1, NodoMVias.nodoVacio());
    }

    
    private void dividir(Stack<NodoMVias<K, V>> ramaActual) {
        NodoMVias<K, V> nodoActual = ramaActual.pop();
        if (nodoActual.nroDeClavesNoVacias()<= NRO_MAXIMO_DATOS) {
            return;
        }
        K claveASubir = nodoActual.getClave(VAL_MEDIO);
        V valorASubir = nodoActual.getValor(VAL_MEDIO);
        if (nodoActual == raiz) {
            NodoMVias<K, V> nodoNuevo = new NodoMVias<>(orden + 1, claveASubir, valorASubir);
            NodoMVias<K, V> parteDerecha = new NodoMVias<>(orden + 1);
            PartirHaciaDerecha(nodoActual, parteDerecha);
            nodoNuevo.setHijo(0, nodoActual);
            nodoNuevo.setHijo(1, parteDerecha);
            this.raiz = nodoNuevo;
            return;
        }
        NodoMVias<K, V> nodoPadre = ramaActual.peek();
        NodoMVias<K, V> parteDerecha = new NodoMVias<>(orden + 1);
        PartirHaciaDerecha(nodoActual, parteDerecha);
        insertarDatoNodoOrdenadamente(nodoPadre, claveASubir, valorASubir, parteDerecha);
        dividir(ramaActual);
    }

    
    private void PartirHaciaDerecha(NodoMVias<K, V> nodoActual, NodoMVias<K, V> parteDerecha) {
        int i = 0;
        int j;
        //Coloca en la parte derecha los valores correctos del nodo actual
        for (j = VAL_MEDIO + 1; j < orden; j++) {
            parteDerecha.setClave(i, nodoActual.getClave(j));
            parteDerecha.setValor(i, nodoActual.getValor(j));
            parteDerecha.setHijo(i, nodoActual.getHijo(j));
            i++;
        }
        parteDerecha.setHijo(i, nodoActual.getHijo(j));
        //Elimina la parte derecha del nodo actual 
        for (j = VAL_MEDIO; j < orden; j++) {
            nodoActual.setClave(j, (K) NodoMVias.datoVacio());
            nodoActual.setValor(j, (V) NodoMVias.datoVacio());
            nodoActual.setHijo(j + 1, NodoMVias.nodoVacio());
        }
    }

    
    @Override
    public V eliminar(K claveAEliminar) {
        try {
            NodoMVias<K, V> nodoActual = this.raiz;
            Stack<NodoMVias<K, V>> pilaAncenstros = new Stack<>();
            //Busca la clave en los nodos, apilando los ancestros por la que
            //pasa. Si no se encuentra lanza una excepcion (Excepcion lanzada
            //desde el metodo "posClaveAEliminar").
            int posClaveAEliminar
                    = buscarApilandoLaClaveEnLosNodosDesde(nodoActual,
                            claveAEliminar,
                            pilaAncenstros);
            nodoActual = pilaAncenstros.pop();
            //Reserva el valor.
            V valorEliminando = nodoActual.getValor(posClaveAEliminar);
            //Si no esta en una hoja.
            if (!nodoActual.esHoja()) {
                //Busca un remplazo apilando la rama por la que pasa.
                NodoMVias<K, V> nodoRemplazo = nodoActual;
                int posValoresRemplazo = busValRemplazoApilandoDesde(
                        nodoRemplazo, posClaveAEliminar,
                        pilaAncenstros);
                nodoRemplazo = pilaAncenstros.pop();
                //Cambio los valores del nodo con la clave a eliminar con los 
                //valores del remplazo.
                nodoActual.setClave(posClaveAEliminar,
                        nodoRemplazo.getClave(posValoresRemplazo));
                nodoActual.setValor(posClaveAEliminar,
                        nodoRemplazo.getValor(posValoresRemplazo));
                //Elimina los valores remplazo recorriendo los valores de
                //izquierda a derecha.
                recorrerValoresHojaDesde(posValoresRemplazo, nodoRemplazo);
                //Equilibra los nodos que se requiera desapilando desde el
                //nodo remplazo.
                prestarOFusionar(nodoRemplazo, pilaAncenstros);
                //Finalemente retorno el valor.
                return valorEliminando;
            }
            //Elimina la clave en nodo actual recorriendo los valores de
            //izquierda a derecha.
            eliminarDatoDeNodos(nodoActual, posClaveAEliminar);
            //Equilibra los nodos que se requiera desapilando desde el
            //nodo Actual.
            prestarOFusionar(nodoActual, pilaAncenstros);
            //Retorno el valor.
            return valorEliminando;
        } catch (ClaveNoEncontrada ex) {
            Logger.getLogger(ArbolB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private int buscarApilandoLaClaveEnLosNodosDesde(
            NodoMVias<K, V> nodoActual, K claveABuscar,
            Stack<NodoMVias<K, V>> ancenstrosDelNodoActual)
            throws ClaveNoEncontrada {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            throw new ClaveNoEncontrada("ArbolB");
        }
        int posABajar = buscarPosicionPorDondeBajar(nodoActual, claveABuscar);
        if (posABajar < nodoActual.nroDeClavesNoVacias()
                && claveABuscar.compareTo(nodoActual.getClave(posABajar))
                == 0) {
            ancenstrosDelNodoActual.add(nodoActual);
            return posABajar;
        }
        ancenstrosDelNodoActual.add(nodoActual);
        return buscarApilandoLaClaveEnLosNodosDesde(
                nodoActual.getHijo(posABajar), claveABuscar,
                ancenstrosDelNodoActual);
    }

    private int busValRemplazoApilandoDesde(NodoMVias<K, V> nodoActual, int posAbuscarRemplazo, Stack<NodoMVias<K, V>> pilaAncenstros) {
        //Se supone que no estamos en una arbolB vacio.
        //Se supone que no estamos en una hoja del arbolB.
        pilaAncenstros.add(nodoActual);
        nodoActual = nodoActual.getHijo(posAbuscarRemplazo);
        return busValMayorApilandoLosAncestros(nodoActual, pilaAncenstros);
    }

    private int busValMayorApilandoLosAncestros(NodoMVias<K, V> nodoActual, Stack<NodoMVias<K, V>> pilaAncenstros) {
        //Se supone que no empezamos en un nodo vacio nunca.
        int cantClaves = nodoActual.nroDeClavesNoVacias();
        if (nodoActual.esHoja()) {
            pilaAncenstros.add(nodoActual);
            return cantClaves - 1;
        }
        pilaAncenstros.add(nodoActual);
        return busValMayorApilandoLosAncestros(nodoActual.getHijo(cantClaves), pilaAncenstros);
    }

    private void recorrerValoresHojaDesde(int posValoresRemplazo, NodoMVias<K, V> nodoActual) {
        int canClavesNodoActual = nodoActual.nroDeClavesNoVacias();
        int i;
        for (i = posValoresRemplazo; i < canClavesNodoActual - 1; i++) {
            nodoActual.setClave(i, nodoActual.getClave(i + 1));
            nodoActual.setValor(i, nodoActual.getValor(i + 1));
        }
        nodoActual.setClave(i, (K) NodoMVias.datoVacio());
        nodoActual.setValor(i, (V) NodoMVias.datoVacio());
    }

    private void prestarOFusionar(NodoMVias<K, V> nodoActual,
            Stack<NodoMVias<K, V>> pilaAncenstros) {
        //Si la pila de ancestros esta vacia terminamos el proceso 
        //(nodo actual es raiz).
        if (pilaAncenstros.isEmpty()) {
            return;
        }
        int canClavesNodoActual = nodoActual.nroDeClavesNoVacias();
        //Si el nodo actual esta equilibrado termina el proceso.
        if (canClavesNodoActual >= NRO_MINIMO_DATOS) {
            return;
        }
        //Encontramos el hermano izquierdo y derecho del nodo actual. (Si no 
        //existe son nulos).
        NodoMVias<K, V> hermanoIzquierdo = NodoMVias.nodoVacio();
        NodoMVias<K, V> hermanoDerecho = NodoMVias.nodoVacio();
        
        NodoMVias<K, V> nodoPadre = pilaAncenstros.peek();
        int posNodoActualEnHijosDelPadre = busPosHijo(nodoPadre, nodoActual);
        if (posNodoActualEnHijosDelPadre == 0) {
            hermanoDerecho = nodoPadre.getHijo(posNodoActualEnHijosDelPadre + 1);
        }else if (posNodoActualEnHijosDelPadre == nodoPadre.nroDeClavesNoVacias()) {
            hermanoIzquierdo = nodoPadre.getHijo(posNodoActualEnHijosDelPadre - 1);
        }else{
        hermanoDerecho = nodoPadre.getHijo(posNodoActualEnHijosDelPadre + 1);
        hermanoIzquierdo = nodoPadre.getHijo(posNodoActualEnHijosDelPadre - 1);
        }
        
        //Se presta del hermano derecho de poder.
        if (!NodoMVias.esNodoVacio(hermanoDerecho) && hermanoDerecho.nroDeClavesNoVacias() > NRO_MINIMO_DATOS) {
            prestarDeAdelante(nodoActual, pilaAncenstros.peek(), hermanoDerecho);
            //FINALIZA
            return;
        }
        //Se presta del hermano izquierdo de poder.
        if (!NodoMVias.esNodoVacio(hermanoIzquierdo) && hermanoIzquierdo.nroDeClavesNoVacias() > NRO_MINIMO_DATOS) {
            prestarDeAtras(nodoActual, pilaAncenstros.peek(), hermanoIzquierdo);
            //FINALIZA
            return;
        }
        //Se fusiona con el hermano derecho de poder.
        if (!NodoMVias.esNodoVacio(hermanoDerecho)) {
            fusionarAdelante(nodoActual, pilaAncenstros.peek(), hermanoDerecho);
            //Recursion para el padre.
            prestarOFusionar(pilaAncenstros.pop(), pilaAncenstros);
            return;
        }
        //Se fusiona con el hermano izquierdo.
        fusionarAtras(hermanoIzquierdo, pilaAncenstros.peek(), nodoActual);
        //Recursion para el padre.
        prestarOFusionar(pilaAncenstros.pop(), pilaAncenstros);
    }

    private int busPosHijo(NodoMVias<K, V> nodoActual, NodoMVias<K, V> nodoHijo) {
        //Suponemos que el nodo actual tiene los valores buscados en algun hijo
        int cantClavesNodoActual = nodoActual.nroDeClavesNoVacias();
        int i;
        for (i = 0; i < cantClavesNodoActual; i++) {
            if (nodoActual.getHijo(i) == nodoHijo) {
                return i;
            }
        }
        return i;
    }

    private void prestarDeAdelante(NodoMVias<K, V> nodoActual, NodoMVias<K, V> nodoPadre, NodoMVias<K, V> hermanoDerecho) {
        int posNodoActualEnElPadre = busPosHijo(nodoPadre, nodoActual);
        //Bajo los valores correspondiente del nodo padre a la derecha del actual
        int cantClavesNodoActual = nodoActual.nroDeClavesNoVacias();
        nodoActual.setClave(cantClavesNodoActual, nodoPadre.getClave(posNodoActualEnElPadre));
        nodoActual.setValor(cantClavesNodoActual, nodoPadre.getValor(posNodoActualEnElPadre));
        //Suben a la posicion correspondiente del nodo padre los valores de la primer posicion del hermano derecho
        nodoPadre.setClave(posNodoActualEnElPadre, hermanoDerecho.getClave(0));
        nodoPadre.setValor(posNodoActualEnElPadre, hermanoDerecho.getValor(0));
        //Reubicamos a la derecha del nodo actual el primer hijo del hermano derecho.
        cantClavesNodoActual++;
        nodoActual.setHijo(cantClavesNodoActual, hermanoDerecho.getHijo(0));
        //Recorremos los valores e hijos del hermano derecho en -1.
        recorrerValoresEHijosAtras(hermanoDerecho, 0);
    }

    private void recorrerValoresEHijosAtras(NodoMVias<K, V> nodoActual, int posInicialARecorrerValores){
        int canClavesNodoActual = nodoActual.nroDeClavesNoVacias();
        int i;
        for (i = posInicialARecorrerValores; i < canClavesNodoActual-1; i++) {
            nodoActual.setClave(i, nodoActual.getClave(i+1));
            nodoActual.setValor(i, nodoActual.getValor(i+1));
            nodoActual.setHijo(i, nodoActual.getHijo(i+1));
        }
        nodoActual.setHijo(i, nodoActual.getHijo(i+1));
        nodoActual.setClave(i, (K) NodoMVias.datoVacio());
        nodoActual.setValor(i, (V) NodoMVias.datoVacio());
        nodoActual.setHijo(i+1, NodoMVias.nodoVacio());
    }

    private void prestarDeAtras(NodoMVias<K, V> nodoActual, NodoMVias<K, V> nodoPadre, NodoMVias<K, V> hermanoIzquierdo) {
        int posNodoActualEnElPadre = busPosHijo(nodoPadre, nodoActual);
        //Recorro los valores del nodoActual en +1
        recorrerValoresEHijosAdelante(nodoActual, 0);
        //Bajo los Valores en la posicion correspondientes al nodo actual -1 
        //del padre a la primer poscion de los valores en el nodo actual.
        nodoActual.setClave(0, nodoPadre.getClave(posNodoActualEnElPadre-1));
        nodoActual.setValor(0, nodoPadre.getValor(posNodoActualEnElPadre-1));
        //En la posicion correspondiente -1 del padre colocamos los valores de 
        //la derecha del hermano izquierdo.
        int cantClavesHermanoIzquierdo = hermanoIzquierdo.nroDeClavesNoVacias();
        nodoPadre.setClave(posNodoActualEnElPadre-1, hermanoIzquierdo.getClave(cantClavesHermanoIzquierdo-1));
        nodoPadre.setValor(posNodoActualEnElPadre-1, hermanoIzquierdo.getValor(cantClavesHermanoIzquierdo-1));
        //Elimino los valores que subieron en el hermano izquierdo.
        hermanoIzquierdo.setClave(cantClavesHermanoIzquierdo-1, (K) NodoMVias.datoVacio());
        hermanoIzquierdo.setValor(cantClavesHermanoIzquierdo-1, (V) NodoMVias.datoVacio());
        //Reubico el hijo de la derecha del hermano izquierdo a la primer 
        //posicion del nodo actual
        nodoActual.setHijo(0, hermanoIzquierdo.getHijo(cantClavesHermanoIzquierdo));
        //Elimino la referencia reubicada del hermano izquierdo.
        hermanoIzquierdo.setHijo(cantClavesHermanoIzquierdo, NodoMVias.nodoVacio());
    }

    private void recorrerValoresEHijosAdelante(NodoMVias<K, V> nodoActual, int posInicialARecorrerValores) {
        int cantClaveNodoActual = nodoActual.nroDeClavesNoVacias();
        int i;
        for (i = cantClaveNodoActual; i > posInicialARecorrerValores; i--) {
            nodoActual.setClave(i, nodoActual.getClave(i-1));
            nodoActual.setValor(i, nodoActual.getValor(i-1));
            nodoActual.setHijo(i+1, nodoActual.getHijo(i-1));
        }
        
        nodoActual.setClave(i, (K) NodoMVias.datoVacio());
        nodoActual.setValor(i, (V) NodoMVias.datoVacio());
        nodoActual.setHijo(i+1, NodoMVias.nodoVacio());
    }

    private void fusionarAdelante(NodoMVias<K, V> nodoActual, NodoMVias<K, V> nodoPadre, NodoMVias<K, V> hermanoDerecho) {
        //Bajamos el valor correspondiente al nodo actual del nodo padre a la derecha del nodo actual.
        int posNodoActualEnELPadre = busPosHijo(nodoPadre, nodoActual);
        int cantidadClavesNodoActual = nodoActual.nroDeClavesNoVacias();
        nodoActual.setClave(cantidadClavesNodoActual, nodoPadre.getClave(posNodoActualEnELPadre));
        nodoActual.setValor(cantidadClavesNodoActual, nodoPadre.getValor(posNodoActualEnELPadre));
        //Colocamos todos los valores e hijos del hermano derecho al nodo actual.
        cantidadClavesNodoActual++;
        int canClavesHermanoDerecho = hermanoDerecho.nroDeClavesNoVacias();
        int i;
        int j = cantidadClavesNodoActual;
        for (i = 0; i < canClavesHermanoDerecho; i++) {
            nodoActual.setClave(j, hermanoDerecho.getClave(i));
            nodoActual.setValor(j, hermanoDerecho.getValor(i));
            nodoActual.setHijo(j, hermanoDerecho.getHijo(i));
            j++;
        }
        nodoActual.setHijo(j, hermanoDerecho.getHijo(i));
       //Recoremos los valores e hijos del nodo padre.
       recorrerValoresEHijosExceptoElPrimeroDesde(nodoPadre, posNodoActualEnELPadre);
       //Si el nodo padre era la raiz y esta vacio, la nueva raiz es el nodo actual.
       if(nodoPadre == this.raiz && nodoPadre.nroDeClavesNoVacias() == 0){
           this.raiz = nodoActual;
       }
    }

    private void recorrerValoresEHijosExceptoElPrimeroDesde(NodoMVias<K, V> nodoActual, int posDelValorInicialARecorrer) {
        NodoMVias<K, V> nodoRescatado = nodoActual.getHijo(posDelValorInicialARecorrer);
        recorrerValoresEHijosAtras(nodoActual, posDelValorInicialARecorrer);
        nodoActual.setHijo(posDelValorInicialARecorrer, nodoRescatado);
    }

    private void fusionarAtras(NodoMVias<K, V> hermanoIzquierdo, NodoMVias<K, V> nodoPadre, NodoMVias<K, V> nodoActual) {
        //Se baja los valores del padre relativo al hermanoIzquierdo.
        int posDelHermanoIzquierdoEnELPadre = busPosHijo(nodoPadre, hermanoIzquierdo);
        int cantClavesEnElHermanoIzquierdo = hermanoIzquierdo.nroDeClavesNoVacias();
        hermanoIzquierdo.setClave(cantClavesEnElHermanoIzquierdo, nodoPadre.getClave(posDelHermanoIzquierdoEnELPadre));
        hermanoIzquierdo.setValor(cantClavesEnElHermanoIzquierdo, nodoPadre.getValor(posDelHermanoIzquierdoEnELPadre));
        cantClavesEnElHermanoIzquierdo++;
        //Se copian todos los valores e hijos del nodo actual al hermano Izquierdo.
        int canClavesNodoActual = nodoActual.nroDeClavesNoVacias();
        int i;
        int j = cantClavesEnElHermanoIzquierdo;
        for (i = 0; i < canClavesNodoActual; i++) {
            hermanoIzquierdo.setClave(j, nodoActual.getClave(i));
            hermanoIzquierdo.setValor(j, nodoActual.getValor(i));
            hermanoIzquierdo.setHijo(j, nodoActual.getHijo(i));
            j++;
        }
        hermanoIzquierdo.setHijo(j, nodoActual.getHijo(i));
       //Recoremos los valores e hijos del nodo padre.
       recorrerValoresEHijosExceptoElPrimeroDesde(nodoPadre, posDelHermanoIzquierdoEnELPadre);
       //Si el nodo padre era la raiz y esta vacio, la nueva raiz es el nodo actual.
       if(nodoPadre == this.raiz && nodoPadre.nroDeClavesNoVacias() == 0){
           this.raiz = hermanoIzquierdo;
       }
    }
    
    
    
    
    public static void main(String[] args) {
//       ArbolB<Integer, String> arbol;
//       ArbolMVBalanceado, ArbolMViasBusqueda
//        arbol = new ArbolB<>();
//        
//        arbol.insertar(51, "a");
//        arbol.insertar(50, "c");
//        arbol.insertar(60, "d");
//        arbol.insertar(55, "d");
//        arbol.insertar(70, "e");
////        arbol.insertar(75, "e");
////        arbol.insertar(53, "e");
////        arbol.insertar(70, "e");
        
//        System.out.println("es Vacio: "+arbol.esArbolVacio());
//        System.out.println("ALTURA: "+arbol.altura()); 
//        arbol.eliminar(51);
//        arbol.mostrar();
    }
    
}
