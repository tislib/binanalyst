package net.tislib.binanalyst.lib.bit;

import net.tislib.binanalyst.lib.calc.BitOpsCalculator;
import net.tislib.binanalyst.lib.calc.SimpleBitOpsCalculator;

import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

import static net.tislib.binanalyst.lib.bit.ConstantBit.ONE;
import static net.tislib.binanalyst.lib.bit.ConstantBit.ZERO;

/**
 * Created by Taleh Ibrahimli on 2/6/18.
 * Email: me@talehibrahimli.com
 */
public final class OperationalBit implements Bit {
    private final Bit[] bits;
    private final Operation operation;

    private OperationalBit(Operation operation,Bit... bits) {
        this.bits = bits;
        this.operation = operation;
    }

    public static Bit and(Bit... bits) {
        return new OperationalBit(Operation.AND,  bits);
    }

    public static Bit or(Bit... bits) {
        return new OperationalBit(Operation.OR,  bits);
    }

    public static Bit not(Bit bit) {
        return new OperationalBit(Operation.NOT,  bit);
    }

    public static Bit equal(Bit bitA, Bit bitB) {
        return or( and( bitA, bitB), and( not( bitA), not( bitB)));
    }

    public static Bit xor(Bit bitA, Bit bitB) {
        return or( and( bitA, not( bitB)), and( not( bitA), bitB));
    }

    public static Bit equal(Bit... bits) {
        if (bits.length == 0) {
            return ONE;
        }
        Bit result = bits[0];
        for (int i = 1; i < bits.length; i++) {
            result = equal( result, bits[i]);
        }
        return result;
    }

    public static Bit xor(Bit... bits) {
        if (bits.length == 0) {
            return ZERO;
        }
        Bit result = bits[0];
        for (int i = 1; i < bits.length; i++) {
            result = xor( result, bits[i]);
        }
        return result;
    }
    public enum Operation {
        AND, OR, NOT
    }

    public Bit calculate() {
        SimpleBitOpsCalculator calculator = new SimpleBitOpsCalculator();
        switch (operation) {
            case OR:
                return calculator.or(bits);
            case AND:
                return calculator.and(bits);
            default:
                return calculator.not(bits[0]);
        }
    }

    @Override
    public boolean getValue() {
        return calculate().getValue();
    }

    @Override
    public String toString() {
        return getStringValue();
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    private String getStringValue() {
        if (bits.length == 0) {
            return "@";
        }
        String str;
        switch (operation) {
            case OR:
                str = join(bits, " | ");
                break;
            case AND:
                str = join(bits, " & ");
                break;
            default:
                str = "~" + bits[0].toString();
                break;
        }
        if (bits.length > 1) {
            str = "(" + str + ")";
        }
        return str;
    }

    private String join(Bit[] bits, String joiner) {
        StringJoiner stringJoiner = new StringJoiner(joiner);
        for (Bit bit : bits) {
            stringJoiner.add(bit.toString());
        }
        return stringJoiner.toString();
    }

    public Operation getOperation() {
        return operation;
    }

    public Bit[] getBits() {
        return bits;
    }
}
