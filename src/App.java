public class App {

    public static void main(String[] args) throws Exception {
        Problem problem = new Problem("./data/ford01.dat");
        Algorithm solver = new LocalSearch(12345678, 1_000);
        Algorithm.Solution solution = solver.Solve(problem);

        Config config = new Config("./config.txt");

        System.out.print("Asignación final: ");
        Utils.printArray(solution.assignations);
        System.out.println("\nCoste: " + solution.cost);
    }
}
