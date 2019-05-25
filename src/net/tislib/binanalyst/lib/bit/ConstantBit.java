package net.tislib.binanalyst.lib.bit;

/**
 * Created by Taleh Ibrahimli on 2/5/18.
 * Email: me@talehibrahimli.com
 */
public final class ConstantBit implements Bit {
    public static final Bit ZERO = new ConstantBit(BinaryValue.FALSE);
    public static final Bit UNKNOWN = new ConstantBit(BinaryValue.UNKNOWN);
    public static final Bit UNSET = new ConstantBit(BinaryValue.UNSET);
    public static final Bit ONE = new ConstantBit(BinaryValue.TRUE);

    private final BinaryValue value;

    private ConstantBit(BinaryValue value) {
        this.value = value;
    }

    @Override
    public BinaryValue getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this == ZERO ? "0" : "1";
    }

    public static Bit not(ConstantBit bit) {
        return bit == ZERO ? ONE : ZERO;
    }
}
