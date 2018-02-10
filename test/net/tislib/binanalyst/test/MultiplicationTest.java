package net.tislib.binanalyst.test;

import net.tislib.binanalyst.lib.BinValueHelper;
import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.VarBit;
import net.tislib.binanalyst.lib.calc.SimpleBitOpsCalculator;
import net.tislib.binanalyst.lib.calc.graph.GraphBitOpsCalculator;
import net.tislib.binanalyst.lib.calc.graph.expression.GraphExpression;
import net.tislib.binanalyst.lib.calc.graph.optimizer.LogicalOptimizer;
import net.tislib.binanalyst.lib.calc.graph.optimizer.SimpleOptimizer;
import net.tislib.binanalyst.lib.operator.BinMul;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;

import static net.tislib.binanalyst.lib.BinValueHelper.*;
import static net.tislib.binanalyst.lib.bit.ConstantBit.ZERO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by Taleh Ibrahimli on 2/4/18.
 * Email: me@talehibrahimli.com
 */
@RunWith(Parameterized.class)
public class MultiplicationTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {23234, 34345}, {455, 65632}, {3, 23443244}, {32332324, 4}, {2, 3}, {5, 6}, {2, 422}
        });
    }

    private final BigInteger a;
    private final BigInteger b;

    public MultiplicationTest(long a, long b) {
        this.a = BigInteger.valueOf(a);
        this.b = BigInteger.valueOf(b);
    }

    @Test
    public void simpleCalc() {
        SimpleBitOpsCalculator calculator = new SimpleBitOpsCalculator();
        Bit[] r = BinMul.multiply(calculator, trim(getBits(calculator, a.longValue())), trim(getBits(calculator, b.longValue())));
        assertEquals(toLong(r), a.multiply(b));
    }

    @Test
    public void graphCalc() {
        GraphBitOpsCalculator calculator = new GraphBitOpsCalculator();

        VarBit[] aBits = VarBit.list("a", 64, ZERO);
        VarBit[] bBits = VarBit.list("b", 64, ZERO);

        setVal(calculator, aBits, a.longValue());
        setVal(calculator, bBits, b.longValue());

        calculator.setInputBits(aBits, bBits);

        Bit[] r = BinMul.multiply(calculator, aBits, bBits);

        calculator.calculate();

        assertEquals(a.multiply(b), toLong(r));
    }

    @Test
    public void graphCalcSingleOperator() {
        GraphBitOpsCalculator calculator = new GraphBitOpsCalculator();

        VarBit[] aBits = VarBit.list("a", 64, ZERO);
        VarBit[] bBits = VarBit.list("b", 64, ZERO);

        setVal(calculator, aBits, a.longValue());
        setVal(calculator, bBits, b.longValue());

        calculator.setInputBits(aBits, bBits);

        Bit[] r = BinMul.multiply(calculator, aBits, bBits);

        calculator.calculate();

        assertEquals(a.multiply(b), toLong(r));
    }


    @Test
    public void graphCalcLogicalOptimizer() {
        GraphBitOpsCalculator calculator = new GraphBitOpsCalculator();

        VarBit[] aBits = VarBit.list("a", 64, ZERO);
        VarBit[] bBits = VarBit.list("b", 64, ZERO);

        setVal(calculator, aBits, a.longValue());
        setVal(calculator, bBits, b.longValue());

        calculator.setInputBits(aBits, bBits);

        calculator.getOptimizers().add(new SimpleOptimizer());
        calculator.getOptimizers().add(new LogicalOptimizer());

        Bit[] r = BinMul.multiply(calculator, aBits, bBits);

        calculator.calculate();

        assertEquals(a.multiply(b), toLong(r));
    }


    @Test
    public void graphExpressionValidator() {
        GraphBitOpsCalculator calculator = new GraphBitOpsCalculator();

        VarBit[] aBits = VarBit.list("a", 64, ZERO);
        VarBit[] bBits = VarBit.list("b", 64, ZERO);

        setVal(calculator, aBits, a.longValue());
        setVal(calculator, bBits, b.longValue());

        calculator.setInputBits(aBits, bBits);

        calculator.getOptimizers().add(new SimpleOptimizer());
        calculator.getOptimizers().add(new LogicalOptimizer());

        Bit[] r = BinMul.multiply(calculator, aBits, bBits);

        VarBit[] result = VarBit.list("c", r.length, ZERO);

        setVal(calculator, result, a.longValue() * b.longValue());

        System.out.println(calculator.getOperationCount());

        calculator.setOutputBits(r);

        GraphExpression graphExpression = new GraphExpression();

        graphExpression.setCalculation(calculator, result);

        assertTrue(graphExpression.check());

    }


}
