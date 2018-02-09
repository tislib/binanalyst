package net.tislib.binanalyst.lib.bit;

import net.tislib.binanalyst.lib.calc.BitOpsCalculator;
import net.tislib.binanalyst.lib.calc.SimpleBitOpsCalculator;
import net.tislib.binanalyst.lib.calc.graph.Operation;

import java.util.*;

import static net.tislib.binanalyst.lib.BinValueHelper.print;
import static net.tislib.binanalyst.lib.BinValueHelper.printValues;
import static net.tislib.binanalyst.lib.bit.ConstantBit.ONE;
import static net.tislib.binanalyst.lib.bit.ConstantBit.ZERO;

/**
 * Created by Taleh Ibrahimli on 2/6/18.
 * Email: me@talehibrahimli.com
 */
public final class OperationalBit extends VarBit implements Bit {

    private final Operation operation;
    private final NamedBit[] bits;

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
            return getName() + " = " + "!" + bits[0].getName();
        }
        StringJoiner joiner = new StringJoiner(" " + getOperation().getSign() + " ");
        for (NamedBit bit : bits) {
            joiner.add(bit.getName());
        }
        return getName() + " = " + joiner;
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
}
