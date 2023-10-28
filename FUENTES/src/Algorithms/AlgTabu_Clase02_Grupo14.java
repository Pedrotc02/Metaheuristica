package Algorithms;

import java.util.Comparator;
import java.util.Random;
import java.util.TreeSet;
import java.util.stream.IntStream;

import DataStructures.CircularArray;
import DataStructures.Dlb;
import DataStructures.Pair;
import Utils.Array;
import Utils.Printer;

public class AlgTabu_Clase02_Grupo14 implements Algorithm {

    public static class Parameters {
        public int seed;
        public int maxIterations;
        public int percentage;
        public int tabuDuration;
        public int numEliteSolutions;
    }

    private final int maxIterations;
    private final Random random;
    private Dlb dlb;

    private final Problem problem;
    private TabuSolution currentSolution;

    private CircularArray<Pair> tabuList;

    // Memoria largo plazo
    private final int threshold;
    private final int numEliteSolutions;
    private int[][] memory;
    private final TreeSet<TabuSolution> eliteSolutions;

    public static class TabuSolution extends Solution {
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
            Printer.printSolution("Solucion " + iterations, this);
        }
    }

    public AlgTabu_Clase02_Grupo14(Parameters p, Problem problem) {
        this.maxIterations = p.maxIterations;
        this.threshold = p.maxIterations * p.percentage / 100;
        this.tabuList = new CircularArray<>(p.tabuDuration);
        this.dlb = new Dlb(problem.size);
        this.random = new Random(p.seed);
        this.memory = new int[problem.size][problem.size];
        this.numEliteSolutions = p.numEliteSolutions;
        this.eliteSolutions = new TreeSet<>(Comparator.comparingInt((Solution s) -> s.cost));

        this.currentSolution = new TabuSolution(problem.size, random);
        currentSolution.cost = problem.calculateCost(currentSolution.assignations);

        this.problem = problem;
    }

    public AlgTabu_Clase02_Grupo14(Parameters p, TabuSolution initialSolution, Problem problem) {
        this.maxIterations = p.maxIterations;
        this.threshold = p.maxIterations * p.percentage / 100;
        this.tabuList = new CircularArray<>(p.tabuDuration);
        this.dlb = new Dlb(problem.size);
        this.random = new Random(p.seed);
        this.memory = new int[problem.size][problem.size];
        this.numEliteSolutions = p.numEliteSolutions;
        this.eliteSolutions = new TreeSet<>(Comparator.comparingInt((Solution s) -> s.cost));

        this.currentSolution = initialSolution;
        this.problem = problem;
    }

    @Override
    public Solution Solve(Problem problem) {

        Printer.printSolution("Solucion inicial", currentSolution);

        while (currentSolution.iterations < maxIterations) {

            TabuSolution previousLocalBest = new TabuSolution(currentSolution);

            eliteSolutions.add(previousLocalBest);
            if (eliteSolutions.size() > numEliteSolutions)
                eliteSolutions.remove(eliteSolutions.last());

            findLocalMaxima(problem, previousLocalBest);

            Printer.printlnDebug("Reset dlb");
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

                    int diffCost = AlgPMDLBrandom_Clase02_Grupo14.calculateDiffCost(problem, currentSolution, swap);
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

                int neighbourCost = AlgPMDLBrandom_Clase02_Grupo14.calculateDiffCost(problem, solution, neighbour);
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

        Printer.printlnDebug("Reinicializando con memoria a largo plazo");
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