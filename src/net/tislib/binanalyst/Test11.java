package net.tislib.binanalyst;

import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.OperationalBit;
import net.tislib.binanalyst.lib.bit.VarBit;
import net.tislib.binanalyst.lib.operator.BinAdd;
import net.tislib.binanalyst.lib.operator.BinMul;

import static net.tislib.binanalyst.lib.BinValueHelper.*;
import static net.tislib.binanalyst.lib.bit.ConstantBit.ZERO;

/**
 * Created by Taleh Ibrahimli on 2/4/18.
 * Email: me@talehibrahimli.com
 */
public class Test11 {

    public static void main(String... args) {

        long c = 2 * 3;

        VarBit[] a = VarBit.list("a", 8, ZERO);
        VarBit[] b = VarBit.list("b", 8, ZERO);

        Bit[] r = BinMul.multiply(a, b);

        setVal(a, 2);
        setVal(b, 3);

        print(c);
        printValues(r);
        printSpaced(r);

    }

}
