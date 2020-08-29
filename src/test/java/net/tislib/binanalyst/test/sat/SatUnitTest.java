package net.tislib.binanalyst.test.sat;

import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.sat.SimpleSatTester;
import net.tislib.binanalyst.lib.calc.graph.sat.TSatBuilder;
import net.tislib.binanalyst.lib.calc.graph.sat.TSatMutation2;
import net.tislib.binanalyst.lib.operator.BinMul;
import net.tislib.binanalyst.test.examples.SimpleTestCalculators;
import org.junit.Assert;
import org.junit.Test;

import static net.tislib.binanalyst.lib.calc.graph.sat.SimpleSatTester.hasSolution;

public class SatUnitTest {

    @Test
    public void test1() {
        int bitCount = 2;
        int maxR = 1 << (bitCount * 2);
        for (int i = 0; i < maxR; i++) {
            boolean res1 = buildFormula1(bitCount, i);
            boolean res2 = buildFormula2(bitCount, i);
            Assert.assertEquals(res1, res2);
        }
    }

    @Test
    public void test2() {
        int bitCount = 2;
        int maxR = 1 << (bitCount * 2);
        for (int i = 0; i < maxR; i++) {
            boolean res1 = buildFormula1(bitCount, i);
            boolean res2 = buildFormula3(bitCount, i);
            Assert.assertEquals(res1, res2);
        }
    }

    private static boolean buildFormula1(int bitCount, int i) {
        BitOpsGraphCalculator calc = buildSat(bitCount, i);

        return hasSolution(calc);
    }

    private static BitOpsGraphCalculator buildSat(int bitCount, int r) {
        BitOpsGraphCalculator twoBitMul = SimpleTestCalculators.nBitFunction(bitCount, BinMul::multiply, "NONE", "ANDOR");

        return SimpleSatTester.buildSat(twoBitMul, r);
    }

    private static boolean buildFormula2(int bitLength, int num) {
        BitOpsGraphCalculator twoBitMul = SimpleTestCalculators.nBitFunction(bitLength, BinMul::multiply, "NONE", "ANDOR");

        TSatBuilder satBuilder = new TSatBuilder();

        return satBuilder.hasSolution(twoBitMul, num);
    }

    private static boolean buildFormula3(int bitLength, int num) {
        BitOpsGraphCalculator twoBitMul = SimpleTestCalculators.nBitFunction(bitLength, BinMul::multiply, "NONE", "ANDOR");

        TSatMutation2 satBuilder = new TSatMutation2();

        return satBuilder.hasSolution(twoBitMul, num);
    }
}
