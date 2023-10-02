package org.example;

import java.util.ArrayList;

public class Utils {
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

}
