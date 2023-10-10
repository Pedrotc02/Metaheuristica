public class App {

    public static void main(String[] args) {
        try {
            Config config = new Config();
            Algorithm.Solution solution = config.Solve();
            Utils.printSolution("Asignación final", solution);
        } catch (Exception e) {
            Utils.printError(e.getMessage());
        }
    }
}
