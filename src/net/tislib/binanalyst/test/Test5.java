package net.tislib.binanalyst.test;

import net.tislib.binanalyst.lib.BinCalc;
import net.tislib.binanalyst.lib.BinValueHelper;
import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.calc.BitOpsCalculator;

import static net.tislib.binanalyst.lib.BinValueHelper.getConstBit;
import static net.tislib.binanalyst.lib.BitOps.equal;

/**
 * Created by Taleh Ibrahimli on 2/4/18.
 * Email: me@talehibrahimli.com
 */
public class Test5 {

    public static void main(String... args) {

        long a = 4332327;
        long b = 7843651;
        long c = a + b;

        BinValueHelper.print(a);
        BinValueHelper.print(b);
        BinValueHelper.print(c);

        int l = BinValueHelper.binLength(a);

        for (int i = 0; i < l - 1; i++) {
            System.out.println((i + 1) + " => " + check(a, b, i));
        }

    }

    private static boolean check(long a, long b, int i) {

        long c = a + b;

        Bit ai, bi, ci, ai1, bi1, ci1;

        ai = getConstBit(a, i);
        bi = getConstBit(b, i);
        ci = getConstBit(c, i);
        ai1 = getConstBit(a, i + 1);
        bi1 = getConstBit(b, i + 1);
        ci1 = getConstBit(c, i + 1);

        Bit cip = BinCalc.getAddPosBit(BitOpsCalculator.getDefault(), ai, bi, ci, ai1, bi1);
        if (!equal(BitOpsCalculator.getDefault(), cip, ci1).getValue()) {
            System.out.println(cip + " <> " + ci1);
        }
        return equal(BitOpsCalculator.getDefault(), cip, ci1).getValue();

    }

}
