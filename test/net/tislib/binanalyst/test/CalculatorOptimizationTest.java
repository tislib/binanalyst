package net.tislib.binanalyst.test;

import static net.tislib.binanalyst.lib.BinValueHelper.*;
import static net.tislib.binanalyst.lib.bit.ConstantBit.ZERO;
import static org.junit.Assert.assertEquals;

import java.math.BigInteger;
import java.util.Collection;
import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.VarBit;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.GraphBitOpsCalculator;
import net.tislib.binanalyst.lib.calc.graph.decorator.AndOrCalculatorDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.SimpleOptimizationDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.UnusedBitOptimizerDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.XorAndCalculatorDecorator;
import net.tislib.binanalyst.lib.operator.BinMul;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 * Created by Taleh Ibrahimli on 2/4/18.
 * Email: me@talehibrahimli.com
 */
@RunWith(Parameterized.class)
public class CalculatorOptimizationTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return TestData.testPairData();
    }

    private final BigInteger a;
    private final BigInteger b;

    public CalculatorOptimizationTest(long a, long b) {
        this.a = BigInteger.valueOf(a);
        this.b = BigInteger.valueOf(b);
    }

    @Test
    public void XorAndTest() {
        BitOpsGraphCalculator calculator = new GraphBitOpsCalculator();

        calculator = new XorAndCalculatorDecorator(calculator, false);
        calculator = new SimpleOptimizationDecorator(calculator);

        check(calculator);
    }

    @Test
    public void UnusedBitOptimizer() {
        BitOpsGraphCalculator calculator = new GraphBitOpsCalculator();

        calculator = new UnusedBitOptimizerDecorator(calculator);

        check(calculator);
    }

    @Test
    public void XorAndTestReverse() {
        BitOpsGraphCalculator calculator = new GraphBitOpsCalculator();

        calculator = new XorAndCalculatorDecorator(calculator, true);
        calculator = new SimpleOptimizationDecorator(calculator);

        check(calculator);
    }

    @Test
    public void AndOrTest() {
        BitOpsGraphCalculator calculator = new GraphBitOpsCalculator();

        calculator = new AndOrCalculatorDecorator(calculator, false);
        calculator = new SimpleOptimizationDecorator(calculator);

        check(calculator);
    }

    @Test
    public void AndOrTestReverse() {
        BitOpsGraphCalculator calculator = new GraphBitOpsCalculator();

        calculator = new AndOrCalculatorDecorator(calculator, true);
        calculator = new SimpleOptimizationDecorator(calculator);

        check(calculator);
    }

    private void check(BitOpsGraphCalculator calculator) {
        int length = Math.max(binLength(a.longValue()), binLength(b.longValue()));
        VarBit[] aBits = VarBit.list("a", length, ZERO);
        VarBit[] bBits = VarBit.list("b", length, ZERO);

        setVal(calculator, aBits, a.longValue());
        setVal(calculator, bBits, b.longValue());

        calculator.setInputBits(aBits, bBits);

        Bit[] r = BinMul.multiply(calculator, aBits, bBits);

        calculator.setOutputBits(r);

        calculator.calculate();

        assertEquals(a.multiply(b), toLong(r));
    }


}
