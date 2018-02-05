package net.tislib.binanalyst.lib;

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
}
