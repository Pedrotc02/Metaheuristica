package Algorithms;

import java.util.Random;
import DataStructures.Dlb;
import DataStructures.Pair;
import Utils.Printer;
import Utils.Array;

public class AlgPMDLBrandom_Clase02_Grupo14 implements Algorithm {

    final Random random;
    final int maxIterations;
    final Dlb dlb;
    Solution currentSolution;

    public AlgPMDLBrandom_Clase02_Grupo14(int seed, int maxIterations, Problem problem) {
        this.random = new Random(seed);
        this.maxIterations = maxIterations;
        this.dlb = new Dlb(problem.size);
        this.currentSolution = new Solution(problem.size, random);
        currentSolution.cost = problem.calculateCost(currentSolution.assignations);
    }

    @Override
    public Solution Solve(Problem problem) {

        Printer.printSolution("Asignación inicial", currentSolution);

        int iterations = 0;

        int randomInitialIndex = random.nextInt(dlb.length);
        while (iterations < maxIterations && !dlb.AllActivated()) {

            for (int i = 0; i < dlb.length; ++i) {
                int first = (randomInitialIndex + i) % dlb.length;
                if (dlb.Get(first))
                    continue;

                boolean improve_flag = false;
                for (int j = 1; j < dlb.length; ++j) {

                    int second = (first + j) % dlb.length;

                    Pair swap = new Pair(first, second);
                    int diffCost = calculateDiffCost(problem, currentSolution, swap);

                    if (diffCost >= 0)
                        continue;

                    Array.swapElements(currentSolution.assignations, swap);
                    currentSolution.cost += diffCost;

                    dlb.Set(first, false);
                    dlb.Set(second, false);

                    improve_flag = true;
                    Printer.printSolution("Solucion " + (iterations + 1), currentSolution);
                    iterations++;
                }
                if (!improve_flag)
                    dlb.Set(first, true);
            }
        }
        return currentSolution;
    }

    public static int calculateDiffCost(Problem problem, Solution solution, Pair swap) {

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
