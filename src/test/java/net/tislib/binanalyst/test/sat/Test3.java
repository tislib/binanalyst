package net.tislib.binanalyst.test.sat;

import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.sat.TNegSatMutation;
import net.tislib.binanalyst.lib.calc.graph.sat.TSatMutation2;
import net.tislib.binanalyst.lib.operator.BinMul;
import net.tislib.binanalyst.test.examples.SimpleTestCalculators;

public class Test3 {

    public static void main(String[] args) {
        BitOpsGraphCalculator calculator = SimpleTestCalculators.nBitFunction(16, BinMul::multiply, "NONE", "NONE");

        TNegSatMutation tSatMutation = new TNegSatMutation();

        BitOpsGraphCalculator calc = tSatMutation.buildWithMutation(calculator, 0, new String[]{
                "full",
        });
    }
}
