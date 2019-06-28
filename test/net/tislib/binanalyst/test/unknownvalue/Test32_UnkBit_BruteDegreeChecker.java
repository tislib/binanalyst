package net.tislib.binanalyst.test.unknownvalue;

import static net.tislib.binanalyst.lib.BinValueHelper.binLength;
import static net.tislib.binanalyst.lib.bit.ConstantBit.UNKNOWN;

import java.util.function.BiFunction;
import net.tislib.binanalyst.lib.BinValueHelper;
import net.tislib.binanalyst.lib.bit.BinaryValue;
import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.VarBit;
import net.tislib.binanalyst.lib.calc.graph.GraphBitOpsCalculator;
import net.tislib.binanalyst.lib.calc.graph.optimizer.LogicalOptimizer;
import net.tislib.binanalyst.lib.calc.graph.optimizer.SimpleOptimizer;
import net.tislib.binanalyst.lib.operator.BinMul;
import net.tislib.binanalyst.test.CalculatorHelper;

/**
 * Created by Taleh Ibrahimli on 2/9/18.
 * Email: me@talehibrahimli.com
 */
public class Test32_UnkBit_BruteDegreeChecker {

    private static int checkCounter = 0;

    public static void main(String... args) {
        GraphBitOpsCalculator calculator = new GraphBitOpsCalculator();

        int a = 10007;
        int b = 10099;

        int l = binLength(a);

        VarBit[] aBits = VarBit.list("a", l, UNKNOWN);
        VarBit[] bBits = VarBit.list("b", l, UNKNOWN);

        calculator.getOptimizers().add(new SimpleOptimizer());
        calculator.getOptimizers().add(new LogicalOptimizer());

        Bit[] expectedValues = CalculatorHelper.doMultiplication(a, b);

        doBruteCheck(aBits, bBits, l - 1, (aBits1, bBits1) -> {
            Bit[] r = BinMul.multiply(calculator, aBits1, bBits1);

            calculator.setInputBits(aBits1, bBits1);
            calculator.setOutputBits(r);

            calculator.calculate();

            return check(r, expectedValues);
        });

        System.out.println("check count: " + checkCounter);


    }

    private static boolean doBruteCheck(VarBit[] aBits, VarBit[] bBits, int i, BiFunction<VarBit[], VarBit[], Boolean> isCorrect) {
        aBits = BinValueHelper.clone(aBits);
        bBits = BinValueHelper.clone(bBits);
        checkCounter++;
        if (i < 0) {
            System.out.println("Result:");
            BinValueHelper.printValues(aBits);
            BinValueHelper.printValues(bBits);
            return true;
        }
        aBits[i] = VarBit.wrap("", BinaryValue.FALSE);
        bBits[i] = VarBit.wrap("", BinaryValue.FALSE);
        if (isCorrect.apply(aBits, bBits) && doBruteCheck(aBits, bBits, i - 1, isCorrect)) {
            return true;
        }

        aBits[i] = VarBit.wrap("", BinaryValue.FALSE);
        bBits[i] = VarBit.wrap("", BinaryValue.TRUE);
        if (isCorrect.apply(aBits, bBits) && doBruteCheck(aBits, bBits, i - 1, isCorrect)) {
            return true;
        }

        aBits[i] = VarBit.wrap("", BinaryValue.TRUE);
        bBits[i] = VarBit.wrap("", BinaryValue.FALSE);
        if (isCorrect.apply(aBits, bBits) && doBruteCheck(aBits, bBits, i - 1, isCorrect)) {
            return true;
        }

        aBits[i] = VarBit.wrap("", BinaryValue.TRUE);
        bBits[i] = VarBit.wrap("", BinaryValue.TRUE);
        if (isCorrect.apply(aBits, bBits) && doBruteCheck(aBits, bBits, i - 1, isCorrect)) {
            return true;
        }
        return false;
    }

    private static boolean check(Bit[] r, Bit[] expectedValues) {
        int l = Math.min(r.length, expectedValues.length);
//        System.out.println("checking:");
//        printValues(r);
//        printValues(expectedValues);
        for (int i = 0; i < l; i++) {
            if (!check(r[i], expectedValues[i])) {
                return false;
            }
        }
        return true;
    }

    private static boolean check(Bit actualBit, Bit expectedValue) {
        return actualBit.getValue().mayBe(expectedValue.getValue());
    }

}
