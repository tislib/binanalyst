package net.tislib.binanalyst.test.optimizer;

import static net.tislib.binanalyst.lib.BinValueHelper.printValues;
import static net.tislib.binanalyst.lib.BinValueHelper.setVal;
import static net.tislib.binanalyst.lib.bit.ConstantBit.ZERO;

import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.VarBit;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.GraphBitOpsCalculator;
import net.tislib.binanalyst.lib.calc.graph.decorator.XorAndCalculatorDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.SimpleOptimizationDecorator;
import net.tislib.binanalyst.lib.calc.graph.optimizer.LogicalOptimizer;
import net.tislib.binanalyst.lib.calc.graph.optimizer.NfOptimizer;
import net.tislib.binanalyst.lib.calc.graph.optimizer.SimpleOptimizer;
import net.tislib.binanalyst.lib.operator.BinMul;

/**
 * Created by Taleh Ibrahimli on 2/9/18.
 * Email: me@talehibrahimli.com
 */
public class XorOrSystemTest {

    public static void main(String... args) {
        BitOpsGraphCalculator calculator = new GraphBitOpsCalculator();

//        calculator = new XorAndCalculatorDecorator(calculator);
        calculator = new SimpleOptimizationDecorator(calculator);

        long a = 23;
        long b = 37;

        VarBit[] aBits = VarBit.list("a", 8, ZERO);
        VarBit[] bBits = VarBit.list("b", 8, ZERO);



        setVal(calculator, aBits, a);
        setVal(calculator, bBits, b);

        calculator.setInputBits(aBits, bBits);

        Bit[] r = BinMul.multiply(calculator, aBits, bBits);

        calculator.setOutputBits(r);

        calculator.calculate();

        calculator.show();

        printValues(r);
        System.out.println("MIDDLE SIZE: " + calculator.getMiddle().getBits().size());
        System.out.println(calculator.getOperationCount());
    }

}
