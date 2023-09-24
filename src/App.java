public class App {

    public static void main(String[] args) throws Exception {
        Problem problem = new Problem("./data/ford01.dat");
        Algorithm solver = new Greedy();
        Algorithm.Solution solution = solver.Solve(problem);

        System.out.print("Asignaciones: ");
        printlnArray(solution.assignations);
        System.out.println("Valor: " + solution.value);
    }

    static void printlnArray(int[] arr) {
        for (int i = 0; i < arr.length; ++i) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }
}
