package net.tislib.binanalyst.test.optimizer;

import static net.tislib.binanalyst.lib.BinValueHelper.*;
import static net.tislib.binanalyst.lib.bit.ConstantBit.ZERO;

import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.VarBit;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.GraphBitOpsCalculator;
import net.tislib.binanalyst.lib.calc.graph.decorator.AndOrCalculatorDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.SimpleOptimizationDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.UnusedBitOptimizerDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.XorAndCalculatorDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.XorOrCalculatorDecorator;
import net.tislib.binanalyst.lib.operator.BinMul;

/**
 * Created by Taleh Ibrahimli on 2/9/18.
 * Email: me@talehibrahimli.com
 */
public class XorOrSystemTest {

    public static void main(String... args) {
        BitOpsGraphCalculator calculator = new GraphBitOpsCalculator();

//        calculator = new XorAndCalculatorDecorator(calculator, true);
        calculator = new XorOrCalculatorDecorator(calculator, false);
        calculator = new SimpleOptimizationDecorator(calculator);
        calculator = new UnusedBitOptimizerDecorator(calculator);

        long a = 23238;
        long b = 34365;

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

//        calculator.show();

        printValues(r);
        System.out.println("MIDDLE SIZE: " + calculator.getMiddle().getBits().size());
        System.out.println(calculator.getOperationCount());
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
