package net.tislib.binanalyst.test;

import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.calc.BitOpsCalculator;
import net.tislib.binanalyst.lib.operator.BinAdd;

import static net.tislib.binanalyst.lib.BinValueHelper.*;

/**
 * Created by Taleh Ibrahimli on 2/4/18.
 * Email: me@talehibrahimli.com
 */
public class Test9 {

    public static void main(String... args) {

        long a = 523123455;
        long b = 823432423;
        long c = 432847238;
        long s = a + b + c;

        print(a);
        System.out.println("+");
        print(b);
        System.out.println("=");
        Bit[] r = BinAdd.add(BitOpsCalculator.getDefault(), getBits(BitOpsCalculator.getDefault(), a), getBits(BitOpsCalculator.getDefault(), b), getBits(BitOpsCalculator.getDefault(), c));
        print(s);
        print(r);
        printError(BitOpsCalculator.getDefault(), r, s);


    }

}
