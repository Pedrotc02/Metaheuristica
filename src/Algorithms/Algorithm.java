package Algorithms;

import java.util.Random;
import java.util.stream.IntStream;

import DataStructures.Pair;
import Utils.Print;

public interface Algorithm {
    public class Solution {
        public int[] assignations;
        public int cost;

        public Solution(int size) {
            this.assignations = new int[size];
            this.cost = 0;
        }

        public Solution(Solution solution) {
            this.assignations = solution.assignations.clone();
            this.cost = solution.cost;
        }

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
                Print.swapElements(this.assignations, new Pair(i, random.nextInt(i + 1)));

            this.cost = 0;
        }
    }

    public abstract Solution Solve(Problem problem);
}
