package net.tislib.binanalyst.test.sat;

import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.VarBit;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.GraphBitOpsCalculator;
import net.tislib.binanalyst.lib.calc.graph.sat.TSatMutation2;
import org.junit.Assert;

import static net.tislib.binanalyst.lib.bit.ConstantBit.ZERO;

public class SatTest2 {

    public static void main(String... args) {
//        BitOpsGraphCalculator twoBitMul = SimpleTestCalculators.nBitFunction(2, BinMul::multiply, "NONE", "NONE");
//
//        TSatMutation2 satBuilder = new TSatMutation2();
//
//        Assert.assertTrue(satBuilder.hasSolution(twoBitMul, 0));
//        Assert.assertFalse(satBuilder.hasSolution(twoBitMul, 15));

        TSatMutation2 satBuilder = new TSatMutation2();

        Assert.assertFalse(satBuilder.hasSolution(makeCalculator(), 2));
        Assert.assertTrue(satBuilder.hasSolution(makeCalculator(), 3));
        Assert.assertTrue(satBuilder.hasSolution(makeCalculator(), 1));
    }

    private static BitOpsGraphCalculator makeCalculator() {
        BitOpsGraphCalculator calculator = new GraphBitOpsCalculator();

        VarBit[] input = VarBit.list("a", 2, ZERO);

        calculator.setInputBits(input);
        calculator.setOutputBits(new Bit[]{
                calculator.and(input[0], input[1]),
                calculator.or(input[0], input[1]),
        });
        return calculator;
    }
}
