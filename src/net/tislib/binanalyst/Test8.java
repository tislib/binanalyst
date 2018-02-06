package net.tislib.binanalyst;

import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.operator.BinAdd;
import net.tislib.binanalyst.lib.operator.BinMul;

import static net.tislib.binanalyst.lib.BinValueHelper.*;
import static net.tislib.binanalyst.lib.bit.ConstantBit.ZERO;

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


        Bit M[][] = BinMul.getMultiplicationMatrix(trim(getBits(a)), trim(getBits(b)));

        Bit[] r = BinAdd.add(M);

        print(c);
        print(r);
        printError(r, c);

//        print(M);
    }


    private static Bit mul(long a, long b, int i) {
        Bit M[][] = BinMul.getMultiplicationMatrix(getBits(a), getBits(b));

        return ZERO;
    }


}
