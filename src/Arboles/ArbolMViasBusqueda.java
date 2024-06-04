/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Arboles;

import java.util.List;
import Excepciones.OrdenInvalidoExcepcion;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
/**
 *
 * 
 */

public class ArbolMViasBusqueda <K extends Comparable<K>, V> implements IArbolBusqueda<K, V>{
    
    protected NodoMVias<K, V> raiz;
    protected int orden;
    protected static final int ORDEN_MINIMO = 3;
    protected static final int POSICION_INVALIDA = -1;
        
    
    public ArbolMViasBusqueda() {
        this.orden = ArbolMViasBusqueda.ORDEN_MINIMO;
    }

    public ArbolMViasBusqueda(int orden) throws OrdenInvalidoExcepcion {
            if (orden < ArbolMViasBusqueda.ORDEN_MINIMO) {
                    throw new OrdenInvalidoExcepcion();
            }
            this.orden = orden;
    }
        
        
    @Override
    public void insertar(K claveAInsertar, V valorAsociado) {
        if (claveAInsertar == null) {
            throw new IllegalArgumentException("Clave invalida, no se aceptan claves vacias");
        }
        
        if (valorAsociado == null) {
            throw new IllegalArgumentException("Valor invalido, no se aceptan valores nulos");
        }
        
        if (this.esArbolVacio()) {
            this.raiz = new NodoMVias<>(this.orden, claveAInsertar, valorAsociado);
            return;
        }
        
        NodoMVias<K,V> nodoAux = this.raiz;
        do{
            int posicionDeClaveAInsertar = this.buscarPosicionDeClave(nodoAux, claveAInsertar);
            if(posicionDeClaveAInsertar != ArbolMViasBusqueda.POSICION_INVALIDA){
                nodoAux.setValor(posicionDeClaveAInsertar, valorAsociado);
                nodoAux = NodoMVias.nodoVacio();
            }
            else if(nodoAux.esHoja()){
                if(nodoAux.clavesLLena()){
                    NodoMVias<K,V> nodoNuevo = new NodoMVias<>(this.orden, claveAInsertar, valorAsociado);
                    int posicionDondeEnlazar = this.buscarPosicionPorDondeBajar(nodoAux, claveAInsertar);
                    nodoAux.setHijo(posicionDondeEnlazar, nodoNuevo);
                }
                else{
                    this.InsertarClaveYValorOrdenado(nodoAux, claveAInsertar, valorAsociado);
                }
                nodoAux = NodoMVias.nodoVacio();
            }
            else{
                int posicionDondeEnlazar = this.buscarPosicionPorDondeBajar(nodoAux, claveAInsertar);
                if(nodoAux.esHijoVacio(posicionDondeEnlazar)){
                    NodoMVias<K,V> nodoNuevo = new NodoMVias<>(this.orden, claveAInsertar, valorAsociado);
                    nodoAux.setHijo(posicionDondeEnlazar, nodoNuevo);
                    nodoAux = NodoMVias.nodoVacio();
                } 
                else{
                    nodoAux = nodoAux.getHijo(posicionDondeEnlazar);
                }
            }
        } while(!NodoMVias.esNodoVacio(nodoAux));
        
    }
    
    protected int buscarPosicionDeClave(NodoMVias<K, V> nodoActual,K claveABuscar) {
        for (int i = 0; i < nodoActual.nroDeClavesNoVacias(); i++) {
                K claveEnTurno = nodoActual.getClave(i);
                if (claveABuscar.compareTo(claveEnTurno) == 0) {
                        return i;
                }
        }
        return ArbolMViasBusqueda.POSICION_INVALIDA;
    }
    
    protected int buscarPosicionPorDondeBajar(NodoMVias<K, V> nodoActual, K claveAInsertar) {
		int i = 0;
		boolean llegoAlFinal = false;
		while (i < nodoActual.nroDeClavesNoVacias()) {
			K claveActual = nodoActual.getClave(i);
			if (claveActual.compareTo(claveAInsertar) < 0) {
				i++;
			} else {
				break;
			}
		}
		if (nodoActual.getClave(nodoActual.nroDeClavesNoVacias()
				- 1).compareTo(claveAInsertar) < 0) {
			return nodoActual.nroDeClavesNoVacias();
		}
		return i;
    }
    
