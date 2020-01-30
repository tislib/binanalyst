package net.tislib.binanalyst.test.calculator;

import net.tislib.binanalyst.lib.bit.BinaryValue;
import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.ConstantBit;
import net.tislib.binanalyst.lib.bit.VarBit;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.GraphBitOpsCalculator;
import net.tislib.binanalyst.lib.calc.graph.UsageFinder;
import net.tislib.binanalyst.lib.calc.graph.decorator.optimizer.DoubleNotRemovalOptimizationDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.optimizer.UnusedBitOptimizerDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.optimizer.pattern.PatternOptimizationDecorator;
import org.junit.Assert;
import org.junit.Test;

public class PatternOptimizationDecoratorTest {

    @Test
    public void notTest() {
        BitOpsGraphCalculator calculator = new GraphBitOpsCalculator();
        calculator = new PatternOptimizationDecorator(calculator);
//        calculator = new DoubleNotRemovalOptimizationDecorator(calculator);


        calculator = new UnusedBitOptimizerDecorator(calculator);

        VarBit[] bits = VarBit.list("a", 1, ConstantBit.ZERO);

        calculator.setInputBits(bits);

        calculator.setOutputBits(new Bit[]{calculator.not(calculator.not(bits[0]))});

        calculator.optimize();

        Assert.assertEquals(calculator.getOutput().getBitL(0), bits[0]);
    }

}
