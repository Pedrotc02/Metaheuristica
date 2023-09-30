import java.util.BitSet;

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

        Dlb dlb = new Dlb(problem.size);
        int iterations = 0;

        while (true) {
            // TODO: Hay que hacer la primera iteración de i sea aleatoria con base en la
            // semilla
            for (int i = 0; i < dlb.length; ++i) {
                if (dlb.Get(i))
                    continue;

                boolean improve_flag = false;
                for (int j = i + 1; j < dlb.length; ++j) {

                    if (iterations >= maxIterations || dlb.AllActivated())
                        return solution;

                    Utils.Pair swap = new Utils.Pair(i, j);
                    int diffCost = calculateDiffCost(problem, solution, swap);
                    if (diffCost >= 0)
                        continue;

                    Utils.swapElements(solution.assignations, swap);
                    solution.cost += diffCost;

                    dlb.Set(i, false);
                    dlb.Set(j, false);

                    improve_flag = true;

                    System.out.print("Nueva Asignación: ");
                    Utils.printSwappedArray(solution.assignations, swap);
                    System.out.println("\nCoste: " + solution.cost);

                    iterations++;
                }
                if (!improve_flag)
                    dlb.Set(i, true);
            }
        }
    }

    private int calculateDiffCost(Problem problem, Algorithm.Solution solution, Utils.Pair swap) {

        int diff = 0;

        // Renombro las variables tal y como aparece en la fórmula
        int[][] f = problem.flowMatrix;
        int[][] d = problem.distanceMatrix;
        int r = swap.first;
        int s = swap.second;

        int elementR = solution.assignations[r];
        int elementS = solution.assignations[s];

        for (int k = 0; k < solution.assignations.length; ++k) {
            if (k == r || k == s)
                continue;
            int elementK = solution.assignations[k];

            diff += 2 * (f[k][r] - f[k][s]) * (d[elementK][elementS] - d[elementK][elementR]);
        }
        return diff;
    }

    private class Dlb {
        private BitSet bitset;
        final int length;

        public Dlb(int size) {
            this.bitset = new BitSet(size);
            this.length = size;
        }

        public boolean Get(int index) {
            return bitset.get(index);
        }

        public void Set(int index, boolean value) {
            bitset.set(index, value);
        }

        public boolean AllActivated() {
            int count = 0;
            for (int i = 0; i < length; ++i) {
                if (!Get(i))
                    count++;
            }
            return count <= 1;
        }
    }
}
