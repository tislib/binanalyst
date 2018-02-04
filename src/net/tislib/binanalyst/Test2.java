package net.tislib.binanalyst;

import net.tislib.binanalyst.lib.BinCalc;
import net.tislib.binanalyst.lib.BinValueHelper;

/**
 * Created by Taleh Ibrahimli on 2/4/18.
 * Email: me@talehibrahimli.com
 */
public class Test2 {

    public static void main(String... args) {

        long a = 4332323;
        long b = 7843451;
        long c = 2434878;
        long d = 1343421;
        long s = a + b + c;

        BinValueHelper.print(a);
        BinValueHelper.print(b);
        BinValueHelper.print(c);
        BinValueHelper.print(d);
        System.out.println("Result");
        BinValueHelper.print(s);
        System.out.println("Predict");

        int l = BinValueHelper.binLength(d);

        System.out.println(l);

        byte[] res = new byte[l];
        for (int i = 0; i < l; i++) {
            res[i] = check(a, b, c, d, i);
        }
        BinValueHelper.print(res);

    }

    private static byte check(long a, long b, long c, long d, int i) {

        long s = a + b ;//+ c;

        if (i == 0) {
            return BinValueHelper.getBit(s, i);
        }
        i--;

        int N = 2;
        byte r[] = new byte[N], ri[] = new byte[N], si[] = new byte[N - 1], si1;

        r[0] = BinValueHelper.getBit(a, i);
        r[1] = BinValueHelper.getBit(b, i);  // <- x11
//        r[2] = BinValueHelper.getBit(c, i); // <- x21
//        r[3] = BinValueHelper.getBit(d, i);
        si[0] = BinValueHelper.getBit(a + b, i);
//        si[1] = BinValueHelper.getBit(a + b + c, i);
//        si[2] = BinValueHelper.getBit(a + b + c + d, i);

        ri[0] = BinValueHelper.getBit(a, i + 1);
        ri[1] = BinValueHelper.getBit(b, i + 1);
//        ri[2] = BinValueHelper.getBit(c, i + 1);
//        ri[3] = BinValueHelper.getBit(d, i + 1);
        si1 = BinValueHelper.getBit(s, i + 1);


//        byte sip = BinCalc.getAddMultiPosBit(r, si, ri)[N - 2];
        byte sip = BinCalc.getAddPosBit(r[0], r[1], si[0], ri[0], ri[1]);
        System.out.println(sip == si1);
        return si1;

    }

}
