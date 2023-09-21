package org.example;

import java.util.ArrayList;
import java.util.Collections;

public class AlgoritmoGreedy {
    ArrayList<Integer> potencialFlujo;
    ArrayList<Integer> potencialDistancias;

    public AlgoritmoGreedy() {
        potencialFlujo = new ArrayList<>();
        potencialDistancias = new ArrayList<>();
    }

    public void rellenarVectores(LecturaMatrices lm){
        int fila = 0;
        Integer sumatoriaFlujos = 0;
        Integer sumatoriaDistancias = 0;

        while (fila < lm.getMatrizFlujoProductos().length){
            for (int i = 0; i < lm.getMatrizFlujoProductos().length; i++) {
                sumatoriaFlujos += lm.getMatrizFlujoProductos()[fila][i];
                sumatoriaDistancias += lm.getContenidoMatrizDistancias()[fila][i];
            }

            potencialFlujo.add(fila, sumatoriaFlujos);
            potencialDistancias.add(fila, sumatoriaDistancias);
            fila++;
            sumatoriaFlujos = 0;
            sumatoriaDistancias = 0;
        }

    }

    public void algoritmo(LecturaMatrices lm){
        rellenarVectores(lm);
        ArrayList<Integer> solucionFlujos = new ArrayList<>();
        ArrayList<Integer> solucionDistancias = new ArrayList<>();
        ArrayList<Integer> usadosFlujo = new ArrayList<>();
        ArrayList<Integer> usadosDistancia = new ArrayList<>();
        int tamVectores = potencialFlujo.size();
        Integer mayorFlujo = 0;
        Integer idFlujo = 0;
        Integer menorDistancia = 999999;
        Integer idDistancia = 0;

        while (tamVectores != 0){
            for (int i = 0; i < potencialFlujo.size(); i++) {
                if (potencialFlujo.get(i) > mayorFlujo && !usadosFlujo.contains(i)){
                    mayorFlujo = potencialFlujo.get(i);
                    idFlujo = i;
                }
            }

            for (int j = 0; j < potencialDistancias.size(); j++) {
                if (potencialDistancias.get(j) < menorDistancia && !usadosDistancia.contains(j)){
                    menorDistancia = potencialDistancias.get(j);
                    idDistancia = j;
                }
            }

            solucionFlujos.add(idFlujo);
            solucionDistancias.add(idDistancia);
            usadosFlujo.add(idFlujo);
            usadosDistancia.add(idDistancia);
            mayorFlujo = 0;
            menorDistancia = 999999;
            tamVectores--;
        }

        mostrarVectoresSolucion(solucionFlujos, solucionDistancias);
        calcularCosteGreedy(solucionFlujos, solucionDistancias, lm);

    }

    public void mostrarVectoresSolucion(ArrayList<Integer> solucionFlujos, ArrayList<Integer> solucionDistancias){
        System.out.println("Vector Solucion de Flujos: ");
        for (int i = 0; i < solucionFlujos.size(); i++) {
            System.out.print(solucionFlujos.get(i));
        }
        System.out.println();

        System.out.println("Vector Solucion de Distancias: ");
        for (int i = 0; i < solucionDistancias.size(); i++) {
            System.out.print(solucionDistancias.get(i));
        }
        System.out.println();
    }

    public int calcularCosteGreedy(ArrayList<Integer> solucionFlujos, ArrayList<Integer> solucionDistancias, LecturaMatrices lm){
        Integer coste = 0;

        for (int i = 0; i < solucionFlujos.size(); i++) {
            for (int j = 0; j < solucionFlujos.size(); j++) {
                if(i != j){
                    coste += lm.matrizFlujoProductos[solucionFlujos.get(i)][solucionFlujos.get(j)] * lm.contenidoMatrizDistancias[solucionDistancias.get(i)][solucionDistancias.get(j)];
                }
            }
        }

        System.out.println(coste);

        return coste;
    }
}
