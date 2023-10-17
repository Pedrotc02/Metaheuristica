package Algorithms;

import java.util.Random;

import DataStructures.CircularArray;
import DataStructures.Dlb;
import DataStructures.Pair;
import Utils.Print;
import Utils.Swap;

public class Taboo implements Algorithm {

    final int seed;
    final int maxIterations;
    final CircularArray<Solution> shortTerm;
    final int[][] longTerm;

    /**
     * Número máximo de iteraciones que impliquen empeoramiento respecto al mejor
     * del momento anterior
     */
    final int threshold;

    public Taboo(int seed, int maxIterations, int percentage, int memorySize) {
        this.seed = seed;
        this.maxIterations = maxIterations;
        this.threshold = maxIterations * percentage / 100;
        this.shortTerm = new CircularArray<>(memorySize);
        this.longTerm = new int[memorySize][memorySize];
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
            if (dlb.AllActivated()) {
                dlb = new Dlb(problem.size, this.seed);
                Pair p = getBestMovement(solution, problem);
                solution.applySwap(problem, p);
                iterations++;
                Print.printSwappedSolution("Asignación peor", solution, p);
            }

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

                    Swap.swapElements(solution.assignations, swap);
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

    private Pair getBestMovement(Solution solution, Problem problem) {

        Pair bestMovement = new Pair(0, 1);
        int bestDiffCost = LocalSearch.calculateDiffCost(problem, solution, bestMovement);

        for (int i = 0; i < solution.assignations.length; ++i) {
            for (int j = i + 1; j < solution.assignations.length; ++j) {

                Pair neighbour = new Pair(i, j);
                int neighbourCost = LocalSearch.calculateDiffCost(problem, solution, neighbour);

                if (neighbourCost < bestDiffCost) {
                    bestDiffCost = neighbourCost;
                    bestMovement = neighbour;
                }
            }
        }

        return bestMovement;
    }
}