package net.tislib.binanalyst.test.experiments.optimizer;

import static net.tislib.binanalyst.lib.BinValueHelper.setVal;
import static net.tislib.binanalyst.lib.bit.ConstantBit.ZERO;

import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.VarBit;
import net.tislib.binanalyst.lib.calc.graph.GraphBitOpsCalculator;
import net.tislib.binanalyst.lib.calc.graph.optimizer.LogicalOptimizer;
import net.tislib.binanalyst.lib.calc.graph.optimizer.NfOptimizer;
import net.tislib.binanalyst.lib.calc.graph.optimizer.SimpleOptimizer;
import net.tislib.binanalyst.lib.operator.BinMul;

/**
 * Created by Taleh Ibrahimli on 2/9/18.
 * Email: me@talehibrahimli.com
 */
public class Test14 {

    public static void main(String... args) {
        GraphBitOpsCalculator calculator = new GraphBitOpsCalculator();

        long a = 3;
        long b = 7;

        VarBit[] aBits = VarBit.list("a", 3, ZERO);
        VarBit[] bBits = VarBit.list("b", 3, ZERO);

        setVal(aBits, a);
        setVal(bBits, b);

        calculator.setInputBits(aBits, bBits);

        calculator.getOptimizers().add(new SimpleOptimizer());
        calculator.getOptimizers().add(new LogicalOptimizer());
        calculator.getOptimizers().add(new NfOptimizer());


        Bit[] r = BinMul.multiply(calculator, aBits, bBits);

        calculator.setOutputBits(r);

        calculator.optimize();

        System.out.println(calculator.getOperationCount());
        System.out.println("MIDDLE SIZE: " + calculator.getMiddle().getBits().size());

        calculator.calculate();

        calculator.show();
//        calculator.showResult();
//        System.out.println(a * b);

//        calculator.showOutputFull();
    }
}
