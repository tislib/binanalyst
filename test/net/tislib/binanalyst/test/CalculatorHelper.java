package net.tislib.binanalyst.test;

import static net.tislib.binanalyst.lib.BinValueHelper.*;
import static net.tislib.binanalyst.lib.bit.ConstantBit.UNKNOWN;

import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.VarBit;
import net.tislib.binanalyst.lib.calc.graph.GraphBitOpsCalculator;
import net.tislib.binanalyst.lib.calc.graph.optimizer.LogicalOptimizer;
import net.tislib.binanalyst.lib.calc.graph.optimizer.SimpleOptimizer;
import net.tislib.binanalyst.lib.operator.BinMul;

public class CalculatorHelper {

    public static Bit[] doMultiplication(long a, long b) {
        GraphBitOpsCalculator calculator = new GraphBitOpsCalculator();

        VarBit[] aBits = VarBit.list("a", binLength(a), UNKNOWN);
        VarBit[] bBits = VarBit.list("b", binLength(b), UNKNOWN);

        calculator.getOptimizers().add(new SimpleOptimizer());
        calculator.getOptimizers().add(new LogicalOptimizer());

        setVal(calculator, aBits, a);
        setVal(calculator, bBits, b);

        Bit[] r = BinMul.multiply(calculator, aBits, bBits);

        calculator.setInputBits(aBits, bBits);
        calculator.setOutputBits(r);

        calculator.calculate();
        return r;
    }

}
