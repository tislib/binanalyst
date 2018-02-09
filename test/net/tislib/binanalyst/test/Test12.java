package net.tislib.binanalyst.test;

import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.VarBit;
import net.tislib.binanalyst.lib.calc.graph.GraphBitOpsCalculator;
import net.tislib.binanalyst.lib.calc.graph.optimizer.LogicalOptimizer;
import net.tislib.binanalyst.lib.calc.graph.optimizer.SimpleOptimizer;
import net.tislib.binanalyst.lib.operator.BinMul;

import static net.tislib.binanalyst.lib.BinValueHelper.print;
import static net.tislib.binanalyst.lib.BinValueHelper.setVal;
import static net.tislib.binanalyst.lib.bit.ConstantBit.ZERO;

/**
 * Created by Taleh Ibrahimli on 2/9/18.
 * Email: me@talehibrahimli.com
 */
public class Test12 {

    public static void main(String... args) {
        GraphBitOpsCalculator calculator = new GraphBitOpsCalculator();

        long a = 3;
        long b = 5;

        VarBit[] aBits = VarBit.list("a", 16, ZERO);
        VarBit[] bBits = VarBit.list("b", 16, ZERO);

        setVal(calculator, aBits, a);
        setVal(calculator, bBits, b);

        calculator.setInputBits(aBits, bBits);

        calculator.getOptimizers().add(new SimpleOptimizer());
        calculator.getOptimizers().add(new LogicalOptimizer());



        Bit[] r = BinMul.multiply(calculator, aBits, bBits);

        System.out.println(calculator.getOperationCount());

        calculator.setOutputBits(r);

//        calculator.transform(new AndNotOptimizer());

        calculator.calculate();

        calculator.show();
        calculator.showResult();
    }
}
