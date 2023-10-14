package Algorithms;

import java.util.Random;

import DataStructures.CircularArray;
import DataStructures.Dlb;
import DataStructures.Pair;
import Utils.Print;

public class Taboo implements Algorithm {

    final int seed;
    final int maxIterations;
    final CircularArray<Solution> shortTerm;

    /**
     * Número máximo de iteraciones que impliquen empeoramiento respecto al mejor
     * del momento anterior
     */
    final int threshold;

    public Taboo(int seed, int maxIterations, int percentage, int shortTermSize) {
        this.seed = seed;
        this.maxIterations = maxIterations;
        this.threshold = maxIterations * percentage / 100;
        this.shortTerm = new CircularArray<>(shortTermSize);
    }

    @Override
    public Solution Solve(Problem problem) {
        Solution solution = new Solution(problem.size, this.seed);
        solution.cost = problem.calculateCost(solution.assignations);

        Print.printSolution("Asignación inicial", solution);

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

                    Pair swap = new Pair(first, second);
                    int diffCost = LocalSearch.calculateDiffCost(problem, solution, swap);

                    if (diffCost >= 0)
                        continue;

                    Print.swapElements(solution.assignations, swap);
                    solution.cost += diffCost;

                    dlb.Set(first, false);
                    dlb.Set(second, false);

                    improve_flag = true;
                    Print.printSwappedSolution("Asignación " + (iterations + 1), solution, swap);
                    iterations++;
                }
                if (!improve_flag)
                    dlb.Set(first, true);
            }
        }
        return solution;
    }
}