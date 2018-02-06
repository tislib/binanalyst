package net.tislib.binanalyst;

import net.tislib.binanalyst.lib.BinCalc;
import net.tislib.binanalyst.lib.bit.Bit;

import static net.tislib.binanalyst.lib.BinValueHelper.*;
import static net.tislib.binanalyst.lib.BitOps.xor;

/**
 * Created by Taleh Ibrahimli on 2/4/18.
 * Email: me@talehibrahimli.com
 */
public class Test6 {

    public static void main(String... args) {

        long a = 433235423;
        long b = 784341251;
        long c = 243434878;
        long d = 134653421;
        long s = a + b + c + d;

        print(a);
        print(b);
        print(c);
        print(d);
        System.out.print("Res:");
        print(s);
        System.out.print("Pre:");

        int l = binLength(d);

        Bit[] res = new Bit[l];
        for (int i = 0; i < l; i++) {
            res[l - i - 1] = check(a, b, c, d, i);
        }
        print(res);
        printError(res, s);

    }


    public static Bit check(long a, long b, long c, long d, int i) {

        int N = 4;
        Bit r[] = new Bit[N], ri[] = new Bit[N], si[] = new Bit[N - 1];


        r[0] = getConstBit(a, i);
        r[1] = getConstBit(b, i);  // <- x11
        r[2] = getConstBit(c, i); // <- x21
        r[3] = getConstBit(d, i);

        if (i == 0) {
            return xor(r);
        }
        i--;

        r[0] = getConstBit(a, i);
        r[1] = getConstBit(b, i);  // <- x11
        r[2] = getConstBit(c, i); // <- x21
        r[3] = getConstBit(d, i);
        si[0] = getConstBit(a + b, i);
        si[1] = getConstBit(a + b + c, i);
        si[2] = getConstBit(a + b + c + d, i);

        ri[0] = getConstBit(a, i + 1);
        ri[1] = getConstBit(b, i + 1);
        ri[2] = getConstBit(c, i + 1);
        ri[3] = getConstBit(d, i + 1);

        return BinCalc.getAddMultiPosBit(r, si, ri)[N - 2];

    }

}
