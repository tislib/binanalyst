package net.tislib.binanalyst.lib.bit;

/**
 * Created by Taleh Ibrahimli on 2/6/18.
 * Email: me@talehibrahimli.com
 */
public class VarBit extends CompositeBit implements NamedBit, Comparable<VarBit> {
    private String name;
    private boolean valueSetted = false;

    public VarBit(String name) {
        this.name = name;
    }

    public VarBit() {

    }

    public static VarBit wrap(String name, Bit bit) {
        return new VarBit(name) {
            @Override
            public BinaryValue getValue() {
                return bit.getValue();
            }
        };
    }

    public static VarBit wrap(String name, BinaryValue bit) {
        return new VarBit(name) {
            @Override
            public BinaryValue getValue() {
                return bit;
            }
        };
    }

    public static <T extends Bit> VarBit[] wrap(T[] bits) {
        VarBit[] result = new VarBit[bits.length];
        for (int i = 0; i < bits.length; i++) {
            if (bits[i] instanceof VarBit) {
                result[i] = (VarBit) bits[i];
            } else {
                result[i] = new VarBit();
                result[i].setValue(bits[i].getValue());
            }
        }
        return result;
    }

    public static VarBit[] list(String name, int count, Bit initialValue) {
        VarBit[] bits = new VarBit[count];
        for (int i = 0; i < bits.length; i++) {
            bits[i] = new VarBit(name + (bits.length - i - 1));
            bits[i].setValue(initialValue.getValue());
        }
        return bits;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public BinaryValue getValue() {
        if (!valueSetted) {
            throw new RuntimeException("Varbit: " + getName() + " has not value");
        }
        return super.getValue();
    }

    @Override
    public String getType() {
        return "var";
    }

    public void setValue(BinaryValue value) {
        if (value != BinaryValue.UNSET) {
            valueSetted = true;
        }
        super.setValue(value);
    }

    @Override
    public int compareTo(VarBit o) {
        return this.name.compareTo(o.getName());
    }
}
