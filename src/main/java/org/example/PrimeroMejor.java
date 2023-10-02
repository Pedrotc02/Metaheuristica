package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PrimeroMejor {

    ArrayList<Boolean> dlb;
    ArrayList<Integer> solucion;
    Random random;
    Boolean improveFlag;
    Greedy algGreedy;
    int iteraciones;
    int coste;

    public PrimeroMejor(LecturaMatrices lecturaMatrices, long semilla) {
        iteraciones = 0;
        algGreedy = new Greedy();
        dlb = new ArrayList<>();
        solucion = new ArrayList<>();
        for (int i = 0; i < lecturaMatrices.getTamProblema(); i++) {
            dlb.add(i,false);
        }
        for (int i = 0; i < lecturaMatrices.getTamProblema(); i++) {
            solucion.add(i,i);
        }
        coste = algGreedy.calcularCosteGreedy(solucion, lecturaMatrices);
        random = new Random(semilla);

    }

    public int dlb(LecturaMatrices lecturaMatrices, int maxIteraciones){

        System.out.println("Solución inicial: ");
        for (int i = 0; i < lecturaMatrices.getTamProblema(); i++) {
            System.out.print(solucion.get(i));
        }

        System.out.println("\nCoste Inicial: " + coste);



        int j;
        int inicioAleatorio = random.nextInt(lecturaMatrices.getTamProblema());

        while(true){
            for (int i = inicioAleatorio; i < lecturaMatrices.getTamProblema(); i++) {
                if(!dlb.get(i)){
                    improveFlag = false;
                    if(i == lecturaMatrices.getTamProblema() -1){
                        j = 0;
                    }else{
                        j = i+1;
                    }
                    for (; j < lecturaMatrices.getTamProblema(); j++) {

                        if(!mejoraNoEncontrada() || iteraciones >= maxIteraciones){
                            System.out.println("Coste final: " + coste);
                            return coste;
                        }

                        if(mejoraConMovimiento(i, j, lecturaMatrices)){
                            dlb.add(i,false);
                            dlb.add(j,false);
                            improveFlag = true;
                            iteraciones++;

                            System.out.print("Nueva Asignación: ");
                            System.out.print(solucion);
                            System.out.println("\nCoste: " + coste);
                            System.out.println("Iteracion " + iteraciones);
                        }else{
                            intercambio(i,j, solucion);
                        }
                    }
                    if(!improveFlag){
                        dlb.add(i,true);
                    }
                }

                if (i == lecturaMatrices.getTamProblema() -1){
                    i = 0;
                }
            }
        }
    }

    public ArrayList<Integer> intercambio(int i, int j, ArrayList<Integer> solucion){
        int aux = solucion.get(i);
        solucion.set(i,solucion.get(j));
        solucion.set(j, aux);
        return solucion;
    }

    public int calculoCoste(ArrayList<Integer> resultado, int r, int s, LecturaMatrices lecturaMatrices){
        int diferencia = 0;
        /**
         * Función del profesor
         */
        for (int k = 0; k < lecturaMatrices.getTamProblema(); k++) {
            if(k == r || k == s){
                diferencia += 0;
            }else{
                diferencia += (lecturaMatrices.getMatrizFlujoProductos()[r][k] * (lecturaMatrices.getMatrizDistancias()[resultado.get(s)][resultado.get(k)] - lecturaMatrices.getMatrizDistancias()[resultado.get(r)][resultado.get(k)]));
                diferencia += (lecturaMatrices.getMatrizFlujoProductos()[s][k] * (lecturaMatrices.getMatrizDistancias()[resultado.get(r)][resultado.get(k)] - lecturaMatrices.getMatrizDistancias()[resultado.get(s)][resultado.get(k)]));
            }
        }

        /**
         * Función del compañero
         */
//        for(int k = 0; k < lecturaMatrices.getMatrizFlujoProductos().length; k++){
//            if(k != r && k != s){
//                diferencia += 2 * (lecturaMatrices.getMatrizFlujoProductos()[k][r] - lecturaMatrices.getMatrizFlujoProductos()[k][s]) * (lecturaMatrices.getMatrizDistancias()[resultado.get(k)][resultado.get(s)] - lecturaMatrices.getMatrizDistancias()[resultado.get(k)][resultado.get(r)]);
//            }
//        }

        return diferencia;
    }

    public boolean mejoraConMovimiento(int i, int j, LecturaMatrices lecturaMatrices){
        ArrayList<Integer> conIntercambio = intercambio(i,j, solucion);
        int costeAntiguo = algGreedy.calcularCosteGreedy(solucion, lecturaMatrices);
        int costeNuevo = calculoCoste(solucion, i, j, lecturaMatrices);
        int costeMovimiento = costeNuevo - costeAntiguo;

        if(costeMovimiento < 0){
            coste += costeNuevo;
            return true;
        }

        return false;
    }

    public boolean mejoraNoEncontrada(){
        int cont = 0;
        for (int i = 0; i < dlb.size(); i++) {
            if(!dlb.get(i)){
                cont++;
            }
        }

        if(cont == dlb.size()){
            return true;
        }
        return false;
    }

}
