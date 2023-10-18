package Algorithms;

import java.util.Random;

import DataStructures.CircularArray;
import DataStructures.Dlb;
import DataStructures.Pair;
import Utils.TerminalPrinter;

public class Tabu implements Algorithm {

    final int seed;
    final int maxIterations;
    CircularArray<Pair> tabuList;
    int[][] memory;
    final int tabuDuration;

    /**
     * Número máximo de iteraciones que impliquen empeoramiento respecto al mejor
     * del momento anterior
     */
    final int threshold;

    public Tabu(int seed, int maxIterations, int percentage, int tabuDuration) {
        this.seed = seed;
        this.maxIterations = maxIterations;
        this.threshold = maxIterations * percentage / 100;
        this.tabuDuration = tabuDuration;
    }

    @Override
    public Solution Solve(Problem problem) {
        this.memory = new int[problem.size][problem.size];
        this.tabuList = new CircularArray<>(problem.size);

        Solution solution = new Solution(problem.size, this.seed);
        solution.cost = problem.calculateCost(solution.assignations);

        Solution localBestSolution = new Solution(solution);
        Solution globalBestSolution = localBestSolution;

        TerminalPrinter.printSolution("Solucion inicial", solution);

        Dlb dlb = new Dlb(problem.size);

        Random rand = new Random(this.seed);
        int randomInitialIndex = rand.nextInt(dlb.length);

        int worseIterations = 0;
        int iterations = 0;
        while (iterations < maxIterations) {

            if (worseIterations >= threshold) {
                // solution = reinitializeLongTermMemory();
            }

            if (dlb.AllActivated()) {
                dlb = new Dlb(problem.size, this.seed);
                Pair p = getBestMovement(solution, problem);
                solution.applySwap(problem, p);
                updateMemory(solution, p);
                TerminalPrinter.printSwappedSolution("Solucion peor", solution, p);
                iterations++;
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

                    solution.applySwap(problem, swap);

                    if (solution.cost < localBestSolution.cost) {
                        localBestSolution = new Solution(solution);
                        worseIterations = 0;
                        if (localBestSolution.cost < globalBestSolution.cost)
                            globalBestSolution = localBestSolution;
                    } else {
                        worseIterations++;
                    }

                    dlb.Set(first, false);
                    dlb.Set(second, false);

                    improve_flag = true;
                    TerminalPrinter.printSwappedSolution("Solucion " + (iterations + 1), solution, swap);
                    iterations++;
                }
                if (!improve_flag)
                    dlb.Set(first, true);
            }
        }
        return globalBestSolution;
    }

    private Pair getBestMovement(Solution solution, Problem problem) {

        Pair bestMovement = null;
        int bestDiffCost = Integer.MAX_VALUE;

        for (int i = 0; i < solution.assignations.length; ++i) {
            for (int j = i + 1; j < solution.assignations.length; ++j) {

                Pair neighbour = new Pair(i, j);
                if (tabuList.contains(neighbour))
                    continue;
                int neighbourCost = LocalSearch.calculateDiffCost(problem, solution, neighbour);

                if (neighbourCost < bestDiffCost) {
                    bestDiffCost = neighbourCost;
                    bestMovement = neighbour;
                }
            }
        }

        return bestMovement;
    }

    private void updateMemory(Solution solution, Pair p) {

        this.tabuList.add(p);

        for (int i = 0; i < memory.length; ++i)
            for (int j = i + 1; j < memory.length; ++j)
                if (memory[i][j] != 0)
                    memory[i][j]--;

        memory[p.first][p.second] = tabuDuration;

        memory[p.first][solution.assignations[p.first]]++;
        memory[p.second][solution.assignations[p.second]]++;

    }

    // private Solution reinitializeLongTermMemory() {
    // }
}