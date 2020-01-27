package net.tislib.binanalyst.test.experiments.optimizer;

import static net.tislib.binanalyst.lib.BinValueHelper.binLength;
import static net.tislib.binanalyst.lib.BinValueHelper.setVal;
import static net.tislib.binanalyst.lib.bit.ConstantBit.ZERO;

import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.VarBit;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.GraphBitOpsCalculator;
import net.tislib.binanalyst.lib.calc.graph.decorator.optimizer.SimpleOptimizationDecorator;
import net.tislib.binanalyst.lib.calc.graph.tools.GraphCalculatorTools;
import net.tislib.binanalyst.lib.operator.BinMul;

/**
 * Created by Taleh Ibrahimli on 2/9/18.
 * Email: me@talehibrahimli.com
 */
public class XorOrSystemTest2 {

    public static void main(String... args) {
        BitOpsGraphCalculator calculator = new GraphBitOpsCalculator();

//        calculator = new XorAndCalculatorDecorator(calculator, true);
//        calculator = new XorOrCalculatorDecorator(calculator, true);
        calculator = new SimpleOptimizationDecorator(calculator);
//        calculator = new UnusedBitOptimizerDecorator(calculator);

        long a = 5;
        long b = 7;

        //32532325, 23403244

        VarBit[] aBits = VarBit.list("a", binLength(a), ZERO);
        VarBit[] bBits = VarBit.list("b", binLength(b), ZERO);


        setVal(aBits, a);
        setVal(bBits, b);

        calculator.setInputBits(aBits, bBits);

        prepareCommonOps(calculator);

        Bit[] r = BinMul.multiply(calculator, aBits, bBits);

        calculator.setOutputBits(r);

        calculator.calculate();

        System.out.println(GraphCalculatorTools.getMaxDepth(calculator));

//        Bit[] bits = GraphCalculatorTools.findReferencedBits(calculator, 3, aBits[0], (NamedBit) calculator.not(aBits[0]));
//
//        for (Bit bit : bits) {
//            BinValueHelper.printFormula((NamedBit) bit);
//        }
//        System.out.println("Done: " + bits.length);
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
