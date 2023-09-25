public class LocalSearch implements Algorithm {

    final int seed;
    final int maxIterations;

    public LocalSearch(int seed, int maxIterations) {
        this.seed = seed;
        this.maxIterations = maxIterations;
    }

    public Algorithm.Solution Solve(Problem problem) {
        Solution solution = new Solution(problem.size, this.seed);
        solution.cost = problem.calculateCost(solution.assignations);

        System.out.print("Asignación inicial: ");
        Utils.printArray(solution.assignations);
        System.out.println("\nCoste: " + solution.cost);

        // TODO: Camiar array de bool por bits?
        boolean[] dlb = new boolean[problem.size];
        int iterations = 0;

        while (true) {
            for (int i = 0; i < dlb.length; ++i) {
                if (dlb[i])
                    continue;

                boolean improve_flag = false;
                for (int j = 0; j < dlb.length; ++j) {

                    if (iterations >= maxIterations || allBitsActivated(dlb))
                        return solution;

                    Utils.Pair swap = new Utils.Pair(i, j);
                    int diffCost = calculateDiffCost(problem, solution, swap);
                    if (diffCost >= 0)
                        continue;

                    Utils.swapElements(solution.assignations, swap);
                    solution.cost += diffCost;
                    dlb[i] = dlb[j] = false;
                    improve_flag = true;

                    System.out.print("Nueva Asignación: ");
                    Utils.printSwappedArray(solution.assignations, swap);
                    System.out.println("\nCoste: " + solution.cost);

                    iterations++;
                }
                if (!improve_flag)
                    dlb[i] = true;
            }
        }
    }

    private int calculateDiffCost(Problem problem, Algorithm.Solution solution, Utils.Pair swap) {

        // TODO: Ahora mismo utiliza el algoritmo clásico para calcular el coste, hay
        // que arreglar el algoritmo de factorización
        boolean usingFactorization = false;
        if (!usingFactorization) {
            Solution newSolution = new Solution(solution);
            Utils.swapElements(newSolution.assignations, swap);
            return problem.calculateCost(newSolution.assignations) - solution.cost;
        } else {
            int diff = 0;

            // Renombro las variables tal y como aparece en la fórmula
            int[][] f = problem.flowMatrix;
            int[][] d = problem.distanceMatrix;
            int r = solution.assignations[swap.first];
            int s = solution.assignations[swap.second];

            for (int k = 0; k < problem.size; ++k) {

                if (k == r || k == s) {
                    continue;
                }

                diff += f[r][k] * (d[s][k] - d[r][k])
                        + f[s][k] * (d[r][k] - d[s][k])
                        + f[k][r] * (d[k][s] - d[k][r])
                        + f[k][s] * (d[k][r] - d[k][s]);
            }

            return diff;
        }
    }

    private boolean allBitsActivated(boolean[] dlb) {
        int count = 0;
        for (boolean b : dlb)
            if (!b)
                count++;
        return count == 1;
    }
}
