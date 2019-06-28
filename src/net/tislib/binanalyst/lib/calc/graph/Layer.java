package net.tislib.binanalyst.lib.calc.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.NamedBit;

/**
 * Created by Taleh Ibrahimli on 2/8/18.
 * Email: me@talehibrahimli.com
 */
public class Layer<T extends NamedBit> implements Iterable<T> {
    private final String name;
    private final List<T> bits;

    public Layer(String name) {
        this.name = name;
        bits = new ArrayList<>();
    }

    protected Layer(String name, List<T> bits) {
        this.name = name;
        this.bits = bits;
    }

    public Integer getBitId(T bit) {
        return null;
    }

    public void setBits(T[][] bitsArray, boolean setNames) {
        this.bits.clear();
        for (T[] bits : bitsArray) {
            this.bits.addAll(Arrays.asList(bits));
        }

        if (setNames) {
            this.rename();
        }
    }

    @Override
    public Iterator<T> iterator() {
        return bits.iterator();
    }

    public Bit register(T bit) {
        Bit foundBit = check(bit);
        if (foundBit != null) {
            return foundBit;
        }
        if (bit.getName() == null || name.toCharArray()[0] != bit.getName().toCharArray()[0]) {
            bit.setName(name.toCharArray()[0] + "[" + (this.bits.size()) + "]");
        }
        this.bits.add(bit);
        return bit;
    }

    private Bit check(T bit) {
//        for (Bit foundBit : bits) {
//            if (foundBit == bit) {
//                return foundBit;
//            }
//        }
        return null;
    }

    @SafeVarargs
    public final void addBits(T... bits) {
        this.bits.addAll(Arrays.asList(bits));
    }

    public List<T> getBits() {
        return bits;
    }

    public void setBits(T[][] bitsArray) {
        setBits(bitsArray, false);
    }

    public void setBits(List<T> bits) {
        this.bits.clear();
        this.bits.addAll(bits);
    }

    public Layer<T> copy() {
        return new Layer<>(name, new ArrayList<>(bits));
    }

    public void show(boolean showValues) {
        System.out.println(name.toUpperCase() + ":");
        for (NamedBit bit : this) {
            System.out.println(bit.toString() + (showValues ? " => " + bit.getValue() : ""));
        }
    }

    public void remove(T bit) {
        if (!bits.remove(bit)) {
            throw new RuntimeException("cannot remove bit: " + bit.getName());
        }
    }

    public boolean contains(Bit bit2) {
        for (T bit : bits) {
            if (bit == bit2) {
                return true;
            }
        }
        return false;
    }

    public void rename() {
        for (int i = 0; i < this.bits.size(); i++) {
            T bit = this.bits.get(i);
            bit.setName(name.toCharArray()[0] + "[" + i + "]");
        }
    }

    public int size() {
        return bits.size();
    }

    public T getBitL(int i) {
        return bits.get(bits.size() - 1 - i);
    }

    public T locate(String name) {
        for (T bit : bits) {
            if (bit.getName().equals(name)) {
                return bit;
            }
        }
        return null;
    }
}
