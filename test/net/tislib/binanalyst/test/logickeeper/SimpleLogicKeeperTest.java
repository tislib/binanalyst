package net.tislib.binanalyst.test.logickeeper;

import static net.tislib.binanalyst.lib.bit.ConstantBit.ZERO;

import java.util.Arrays;
import net.tislib.binanalyst.lib.bit.VarBit;
import net.tislib.binanalyst.lib.calc.graph.GraphBitOpsCalculator;
import net.tislib.binanalyst.lib.calc.logickeeper.BitLogicalKeeper;
import net.tislib.binanalyst.lib.calc.logickeeper.SimpleBitLogicKeeper;
import org.junit.Test;

public class SimpleLogicKeeperTest {

    @Test
    public void test1() {
        // 101 001
        GraphBitOpsCalculator calculator = new GraphBitOpsCalculator();
        BitLogicalKeeper bitLogicalKeeper = new SimpleBitLogicKeeper(calculator);

        VarBit[] bits = VarBit.list("a", 3, ZERO);
        bitLogicalKeeper.setInput(bits);

        bitLogicalKeeper.restrict(calculator.and(bits[2], calculator.not(bits[1])));
        // should be gulped
        bitLogicalKeeper.restrict(calculator.or(bits[2], calculator.not(bits[1])));
        // should be gulped
        bitLogicalKeeper.restrict(calculator.or(bits[0], calculator.not(bits[0])));

        bitLogicalKeeper.calculate();

        System.out.println("variation count: " + bitLogicalKeeper.getVariationCount());
        System.out.println("result: " + Arrays.asList(bitLogicalKeeper.getResult()));
    }

}
