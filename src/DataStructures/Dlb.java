package DataStructures;

import java.util.BitSet;
import java.util.Random;

public class Dlb {
    private BitSet bitset;
    public final int length;

    public Dlb(int size) {
        this.bitset = new BitSet(size);
        this.length = size;
    }

    /**
     * Crea un nuevo Dlb con valores aleatorios
     * 
     * @param size
     * @param seed
     */
    public Dlb(int size, int seed) {
        var random = new Random(seed);
        this.bitset = new BitSet(size);
        this.length = size;

        for (int i = 0; i < size; ++i)
            Set(i, random.nextBoolean());
    }

    public boolean Get(int index) {
        return bitset.get(index);
    }

    public void Set(int index, boolean value) {
        bitset.set(index, value);
    }

    public boolean AllActivated() {
        return bitset.cardinality() == length;
    }
}