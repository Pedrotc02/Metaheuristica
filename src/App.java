import Algorithms.Algorithm.Solution;
import Utils.Printer;

public class App {

    public static void main(String[] args) {
        try {
            Config config = new Config();
            Solution solution = config.Solve();
            Printer.printSolution("Solucion final", solution);
        } catch (Exception e) {
            Printer.printException(e);
        } finally {
            Printer.close();
        }
    }
}
