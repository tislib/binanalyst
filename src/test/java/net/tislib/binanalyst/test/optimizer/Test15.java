package net.tislib.binanalyst.test.optimizer;

import static net.tislib.binanalyst.lib.BinValueHelper.setVal;
import static net.tislib.binanalyst.lib.bit.ConstantBit.ZERO;

import net.tislib.binanalyst.lib.BinValueHelper;
import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.NamedBit;
import net.tislib.binanalyst.lib.bit.VarBit;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.GraphBitOpsCalculator;
import net.tislib.binanalyst.lib.calc.graph.decorator.SimpleOptimizationDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.UnusedBitOptimizerDecorator;
import net.tislib.binanalyst.lib.calc.graph.tools.GraphCalculatorTools;
import net.tislib.binanalyst.lib.operator.BinMul;

/**
 * Created by Taleh Ibrahimli on 2/9/18.
 * Email: me@talehibrahimli.com
 */
public class Test15 {

    public static void main(String... args) {
        BitOpsGraphCalculator calculator = new GraphBitOpsCalculator();

//        calculator = new AndOrCalculatorDecorator(calculator, true);
//        calculator = new XorOrCalculatorDecorator(calculator, true);
        calculator = new SimpleOptimizationDecorator(calculator);
        calculator = new UnusedBitOptimizerDecorator(calculator);

        long a = 5;
        long b = 7;

        //32532325, 23403244

        VarBit[] aBits = VarBit.list("a", 2, ZERO);
        VarBit[] bBits = VarBit.list("b", 2, ZERO);


        setVal(aBits, a);
        setVal(bBits, b);

        calculator.setInputBits(aBits, bBits);

        prepareCommonOps(calculator);

        Bit[] r = BinMul.multiply(calculator, aBits, bBits);

        calculator.setOutputBits(r);

        calculator.calculate();

//        calculator.show();

//        printValues(r);
        System.out.println("MIDDLE SIZE: " + calculator.getMiddle().getBits().size());
        System.out.println("DEPTH: " + GraphCalculatorTools.getMaxDepth(calculator));
//
        for (int i = 0; i < r.length; i++) {
            BinValueHelper.printFormula((NamedBit) r[r.length - i - 1]);
        }
    }

    private static void prepareCommonOps(BitOpsGraphCalculator calculator) {
        for (Bit bit : calculator.getInput()) {
            calculator.not(bit);
        }
        for (Bit bit1 : calculator.getInput()) {
            for (Bit bit2 : calculator.getInput()) {
                calculator.and(bit1, bit2);
            }
        }
    }

}
