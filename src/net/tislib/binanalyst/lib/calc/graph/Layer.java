package net.tislib.binanalyst.lib.calc.graph;

import net.tislib.binanalyst.lib.bit.Bit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Taleh Ibrahimli on 2/8/18.
 * Email: me@talehibrahimli.com
 */
public class Layer implements Iterable<Bit> {
    private final String name;
    private final List<Bit> bits = new ArrayList<>();

    public Layer(String name) {
        this.name = name;
    }

    public Integer getBitId(Bit bit) {
        return null;
    }

    public void setBits(Bit[][] bitsArray) {
        this.bits.clear();
        for (Bit[] bits : bitsArray) {
            this.bits.addAll(Arrays.asList(bits));
        }
    }

    @Override
    public Iterator<Bit> iterator() {
        return bits.iterator();
    }
}
