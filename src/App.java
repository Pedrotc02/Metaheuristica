import Algorithms.Algorithm.Solution;
import Utils.Print;

public class App {

    public static void main(String[] args) {
        try {
            Config config = new Config();
            Solution solution = config.Solve();
            Print.printSolution("Asignación final", solution);
        } catch (Exception e) {
            Print.printError(e.getMessage());
        }
    }
}
