package net.tislib.binanalyst.lib.bit;

/**
 * Created by Taleh Ibrahimli on 2/5/18.
 * Email: me@talehibrahimli.com
 */
public final class ConstantBit implements Bit, NamedBit {
    public static final ConstantBit ZERO = new ConstantBit(BinaryValue.FALSE);
    public static final ConstantBit UNKNOWN = new ConstantBit(BinaryValue.UNKNOWN);
    public static final ConstantBit UNSET = new ConstantBit(BinaryValue.UNSET);
    public static final ConstantBit ONE = new ConstantBit(BinaryValue.TRUE);

    private final BinaryValue value;

    private ConstantBit(BinaryValue value) {
        this.value = value;
    }

    public static Bit not(ConstantBit bit) {
        return bit == ZERO ? ONE : ZERO;
    }

    @Override
    public BinaryValue getValue() {
        return value;
    }

    @Override
    public String getType() {
        return "const";
    }

    @Override
    public String toString() {
        return this == ZERO ? "0" : "1";
    }

    @Override
    public String getName() {
        return value.toString();
    }

    @Override
    public void setName(String name) {
        throw new UnsupportedOperationException();
    }
}
