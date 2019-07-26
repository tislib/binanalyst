package net.tislib.binanalyst.test.experiments.old;

import static net.tislib.binanalyst.lib.BinValueHelper.*;

import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.calc.BitOpsCalculator;
import net.tislib.binanalyst.lib.operator.BinAdd;
import net.tislib.binanalyst.lib.operator.BinMul;

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


        Bit M[][] = BinMul.getMultiplicationMatrix(BitOpsCalculator.getDefault(), trim(getBits(BitOpsCalculator.getDefault(), a)), trim(getBits(BitOpsCalculator.getDefault(), b)));

        Bit[] r = BinAdd.add(BitOpsCalculator.getDefault(), M);

        print(c);
        print(r);
        printError(BitOpsCalculator.getDefault(), r, c);

    }


}
