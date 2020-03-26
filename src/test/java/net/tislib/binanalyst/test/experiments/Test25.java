package net.tislib.binanalyst.test.experiments;

import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.operator.BinMul;
import net.tislib.binanalyst.test.examples.SimpleTestCalculators;

public class Test25 {

    public static void main(String[] args) {
        int bitCount = 3;
        BitOpsGraphCalculator calculator = SimpleTestCalculators.nBitFunction(bitCount, BinMul::multiply, "ANDOR", "TWO_BIT");


        calculator.show();

    }
}
