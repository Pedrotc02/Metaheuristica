import java.io.File;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Config {

    static final String configFilePath = "./config.json";
    static final Pattern filepathPattern = Pattern.compile("\"filepath\":\\s*\"([^\"]*)\"");
    static final Pattern algorithmPattern = Pattern.compile("\"algorithm\":\\s*\"([^\"]*)\"");
    static final Pattern propertiesPattern = Pattern
            .compile("\"properties\":\\s*\\{\\s*((\"[^\"]*\":\\s*[^,}\\s]+,?\\s*)+)\\}");

    Problem problem;
    Algorithm algorithm;

    // TODO: Faltan exceptions para cuando falta algún parámetro o tiene un valor
    // que no corresponde.
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

        this.algorithm = chooseAlgorithm(algorithmType, properties);
    }

    private Algorithm chooseAlgorithm(String algorithmType, Hashtable<String, String> properties) throws Exception {

        switch (algorithmType) {
            case "Greedy":
                return new Greedy();
            case "LocalSearch": {
                int seed = Integer.parseInt(properties.get("seed"));
                int maxIterations = Integer.parseInt(properties.get("maxIterations"));
                return new LocalSearch(seed, maxIterations);
            }
            // case "Taboo": {
            // }
            default:
                throw new Exception();
        }
    }

    public Algorithm.Solution Solve() {
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

    private Hashtable<String, String> readAlgorithmProperties(String input) throws Exception {

        var table = new Hashtable<String, String>();
        Matcher matcher = propertiesPattern.matcher(input);

        if (matcher.find()) {
            String propertiesContent = matcher.group(1);

            String[] keyValuePairs = propertiesContent.split(",\\s*");

            for (String pair : keyValuePairs) {
                var entry = pair.split(":");
                var key = entry[0].trim().substring(1, entry[0].length() - 1);
                var value = entry[1].trim();

                table.put(key, value);
            }
        }

        return table;
    }
}