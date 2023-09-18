package org.example;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Configurador {
    ArrayList<String> archivos;
    ArrayList<String> algoritmos;
    ArrayList<Long> semillas;
    Integer parametrosExtra;

    public Configurador(String ruta) {
        archivos = new ArrayList<>();
        algoritmos = new ArrayList<>();
        semillas = new ArrayList<>();

        String linea;
        FileReader f = null;
        try {
            f = new FileReader(ruta);
            BufferedReader b = new BufferedReader(f);
            while ((linea = b.readLine()) != null){
                String[] split = linea.split("=");
                switch (split[0]){
                    case "Archivos":
                        String[] vArchivos = split[1].split(" ");
                        for (int i = 0; i < vArchivos.length; i++){
                            archivos.add(vArchivos[i]);
                        }
                        break;

                    case "Semillas":
                        String[] vSemillas = split[1].split(" ");
                        for (int i = 0; i < vSemillas.length; i++){
                            semillas.add(Long.parseLong(vSemillas[i]));
                        }
                        break;

                    case "Algoritmos":
                        String[] vAlgoritmos = split[1].split(" ");
                        for (int i = 0; i < vAlgoritmos.length; i++){
                            algoritmos.add(vAlgoritmos[i]);
                        }
                        break;

                    case "OtrosParametros":
                        parametrosExtra = Integer.parseInt(split[1]);
                        break;
                }
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<String> getArchivos() {
        return archivos;
    }

    public ArrayList<String> getAlgoritmos() {
        return algoritmos;
    }

    public ArrayList<Long> getSemillas() {
        return semillas;
    }

    public Integer getParametrosExtra() {
        return parametrosExtra;
    }
}
