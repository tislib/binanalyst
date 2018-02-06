package net.tislib.binanalyst.lib.bit;

/**
 * Created by Taleh Ibrahimli on 2/6/18.
 * Email: me@talehibrahimli.com
 */
public class VarBit extends CompositeBit {
    private final String name;

    public VarBit(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return getValue() ? "1" : "0";
    }

    public static VarBit[] list(String name, int count, Bit initialValue) {
        VarBit[] bits = new VarBit[count];
        for (int i = 0; i < bits.length; i++) {
            bits[i] = new VarBit(name + "[" + i + "]");
            bits[i].setValue(initialValue.getValue());
        }
        return bits;
    }
}
