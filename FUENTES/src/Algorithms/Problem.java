package Algorithms;

import java.util.Scanner;
import java.io.File;

public class Problem {
    final int[][] flowMatrix;
    final int[][] distanceMatrix;
    final int size;

    public Problem(String filepath) throws Exception {

        Scanner input = new Scanner(new File(filepath));
        this.size = input.nextInt();
        input.nextLine();
        input.nextLine();

        this.flowMatrix = ReadMatrix(input, this.size);
        input.nextLine();

        this.distanceMatrix = ReadMatrix(input, this.size);
        input.close();
    }

    public int calculateCost(int[] assignations) {
        int cost = 0;
        for (int i = 0; i < this.size; ++i)
            for (int j = 0; j < this.size; ++j)
                cost += this.flowMatrix[i][j] * this.distanceMatrix[assignations[i]][assignations[j]];
        return cost;
    }

    private static int[][] ReadMatrix(Scanner input, int size) {

        int[][] matrix = new int[size][size];
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j)
                matrix[i][j] = input.nextInt();
        }
        return matrix;
    }
}
