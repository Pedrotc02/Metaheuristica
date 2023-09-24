import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Problem {
    final int[][] flowMatrix;
    final int[][] distanceMatrix;
    final int size;

    public Problem(String filepath) throws FileNotFoundException {

        Scanner input = new Scanner(new File(filepath));
        this.size = input.nextInt();
        input.nextLine();
        input.nextLine();

        this.flowMatrix = ReadMatrix(input, this.size);
        input.nextLine();

        this.distanceMatrix = ReadMatrix(input, this.size);
        input.close();
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
