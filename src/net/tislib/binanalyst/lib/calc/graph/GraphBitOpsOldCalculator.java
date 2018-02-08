package net.tislib.binanalyst.lib.calc.graph;

import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.CompositeBit;
import net.tislib.binanalyst.lib.bit.ConstantBit;
import net.tislib.binanalyst.lib.bit.OperationalBit;
import net.tislib.binanalyst.lib.calc.BitOpsCalculator;

import java.util.*;

import static net.tislib.binanalyst.lib.bit.ConstantBit.ONE;
import static net.tislib.binanalyst.lib.bit.ConstantBit.ZERO;

/**
 * Created by Taleh Ibrahimli on 2/5/18.
 * Email: me@talehibrahimli.com
 */
public class GraphBitOpsOldCalculator implements BitOpsCalculator {
    @Override
    public Bit xor(Bit... bits) {
        return OperationalBit.xor(bits);
    }

    @Override
    public Bit and(Bit... bits) {
        return OperationalBit.and(bits);
    }

    @Override
    public Bit or(Bit... bits) {
        return OperationalBit.or(bits);
    }

    @Override
    public Bit not(Bit bit) {
        return OperationalBit.not(bit);
    }

    @Override
    public Bit equal(Bit... bits) {
        return OperationalBit.equal(bits);
    }

    @Override
    public Bit wrap(Number num) {
        return num.longValue() == 0 ? ZERO : ONE;
    }

    public Bit[] simplify(Bit[] bits) {
        CleanSimplifier cleanSimplifier = new CleanSimplifier();
        return cleanSimplifier.simplify(bits);
    }


    public long countOps(Bit[] bits) {
        return countOps(new HashSet<>(), bits);
    }

    public long countOps(Set<Integer> counted, Bit[] bits) {
        long ops = 0;
        for (int i = 0; i < bits.length; i++) {
            if (bits[i] instanceof OperationalBit) {
                OperationalBit opBit = (OperationalBit) (bits[i]);
                if (counted.contains(opBit.hashCode())) continue;
                counted.add(opBit.hashCode());
                ops++;
                ops += countOps(counted, opBit.getBits());
            }
        }
        return ops;
    }

//    public void calculate(BitOpsCalculator calculator, Bit... bits) {
//        for (Bit bit : bits) {
//            if (bit instanceof OperationalBit) {
//                ((OperationalBit) bit).calculate(calculator, true);
//            }
//        }
//    }
}
