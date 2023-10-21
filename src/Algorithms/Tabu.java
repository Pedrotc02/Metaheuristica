package Algorithms;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.TreeSet;
import java.util.stream.IntStream;

import DataStructures.CircularArray;
import DataStructures.Dlb;
import DataStructures.Pair;
import Utils.Array;
import Utils.TerminalPrinter;

public class Tabu implements Algorithm {

    final int maxIterations;
    final Random random;
    Dlb dlb;

    final Problem problem;
    TabuSolution currentSolution;

    CircularArray<Pair> tabuList;

    // Memoria largo plazo
    final int threshold;
    final int numEliteSolutions;
    int[][] memory;
    final TreeSet<TabuSolution> eliteSolutions;

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

        public TabuSolution(Solution other) {
            super(other);
            this.iterations = 0;
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

    public Tabu(int seed, int maxIterations, int percentage, int tabuDuration, int numEliteSolutions, Problem problem) {
        this.maxIterations = maxIterations;
        this.threshold = maxIterations * percentage / 100;
        this.tabuList = new CircularArray<>(tabuDuration);
        this.dlb = new Dlb(problem.size);
        this.random = new Random(seed);
        this.memory = new int[problem.size][problem.size];
        this.numEliteSolutions = numEliteSolutions;
        this.eliteSolutions = new TreeSet<>(Comparator.comparingInt((Solution s) -> s.cost));

        this.currentSolution = new TabuSolution(problem.size, random);
        currentSolution.cost = problem.calculateCost(currentSolution.assignations);

        this.problem = problem;
    }

    @Override
    public Solution Solve(Problem problem) {

        TerminalPrinter.printSolution("Solucion inicial", currentSolution);

        while (currentSolution.iterations < maxIterations) {

            TabuSolution previousLocalBest = new TabuSolution(currentSolution);

            eliteSolutions.add(previousLocalBest);
            if (eliteSolutions.size() > numEliteSolutions)
                eliteSolutions.remove(eliteSolutions.last());

            findLocalMaxima(problem, previousLocalBest);

            TerminalPrinter.printlnDebug("Reset dlb");
            dlb = new Dlb(problem.size, random);

            Pair swap = getBestMovement(currentSolution, problem);
            currentSolution.applySwap(problem, swap);
            updateMemory(swap);

        }

        return eliteSolutions.first();
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

                    if ((currentSolution.iterations - previousLocalBest.iterations) >= threshold
                            && currentSolution.cost >= previousLocalBest.cost) {
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

        TerminalPrinter.printlnDebug("Reinicializando con memoria a largo plazo");
        if (rand.nextBoolean()) {
            generateIntensifiedSolution(rand);
        } else {
            generateDiversifiedSolution(rand);
        }
        currentSolution.iterations++;
    }

    private void generateDiversifiedSolution(Random rand) {

        TabuSolution newSolution = new TabuSolution(problem.size);
        int randomInitialIndex = rand.nextInt(problem.size);
        for (int k = 0; k < problem.size; ++k) {

            int index = (randomInitialIndex + k) % problem.size;

            int value = IntStream.range(0, problem.size)
                    .filter(position -> !Array.contains(newSolution.assignations, position))
                    .boxed()
                    .min((i, j) -> Integer.compare(memory[randomInitialIndex][i], memory[randomInitialIndex][j]))
                    .orElse(-1);
            newSolution.assignations[index] = value;
        }

        newSolution.cost = problem.calculateCost(newSolution.assignations);
        newSolution.iterations = currentSolution.iterations;

        currentSolution = newSolution;
    }

    private void generateIntensifiedSolution(Random rand) {

        int randomIndex = rand.nextInt(eliteSolutions.size());
        var it = eliteSolutions.iterator();
        var newSolution = it.next();

        for (int i = 0; i < randomIndex; ++i)
            newSolution = it.next();

        eliteSolutions.remove(newSolution);

        newSolution.iterations = currentSolution.iterations;
        currentSolution = newSolution;
    }
}