package net.tislib.binanalyst.test.calculator;

import static net.tislib.binanalyst.lib.BinValueHelper.*;
import static net.tislib.binanalyst.lib.bit.ConstantBit.ZERO;
import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigInteger;
import java.util.Collection;

import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.VarBit;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.GraphBitOpsCalculator;
import net.tislib.binanalyst.lib.calc.graph.decorator.AndOrCalculatorDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.BinderOptimizationDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.ConstantOperationRemoverOptimizationDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.optimizer.Logical2OptimizationDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.optimizer.NewOptimizerOptimizationDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.optimizer.SimpleOptimizationDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.optimizer.UnusedBitOptimizerDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.XorAndCalculatorDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.XorOrCalculatorDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.optimizer.pattern.PatternOptimizationDecorator;
import net.tislib.binanalyst.lib.calc.graph.operations.MutationOperation;
import net.tislib.binanalyst.lib.calc.graph.tools.GraphCalculatorTools;
import net.tislib.binanalyst.lib.calc.graph.tools.GraphCalculatorTools.GraphCalculatorSerializedData;
import net.tislib.binanalyst.lib.operator.BinMul;
import net.tislib.binanalyst.test.TestData;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 * Created by Taleh Ibrahimli on 2/4/18.
 * Email: me@talehibrahimli.com
 */
@RunWith(Parameterized.class)
public class CalculatorTest {

    private final BigInteger a;
    private final BigInteger b;

