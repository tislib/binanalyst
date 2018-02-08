package net.tislib.binanalyst.lib.bit;

/**
 * Created by Taleh Ibrahimli on 2/5/18.
 * Email: me@talehibrahimli.com
 */
public class CompositeBit implements Bit {
    private boolean value;

    public CompositeBit(boolean value) {
        this.value = value;
    }

    public CompositeBit() {
    }

    @Override
    public boolean getValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.getValue() ? "1" : "0";
    }

    @Override
    public int hashCode() {
        return value ? 1 : 0;
    }
}
