package net.tislib.binanalyst.test.experiments.unknownvalue;

import static net.tislib.binanalyst.lib.BinValueHelper.printValues;
import static net.tislib.binanalyst.lib.BinValueHelper.setVal;
import static net.tislib.binanalyst.lib.bit.ConstantBit.UNKNOWN;

import net.tislib.binanalyst.lib.bit.BinaryValue;
import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.VarBit;
import net.tislib.binanalyst.lib.calc.graph.GraphBitOpsCalculator;
import net.tislib.binanalyst.lib.calc.graph.optimizer.LogicalOptimizer;
import net.tislib.binanalyst.lib.calc.graph.optimizer.SimpleOptimizer;
import net.tislib.binanalyst.lib.operator.BinMul;

/**
 * Created by Taleh Ibrahimli on 2/9/18.
 * Email: me@talehibrahimli.com
 */
public class Test31 {

    public static void main(String... args) {
        test1();

        test2();

    }

    private static void test1() {
        GraphBitOpsCalculator calculator = new GraphBitOpsCalculator();

        VarBit[] aBits = VarBit.list("a", 5, UNKNOWN);
        VarBit[] bBits = VarBit.list("b", 5, UNKNOWN);

        calculator.getOptimizers().add(new SimpleOptimizer());
        calculator.getOptimizers().add(new LogicalOptimizer());

//        aBits[0] = VarBit.wrap("", BinaryValue.FALSE);
//        bBits[0] = VarBit.wrap("", BinaryValue.FALSE);
//
        aBits[1] = VarBit.wrap("", BinaryValue.TRUE);
        bBits[1] = VarBit.wrap("", BinaryValue.TRUE);
//
        aBits[2] = VarBit.wrap("", BinaryValue.FALSE);
        bBits[2] = VarBit.wrap("", BinaryValue.FALSE);
//
        aBits[3] = VarBit.wrap("", BinaryValue.FALSE);
        bBits[3] = VarBit.wrap("", BinaryValue.TRUE);

        aBits[4] = VarBit.wrap("", BinaryValue.TRUE);
        bBits[4] = VarBit.wrap("", BinaryValue.TRUE);

        Bit[] r = BinMul.multiply(calculator, aBits, bBits);

        calculator.setInputBits(aBits, bBits);
        calculator.setOutputBits(r);

        calculator.calculate();

        calculator.showResult();

        calculator.clean();
    }

    private static void test2() {
        GraphBitOpsCalculator calculator = new GraphBitOpsCalculator();

        VarBit[] aBits = VarBit.list("a", 5, UNKNOWN);
        VarBit[] bBits = VarBit.list("b", 5, UNKNOWN);

        calculator.getOptimizers().add(new SimpleOptimizer());
        calculator.getOptimizers().add(new LogicalOptimizer());

        setVal(aBits, 23);
        setVal(bBits, 21);

        System.out.print("aBits: ");
        printValues(aBits);

        System.out.print("bBits: ");
        printValues(bBits);

        Bit[] r = BinMul.multiply(calculator, aBits, bBits);

        calculator.setInputBits(aBits, bBits);
        calculator.setOutputBits(r);

        calculator.calculate();

        calculator.showResult();

        calculator.clean();
    }
}
