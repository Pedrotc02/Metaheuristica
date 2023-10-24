import Algorithms.Algorithm.Solution;
import Utils.Printer;

public class App {

    public static void main(String[] args) {
        try {
            Config config = new Config();

            var start = System.nanoTime();
            Solution solution = config.Solve();
            var end = System.nanoTime();

            System.out.format("Tiempo de ejecución: %.3fms\n\n", (end - start) * 1e-6);

            Printer.printSolution("Solucion final", solution);
            Printer.printTerminalSolution("Solucion final", solution);
        } catch (Exception e) {
            Printer.printException(e);
        } finally {
            Printer.close();
        }
    }
}
