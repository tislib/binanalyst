package net.tislib.binanalyst.lib.calc.graph;

import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.NamedBit;
import net.tislib.binanalyst.lib.bit.OperationalBit;
import net.tislib.binanalyst.lib.bit.VarBit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Taleh Ibrahimli on 2/8/18.
 * Email: me@talehibrahimli.com
 */
public class Layer<T extends NamedBit> implements Iterable<T> {
    private final String name;
    private final List<T> bits = new ArrayList<>();

    public Layer(String name) {
        this.name = name;
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

    public void register(T bit) {
        check(bit);
        bit.setName(name.toCharArray()[0] + "[" + (this.bits.size()) + "]");
        this.bits.add(bit);
    }

    private void check(T bit) {

    }

    @SafeVarargs
    public final void addBits(T... bits) {
        this.bits.addAll(Arrays.asList(bits));
    }

    public List<T> getBits() {
        return bits;
    }
}
