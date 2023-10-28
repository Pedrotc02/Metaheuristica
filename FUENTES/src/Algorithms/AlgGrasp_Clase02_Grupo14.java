package Algorithms;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.stream.IntStream;

import Algorithms.AlgTabu_Clase02_Grupo14.TabuSolution;
import Utils.Printer;

public class AlgGrasp_Clase02_Grupo14 implements Algorithm {

    public static class Parameters {
        public AlgTabu_Clase02_Grupo14.Parameters tabuParameters;
        public int greedySize;
        public int numExecutions;
    }

    private final Random random;
    private final Problem problem;
    private final int maxSize;

    private final Solution[] solutions;
    private final AlgTabu_Clase02_Grupo14.Parameters tabuParameters;

    public AlgGrasp_Clase02_Grupo14(Parameters p, Problem problem) {
        this.random = new Random(p.tabuParameters.seed);
        this.maxSize = p.greedySize;
        this.problem = problem;
        this.tabuParameters = p.tabuParameters;

        this.solutions = new Solution[p.numExecutions];
    }

    public Solution Solve(Problem problem) {

        for (int i = 0; i < solutions.length; ++i) {
            Printer.printlnDebug("Ejecución GRASP " + i);
            var tabuSolver = new AlgTabu_Clase02_Grupo14(this.tabuParameters, createGreedyRandomSolution(random),
                    this.problem);
            solutions[i] = new TabuSolution(tabuSolver.Solve(problem));
        }

        return Arrays.stream(solutions).min(Comparator.comparing((Solution sol) -> sol.cost)).get();
    }

    private TabuSolution createGreedyRandomSolution(Random random) {

        TabuSolution solution = new TabuSolution(problem.size);

        int[] flow_sum = Arrays.stream(problem.flowMatrix).mapToInt(row -> Arrays.stream(row).sum()).toArray();
        int[] distance_sum = Arrays.stream(problem.distanceMatrix).mapToInt(row -> Arrays.stream(row).sum()).toArray();

        // Coge un aleatorio entre los (10)maxSize mejores y lo asigna a la mejor
        // distancia(mínima)
        for (int k = 0; k < problem.size; ++k) {
            int max_index = getRandomMax(random, flow_sum);

            int min_index = IntStream.range(0, distance_sum.length)
                    .reduce((i, j) -> distance_sum[i] < distance_sum[j] ? i : j)
                    .getAsInt();

            flow_sum[max_index] = Integer.MIN_VALUE;
            distance_sum[min_index] = Integer.MAX_VALUE;

            solution.assignations[max_index] = min_index;
        }

        solution.cost = problem.calculateCost(solution.assignations);
        return solution;
    }

    private int getRandomMax(Random random, int[] flow_sum) {

        var array = IntStream.range(0, flow_sum.length).boxed().toArray();
        Arrays.sort(array, Comparator.comparingInt((i) -> flow_sum[(int) i]).reversed());

        var list = Arrays.stream(array).filter((i) -> flow_sum[(int) i] != (Integer.MIN_VALUE)).limit(maxSize).toList();
        return (int) list.get(random.nextInt(list.size()));
    }

}
