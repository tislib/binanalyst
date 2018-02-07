package net.tislib.binanalyst.test;

import net.tislib.binanalyst.lib.BinCalc;
import net.tislib.binanalyst.lib.BinValueHelper;
import net.tislib.binanalyst.lib.calc.BitOpsCalculator;

/**
 * Created by Taleh Ibrahimli on 2/4/18.
 * Email: me@talehibrahimli.com
 */
public class Test1 {

    public static void main(String... args) {

        long a = 4332323;
        long b = 7843451;
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

        byte ai, bi, ci, ai1, bi1, ci1;

        ai = BinValueHelper.getBit(a, i);
        bi = BinValueHelper.getBit(b, i);
        ci = BinValueHelper.getBit(c, i);
        ai1 = BinValueHelper.getBit(a, i + 1);
        bi1 = BinValueHelper.getBit(b, i + 1);
        ci1 = BinValueHelper.getBit(c, i + 1);

        byte cip = BinCalc.getAddPosBit(BitOpsCalculator.getDefault(), ai, bi, ci, ai1, bi1);
        if (cip != ci1) {
            System.out.println(cip + " <> " + ci1);
        }
        return cip == ci1;

    }

}
