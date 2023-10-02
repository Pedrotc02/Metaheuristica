package org.example;

import java.util.ArrayList;

public class Greedy {

    Utils utils;

    public Greedy() {
        utils = new Utils();
    }



    public int calcularCosteGreedy(ArrayList<Integer> solucion, LecturaMatrices lm){
        Integer coste = 0;

        for (int i = 0; i < solucion.size(); i++) {
            for (int j = 0; j < solucion.size(); j++) {
                if(i != j){
                    coste += lm.matrizFlujoProductos[i][j] * lm.contenidoMatrizDistancias[solucion.get(i)][solucion.get(j)];
                }
            }
        }

        //System.out.println(coste);

        return coste;
    }

    public int algoritmo(LecturaMatrices lm){
        ArrayList<Integer> usadosFlujo = new ArrayList<>();
        ArrayList<Integer> usadosDistancia = new ArrayList<>();
        ArrayList<Integer> solucion = new ArrayList<>(lm.tamProblema);

        for (int i = 0; i < lm.getTamProblema(); i++) {
            solucion.add(0);
        }
        int tamVectores = lm.potencialFlujo.size();
        Integer mayorFlujo = 0;
        Integer idFlujo = 0;
        Integer menorDistancia = 999999;
        Integer idDistancia = 0;

        while (tamVectores != 0){
            for (int i = 0; i < lm.getPotencialFlujo().size(); i++) {
                if (lm.getPotencialFlujo().get(i) > mayorFlujo && !usadosFlujo.contains(i)){
                    mayorFlujo = lm.getPotencialFlujo().get(i);
                    idFlujo = i;
                }
            }

            for (int j = 0; j < lm.getPotencialDistancias().size(); j++) {
                if (lm.getPotencialDistancias().get(j) < menorDistancia && !usadosDistancia.contains(j)){
                    menorDistancia = lm.getPotencialDistancias().get(j);
                    idDistancia = j;
                }
            }

            usadosFlujo.add(idFlujo);
            usadosDistancia.add(idDistancia);
            mayorFlujo = 0;
            menorDistancia = 999999;
            tamVectores--;

            solucion.set(idFlujo, idDistancia);
        }

        //utils.mostrarVectoresSolucion(solucionFlujos, solucionDistancias);
        return calcularCosteGreedy(solucion, lm);

    }


}
