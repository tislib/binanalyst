package net.tislib.binanalyst.test.old;

import static net.tislib.binanalyst.lib.BinValueHelper.*;
import static net.tislib.binanalyst.lib.bit.ConstantBit.ZERO;

import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.VarBit;
import net.tislib.binanalyst.lib.calc.graph.GraphBitOpsCalculator;
import net.tislib.binanalyst.lib.calc.graph.optimizer.LogicalOptimizer;
import net.tislib.binanalyst.lib.calc.graph.optimizer.SimpleOptimizer;
import net.tislib.binanalyst.lib.operator.BinMul;

/**
 * Created by Taleh Ibrahimli on 2/4/18.
 * Email: me@talehibrahimli.com
 */
public class Test11 {

    public static void main(String... args) {
//        while (true) {


        GraphBitOpsCalculator calculator = new GraphBitOpsCalculator();

        VarBit[] a = VarBit.list("a", 4, ZERO);
        VarBit[] b = VarBit.list("b", 4, ZERO);

        long ax = 1;
        long bx = 3;

        setVal(a, ax);
        setVal(b, bx);

        calculator.getOptimizers().add(new SimpleOptimizer());
        calculator.getOptimizers().add(new LogicalOptimizer());

        Bit[] r = BinMul.multiply(calculator, a, b);

        calculator.setInputBits(a, b);
        calculator.setOutputBits(r);

        calculator.clean();

        calculator.calculate();

        calculator.show();

        printValues(r);
        print(ax * bx);
        System.out.println(calculator.getOperationCount());
    }

}
