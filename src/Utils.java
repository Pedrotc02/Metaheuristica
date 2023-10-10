public class Utils {

    private static final String RESET = "\u001B[0m";
    private static final String GREEN = "\u001B[32m";
    private static final String RED = "\u001B[31m";

    static void printArray(int[] arr) {
        for (int i = 0; i < arr.length; ++i)
            System.out.print(arr[i] + " ");
    }

    static void printArray(boolean[] arr) {
        for (int i = 0; i < arr.length; ++i)
            System.out.print(arr[i] + " ");
    }

    static void printSwappedArray(int[] arr, Pair p) {
        for (int i = 0; i < arr.length; ++i) {
            if (i == p.first || i == p.second)
                System.out.print(GREEN + arr[i] + RESET + " ");
            else
                System.out.print(arr[i] + " ");
        }
    }

    static void printError(String message) {
        System.out.println(RED + "[ERROR]: " + RESET + message);
    }

    static void printSolution(String name, Algorithm.Solution solution) {
        System.out.print(name + ": ");
        Utils.printArray(solution.assignations);
        System.out.println("\nCoste: " + solution.cost);
    }

    static void printSwappedSolution(String name, Algorithm.Solution solution, Pair swap) {
        System.out.print(name + ": ");
        Utils.printSwappedArray(solution.assignations, swap);
        System.out.println("\nCoste: " + solution.cost);
    }

    static void swapElements(int[] arr, Pair p) {
        int temp = arr[p.first];
        arr[p.first] = arr[p.second];
        arr[p.second] = temp;
    }

    public static class Pair {
        int first;
        int second;

        Pair(int first, int second) {
            this.first = first;
            this.second = second;
        }
    }
}
