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
        System.out.println("Tamano del Problema: " + lm.getTamProblema());
        System.out.println("Matriz del flujo de productos entre cada par de unidades: ");
        lm.getMatrizFlujoProductosPantalla();

        System.out.println("Contenido de la matriz de distancias entre cada par de localizaciones: ");
        lm.getContenidoMatrizDistanciasPantalla();

        switch (config.getAlgoritmos()){
            case "Greedy":
                Greedy algGreedy = new Greedy();
                algGreedy.algoritmo(lm);
                break;

            case "Busqueda_Local":
                PrimeroMejor algBusquedaLocal = new PrimeroMejor(lm, config.getSemillas().get(0));
                algBusquedaLocal.dlb(lm, config.getParametrosExtra());
                break;
        }
    }
}