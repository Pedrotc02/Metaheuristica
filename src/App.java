public class App {

    public static void main(String[] args) throws Exception {
        Problem problem = new Problem("./data/ford01.dat");
        Greedy solver = new Greedy();
        Algorithm.Solution solution = solver.Solve(problem);
        System.out.println(solution.value);
    }
}
