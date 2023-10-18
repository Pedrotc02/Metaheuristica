package Utils;

import java.io.BufferedOutputStream;
import java.io.IOException;

import Algorithms.Algorithm.Solution;
import DataStructures.Pair;

public final class TerminalPrinter {

    private static TerminalPrinter instance;
    private final BufferedOutputStream outputStream;

    private static final String RESET = "\u001B[0m";
    private static final String GREEN = "\u001B[32m";
    private static final String RED = "\u001B[31m";
    private static final String PURPLE = "\u001B[35m";

    public static void printlnDebug(String message) {
        print(PURPLE + "[DEBUG]: " + RESET + message + "\n");
    }

    public static void printSwappedArray(int[] arr, Pair p) {
        for (int i = 0; i < arr.length; ++i) {
            if (i == p.first || i == p.second)
                print(GREEN + arr[i] + RESET + " ");
            else
                print(arr[i] + " ");
        }
    }

    public static void printError(String message) {
        System.out.println(RED + "[ERROR]: " + RESET + message);
    }

    public static void printSolution(String name, Solution solution) {
        print(name + ": ");
        printArray(solution.assignations);
        print("\nCoste: " + solution.cost + "\n");
    }

    public static void printSwappedSolution(String name, Solution solution, Pair swap) {
        print(name + ": ");
        printSwappedArray(solution.assignations, swap);
        print("\nCoste: " + solution.cost + "\n");
    }

    /**
     * Es necesario llamar a esta función al final de la ejecución del programa
     */
    public static void flush() {
        var singleton = getInstance();
        try {
            singleton.outputStream.flush();
        } catch (IOException e) {
            printError(e.getMessage());
        }
    }

    private static void printArray(int[] arr) {
        for (int i = 0; i < arr.length; ++i)
            print(arr[i] + " ");
    }

    private static synchronized TerminalPrinter getInstance() {
        if (instance == null) {
            instance = new TerminalPrinter();
        }
        return instance;
    }

    private static void print(String message) {
        var singleton = getInstance();
        try {
            singleton.outputStream.write(message.getBytes());
        } catch (IOException e) {
            printError(e.getMessage());
        }
    }

    private TerminalPrinter() {
        this.outputStream = new BufferedOutputStream(System.out);
    }
}