    protected void InsertarClaveYValorOrdenado(NodoMVias<K, V> nodoActual,K claveAInsertar, V valorAsociado) {
		boolean insertado = false;
		for (int i = nodoActual.nroDeClavesNoVacias() - 1; i >= 0 && !insertado; i--) {
			K claveActual = nodoActual.getClave(i);
			if (claveAInsertar.compareTo(claveActual) > 0) { // claveAInsertar > claveActual
				nodoActual.setClave(i + 1, claveAInsertar);
				nodoActual.setValor(i + 1, valorAsociado);
				insertado = true;
				//return;
			} else {
				nodoActual.setClave(i + 1, claveActual);
				nodoActual.setValor(i + 1, nodoActual.getValor(i));
			}
		}
		if (!insertado) {
			nodoActual.setClave(0, claveAInsertar);
			nodoActual.setValor(0, valorAsociado);
		}
    }
    
    @Override
    public V eliminar(K claveAEliminar) {
        if (claveAEliminar == null) {
            throw new IllegalArgumentException("Clave invalida,no se aceptan claves vacias");
        }
        
        V valorAsociado = buscar(claveAEliminar);
        if (valorAsociado == null) {
            throw new IllegalArgumentException("Valor invalida,no se aceptan valores vacios");
        }
        this.raiz = eliminar(this.raiz, claveAEliminar);
        return valorAsociado;
    }

    private NodoMVias<K, V> eliminar(NodoMVias<K, V> nodoActual, K claveAEliminar) {
        for (int i = 0; i < nodoActual.nroDeClavesNoVacias(); i++) {
            K claveEnTurno = nodoActual.getClave(i);
            if (claveAEliminar.compareTo(claveEnTurno) == 0) {
                if (nodoActual.esHoja()) {
                        eliminarClaveDePosicion(nodoActual, i);
                        if (!nodoActual.hayClavesNoVacias()) {
                                return NodoMVias.nodoVacio();
                        } else {
                                return nodoActual;
                        }
                } else {
                    K claveDeReemplazo;
                    if (hayHijosNoVaciosMasAdelante(nodoActual, i)) {
                            claveDeReemplazo = obtenerSucesorInOrden(nodoActual, claveAEliminar);  //obtenerSucesorInOrden(nodoActual, i);
                    } else {
                            claveDeReemplazo = obtenerPredecesorInOrden(nodoActual, claveAEliminar);  //obtenerPredecesorInOrden(nodoActual, i);
                    }

                    V valorDeReemplazo = buscar(claveDeReemplazo);
                    nodoActual = eliminar(nodoActual, claveDeReemplazo);
                    nodoActual.setClave(i, claveDeReemplazo);
                    nodoActual.setValor(i, valorDeReemplazo);
                    return nodoActual;
                }
            } // Fin if (claveAEliminar.compareTo(claveEnTurno) == 0)

            if (claveAEliminar.compareTo(claveEnTurno) < 0) {
                    NodoMVias<K, V> supuestoNuevoHijo = eliminar(nodoActual.getHijo(i), claveAEliminar);
                    nodoActual.setHijo(i, supuestoNuevoHijo);
                    return nodoActual;
            }
        } // Fin For
        NodoMVias<K, V> supuestoNuevoHijo = eliminar(nodoActual.getHijo(
                        nodoActual.nroDeClavesNoVacias()), claveAEliminar);
        nodoActual.setHijo(nodoActual.nroDeClavesNoVacias(), supuestoNuevoHijo);
        return nodoActual;
    }

    protected void eliminarClaveDePosicion(NodoMVias<K, V> nodoActual, int posicion) {
            for (int i = posicion; i <= nodoActual.nroDeClavesNoVacias() - 1; i++) {
                    nodoActual.setClave(i, nodoActual.getClave(i + 1));
                    nodoActual.setValor(i, nodoActual.getValor(i + 1));
            }
    }

	protected boolean hayHijosNoVaciosMasAdelante(NodoMVias<K, V> nodoActual, int posicion) {
		boolean existe = false;
		for (int i = posicion + 1; i <= nodoActual.nroDeClavesNoVacias() && !existe; i++) {
			if (!nodoActual.esHijoVacio(i)) {
				existe = true;
			}
		}
		return existe;
	}

