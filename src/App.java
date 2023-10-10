public class App {

    public static void main(String[] args) {
        try {
            Config config = new Config();
            Algorithm.Solution solution = config.Solve();

            System.out.print("Asignación final: ");
            Utils.printArray(solution.assignations);
            System.out.println("\nCoste: " + solution.cost);
        } catch (Exception e) {
            Utils.printError(e.getMessage());
        }
    }
}
