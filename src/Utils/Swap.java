package Utils;

import DataStructures.Pair;

public abstract class Swap {
    public static void swapElements(int[] arr, Pair p) {
        int temp = arr[p.first];
        arr[p.first] = arr[p.second];
        arr[p.second] = temp;
    }
}
