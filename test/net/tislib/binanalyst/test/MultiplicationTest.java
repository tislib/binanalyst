package net.tislib.binanalyst.test;

import static junit.framework.TestCase.assertTrue;
import static net.tislib.binanalyst.lib.BinValueHelper.*;
import static net.tislib.binanalyst.lib.bit.ConstantBit.ZERO;
import static org.junit.Assert.assertEquals;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;
import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.VarBit;
import net.tislib.binanalyst.lib.calc.SimpleBitOpsCalculator;
import net.tislib.binanalyst.lib.calc.graph.GraphBitOpsCalculator;
import net.tislib.binanalyst.lib.calc.graph.expression.GraphExpression;
import net.tislib.binanalyst.lib.calc.graph.optimizer.LogicalOptimizer;
import net.tislib.binanalyst.lib.calc.graph.optimizer.NfOptimizer;
import net.tislib.binanalyst.lib.calc.graph.solver.SimpleSolver;
import net.tislib.binanalyst.lib.operator.BinMul;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 * Created by Taleh Ibrahimli on 2/4/18.
 * Email: me@talehibrahimli.com
 */
@RunWith(Parameterized.class)
public class MultiplicationTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {23234, 34345}, {455, 65632}, {3, 23443244}, {32932321, 1}, {2, 1}, {5, 6}, {2, 422},
                {23235, 34335}, {415, 65532}, {2, 23473244}, {32832322, 2}, {1, 209}, {5, 6}, {2, 422},
                {23236, 34345}, {425, 65032}, {1, 23483244}, {32732323, 3}, {2, 3}, {5, 6}, {2, 422},
                {23237, 34355}, {435, 65732}, {3, 23493244}, {32632324, 4}, {3, 4}, {5, 6}, {2, 422},
                {23238, 34365}, {445, 65832}, {5, 23403244}, {32532325, 5}, {4, 5}, {5, 6}, {2, 422},
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

        assertEquals(toLong(r), a.multiply(b));
    }

    @Test
    public void graphCalcOptimized() {
        GraphBitOpsCalculator calculator = new GraphBitOpsCalculator();

        VarBit[] aBits = VarBit.list("a", 8, ZERO);
        VarBit[] bBits = VarBit.list("b", 8, ZERO);

        if (a.longValue() > 1 << 8 | b.longValue() > 1 << 8) {
            return;
        }

        setVal(calculator, aBits, a.longValue());
        setVal(calculator, bBits, b.longValue());

        calculator.getOptimizers().add(new LogicalOptimizer());
        calculator.getOptimizers().add(new NfOptimizer());

        calculator.setInputBits(aBits, bBits);

        Bit[] r = BinMul.multiply(calculator, aBits, bBits);

        calculator.setOutputBits(r);

        calculator.optimize();

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

        calculator.getOptimizers().add(new LogicalOptimizer());

        Bit[] r = BinMul.multiply(calculator, aBits, bBits);

        calculator.calculate();

        assertEquals(a.multiply(b), toLong(r));
    }


    @Test
    public void graphExpressionValidator() {
        GraphExpression graphExpression = Sampler.graphExpressionSampler(64, a.longValue(), b.longValue());
        assertTrue(graphExpression.check());
    }


    @Test
    public void graphExpressionSimpleSolverValidator() {
        GraphExpression graphExpression = Sampler.graphExpressionSampler(64, a.longValue(), b.longValue());
        SimpleSolver solver = new SimpleSolver();
        graphExpression = solver.solve(graphExpression);
        assertTrue(graphExpression.check());
    }


}
