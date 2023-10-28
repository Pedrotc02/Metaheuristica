package Utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import Algorithms.Algorithm.Solution;

public final class Printer {

    private static Printer instance;
    private PrintWriter printer;

    private static final String RED = "\u001B[31m";
    private static final String RESET = "\u001B[0m";

    public static void init(String filepath) throws IOException {
        Printer.instance = new Printer(filepath);
    }

    public static void printlnDebug(String message) {
        print("[DEBUG]: " + message + "\n");
    }

    public static void printException(Exception message) {
        System.out.println(RED + "[ERROR]: " + RESET + message.getMessage());
    }

    public static void printSolution(String name, Solution solution) {
        print(name + ": ");
        printArray(solution.assignations);
        print("\nCoste: " + solution.cost + "\n");
    }

    public static void printTerminalSolution(String name, Solution solution) {
        System.out.print(name + ": ");
        printTerminalArray(solution.assignations);
        System.out.print("\nCoste: " + solution.cost + "\n");
    }

    /**
     * Es necesario llamar a esta función al final de la ejecución del programa
     */
    public static void close() {
        if (instance != null)
            instance.printer.close();
    }

    private static void printArray(int[] arr) {
        for (int i = 0; i < arr.length; ++i)
            print(arr[i] + " ");
    }

    private static void printTerminalArray(int[] arr) {
        for (int i = 0; i < arr.length; ++i)
            System.out.print(arr[i] + " ");
    }

    private static void print(String message) {
        instance.printer.write(message);
    }

    private Printer(String filepath) throws IOException {

        File file = new File(filepath);
        file.getParentFile().mkdir();
        this.printer = new PrintWriter(new BufferedWriter(new FileWriter(file)));
    }
}
