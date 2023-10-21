package Utils;

import DataStructures.Pair;

public abstract class Array {
    public static void swapElements(int[] arr, Pair p) {
        int temp = arr[p.first];
        arr[p.first] = arr[p.second];
        arr[p.second] = temp;
    }

    public static boolean contains(int[] arr, int element) {
        for (var x : arr)
            if (x == element)
                return true;
        return false;
    }
}
