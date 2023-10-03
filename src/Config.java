import java.io.File;
import java.util.Scanner;

public class Config {
    Problem problem;
    Algorithm algorithm;
    int seed;

    public Config(String configFilePath) throws Exception {

        Scanner input = new Scanner(new File(configFilePath));

        String problemPath = input.nextLine().split("\"")[1];
        this.problem = new Problem(problemPath);

        input.close();
    }
}
