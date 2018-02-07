package net.tislib.binanalyst.test;

import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.calc.BitOpsCalculator;
import net.tislib.binanalyst.lib.operator.BinMul;

import static net.tislib.binanalyst.lib.BinValueHelper.*;

/**
 * Created by Taleh Ibrahimli on 2/4/18.
 * Email: me@talehibrahimli.com
 */
public class Test10 {

    public static void main(String... args) {

        long a = 1023;
        long b = 1023;
        long c = a * b;

        print(a);
        print(b);
        System.out.print("Res:");
        print(c);
        System.out.print("Pre:");


        Bit[] r = BinMul.multiply(BitOpsCalculator.getDefault(), trim(getBits(BitOpsCalculator.getDefault(), a)), trim(getBits(BitOpsCalculator.getDefault(), b)));

        print(c);
        print(r);
        printError(BitOpsCalculator.getDefault(), r, c);

    }

}
