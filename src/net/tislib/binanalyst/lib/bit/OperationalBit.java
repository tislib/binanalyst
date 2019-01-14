package net.tislib.binanalyst.lib.bit;

import java.util.StringJoiner;
import net.tislib.binanalyst.lib.calc.graph.Operation;

/**
 * Created by Taleh Ibrahimli on 2/6/18.
 * Email: me@talehibrahimli.com
 */
public final class OperationalBit extends VarBit implements Bit {

    private Operation operation;
    private NamedBit[] bits;

    public OperationalBit(Operation operation, NamedBit[] bits) {
        super();
        this.operation = operation;
        this.bits = bits;
    }

    public Operation getOperation() {
        return operation;
    }

    public NamedBit[] getBits() {
        return bits;
    }

    @Override
    public String toString() {
        if (operation == Operation.NOT) {
            return getName() + " : " + "!" + bits[0].getName();
        }
        StringJoiner joiner = new StringJoiner(" " + getOperation().getSign() + " ");
        for (NamedBit bit : bits) {
            joiner.add(bit.getName());
        }
        return getName() + " : " + joiner;
    }

    public void calculate() {
        this.setValue(calculateInternal());
    }

    private boolean calculateInternal() {
        switch (operation) {
            case AND:
                for (NamedBit namedBit : bits) {
                    if (!namedBit.getValue()) return false;
                }
                return true;
            case OR:
                for (NamedBit namedBit : bits) {
                    if (namedBit.getValue()) return true;
                }
                return false;
            case NOT:
                return !bits[0].getValue();
            case XOR:
                int ONE_count = 0;
                for (NamedBit namedBit : bits) {
                    if (namedBit.getValue()) ONE_count++;
                }
                return (ONE_count % 2) == 1;
            default:
                throw new UnsupportedOperationException();
        }
    }

    public void init(Operation operation, NamedBit[] bits) {
        this.operation = operation;
        this.bits = bits;
    }

    public void reinit(NamedBit[] bits) {
        this.bits = bits;
    }

    public boolean hasBit(NamedBit bit2) {
        for (NamedBit bit : bits) {
            if (bit == bit2) return true;
        }
        return false;
    }

    public String showFull() {
        return showFull(true);
    }

    public String showFull(boolean showSelf) {
        if (operation == Operation.NOT) {
            return showSelf ? getName() + " : " + "!" + showFull(bits[0]) : showFull(bits[0]);
        }
        StringJoiner joiner = new StringJoiner(" " + getOperation().getSign() + " ");
        for (NamedBit bit : bits) {
            joiner.add(showFull(bit));
        }
        return showSelf ? getName() + " : " + joiner : joiner.toString();
    }

    public static String showFull(NamedBit bit) {
        if (bit instanceof OperationalBit) {
            return "(" + ((OperationalBit) bit).showFull(false) + ")";
        }
        return bit.getName();
    }
}
