import java.util.Random;

public class LocalSearch implements Algorithm {

    final int seed;
    final int maxIterations;

    public LocalSearch(int seed, int maxIterations) {
        this.seed = seed;
        this.maxIterations = maxIterations;
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
        while (iterations < maxIterations && !dlb.AllActivated()) {

            for (int i = 0; i < dlb.length; ++i) {
                int first = (randomInitialIndex + i) % dlb.length;
                if (dlb.Get(first))
                    continue;

                boolean improve_flag = false;
                for (int j = 1; j < dlb.length; ++j) {

                    int second = (first + j) % dlb.length;

                    Utils.Pair swap = new Utils.Pair(first, second);
                    int diffCost = calculateDiffCost(problem, solution, swap);

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

    public static int calculateDiffCost(Problem problem, Algorithm.Solution solution, Utils.Pair swap) {

        int diff = 0;

        // Renombro las variables tal y como aparece en la fórmula
        int[][] f = problem.flowMatrix;
        int[][] d = problem.distanceMatrix;
        int r = swap.first;
        int s = swap.second;

        int elementR = solution.assignations[r];
        int elementS = solution.assignations[s];

        for (int k = 0; k < solution.assignations.length; ++k) {
            if (k != r && k != s) {
                int elementK = solution.assignations[k];
                diff += 2 * (f[k][r] - f[k][s]) * (d[elementK][elementS] - d[elementK][elementR]);
            }
        }
        return diff;
    }
}
