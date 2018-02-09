package net.tislib.binanalyst.lib.bit;

import java.util.StringJoiner;

/**
 * Created by Taleh Ibrahimli on 2/6/18.
 * Email: me@talehibrahimli.com
 */
public class VarBit extends CompositeBit implements NamedBit {
    private String name;

    public VarBit(String name) {
        this.name = name;
    }

    private boolean valueSetted = false;

    public VarBit() {

    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    public static VarBit[] list(String name, int count, Bit initialValue) {
        VarBit[] bits = new VarBit[count];
        for (int i = 0; i < bits.length; i++) {
            bits[i] = new VarBit(name + "[" + (bits.length - i - 1) + "]");
            bits[i].setValue(initialValue.getValue());
        }
        return bits;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean getValue() {
        if(!valueSetted){
            throw new RuntimeException("Varbit: " + getName() + " has not value");
        }
        return super.getValue();
    }

    public void setValue(boolean value) {
        valueSetted = true;
        super.setValue(value);
    }
}
