package net.tislib.binanalyst;

import net.tislib.binanalyst.lib.Bit;
import net.tislib.binanalyst.lib.operator.BinMul;

import static net.tislib.binanalyst.lib.BinValueHelper.*;
import static net.tislib.binanalyst.lib.ConstantBit.ZERO;

/**
 * Created by Taleh Ibrahimli on 2/4/18.
 * Email: me@talehibrahimli.com
 */
public class Test8 {

    public static void main(String... args) {

        long a = 1023;
        long b = 1023;
        long c = a * b;

        print(a);
        print(b);
        System.out.print("Res:");
        print(c);
        System.out.print("Pre:");

        int l = binLength(c);

        Bit[] res = new Bit[l];
        for (int i = 0; i < l; i++) {
            res[l - i - 1] = mul(a, b, i);
        }
        print(res);
        printError(res, c);


        Bit M[][] = BinMul.getMultiplicationMatrix(trim(getBits(a)), trim(getBits(b)));

        long pre = 0;
        for(Bit[] L : M){
            pre += toLong(L);
        }

        print(c);
        print(pre);

        print(M);
    }


    private static Bit mul(long a, long b, int i) {
        Bit M[][] = BinMul.getMultiplicationMatrix(getBits(a), getBits(b));

        return ZERO;
    }


}
