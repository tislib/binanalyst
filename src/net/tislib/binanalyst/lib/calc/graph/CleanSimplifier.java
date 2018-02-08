package net.tislib.binanalyst.lib.calc.graph;

import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.ConstantBit;
import net.tislib.binanalyst.lib.bit.OperationalBit;

import java.util.ArrayList;
import java.util.List;

import static net.tislib.binanalyst.lib.bit.ConstantBit.ONE;
import static net.tislib.binanalyst.lib.bit.ConstantBit.ZERO;

/**
 * Created by Taleh Ibrahimli on 2/7/18.
 * Email: me@talehibrahimli.com
 */
public class CleanSimplifier implements Simplifier {

    @Override
    public Bit[] simplify(Bit... bits) {
        Bit[] newBits = new Bit[bits.length];
        for (int i = 0; i < bits.length; i++) {
            if (bits[i] instanceof OperationalBit) {
                newBits[i] = simplify((OperationalBit) bits[i]);
            } else {
                newBits[i] = bits[i];
            }
        }
        return newBits;
    }

    @Override
    public Bit simplify(Bit result) {
        if (result instanceof OperationalBit) {
            if (((OperationalBit) result).getBits().length == 0) {
                return ZERO;
            }
        }
        return result;
    }

    private Bit simplify(OperationalBit opBit) {
        Bit result = simplifyX(opBit);
        if (result instanceof OperationalBit) {
            if (((OperationalBit) result).getBits().length == 0) {
                return ZERO;
            }
        }
        return result;
    }

    private Bit simplifyX(OperationalBit opBit) {
        //reconstruct
        OperationalBit.Operation operation = opBit.getOperation();
        Bit[] bits = simplify(opBit.getBits());
        List<Bit> newBits = new ArrayList<>();

        switch (operation) {
            case AND:
                if (cleanConstant(bits, newBits, ZERO)) return ZERO;
                if (newBits.size() == 0) {
                    return ZERO;
                } else if (newBits.size() == 1) {
                    return newBits.get(0);
                }
                return OperationalBit.and(newBits.toArray(new Bit[]{}));
            case OR:
                if (cleanConstant(bits, newBits, ONE)) return ONE;
                if (newBits.size() == 0) {
                    return ONE;
                } else if (newBits.size() == 1) {
                    return newBits.get(0);
                }
                return OperationalBit.or(newBits.toArray(new Bit[]{}));
            case NOT:
                if (bits.length != 1 || bits[0] == null) {
                    throw new RuntimeException("incorrect bits for not operation");
                }
                if (bits[0] instanceof ConstantBit) {
                    return ConstantBit.not((ConstantBit) bits[0]);
                }
                if (bits[0] instanceof OperationalBit && ((OperationalBit) bits[0]).getOperation() == OperationalBit.Operation.NOT) {
                    return ((OperationalBit) bits[0]).getBits()[0];
                }
                return OperationalBit.not(bits[0]);
            default:
                return opBit;
        }

    }

    private boolean cleanConstant(Bit[] bits, List<Bit> newBits, Bit zero) {
        for (Bit bit : bits) {
            if (bit instanceof ConstantBit) {
                if (bit == zero) {
                    return true;
                }
            } else {
                newBits.add(bit);
            }
        }
        return false;
    }

}
