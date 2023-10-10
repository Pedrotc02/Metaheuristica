import java.util.Random;

public class Taboo implements Algorithm {

    final int seed;
    final int maxIterations;

    /**
     * Número máximo de iteraciones que impliquen empeoramiento respecto al mejor
     * del momento anterior
     */
    final int threshold;

    public Taboo(int seed, int maxIterations, int percentage) {
        this.seed = seed;
        this.maxIterations = maxIterations;
        this.threshold = maxIterations * percentage / 100;
    }

    @Override
    public Algorithm.Solution Solve(Problem problem) {
        Solution solution = new Solution(problem.size, this.seed);
        solution.cost = problem.calculateCost(solution.assignations);

        Utils.printSolution("Asignación inicial", solution);

        Dlb dlb = new Dlb(problem.size);
        int iterations = 0;

        Random rand = new Random(this.seed);
        int randomInitialIndex = rand.nextInt(dlb.length);

        while (iterations < maxIterations) {
            if (dlb.AllActivated())
                dlb = new Dlb(problem.size, this.seed);

            for (int i = 0; i < dlb.length; ++i) {
                int first = (randomInitialIndex + i) % dlb.length;
                if (dlb.Get(first))
                    continue;

                boolean improve_flag = false;
                for (int j = 1; j < dlb.length; ++j) {

                    int second = (first + j) % dlb.length;

                    Utils.Pair swap = new Utils.Pair(first, second);
                    int diffCost = LocalSearch.calculateDiffCost(problem, solution, swap);

                    if (diffCost >= 0)
                        continue;

                    Utils.swapElements(solution.assignations, swap);
                    solution.cost += diffCost;

                    dlb.Set(first, false);
                    dlb.Set(second, false);

                    improve_flag = true;
                    Utils.printSwappedSolution("Asignación " + (iterations + 1), solution, swap);
                    iterations++;
                }
                if (!improve_flag)
                    dlb.Set(first, true);
            }
        }
        return solution;
    }

}