	private K obtenerPredecesorInOrden(NodoMVias<K, V> nodoActual, K claveAEliminar) {   //int i) {
		int posicion = buscarPosicionDeClave(nodoActual, claveAEliminar);
		K claveDeRetorno = (K) NodoMVias.datoVacio();
		if (!nodoActual.esHijoVacio(posicion)) {
			NodoMVias<K, V> nodoAuxiliar = nodoActual.getHijo(posicion);
			while (!NodoMVias.esNodoVacio(nodoAuxiliar)) {
				claveDeRetorno = nodoAuxiliar.getClave(nodoAuxiliar.nroDeClavesNoVacias() - 1);
				nodoAuxiliar = nodoAuxiliar.getHijo(0);
			}
			return claveDeRetorno;
		} else {
			return nodoActual.getClave(posicion-1);
		}
	}

	private K obtenerSucesorInOrden(NodoMVias<K, V> nodoActual, K claveAEliminar) {   //int i) {
		int posicion = buscarPosicionDeClave(nodoActual, claveAEliminar);
		K claveDeRetorno = (K) NodoMVias.datoVacio();
		if (!nodoActual.esHijoVacio(posicion + 1)) {
			//int posicion = obtenerPosicionPorDondeBajar(nodoActual, claveAEliminar);

			NodoMVias<K, V> nodoAuxiliar = nodoActual.getHijo(posicion + 1);

			while (!NodoMVias.esNodoVacio(nodoAuxiliar)) {
				claveDeRetorno = nodoAuxiliar.getClave(0);
				nodoAuxiliar = nodoAuxiliar.getHijo(0);
			}
			return claveDeRetorno;
		} else {
			return nodoActual.getClave(posicion + 1);
		}

	}

    @Override
    public V buscar(K claveABuscar) {
      if (!this.esArbolVacio()) {
			NodoMVias<K, V> nodoActual = this.raiz;
			do {
				boolean cambiaElNodoActual = false;
				for (int i = 0; i < nodoActual.nroDeClavesNoVacias()
						&& !cambiaElNodoActual; i++) {
					K claveDelNodoActual = nodoActual.getClave(i);
					if (claveABuscar.compareTo(claveDelNodoActual) == 0) {
						return nodoActual.getValor(i);
					}
					if (claveABuscar.compareTo(claveDelNodoActual) < 0) {
						nodoActual = nodoActual.getHijo(i);
						cambiaElNodoActual = true;
					}
				}
				if (!cambiaElNodoActual) {
					nodoActual = nodoActual.getHijo(
							nodoActual.nroDeClavesNoVacias());
				}
			} while (!NodoMVias.esNodoVacio(nodoActual));
		}
		return null;
    }
    
    @Override
    public boolean contiene(K claveAVerificar) {
        return this.buscar(claveAVerificar) != null;
    }

    @Override
    public int size() {
      int cantidadDeNodos = 0;
		if (!this.esArbolVacio()) {
			Queue<NodoMVias<K, V>> colaDeNodos = new LinkedList<>();
			colaDeNodos.offer(this.raiz);

			do {
				NodoMVias<K, V> nodoActual = colaDeNodos.poll();
				cantidadDeNodos++;
				for (int i = 0; i < orden; i++) {
					if (!nodoActual.esHijoVacio(i)) {
						colaDeNodos.offer(nodoActual.getHijo(i));
					}
				}
			} while (!colaDeNodos.isEmpty());
		}
		return cantidadDeNodos;
    }

    @Override
    public int altura() {
        return altura(this.raiz);
    }

	protected int altura(NodoMVias<K, V> nodoActual) {
		if (NodoMVias.esNodoVacio(nodoActual)) {
			return 0;
		}
		int alturaMayor = 0;
		for (int i = 0; i < nodoActual.nroDeClavesNoVacias() + 1; i++) {
			int alturaDeHijoActual = altura(nodoActual.getHijo(i));
			if (alturaDeHijoActual > alturaMayor) {
				alturaMayor = alturaDeHijoActual;
			}
		}
		return alturaMayor + 1;
	}

    @Override
    public void vaciar() {
        this.raiz = NodoMVias.nodoVacio();
    }

    @Override
    public boolean esArbolVacio() {
        return NodoMVias.esNodoVacio(this.raiz);
    }
    
