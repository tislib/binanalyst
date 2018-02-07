package net.tislib.binanalyst.test;

import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.VarBit;
import net.tislib.binanalyst.lib.calc.graph.GraphBitOpsCalculator;
import net.tislib.binanalyst.lib.operator.BinMul;

import static net.tislib.binanalyst.lib.BinValueHelper.*;
import static net.tislib.binanalyst.lib.bit.ConstantBit.ZERO;

/**
 * Created by Taleh Ibrahimli on 2/4/18.
 * Email: me@talehibrahimli.com
 */
public class Test11 {

    public static void main(String... args) {
//        while (true) {
            long c = 1 * 3;

            GraphBitOpsCalculator calculator = new GraphBitOpsCalculator();

            VarBit[] a = VarBit.list("a", 2, ZERO);
            VarBit[] b = VarBit.list("b", 2, ZERO);

            setVal(calculator, a, 1);
            setVal(calculator, b, 3);

            Bit[] r = BinMul.multiply(calculator, a, b);

            r = calculator.simplify(r);

            printSpaced(r);

            System.out.println("RESULT: ");
            printValues(r);
            System.out.println("OPS: " + calculator.countOps(r));

//        }
    }

}
