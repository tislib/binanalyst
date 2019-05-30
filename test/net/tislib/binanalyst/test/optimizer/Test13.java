package net.tislib.binanalyst.test.optimizer;

import static net.tislib.binanalyst.lib.BinValueHelper.setVal;
import static net.tislib.binanalyst.lib.bit.ConstantBit.ZERO;

import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.VarBit;
import net.tislib.binanalyst.lib.calc.graph.GraphBitOpsCalculator;
import net.tislib.binanalyst.lib.calc.graph.optimizer.LogicalOptimizer;
import net.tislib.binanalyst.lib.calc.graph.optimizer.SimpleOptimizer;
import net.tislib.binanalyst.lib.neo.NeoGraphExpressionRenderer;
import net.tislib.binanalyst.lib.operator.BinMul;

/**
 * Created by Taleh Ibrahimli on 2/9/18.
 * Email: me@talehibrahimli.com
 */
public class Test13 {

    public static void main(String... args) {
        GraphBitOpsCalculator calculator = new GraphBitOpsCalculator();

        long a = 1;
        long b = 3;

        VarBit[] aBits = VarBit.list("a", 2, ZERO);
        VarBit[] bBits = VarBit.list("b", 2, ZERO);

        setVal(aBits, a);
        setVal(bBits, b);

        calculator.setInputBits(aBits, bBits);

        calculator.getOptimizers().add(new SimpleOptimizer());
        calculator.getOptimizers().add(new LogicalOptimizer());


        try (NeoGraphExpressionRenderer neoGraphExpressionRenderer = new NeoGraphExpressionRenderer("Bit3")) {

            Bit[] r = BinMul.multiply(calculator, aBits, bBits);

            calculator.setOutputBits(r);

            calculator.optimize();

            System.out.println(calculator.getOperationCount());
            System.out.println("MIDDLE SIZE: " + calculator.getMiddle().getBits().size());

            calculator.calculate();

//            calculator.show();
            calculator.showResult();

            calculator.showOutputFull();

//            neoGraphExpressionRenderer.render(calculator);
        }
    }
}
