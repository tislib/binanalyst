package net.tislib.binanalyst.lib;

import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.calc.BitOpsCalculator;

import static net.tislib.binanalyst.lib.BitOps.*;

/**
 * Created by Taleh Ibrahimli on 2/4/18.
 * Email: me@talehibrahimli.com
 */
public class BinCalc {
    public static byte getAddPosBit(BitOpsCalculator calculator, byte ai, byte bi, byte si, byte ai1, byte bi1) {
        return convert(getAddPosBit(calculator, wrap(calculator, ai), wrap(calculator, bi), wrap(calculator, si), wrap(calculator, ai1), wrap(calculator, bi1)));
    }

    public static Bit getAddPosBit(BitOpsCalculator calculator, Bit ai, Bit bi, Bit si, Bit ai1, Bit bi1) {
        return xor(calculator, ai1, bi1, and(calculator, or(calculator, ai, bi), (or(calculator, and(calculator, ai, bi), not(calculator, si)))));
    }

    public static byte[] getAddMultiPosBit(BitOpsCalculator calculator, byte ri[], byte[] si, byte ri1[]) {
        return convert(getAddMultiPosBit(calculator, wrap(calculator, ri), wrap(calculator, si), wrap(calculator, ri1)));
    }

    public static Bit[] getAddMultiPosBit(BitOpsCalculator calculator, Bit ri[], Bit[] si, Bit ri1[]) {
        if (ri.length != ri1.length) {
            throw new RuntimeException("incorrect parameter length");
        }
        if (ri.length == 2) {
            Bit res = getAddPosBit(calculator, ri[0], ri[1], si[0], ri1[0], ri1[1]);
            return new Bit[]{res};
        }
        Bit xi[] = new Bit[ri.length - 1];


        xi[0] = getAddPosBit(calculator, ri[0], ri[1], si[0], ri1[0], ri1[1]);


        for (int i = 0; i < ri.length - 2; i++) {
            xi[i + 1] = getAddPosBit(calculator, si[i], ri[i + 2], si[i + 1], xi[i], ri1[i + 2]);
        }

        return xi;
    }
}
