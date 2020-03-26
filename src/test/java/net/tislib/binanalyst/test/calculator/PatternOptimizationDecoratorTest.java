package net.tislib.binanalyst.test.calculator;

import net.tislib.binanalyst.lib.bit.*;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.GraphBitOpsCalculator;
import net.tislib.binanalyst.lib.calc.graph.decorator.optimizer.UnusedBitOptimizerDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.optimizer.pattern.PatternOptimizationDecorator;
import net.tislib.binanalyst.test.lib.CalculationTestRule;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class PatternOptimizationDecoratorTest {

    @Rule
    public CalculationTestRule calculationTestRule = new CalculationTestRule();

    private BitOpsGraphCalculator calculator;

    @Before
    public void init() {
        calculationTestRule.setOnUpdate(() -> {
            calculator = new GraphBitOpsCalculator();
            calculator = new PatternOptimizationDecorator(calculator);
            calculator = new UnusedBitOptimizerDecorator(calculator);
            calculationTestRule.setCalculator(calculator);
        });
    }

    @Test
    public void basicTests() {
        // double not
        calculationTestRule.checkTransformation(item ->
                        calculator.not(calculator.not(item)),
                item -> item);

        // a or not a
        calculationTestRule.checkTransformationResult(item ->
                        calculator.or(item, calculator.not(item)),
                item -> "!0");

        // a or not aa and not a
        calculationTestRule.checkTransformationResult(item ->
                        calculator.and(item, calculator.not(item)),
                item -> "0");

        // a or not aa and not a
        calculationTestRule.checkTransformationResult(item ->
                        calculator.and(calculator.not(item), item),
                item -> "01");
    }

    @Test
    public void xorTest() {
        BitOpsGraphCalculator calculator = new GraphBitOpsCalculator();
        calculator = new PatternOptimizationDecorator(calculator);


        calculator = new UnusedBitOptimizerDecorator(calculator);
//        calculator = new SimpleOptimizationDecorator(calculator);

        VarBit[] bits = VarBit.list("a", 1, ConstantBit.ZERO);

        calculator.setInputBits(bits);

        calculator.setOutputBits(new Bit[]{
                calculator.not(calculator.xor(bits[0], calculator.not(bits[0]))),
                calculator.xor(bits[0], bits[0]),
                calculator.xor(bits[0], bits[0], bits[0]),
        });

        calculator.optimize();

        Assert.assertEquals(calculator.getOutput().getBitL(2).toString(), "0");
        Assert.assertEquals(calculator.getOutput().getBitL(1).toString(), "0");
        Assert.assertNotEquals(calculator.getOutput().getBitL(0).toString(), "0");
    }

    @Test
    public void lackTest() {
        BitOpsGraphCalculator calculator = new GraphBitOpsCalculator();
        calculator = new PatternOptimizationDecorator(calculator);


        calculator = new UnusedBitOptimizerDecorator(calculator);
//        calculator = new SimpleOptimizationDecorator(calculator);

        VarBit[] bits = VarBit.list("a", 1, ConstantBit.ZERO);

        calculator.setInputBits(bits);

        calculator.setOutputBits(new Bit[]{
                calculator.and(bits[0]),
                calculator.or(bits[0]),
                calculator.xor(bits[0]),

                calculator.and(bits[0], ConstantBit.ZERO),
                calculator.not(calculator.or(calculator.and(bits[0], ConstantBit.ONE), calculator.not(bits[0]))),
                calculator.not(calculator.or(bits[0], ConstantBit.ONE)),
        });

        calculator.optimize();

        Assert.assertEquals(calculator.getOutput().getBitL(0).toString(), "0");
        Assert.assertEquals(calculator.getOutput().getBitL(1).toString(), "0");
        Assert.assertEquals(calculator.getOutput().getBitL(2).toString(), "0");

        Assert.assertFalse(calculator.getOutput().getBitL(3) instanceof OperationalBit);
        Assert.assertFalse(calculator.getOutput().getBitL(4) instanceof OperationalBit);
        Assert.assertFalse(calculator.getOutput().getBitL(5) instanceof OperationalBit);
    }

}
