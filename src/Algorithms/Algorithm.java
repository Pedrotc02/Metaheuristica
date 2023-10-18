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

        public Solution(Solution other) {
            this.assignations = other.assignations.clone();
            this.cost = other.cost;
        }

        /**
         * Creates new solution with Fisher-Yates shuffle
         */
        public Solution(int size, Random random) {

            this.assignations = IntStream.range(0, size).toArray();
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
