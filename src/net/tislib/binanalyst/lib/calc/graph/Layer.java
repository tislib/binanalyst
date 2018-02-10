package net.tislib.binanalyst.lib.calc.graph;

import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.NamedBit;

import java.util.*;

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

    public void setBits(T[][] bitsArray) {
        this.bits.clear();
        for (T[] bits : bitsArray) {
            this.bits.addAll(Arrays.asList(bits));
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
        if (bit.getName() == null) {
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

    public Layer<T> copy() {
        return new Layer<>(name, new ArrayList<>(bits));
    }

    public void show(boolean showValues) {
        System.out.println(name.toUpperCase() + ":");
        for (NamedBit bit : this) {
            System.out.println(bit.toString() + (showValues ? " => " + bit.getValue() : ""));
        }
    }
}
