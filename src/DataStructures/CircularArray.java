package DataStructures;

import java.util.Arrays;

public class CircularArray<T> {
    private int insertionIndex;
    private final T[] array;

    // Necesario para crear un array utilizando Generics
    @SuppressWarnings("unchecked")
    public CircularArray(int capacity) {
        this.insertionIndex = 0;
        this.array = (T[]) new Object[capacity];
    }

    public int size() {
        return array.length;
    }

    public void add(T element) {
        array[insertionIndex] = element;
        insertionIndex = (insertionIndex + 1) % array.length;
    }

    public T get(int index) {
        return array[index];
    }

    public boolean contains(T element) {
        return Arrays.stream(array)
                .filter(el -> el != null)
                .anyMatch(el -> el.equals(element));
    }
}