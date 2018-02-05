package net.tislib.binanalyst.lib;

/**
 * Created by Taleh Ibrahimli on 2/5/18.
 * Email: me@talehibrahimli.com
 */
public final class ConstantBit implements Bit {
    public static final Bit ZERO = new ConstantBit(false);
    public static final Bit ONE = new ConstantBit(true);

    private final boolean value;

    private ConstantBit(boolean value) {
        this.value = value;
    }

    @Override
    public boolean getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this == ZERO ? "0" : "1";
    }
}
