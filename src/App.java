import Algorithms.Algorithm.Solution;
import Utils.TerminalPrinter;

public class App {

    public static void main(String[] args) {
        try {
            Config config = new Config();
            Solution solution = config.Solve();
            TerminalPrinter.printSolution("Solucion final", solution);
        } catch (Exception e) {
            TerminalPrinter.printError(e.getMessage());
        } finally {
            TerminalPrinter.flush();
        }
    }
}
