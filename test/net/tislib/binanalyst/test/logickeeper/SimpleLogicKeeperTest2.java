package net.tislib.binanalyst.test.logickeeper;

import static net.tislib.binanalyst.lib.BinValueHelper.binLength;
import static net.tislib.binanalyst.lib.BinValueHelper.setVal;
import static net.tislib.binanalyst.lib.bit.ConstantBit.ZERO;

import net.tislib.binanalyst.lib.BinValueHelper;
import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.VarBit;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.GraphBitOpsCalculator;
import net.tislib.binanalyst.lib.calc.graph.decorator.AndOrCalculatorDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.SimpleOptimizationDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.UnusedBitOptimizerDecorator;
import net.tislib.binanalyst.lib.calc.logickeeper.BitLogicalKeeper;
import net.tislib.binanalyst.lib.calc.logickeeper.SimpleBitLogicKeeper;
import net.tislib.binanalyst.lib.operator.BinMul;
import org.junit.Test;

public class SimpleLogicKeeperTest2 {

    @Test
    public void test1() {

        BitOpsGraphCalculator calculator = new GraphBitOpsCalculator();

        calculator = new AndOrCalculatorDecorator(calculator, true);
        calculator = new SimpleOptimizationDecorator(calculator);
//        calculator = new UnusedBitOptimizerDecorator(calculator);

        long c = 3 * 7;

        VarBit[] aBits = VarBit.list("a", binLength(c), ZERO);
        VarBit[] bBits = VarBit.list("b", binLength(c), ZERO);
        VarBit[] cBits = VarBit.list("b", binLength(c), ZERO);

        setVal(cBits, c);

        calculator.setInputBits(aBits, bBits);

        Bit[] r = BinMul.multiply(calculator, aBits, bBits);

        calculator.setOutputBits(r);

        cBits = BinValueHelper.setLength(cBits, r.length);


        BitLogicalKeeper bitLogicalKeeper = new SimpleBitLogicKeeper(calculator);

        bitLogicalKeeper.setResultingInput(aBits);
        bitLogicalKeeper.setInput(aBits, bBits);

        for (int i = 0; i < cBits.length; i++) {
            VarBit varBit = cBits[i];
            bitLogicalKeeper.restrict(varBit.getValue().isTrue() ? r[i] : calculator.not(r[i]));
        }

        bitLogicalKeeper.calculate();

        System.out.println("variation count: " + bitLogicalKeeper.getVariationCount());
        System.out.println("operation count: " + bitLogicalKeeper.getOperationCount());
        System.out.println("result: ");
        bitLogicalKeeper.getResult().forEach(BinValueHelper::printValues);

    }

}
