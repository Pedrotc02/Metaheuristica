package Utils;

import Algorithms.Algorithm.Solution;
import DataStructures.Pair;

public abstract class Print {

    private static final String RESET = "\u001B[0m";
    private static final String GREEN = "\u001B[32m";
    private static final String RED = "\u001B[31m";

    static void printArray(int[] arr) {
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

    public static void printError(String message) {
        System.out.println(RED + "[ERROR]: " + RESET + message);
    }

    public static void printSolution(String name, Solution solution) {
        System.out.print(name + ": ");
        Print.printArray(solution.assignations);
        System.out.println("\nCoste: " + solution.cost);
    }

    public static void printSwappedSolution(String name, Solution solution, Pair swap) {
        System.out.print(name + ": ");
        Print.printSwappedArray(solution.assignations, swap);
        System.out.println("\nCoste: " + solution.cost);
    }

    public static void swapElements(int[] arr, Pair p) {
        int temp = arr[p.first];
        arr[p.first] = arr[p.second];
        arr[p.second] = temp;
    }
}
