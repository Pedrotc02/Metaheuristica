package org.example;

public class Main {
    public static void main(String[] args) {
        Configurador config = new Configurador(args[0]);

        System.out.println("Archivos: " + config.getArchivos());
        System.out.println("Semillas: " + config.getSemillas());
        System.out.println("Algoritmos: " + config.getAlgoritmos());
        System.out.println("Parametros Extra: " + config.getParametrosExtra());
        System.out.println();

        LecturaMatrices lm = new LecturaMatrices(config.getArchivos().get(0));
        System.out.println("Matriz del flujo de productos entre cada par de unidades: \n");
        lm.getMatrizFlujoProductosPantalla();

        System.out.println("Contenido de la matriz de distancias entre cada par de localizaciones: \n");
        lm.getContenidoMatrizDistanciasPantalla();

        System.out.println("Greedy");
        AlgoritmoGreedy algGreedy = new AlgoritmoGreedy();
        algGreedy.algoritmo(lm);
    }
}