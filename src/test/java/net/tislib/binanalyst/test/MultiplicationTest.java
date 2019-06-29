package net.tislib.binanalyst.test;

import static junit.framework.TestCase.assertTrue;
import static net.tislib.binanalyst.lib.BinValueHelper.*;
import static net.tislib.binanalyst.lib.bit.ConstantBit.ZERO;
import static org.junit.Assert.assertEquals;

import java.math.BigInteger;
import java.util.Collection;
import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.VarBit;
import net.tislib.binanalyst.lib.calc.SimpleBitOpsCalculator;
import net.tislib.binanalyst.lib.calc.graph.GraphBitOpsCalculator;
import net.tislib.binanalyst.lib.calc.graph.expression.GraphExpression;
import net.tislib.binanalyst.lib.calc.graph.optimizer.LogicalOptimizer;
import net.tislib.binanalyst.lib.calc.graph.optimizer.NfOptimizer;
import net.tislib.binanalyst.lib.calc.graph.solver.SimpleSolver;
import net.tislib.binanalyst.lib.operator.BinAdd;
import net.tislib.binanalyst.lib.operator.BinMul;
import net.tislib.binanalyst.lib.operator.BinMulRec;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 * Created by Taleh Ibrahimli on 2/4/18.
 * Email: me@talehibrahimli.com
 */
@RunWith(Parameterized.class)
public class MultiplicationTest {

    private final BigInteger a;
    private final BigInteger b;
    public MultiplicationTest(long a, long b) {
        this.a = BigInteger.valueOf(a);
        this.b = BigInteger.valueOf(b);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return TestData.testPairData();
    }

    @Test
    public void simpleCalc() {
        SimpleBitOpsCalculator calculator = new SimpleBitOpsCalculator();
        Bit[] r = BinMul.multiply(calculator, trim(getBits(calculator, a.longValue())), trim(getBits(calculator, b.longValue())));
        assertEquals(a.multiply(b), toLong(r));
    }

    @Test
    public void add2Simple() {
        SimpleBitOpsCalculator calculator = new SimpleBitOpsCalculator();
        Bit[] r = BinAdd.add(calculator, trim(getBits(calculator, a.longValue())), trim(getBits(calculator, b.longValue())));
        System.out.println(a + " " + b);
        assertEquals(a.add(b), toLong(r));

    }

    @Test
    public void add2Tree() {
        SimpleBitOpsCalculator calculator = new SimpleBitOpsCalculator();
        Bit[] r = BinMulRec.add2(calculator, trim(getBits(calculator, a.longValue())), trim(getBits(calculator, b.longValue())));
        System.out.println(a + " " + b);
        assertEquals(a.add(b), toLong(r));

    }

    @Test
    public void multiplyTree2() {
        SimpleBitOpsCalculator calculator = new SimpleBitOpsCalculator();
        Bit[] r = BinMulRec.multiplyTree2Rec(calculator, trim(getBits(calculator, a.longValue())), trim(getBits(calculator, b.longValue())), false);
        System.out.println(a + " " + b);
        assertEquals(a.multiply(b), toLong(r));
    }

    @Test
    public void multiplyTree22() {
        SimpleBitOpsCalculator calculator = new SimpleBitOpsCalculator();
        Bit[] r = BinMulRec.multiplyTree2Rec(calculator, trim(getBits(calculator, a.longValue())), trim(getBits(calculator, b.longValue())), true);
        System.out.println(a + " " + b);
        assertEquals(a.multiply(b), toLong(r));
    }

//    @Test
//    public void binMul2() {
//        SimpleBitOpsCalculator calculator = new SimpleBitOpsCalculator();
//        Bit[] r = BinMul.multiply2(calculator, trim(getBits(calculator, a.longValue())), trim(getBits(calculator, b.longValue())));
//        assertEquals(toLong(r), a.multiply(b));
//    }

    @Test
    public void graphCalc() {
        GraphBitOpsCalculator calculator = new GraphBitOpsCalculator();

        VarBit[] aBits = VarBit.list("a", 64, ZERO);
        VarBit[] bBits = VarBit.list("b", 64, ZERO);

        setVal(aBits, a.longValue());
        setVal(bBits, b.longValue());

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

        setVal(aBits, a.longValue());
        setVal(bBits, b.longValue());

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

        setVal(aBits, a.longValue());
        setVal(bBits, b.longValue());

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

        setVal(aBits, a.longValue());
        setVal(bBits, b.longValue());

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
