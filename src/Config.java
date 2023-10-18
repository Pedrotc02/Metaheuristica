import java.io.File;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Algorithms.*;
import Algorithms.Algorithm.Solution;

public class Config {

    static final String configFilePath = "./config.json";
    static final Pattern filepathPattern = Pattern.compile("\"archivo\":\\s*\"([^\"]*)\"");
    static final Pattern algorithmPattern = Pattern.compile("\"algoritmo\":\\s*\"([^\"]*)\"");
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
    }

    private Algorithm chooseAlgorithm(String algorithmType, Hashtable<String, Integer> properties, Problem problem)
            throws Exception {

        switch (algorithmType) {
            case "Greedy":
                return new Greedy();
            case "LocalSearch": {
                try {
                    int seed = properties.get("semilla");
                    int maxIterations = properties.get("maxIteraciones");
                    return new LocalSearch(seed, maxIterations, problem);
                } catch (Exception e) {
                    throw new Exception(
                            "Los parámetros del algoritmo LocalSearch deben ser \"semilla\" y \"maxIteraciones\".");
                }
            }
            case "Tabu": {
                try {
                    int seed = properties.get("semilla");
                    int maxIterations = properties.get("maxIteraciones");
                    int percentage = properties.get("porcentajeReinicializacion");
                    int tabuDuration = properties.get("tenenciaTabu");
                    return new Tabu(seed, maxIterations, percentage, tabuDuration, problem);
                } catch (Exception e) {
                    throw new Exception(
                            "Los parámetros del algoritmo Tabu deben ser \"semilla\", \"maxIteraciones\", \"porcentajeReinicializacion\" y \"tenenciaTabu\".");
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