    @Override
    public int nivel() {
        return altura() - 1;
    }

    @Override
    public List<K> recorridoEnInOrden() {
        List<K> recorrido = new ArrayList<>();
        recorridoEnInOrden(this.raiz, recorrido);
        return recorrido;
    }

	private void recorridoEnInOrden(NodoMVias<K, V> nodoActual,
			List<K> recorrido) {
		if (NodoMVias.esNodoVacio(nodoActual)) {
			return;
		}
		for (int i = 0; i < nodoActual.nroDeClavesNoVacias(); i++) {
			recorridoEnInOrden(nodoActual.getHijo(i), recorrido);
			recorrido.add(nodoActual.getClave(i));
		}
		recorridoEnInOrden(nodoActual.getHijo(nodoActual.nroDeClavesNoVacias()),
				recorrido);
	}

    @Override
    public List<K> recorridoEnPreOrden() {
        List<K> recorrido = new ArrayList<>();
        recorridoEnPreOrden(raiz, recorrido);
        return recorrido;
    }
    
    private void recorridoEnPreOrden(NodoMVias<K,V> nodoActual, List<K>recorrido){
        if(!NodoMVias.esNodoVacio(nodoActual)){
            for (int i = 0; i < nodoActual.nroDeClavesNoVacias(); i++) {
                recorrido.add(nodoActual.getClave(i));
                recorridoEnPreOrden(nodoActual.getHijo(i), recorrido);
            }
            recorridoEnPreOrden(nodoActual.getHijo(nodoActual.nroDeClavesNoVacias()),recorrido);
        }
    }

    @Override
    public List<K> recorridoEnPostOrden() {
        List<K> recorrido = new ArrayList<>();
		recorridoEnPostOrden(this.raiz, recorrido);
		return recorrido;
	}

    private void recorridoEnPostOrden(NodoMVias<K, V> nodoActual, List<K> recorrido) {
            if (NodoMVias.esNodoVacio(nodoActual)) {
                    return;
            }
            recorridoEnPostOrden(nodoActual.getHijo(0), recorrido);
            for (int i = 0; i < nodoActual.nroDeClavesNoVacias(); i++) {
                    recorridoEnPostOrden(nodoActual.getHijo(i + 1), recorrido);
                    recorrido.add(nodoActual.getClave(i));
            }
	
    }

    @Override
    public List<K> recorridoPorNiveles() {
        List<K> recorrido = new ArrayList<>();
        if (!this.esArbolVacio()) {
                Queue<NodoMVias<K, V>> colaDeNodos = new LinkedList<>();
                colaDeNodos.offer(this.raiz);
                do {
                    NodoMVias<K, V> nodoActual = colaDeNodos.poll();
                    for (int i = 0; i < nodoActual.nroDeClavesNoVacias(); i++) {
                            recorrido.add(nodoActual.getClave(i));
                            if (!nodoActual.esHijoVacio(i)) {
                                    colaDeNodos.offer(nodoActual.getHijo(i));
                            }
                    }
                    if (!nodoActual.esHijoVacio(nodoActual.nroDeClavesNoVacias())) {
                            colaDeNodos.offer(nodoActual.getHijo(nodoActual.nroDeClavesNoVacias()));
                    }
                } while (!colaDeNodos.isEmpty());
        }
        return recorrido;
    }
    


    public String toString() {
        return toStringNodo(raiz, "");
    }

    private String toStringNodo(NodoMVias<K,V> nodoActual, String ant) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < nodoActual.nroDeClavesNoVacias(); i++) {
            sb.append(ant).append("║\n");
            sb.append(ant).append("╠═Clave: ").append(nodoActual.getClave(i)).append(", Valor: ").append(nodoActual.getValor(i)).append("\n");
            sb.append(ant).append("║\n");
            sb.append(ant).append("╠═Hijo ").append(i).append(": ").append(toStringNodo(nodoActual.getHijo(i), ant + "║  ")).append("\n");
        }
        sb.append(ant).append("║\n");
        sb.append(ant).append("╚═Hijo ").append(nodoActual.nroDeClavesNoVacias()).append(": ").append(toStringNodo(nodoActual.getHijo(nodoActual.nroDeClavesNoVacias()), ant + "║  "));
        return sb.toString();
    }

    
    
}
