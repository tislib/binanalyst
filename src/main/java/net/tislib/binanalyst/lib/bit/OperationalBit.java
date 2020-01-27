package net.tislib.binanalyst.lib.bit;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.StringJoiner;
import java.util.function.Function;
import java.util.function.Predicate;
import net.tislib.binanalyst.lib.calc.graph.Operation;

/**
 * Created by Taleh Ibrahimli on 2/6/18.
 * Email: me@talehibrahimli.com
 */
public class OperationalBit extends VarBit implements Bit {

    private final Operation operation;
    private final NamedBit[] bits;

    public OperationalBit(Operation operation, NamedBit[] bits) {
        super();
        this.operation = operation;
        this.bits = bits;
    }

    @Override
    public String getType() {
        return "operational";
    }

    public static String showFull(NamedBit bit) {
        if (bit instanceof OperationalBit) {
            if (((OperationalBit) bit).getOperation() != Operation.NOT) {
                return "(" + ((OperationalBit) bit).showFull(false) + ")";
            } else {
                return ((OperationalBit) bit).showFull(false);
            }
        }
        return bit.getName();
    }

    public static String showFull(NamedBit bit, Predicate<NamedBit> resolveFormula) {
        if (bit instanceof OperationalBit) {
            if (((OperationalBit) bit).getOperation() != Operation.NOT && resolveFormula.test(bit)) {
                return "(" + ((OperationalBit) bit).showFull(false, resolveFormula) + ")";
            } else {
                return ((OperationalBit) bit).showFull(false, resolveFormula);
            }
        }
        return bit.getName();
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
        List<String> bitNames = new ArrayList<>();
        for (NamedBit bit : bits) {
            bitNames.add(bit.getName());
        }
        bitNames.sort(Comparator.comparing(Function.identity()));
        for (String bitName : bitNames) {
            joiner.add(bitName);
        }
        return getName() + " : " + joiner;
    }

    public void calculate() {
        this.setValue(calculateInternal());
    }

    @Override
    public BinaryValue getValue() {
        if (operation == Operation.COPY) {
            return bits[0].getValue();
        }
        return super.getValue();
    }

    private BinaryValue calculateInternal() {
        boolean unknownFound = false;
        switch (operation) {
            case AND:
                for (NamedBit namedBit : bits) {
                    if (namedBit.getValue() == BinaryValue.FALSE) return BinaryValue.FALSE;
                    if (namedBit.getValue() == BinaryValue.UNKNOWN) {
                        unknownFound = true;
                    }
                }
                if (unknownFound) {
                    return BinaryValue.UNKNOWN;
                }
                return BinaryValue.TRUE;
            case OR:
                for (NamedBit namedBit : bits) {
                    if (namedBit.getValue() == BinaryValue.TRUE) return BinaryValue.TRUE;
                    if (namedBit.getValue() == BinaryValue.UNKNOWN) {
                        unknownFound = true;
                    }
                }
                if (unknownFound) {
                    return BinaryValue.UNKNOWN;
                }
                return BinaryValue.FALSE;
            case NOT:
                return bits[0].getValue().reverse();
            case XOR:
                int ONE_count = 0;
                for (NamedBit namedBit : bits) {
                    if (namedBit.getValue() == BinaryValue.UNKNOWN)
                        return BinaryValue.UNKNOWN;
                    if (namedBit.getValue() == BinaryValue.TRUE) ONE_count++;
                }
                return (ONE_count % 2) == 1 ? BinaryValue.TRUE : BinaryValue.FALSE;
            default:
                throw new UnsupportedOperationException();
        }
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
//        if (!isFullMiddle()) {
//            return getName();
//        }
        if (operation == Operation.NOT) {
            return showSelf ? getName() + " : " + "!" + showFull(bits[0]) : "!" + showFull(bits[0]);
        }
        StringJoiner joiner = new StringJoiner(" " + getOperation().getSign() + " ");
        for (NamedBit bit : bits) {
            joiner.add(showFull(bit));
        }
        return showSelf ? getName() + " : " + joiner : joiner.toString();
    }

    public String showFull(boolean showSelf, Predicate<NamedBit> resolveFormula) {
        if (!resolveFormula.test(this)) {
            return getName();
        }
//        if (!isFullMiddle()) {
//            return getName();
//        }
        if (operation == Operation.NOT) {
            return showSelf ? getName() + " : " + "!" + showFull(bits[0], resolveFormula) : "!" + showFull(bits[0], resolveFormula);
        }
        StringJoiner joiner = new StringJoiner(" " + getOperation().getSign() + " ");
        for (NamedBit bit : bits) {
            joiner.add(showFull(bit, resolveFormula));
        }
        return showSelf ? getName() + " : " + joiner : joiner.toString();
    }

    public boolean isFullMiddle() {
        for (NamedBit bit : bits) {
            if (!(bit instanceof OperationalBit)) return false;
        }
        return true;
    }

    public boolean testBits(Predicate<Bit> bitTester) {
        boolean val = true;
        for (Bit bit : bits) {
            val &= bitTester.test(bit);
        }
        return val;
    }

    public boolean isTransitive() {
        for (NamedBit bit : bits) {
            if (bit instanceof OperationalBit) return false;
        }
        return this.getOperation() == Operation.NOT;
    }

    public void replaceBit(NamedBit from, NamedBit to) {
        for (int i = 0; i < bits.length; i++) {
            if (bits[i] == from) {
                bits[i] = to;
            }
        }
    }

    public boolean hasBit(String name) {
        for (NamedBit bit : bits) {
            if (bit.getName().equals(name)) return true;
        }
        return false;
    }
}
