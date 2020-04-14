package net.tislib.binanalyst.test.sat;

import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.NamedBit;
import net.tislib.binanalyst.lib.bit.OperationalBit;
import net.tislib.binanalyst.lib.bit.VarBit;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.GraphBitOpsCalculator;
import net.tislib.binanalyst.lib.calc.graph.decorator.ConstantOperationRemoverOptimizationDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.optimizer.Logical2OptimizationDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.optimizer.SimpleOptimizationDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.optimizer.UnusedBitOptimizerDecorator;
import net.tislib.binanalyst.lib.calc.graph.sat.SimpleSatTester;
import net.tislib.binanalyst.lib.calc.graph.sat.TSatBuilder;
import net.tislib.binanalyst.lib.operator.BinMul;
import net.tislib.binanalyst.test.examples.SimpleTestCalculators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.tislib.binanalyst.lib.calc.graph.sat.SimpleSatTester.hasSolution;

public class SatTest1 {

    public static void main(String... args) {

        int bitCount = 30;
        long maxR = 1L << (bitCount * 2);
        for (long i = 0; i < maxR; i++) {
            boolean res2 = buildFormula2(bitCount, i);
            boolean res1 = buildFormula1(bitCount, i);
            System.out.println(res1 + " " + res2 + " " + i);
            if (res1 != res2) {
                System.out.println("diff found");
            }
        }

    }

    private static boolean buildFormula1(int bitCount, long i) {
        BitOpsGraphCalculator calc = buildSat(bitCount, i);

        return hasSolution(calc);
    }

    private static BitOpsGraphCalculator buildSat(int bitCount, long r) {
        BitOpsGraphCalculator twoBitMul = SimpleTestCalculators.nBitFunction(bitCount, BinMul::multiply, "NONE", "ANDOR");

        return SimpleSatTester.buildSat(twoBitMul, r);
    }

    private static boolean buildFormula2(int bitLength, long num) {
        BitOpsGraphCalculator twoBitMul = SimpleTestCalculators.nBitFunction(bitLength, BinMul::multiply, "NONE", "ANDOR");

        TSatBuilder satBuilder = new TSatBuilder();

        return satBuilder.hasSolution(twoBitMul, num);
    }

    private static VarBit fromBit(NamedBit namedBit) {
        VarBit varBit = new VarBit();
        varBit.setName(namedBit.getName() + "R");
        return varBit;
    }
}
