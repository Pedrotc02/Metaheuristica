import java.io.File;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Algorithms.*;
import Algorithms.Algorithm.Solution;
import Utils.Printer;

public class Config {

    static final String configFilePath = "./config.json";
    static final Pattern filepathPattern = Pattern.compile("\"archivo\":\\s*\"([^\"]*)\"");
    static final Pattern algorithmPattern = Pattern.compile("\"algoritmo\":\\s*\"([^\"]*)\"");
    static final Pattern logFilePattern = Pattern.compile("\"log\":\\s*\"([^\"]*)\"");
    static final Pattern propertiesPattern = Pattern
            .compile("\"propiedades\":\\s*\\{\\s*((\"[^\"]*\":\\s*[^,}\\s]+,?\\s*)+)\\}");

    Problem problem;
    Algorithm algorithm;

    public Config() throws Exception {
        Scanner sc = new Scanner(new File(configFilePath));
        StringBuilder sb = new StringBuilder();
        while (sc.hasNextLine())
            sb.append(sc.nextLine());
        sc.close();
        String config = sb.toString();

        String problemFilePath = readField(filepathPattern, config);
        this.problem = new Problem(problemFilePath);

        var properties = readAlgorithmProperties(config);
        String algorithmType = readField(algorithmPattern, config);

        this.algorithm = chooseAlgorithm(algorithmType, properties, problem);

        String logFilename = "./logs/log" + algorithmType + "_";
        int index = 0;
        while (new File(logFilename + index + ".txt").exists())
            index++;

        logFilename += index + ".txt";

        try {
            Printer.init(logFilename);
        } catch (Exception e) {
            throw new Exception(
                    "El campo \"log\" debe contener el nombre del archivo donde se guarden los logs durante la ejecución del programa.\n");
        }
        System.out.println("Ejecutando " + algorithmType + " sobre el problema " + problemFilePath + ".");
        System.out.println("El archivo log se encuentra en " + logFilename);
    }

    private Algorithm chooseAlgorithm(String algorithmType, Hashtable<String, Integer> properties, Problem problem)
            throws Exception {

        switch (algorithmType) {
            case "Greedy":
                return new Greedy();
            case "PMDLBrandom": {
                try {
                    int seed = properties.get("semilla");
                    int maxIterations = properties.get("maxIteraciones");
                    return new LocalSearch(seed, maxIterations, problem);
                } catch (Exception e) {
                    throw new Exception(
                            "Los parámetros del algoritmo PMDLBrandom deben ser \"semilla\" y \"maxIteraciones\".");
                }
            }
            case "TabuMar": {
                try {
                    var p = new Tabu.Parameters();
                    p.seed = properties.get("semilla");
                    p.maxIterations = properties.get("maxIteraciones");
                    p.percentage = properties.get("porcentajeReinicializacion");
                    p.tabuDuration = properties.get("tenenciaTabu");
                    p.numEliteSolutions = properties.get("numSolucionesElite");
                    return new Tabu(p, problem);
                } catch (Exception e) {
                    throw new Exception(
                            "Los parámetros del algoritmo TabuMar deben ser \"semilla\", \"maxIteraciones\", \"porcentajeReinicializacion\", \"numSolucionesElite\" y \"tenenciaTabu\".");
                }
            }
            case "Grasp": {
                try {
                    var p = new Grasp.Parameters();
                    p.tabuParameters = new Tabu.Parameters();
                    p.greedySize = properties.get("tamañoGreedy");
                    p.numExecutions = properties.get("numEjecuciones");
                    p.tabuParameters.seed = properties.get("semilla");
                    p.tabuParameters.maxIterations = properties.get("maxIteraciones");
                    p.tabuParameters.percentage = properties.get("porcentajeReinicializacion");
                    p.tabuParameters.tabuDuration = properties.get("tenenciaTabu");
                    p.tabuParameters.numEliteSolutions = properties.get("numSolucionesElite");
                    return new Grasp(p, problem);
                } catch (Exception e) {
                    throw new Exception(
                            "Los parámetros del algoritmo Grasp deben ser \"semilla\", \"tamañoGreedy\", \"numEjecuciones\", \"maxIteraciones\", \"porcentajeReinicializacion\", \"numSolucionesElite\" y \"tenenciaTabu\".");
                }
            }
            default:
                throw new Exception("El tipo de algoritmo " + algorithmType + " no está reconocido.");
        }
    }

    public Solution Solve() {
        return this.algorithm.Solve(this.problem);
    }

    private String readField(Pattern pattern, String input) throws Exception {
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            throw new Exception();
        }
    }

    private Hashtable<String, Integer> readAlgorithmProperties(String input) throws Exception {

        var table = new Hashtable<String, Integer>();
        Matcher matcher = propertiesPattern.matcher(input);

        if (matcher.find()) {
            String propertiesContent = matcher.group(1);
            String[] keyValuePairs = propertiesContent.split(",\\s*");

            for (String pair : keyValuePairs) {
                var entry = pair.split(":");
                var key = entry[0].trim().substring(1, entry[0].length() - 1);
                try {
                    var value = Integer.parseInt(entry[1].trim());
                    table.put(key, value);
                } catch (Exception e) {
                    throw new Exception("El valor de " + key + " debe ser un número entero.");
                }
            }
        }

        return table;
    }
}