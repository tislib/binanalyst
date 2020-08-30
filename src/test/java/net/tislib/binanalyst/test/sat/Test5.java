package net.tislib.binanalyst.test.sat;

import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.sat.matrix.Sat3Matrix;
import net.tislib.binanalyst.lib.operator.BinMul;
import net.tislib.binanalyst.test.examples.SimpleTestCalculators;

public class Test5 {

    public static void main(String[] args) {
        BitOpsGraphCalculator calc = SimpleTestCalculators.nBitFunction(4, BinMul::multiply, "NONE", "ANDOR");

        byte[][] res = Sat3Matrix.buildSat3Matrix(calc, 5);

        Sat3Matrix.print(res);
    }

}
