package Algorithms;

import java.util.Arrays;
import java.util.stream.IntStream;

public class AlgGreedy_Clase02_Grupo14 implements Algorithm {

    @Override
    public Solution Solve(Problem problem) {

        Solution solution = new Solution(problem.size);

        int[] flow_sum = Arrays.stream(problem.flowMatrix).mapToInt(row -> Arrays.stream(row).sum()).toArray();
        int[] distance_sum = Arrays.stream(problem.distanceMatrix).mapToInt(row -> Arrays.stream(row).sum()).toArray();

        for (int k = 0; k < problem.size; ++k) {
            int max_index = IntStream.range(0, flow_sum.length)
                    .reduce((i, j) -> flow_sum[i] > flow_sum[j] ? i : j)
                    .orElse(-1);

            int min_index = IntStream.range(0, distance_sum.length)
                    .reduce((i, j) -> distance_sum[i] < distance_sum[j] ? i : j)
                    .orElse(-1);

            flow_sum[max_index] = Integer.MIN_VALUE;
            distance_sum[min_index] = Integer.MAX_VALUE;

            solution.assignations[max_index] = min_index;
        }

        solution.cost = problem.calculateCost(solution.assignations);

        return solution;
    }
}
