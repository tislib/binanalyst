package net.tislib.binanalyst.lib.operator;

import static net.tislib.binanalyst.lib.bit.ConstantBit.ONE;
import static net.tislib.binanalyst.lib.bit.ConstantBit.ZERO;

import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.NamedBit;
import net.tislib.binanalyst.lib.bit.OperationalBit;
import net.tislib.binanalyst.lib.bit.VarBit;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.Operation;

public class BinMulDiv {

    public static Bit[] mulDiv(BitOpsGraphCalculator calculator, VarBit[] aBits, VarBit[] cBits) {
        VarBit[] bBits = VarBit.list("b", aBits.length, ZERO);

        Bit[] r = BinMul.multiply(calculator, aBits, bBits);


        VarBit a0 = aBits[aBits.length - 1];
        VarBit b0 = bBits[aBits.length - 1];
        OperationalBit[] truth = new OperationalBit[r.length];

        replaceBit(calculator, b0, (NamedBit) ONE);

        for (int i = bBits.length - 2; i >= 0; i--) {
            VarBit bi = bBits[i];
            Bit bMi = calculator.and(a0, bi);
            OperationalBit ri = (OperationalBit) r[r.length - (bBits.length - i)];
            if (!ri.hasBit((NamedBit) bMi) || ri.getOperation() != Operation.XOR) {
                throw new RuntimeException();
            }

            NamedBit[] bits = ri.getBits();
            for (int j = 0; j < bits.length; j++) {
                if (bits[j] == bMi) {
                    bits[j] = cBits[i];
                }
            }
            bBits[i] = (OperationalBit) calculator.operation(ri.getOperation(), bits);
            replaceBit(calculator, bi, bBits[i]);
        }

        for (int i = 0; i < truth.length; i++) {
            truth[i] = (OperationalBit) calculator.not(calculator.xor(r[i], cBits[i]));
        }

        return truth;
    }

    private static void replaceBit(BitOpsGraphCalculator calculator, NamedBit from, NamedBit to) {
        for (OperationalBit operationalBit : calculator.getMiddle()) {
            operationalBit.replaceBit(from, to);
        }
    }

}
