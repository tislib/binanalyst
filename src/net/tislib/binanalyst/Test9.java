package net.tislib.binanalyst;

import net.tislib.binanalyst.lib.BinCalc;
import net.tislib.binanalyst.lib.BinValueHelper;
import net.tislib.binanalyst.lib.Bit;
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
        Bit[] r = BinAdd.add(getBits(a), getBits(b), getBits(c));
        print(s);
        print(r);
        printError(r, s);


    }

}
