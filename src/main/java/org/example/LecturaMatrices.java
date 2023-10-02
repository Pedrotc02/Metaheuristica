package org.example;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class LecturaMatrices {
    Integer tamProblema;
    Integer[][] matrizFlujoProductos;
    Integer[][] contenidoMatrizDistancias;
    ArrayList<Integer> potencialFlujo = new ArrayList<>();
    ArrayList<Integer> potencialDistancias = new ArrayList<>();

    public LecturaMatrices(String ruta) {

        String linea;
        FileReader f = null;
        try {
            f = new FileReader(ruta);
            BufferedReader b = new BufferedReader(f);
            linea = b.readLine().trim();
            tamProblema = Integer.parseInt(linea);
            matrizFlujoProductos = new Integer[tamProblema][tamProblema];
            contenidoMatrizDistancias = new Integer[tamProblema][tamProblema];

            linea = b.readLine();
            linea = b.readLine();

            Integer fila = 0;
            while (!linea.isBlank()){
                String[] enteros = linea.split(" ");
                for (int i = 0; i < enteros.length; i++) {
                    matrizFlujoProductos[fila][i] = Integer.parseInt(enteros[i]);
                }
                fila++;
                linea = b.readLine();
            }

            fila = 0;
            linea = b.readLine();
            while (linea != null){
                String[] enterosSegundaMatriz = linea.split(" ");
                for (int i = 0; i < enterosSegundaMatriz.length; i++) {
                    contenidoMatrizDistancias[fila][i] = Integer.parseInt(enterosSegundaMatriz[i]);
                }
                fila++;
                linea = b.readLine();
            }

            b.close();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        rellenarVectores();
    }

    public void getMatrizFlujoProductosPantalla() {
        for (int i = 0; i < tamProblema; i++) {
            for (int j = 0; j < tamProblema; j++) {
                System.out.print(matrizFlujoProductos[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void getContenidoMatrizDistanciasPantalla() {
        for (int i = 0; i < tamProblema; i++) {
            for (int j = 0; j < tamProblema; j++) {
                System.out.print(contenidoMatrizDistancias[i][j] + " ");
            }
            System.out.println();
        }
    }

    public Integer[][] getMatrizFlujoProductos() {
        return matrizFlujoProductos;
    }

    public Integer[][] getMatrizDistancias() {
        return contenidoMatrizDistancias;
    }

    public int getTamProblema() {
        return tamProblema;
    }

    public void rellenarVectores(){
        int fila = 0;
        Integer sumatoriaFlujos = 0;
        Integer sumatoriaDistancias = 0;

        while (fila < getMatrizFlujoProductos().length){
            for (int i = 0; i < getMatrizFlujoProductos().length; i++) {
                sumatoriaFlujos += getMatrizFlujoProductos()[fila][i];
                sumatoriaDistancias += getMatrizDistancias()[fila][i];
            }

            potencialFlujo.add(fila, sumatoriaFlujos);
            potencialDistancias.add(fila, sumatoriaDistancias);
            fila++;
            sumatoriaFlujos = 0;
            sumatoriaDistancias = 0;
        }
    }

    public ArrayList<Integer> getPotencialFlujo() {
        return potencialFlujo;
    }

    public ArrayList<Integer> getPotencialDistancias() {
        return potencialDistancias;
    }
}
