package net.tislib.binanalyst.lib.bit;

import net.tislib.binanalyst.lib.calc.BitOpsCalculator;
import net.tislib.binanalyst.lib.calc.SimpleBitOpsCalculator;

import java.util.*;

import static net.tislib.binanalyst.lib.bit.ConstantBit.ONE;
import static net.tislib.binanalyst.lib.bit.ConstantBit.ZERO;

/**
 * Created by Taleh Ibrahimli on 2/6/18.
 * Email: me@talehibrahimli.com
 */
public final class OperationalBit extends CompositeBit implements Bit {
    private final Bit[] bits;
    private final Operation operation;

    private OperationalBit(Operation operation, Bit... bits) {
        this.bits = bits;
        this.operation = operation;
    }

    public static Bit and(Bit... bits) {
        return new OperationalBit(Operation.AND, bits);
    }

    public static Bit or(Bit... bits) {
        return new OperationalBit(Operation.OR, bits);
    }

    public static Bit not(Bit bit) {
        return new OperationalBit(Operation.NOT, bit);
    }

    public static Bit equal(Bit bitA, Bit bitB) {
        return or(and(bitA, bitB), and(not(bitA), not(bitB)));
    }

    public static Bit xor(Bit bitA, Bit bitB) {
        return or(and(bitA, not(bitB)), and(not(bitA), bitB));
    }

    public static Bit equal(Bit... bits) {
        if (bits.length == 0) {
            return ONE;
        }
        Bit result = bits[0];
        for (int i = 1; i < bits.length; i++) {
            result = equal(result, bits[i]);
        }
        return result;
    }

    public static Bit xor(Bit... bits) {
        if (bits.length == 0) {
            return ZERO;
        }
        Bit result = bits[0];
        for (int i = 1; i < bits.length; i++) {
            result = xor(result, bits[i]);
        }
        return result;
    }

    public enum Operation {
        AND, OR, NOT
    }

//    public void calculate(BitOpsCalculator calculator, boolean recursive) {
//        if (recursive) {
//            for (Bit bit : bits) {
//                if (bit instanceof OperationalBit) {
//                    ((OperationalBit) bit).calculate(calculator, true);
//                }
//            }
//        }
//    }
//
//    private void calculate(BitOpsCalculator calculator) {
//        switch (operation) {
//            case OR:
//                value = calculator.or(bits).getValue();
//            case AND:
//                value = calculator.and(bits).getValue();
//            default:
//                value = calculator.not(bits[0]).getValue();
//        }
//    }


    @Override
    public String toString() {
        return getStringValue();
    }

    int hash = 0;

    @Override
    public int hashCode() {
        if (hash == 0) {
            Objects.hash(operation, Arrays.hashCode(getBits()));
        } else {
            System.out.println("hash calculated");
        }
        return hash;
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
