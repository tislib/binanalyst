package net.tislib.binanalyst.lib.bit;

/**
 * Created by Taleh Ibrahimli on 2/5/18.
 * Email: me@talehibrahimli.com
 */
public class CompositeBit implements Bit {
    private BinaryValue value = BinaryValue.UNSET;

    public CompositeBit(BinaryValue value) {
        this.value = value;
    }

    public CompositeBit() {
    }

    @Override
    public BinaryValue getValue() {
        return value;
    }

    public void setValue(BinaryValue value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.getValue().toString();
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
