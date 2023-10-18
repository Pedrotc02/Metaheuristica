package Algorithms;

import java.util.Random;
import DataStructures.CircularArray;
import DataStructures.Dlb;
import DataStructures.Pair;
import Utils.TerminalPrinter;

public class Tabu implements Algorithm {

    final int maxIterations;
    CircularArray<Pair> tabuList;
    int[][] memory;
    final Random random;
    Dlb dlb;

    /**
     * Número máximo de iteraciones que impliquen empeoramiento respecto al mejor
     * del momento anterior
     */
    final int threshold;

    final TabuSolution currentSolution;
    Solution globalBest;

    private class TabuSolution extends Solution {
        int iterations;

        public TabuSolution(int size) {
            super(size);
            this.iterations = 0;
        }

        public TabuSolution(TabuSolution other) {
            super(other);
            this.iterations = other.iterations;
        }

        public TabuSolution(int size, Random random) {
            super(size, random);
            this.iterations = 0;
        }

        public void applySwap(Problem problem, Pair swap) {
            super.applySwap(problem, swap);
            this.iterations++;
            TerminalPrinter.printSwappedSolution("Solucion " + iterations, this, swap);
        }
    }

    public Tabu(int seed, int maxIterations, int percentage, int tabuDuration, Problem problem) {
        this.maxIterations = maxIterations;
        this.threshold = maxIterations * percentage / 100;
        this.tabuList = new CircularArray<>(tabuDuration);
        this.dlb = new Dlb(problem.size);
        this.random = new Random(seed);
        this.memory = new int[problem.size][problem.size];

        this.currentSolution = new TabuSolution(problem.size, random);
        currentSolution.cost = problem.calculateCost(currentSolution.assignations);

        this.globalBest = new Solution(currentSolution);
    }

    @Override
    public Solution Solve(Problem problem) {

        TerminalPrinter.printSolution("Solucion inicial", currentSolution);

        while (currentSolution.iterations < maxIterations) {

            TabuSolution previousLocalBest = new TabuSolution(currentSolution);
            findLocalMaxima(problem, previousLocalBest);

            if (currentSolution.cost < globalBest.cost)
                globalBest = new Solution(currentSolution);

            TerminalPrinter.printlnDebug("Reset dlb");
            dlb = new Dlb(problem.size, random);

            Pair swap = getBestMovement(currentSolution, problem);
            currentSolution.applySwap(problem, swap);
            updateMemory(swap);

        }
        return globalBest;
    }

    private void findLocalMaxima(Problem problem, TabuSolution previousLocalBest) {

        int randomInitialIndex = random.nextInt(dlb.length);
        while (!dlb.AllActivated()) {
            for (int i = 0; i < dlb.length; ++i) {
                int first = (randomInitialIndex + i) % dlb.length;
                if (dlb.Get(first))
                    continue;

                boolean improve_flag = false;
                for (int j = 1; j < dlb.length; ++j) {

                    int second = (first + j) % dlb.length;

                    Pair swap = new Pair(first, second);
                    if (tabuList.contains(swap))
                        continue;

                    int diffCost = LocalSearch.calculateDiffCost(problem, currentSolution, swap);
                    if (diffCost >= 0)
                        continue;

                    currentSolution.applySwap(problem, swap);
                    updateMemory(swap);

                    if ((currentSolution.iterations - previousLocalBest.iterations) >= threshold) {
                        reinitializeLongTermMemory(random);
                        return;
                    }

                    dlb.Set(first, false);
                    dlb.Set(second, false);

                    improve_flag = true;
                }
                if (!improve_flag)
                    dlb.Set(first, true);
            }
        }
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

    private void updateMemory(Pair p) {

        this.tabuList.add(p);

        memory[p.first][currentSolution.assignations[p.first]]++;
        memory[p.second][currentSolution.assignations[p.second]]++;
    }

    private void reinitializeLongTermMemory(Random rand) {

        TerminalPrinter.printlnDebug("Hay que resetear");
        if (rand.nextBoolean()) {
            // Intensificación

        } else {
            // Diversificación

        }
    }
}