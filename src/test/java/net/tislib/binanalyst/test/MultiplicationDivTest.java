//package net.tislib.binanalyst.test;
//
//import static junit.framework.TestCase.assertTrue;
//import static net.tislib.binanalyst.lib.BinValueHelper.*;
//import static net.tislib.binanalyst.lib.bit.ConstantBit.ZERO;
//import static org.junit.Assert.assertEquals;
//
//import java.math.BigInteger;
//import java.util.Collection;
//import net.tislib.binanalyst.lib.bit.Bit;
//import net.tislib.binanalyst.lib.bit.VarBit;
//import net.tislib.binanalyst.lib.calc.SimpleBitOpsCalculator;
//import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
//import net.tislib.binanalyst.lib.calc.graph.GraphBitOpsCalculator;
//import net.tislib.binanalyst.lib.calc.graph.decorator.BinderOptimizationDecorator;
//import net.tislib.binanalyst.lib.calc.graph.decorator.UnusedBitOptimizerDecorator;
//import net.tislib.binanalyst.lib.calc.graph.expression.GraphExpression;
//import net.tislib.binanalyst.lib.calc.graph.optimizer.LogicalOptimizer;
//import net.tislib.binanalyst.lib.calc.graph.optimizer.NfOptimizer;
//import net.tislib.binanalyst.lib.calc.graph.solver.SimpleSolver;
//import net.tislib.binanalyst.lib.operator.BinAdd;
//import net.tislib.binanalyst.lib.operator.BinMul;
//import net.tislib.binanalyst.lib.operator.BinMulDiv;
//import net.tislib.binanalyst.lib.operator.BinMulRec;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.junit.runners.Parameterized;
//
///**
// * Created by Taleh Ibrahimli on 2/4/18.
// * Email: me@talehibrahimli.com
// */
//@RunWith(Parameterized.class)
//public class MultiplicationDivTest {
//
//    @Parameterized.Parameters
//    public static Collection<Object[]> data() {
//        return TestData.testPairData();
//    }
//
//    private final BigInteger a;
//    private final BigInteger b;
//
//    public MultiplicationDivTest(long a, long b) {
//        this.a = BigInteger.valueOf(a);
//        this.b = BigInteger.valueOf(b);
//    }
//
//    @Test
//    public void checkDivA() {
//        if(b.longValue() % 2 == 1) {
//            BitOpsGraphCalculator calculator = new GraphBitOpsCalculator();
//            calculator = new BinderOptimizationDecorator(calculator);
//            calculator = new UnusedBitOptimizerDecorator(calculator);
//
//            VarBit[] aBits = VarBit.list("a", 32, ZERO);
//            VarBit[] cBits = VarBit.list("c", 200, ZERO);
//
//            setVal(aBits, a.longValue());
//            setVal(cBits, a.multiply(b).longValue());
//
//            calculator.setInputBits(aBits, cBits);
//
//            Bit[] r = BinMulDiv.mulDiv(calculator, aBits, cBits);
//
//            Bit truth = calculator.and(r);
//
//            calculator.setOutputBits(new Bit[]{truth});
//
//            calculator.calculate();
//
//            assertTrue(truth.getValue().isTrue());
//            System.out.println("WORKING!");
//        }
//    }
//}
