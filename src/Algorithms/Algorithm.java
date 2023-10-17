package Algorithms;

import java.util.Random;
import java.util.stream.IntStream;

import DataStructures.Pair;
import Utils.Swap;

public interface Algorithm {
    public class Solution {
        public int[] assignations;
        public int cost;

        public Solution(int size) {
            this.assignations = new int[size];
            this.cost = 0;
        }

        /**
         * Creates new solution from an existing one, applying the factorization
         * algorithm to calculate the cost
         * 
         * @param solution
         * @param problem
         * @param swap
         */
        // public Solution(Solution solution, Problem problem, Pair swap) {

        // this.assignations = solution.assignations.clone();
        // Swap.swapElements(assignations, swap);
        // this.cost = solution.cost + LocalSearch.calculateDiffCost(problem, solution,
        // swap);
        // }

        /**
         * Creates new solution with Fisher-Yates shuffle
         * 
         * @param size Size of problem
         * @param seed Seed for random generator
         */
        public Solution(int size, int seed) {

            this.assignations = IntStream.range(0, size).toArray();
            Random random = new Random(seed);
            for (int i = size - 1; i >= 0; --i)
                Swap.swapElements(this.assignations, new Pair(i, random.nextInt(i + 1)));

            this.cost = 0;
        }

        public void applySwap(Problem problem, Pair swap) {
            this.cost = this.cost + LocalSearch.calculateDiffCost(problem, this, swap);
            Swap.swapElements(assignations, swap);
        }
    }

    public abstract Solution Solve(Problem problem);
}