    public CalculatorTest(long a, long b) {
        this.a = BigInteger.valueOf(a);
        this.b = BigInteger.valueOf(b);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return TestData.testPairData();
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

    @Test
    public void XorOrTest() {
        BitOpsGraphCalculator calculator = new GraphBitOpsCalculator();

        calculator = new XorOrCalculatorDecorator(calculator, false);
        calculator = new SimpleOptimizationDecorator(calculator);

        check(calculator);
    }

    @Test
    public void XorOrTestReverse() {
        BitOpsGraphCalculator calculator = new GraphBitOpsCalculator();

        calculator = new XorOrCalculatorDecorator(calculator, true);
        calculator = new SimpleOptimizationDecorator(calculator);
        calculator = new UnusedBitOptimizerDecorator(calculator);

        check(calculator);
    }

    @Test
    public void binderOptimizationDecorator() {
        if (a.longValue() > 1000 || b.longValue() > 1000) {
            return;
        }

        BitOpsGraphCalculator calculator = new GraphBitOpsCalculator();

        calculator = new BinderOptimizationDecorator(calculator);
        calculator = new XorOrCalculatorDecorator(calculator, true);
        calculator = new SimpleOptimizationDecorator(calculator);
        calculator = new UnusedBitOptimizerDecorator(calculator);

        check(calculator);
    }

    @Test
    public void Logical2OptimizationDecorator() {
        BitOpsGraphCalculator calculator = new GraphBitOpsCalculator();

        calculator = new Logical2OptimizationDecorator(calculator);
        calculator = new SimpleOptimizationDecorator(calculator);
        calculator = new UnusedBitOptimizerDecorator(calculator);

        check(calculator);
    }

//    @Test
//    public void NewOptimizerOptimizationDecorator() {
//        BitOpsGraphCalculator calculator = new GraphBitOpsCalculator();
//
//        calculator = new NewOptimizerOptimizationDecorator(calculator);
//        calculator = new SimpleOptimizationDecorator(calculator);
//        calculator = new UnusedBitOptimizerDecorator(calculator);
//
//        check(calculator);
//    }

    @Test
    public void constantOperationRemoverOptimizationDecorator() {
        if (a.longValue() > 1000 || b.longValue() > 1000) {
            return;
        }
        BitOpsGraphCalculator calculator = new GraphBitOpsCalculator();

        calculator = new BinderOptimizationDecorator(calculator);
        calculator = new XorOrCalculatorDecorator(calculator, true);
        calculator = new SimpleOptimizationDecorator(calculator);
        calculator = new ConstantOperationRemoverOptimizationDecorator(calculator);
        calculator = new UnusedBitOptimizerDecorator(calculator);

        check(calculator);
    }

    @Test
    @Ignore
    public void PatternOptimizationDecorator() {
        BitOpsGraphCalculator calculator = new GraphBitOpsCalculator();

        calculator = new PatternOptimizationDecorator(calculator);
        calculator = new AndOrCalculatorDecorator(calculator, true);
        calculator = new SimpleOptimizationDecorator(calculator);
        calculator = new ConstantOperationRemoverOptimizationDecorator(calculator);
        calculator = new UnusedBitOptimizerDecorator(calculator);

        check(calculator);
    }

    @Test
    @Ignore
    public void MutationOperation() {
        if (a.longValue() > 100 || b.longValue() > 100) {
            return;
        }
        BitOpsGraphCalculator calculator = new GraphBitOpsCalculator();

        calculator = new MutationOperation(calculator.getInput().locate("a0")).transform(calculator);
        calculator = new MutationOperation(calculator.getInput().locate("a1")).transform(calculator);
        calculator = new AndOrCalculatorDecorator(calculator, true);
        calculator = new SimpleOptimizationDecorator(calculator);
        calculator = new ConstantOperationRemoverOptimizationDecorator(calculator);
        calculator = new UnusedBitOptimizerDecorator(calculator);

        check(calculator);
    }

    @Test
    public void storeTest() {
        BitOpsGraphCalculator calculator = new GraphBitOpsCalculator();

        VarBit[] aBits = VarBit.list("a", binLength(a.longValue()), ZERO);
        VarBit[] bBits = VarBit.list("b", binLength(b.longValue()), ZERO);

        setVal(aBits, a.longValue());
        setVal(bBits, b.longValue());

        calculator.setInputBits(aBits, bBits);

        Bit[] r = BinMul.multiply(calculator, aBits, bBits);

        calculator.setOutputBits(r);


        GraphCalculatorSerializedData data = GraphCalculatorTools.serializeCalculator(calculator, false);

        BitOpsGraphCalculator calculator2 = GraphCalculatorTools.deSerializeCalculator(data);

        VarBit[] aBits2 = calculator2.getInput().getBits().subList(0, binLength(a.longValue())).toArray(new VarBit[0]);
        VarBit[] bBits2 = calculator2.getInput().getBits().subList(binLength(a.longValue()), calculator.getInput().size()).toArray(new VarBit[0]);

        setVal(aBits2, a.longValue());
        setVal(bBits2, b.longValue());

        calculator2.calculate();

        assertEquals(a.multiply(b), toLong(calculator2.getOutput().getBits().toArray(new Bit[0])));
    }

    @Test
    public void storeJsonTest() throws Throwable {
        BitOpsGraphCalculator calculator = new GraphBitOpsCalculator();

        VarBit[] aBits = VarBit.list("a", binLength(a.longValue()), ZERO);
        VarBit[] bBits = VarBit.list("b", binLength(b.longValue()), ZERO);

        setVal(aBits, a.longValue());
        setVal(bBits, b.longValue());

        calculator.setInputBits(aBits, bBits);

        System.out.println("a: " + a);
        System.out.println("b: " + b);

        Bit[] r = BinMul.multiply(calculator, aBits, bBits);

        calculator.setOutputBits(r);


        GraphCalculatorSerializedData data = GraphCalculatorTools.serializeCalculator(calculator, false);

        ObjectMapper objectMapper = new ObjectMapper();

        String dataStr = objectMapper.writeValueAsString(data);

        GraphCalculatorSerializedData data2 = objectMapper.readValue(dataStr, GraphCalculatorSerializedData.class);

        BitOpsGraphCalculator calculator2 = GraphCalculatorTools.deSerializeCalculator(data2);

        VarBit[] aBits2 = calculator2.getInput().getBits().subList(0, binLength(a.longValue())).toArray(new VarBit[0]);
        VarBit[] bBits2 = calculator2.getInput().getBits().subList(binLength(a.longValue()), calculator.getInput().size()).toArray(new VarBit[0]);

        setVal(aBits2, a.longValue());
        setVal(bBits2, b.longValue());

        calculator2.calculate();

        assertEquals(a.multiply(b), toLong(calculator2.getOutput().getBits().toArray(new Bit[0])));
    }

    private void check(BitOpsGraphCalculator calculator) {
        VarBit[] aBits = VarBit.list("a", binLength(a.longValue()), ZERO);
        VarBit[] bBits = VarBit.list("b", binLength(b.longValue()), ZERO);

        setVal(aBits, a.longValue());
        setVal(bBits, b.longValue());

        calculator.setInputBits(aBits, bBits);

        System.out.println("a: " + a);
        System.out.println("b: " + b);

        Bit[] r = BinMul.multiply(calculator, aBits, bBits);

        calculator.setOutputBits(r);

        calculator.calculate();

        System.out.println("MIDDLE SIZE: " + calculator.getMiddle().getBits().size());
        System.out.println("OPERATION COUNT: " + calculator.getOperationCount());

        assertEquals(a.multiply(b), toLong(r));
    }


}
