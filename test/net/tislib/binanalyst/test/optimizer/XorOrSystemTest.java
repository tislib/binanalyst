package net.tislib.binanalyst.test.optimizer;

import static net.tislib.binanalyst.lib.BinValueHelper.setVal;
import static net.tislib.binanalyst.lib.bit.ConstantBit.ZERO;

import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.VarBit;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.GraphBitOpsCalculator;
import net.tislib.binanalyst.lib.calc.graph.decorator.AndOrCalculatorDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.BinderOptimizationDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.SimpleOptimizationDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.UnusedBitOptimizerDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.XorOrCalculatorDecorator;
import net.tislib.binanalyst.lib.calc.graph.tools.GraphCalculatorTools;
import net.tislib.binanalyst.lib.operator.BinMul;

/**
 * Created by Taleh Ibrahimli on 2/9/18.
 * Email: me@talehibrahimli.com
 */
public class XorOrSystemTest {

    public static void main(String... args) {
        BitOpsGraphCalculator calculator = new GraphBitOpsCalculator();

        calculator = new BinderOptimizationDecorator(calculator);
        calculator = new AndOrCalculatorDecorator(calculator, true);
//        calculator = new XorOrCalculatorDecorator(calculator, true);
        calculator = new SimpleOptimizationDecorator(calculator);
        calculator = new UnusedBitOptimizerDecorator(calculator);

        long a = 5;
        long b = 7;

        //32532325, 23403244

        VarBit[] aBits = VarBit.list("a", 3, ZERO);
        VarBit[] bBits = VarBit.list("b", 3, ZERO);


        setVal(aBits, a);
        setVal(bBits, b);

        calculator.setInputBits(aBits, bBits);

        prepareCommonOps(calculator);

        Bit[] r = BinMul.multiply(calculator, aBits, bBits);

        calculator.setOutputBits(r);

        calculator.calculate();

        calculator.show();

//        printValues(r);
        System.out.println("MIDDLE SIZE: " + calculator.getMiddle().getBits().size());
        System.out.println("DEPTH: " + GraphCalculatorTools.getMaxDepth(calculator));
//
//        for (int i = 0; i < r.length; i++) {
//            BinValueHelper.printFormula((NamedBit) r[r.length - i - 1]);
//        }
    }

    private static void prepareCommonOps(BitOpsGraphCalculator calculator) {
        for (Bit bit : calculator.getInput()) {
            calculator.not(bit);
        }
        int i = 0;
        int l = calculator.getInput().getBits().size();
        Bit[][] M = new Bit[l][l];
        for (Bit bit1 : calculator.getInput()) {
            int j = 0;
            for (Bit bit2 : calculator.getInput()) {
                M[i][j] = calculator.and(bit1, bit2);
                j++;
            }
            i++;
        }

        for (int k1 = 0; k1 < l; k1++) {
            for (int k2 = 0; k2 < l; k2++) {
                for (int k3 = 0; k3 < l; k3++) {
                    for (int k4 = 0; k4 < l; k4++) {
                        calculator.xor(M[k1][k2], M[k3][k4]);
                    }
                }
            }
        }
    }

}
