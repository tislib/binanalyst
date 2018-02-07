package net.tislib.binanalyst.test;

import net.tislib.binanalyst.lib.BinCalc;
import net.tislib.binanalyst.lib.BinValueHelper;
import net.tislib.binanalyst.lib.calc.BitOpsCalculator;

/**
 * Created by Taleh Ibrahimli on 2/4/18.
 * Email: me@talehibrahimli.com
 */
public class Test2 {

    public static void main(String... args) {

        long a = 433235423;
        long b = 784341251;
        long c = 243434878;
        long d = 134653421;
        long s = a + b + c + d;

        BinValueHelper.print(a);
        BinValueHelper.print(b);
        BinValueHelper.print(c);
        BinValueHelper.print(d);
        System.out.print("Res:");
        BinValueHelper.print(s);
        System.out.print("Pre:");

        int l = BinValueHelper.binLength(d);

        byte[] res = new byte[l];
        for (int i = 0; i < l; i++) {
            res[l - i - 1] = check(a, b, c, d, i);
        }
        BinValueHelper.print(res);
        BinValueHelper.printError(res, s);

    }


    public static byte check(long a, long b, long c, long d, int i) {

        int N = 4;
        byte r[] = new byte[N], ri[] = new byte[N], si[] = new byte[N - 1];


        r[0] = BinValueHelper.getBit(a, i);
        r[1] = BinValueHelper.getBit(b, i);  // <- x11
        r[2] = BinValueHelper.getBit(c, i); // <- x21
        r[3] = BinValueHelper.getBit(d, i);

        if (i == 0) {
            return BinValueHelper.getBit(r[0] ^ r[1] ^ r[2] ^ r[3], i);
        }
        i--;

        r[0] = BinValueHelper.getBit(a, i);
        r[1] = BinValueHelper.getBit(b, i);  // <- x11
        r[2] = BinValueHelper.getBit(c, i); // <- x21
        r[3] = BinValueHelper.getBit(d, i);
        si[0] = BinValueHelper.getBit(a + b, i);
        si[1] = BinValueHelper.getBit(a + b + c, i);
        si[2] = BinValueHelper.getBit(a + b + c + d, i);

        ri[0] = BinValueHelper.getBit(a, i + 1);
        ri[1] = BinValueHelper.getBit(b, i + 1);
        ri[2] = BinValueHelper.getBit(c, i + 1);
        ri[3] = BinValueHelper.getBit(d, i + 1);

        return BinCalc.getAddMultiPosBit(BitOpsCalculator.getDefault(), r, si, ri)[N - 2];

    }

}
