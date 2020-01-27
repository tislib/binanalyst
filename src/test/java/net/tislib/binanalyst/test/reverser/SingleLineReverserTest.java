//package net.tislib.binanalyst.test.reverser;
//
//import static net.tislib.binanalyst.lib.bit.ConstantBit.UNSET;
//
//import java.util.Collection;
//import java.util.List;
//import net.tislib.binanalyst.lib.BinValueHelper;
//import net.tislib.binanalyst.lib.analyse.BriteBitReverser;
//import net.tislib.binanalyst.lib.analyse.GraphExpressionNFTReverserLogic;
//import net.tislib.binanalyst.lib.analyse.GraphExpressionReverserLogic;
//import net.tislib.binanalyst.lib.analyse.GraphExpressionReverserLogicMulti;
//import net.tislib.binanalyst.lib.bit.BinaryValue;
//import net.tislib.binanalyst.lib.bit.Bit;
//import net.tislib.binanalyst.lib.bit.VarBit;
//import net.tislib.binanalyst.lib.calc.Expression;
//import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
//import net.tislib.binanalyst.lib.calc.graph.GraphBitOpsCalculator;
//import net.tislib.binanalyst.lib.calc.graph.decorator.optimizer.UnusedBitOptimizerDecorator;
//import net.tislib.binanalyst.lib.calc.graph.expression.BooleanExpression;
//import net.tislib.binanalyst.lib.calc.reverse.SingleBitReverser;
//import org.junit.Assert;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.junit.runners.Parameterized;
//
//@RunWith(Parameterized.class)
//public class SingleLineReverserTest {
//
//    private final BitOpsGraphCalculator calculator = new UnusedBitOptimizerDecorator(
//            new GraphBitOpsCalculator()
//    );
//
//    public SingleLineReverserTest(Expression expression) {
//        VarBit[] input = VarBit.list("a", 8, UNSET);
//        Bit[] output = expression.prepare(input, calculator);
//        calculator.setInputBits(input);
//        calculator.setOutputBits(output);
////        calculator.show();
//
//        calculator.getOutput().setLabelPrefix("O");
//        calculator.getOutput().rename();
//    }
//
//    @Parameterized.Parameters
//    public static Collection<Object[]> data() {
//        return TestData.getSingleLineExpressions();
//    }
//
////    @Test
////    public void testReversingLogic3() {
////        testReversingLogic3Internal(true);
////        testReversingLogic3Internal(false);
////    }
//
//    @Test
//    public void testReversingGraphExpressionNFTReverserLogic() {
//        calculator.show();
//        testReversingGraphExpressionNFTReverserLogic(true);
//        testReversingGraphExpressionNFTReverserLogic(false);
//    }
//
//    private void testReversingLogic3Internal(boolean validateTruth) {
//        calculator.optimize();
//
//        List<VarBit> bits = calculator.getInput().getBits();
//
//        for (int i1 = 0; i1 < bits.size(); i1++) {
//            VarBit varBit = bits.get(i1);
//            SingleBitReverser singleBitReverser = GraphExpressionReverserLogic.reverser();
//            SingleBitReverser bruteBitReverser = new BriteBitReverser();
//
//            int oSize = calculator.getOutput().size(); // possibility count
//
//            BooleanExpression gerlBe = singleBitReverser.reverse(calculator, varBit.getName(), validateTruth);
//            BooleanExpression bruteBe = bruteBitReverser.reverse(calculator, varBit.getName(), validateTruth);
//
//            for (long i = 0; i < 1 << oSize; i++) {
//                BinaryValue[] val = BinValueHelper.toBinValueArray(oSize, i);
//                boolean gerlBeRes = gerlBe.calculate(val);
//                boolean bruteBeRes = bruteBe.calculate(val);
//                if (gerlBeRes != bruteBeRes) {
//                    System.out.println("DEBUG!");
//                }
//                Assert.assertEquals(varBit.getName() + " failed", gerlBeRes, bruteBeRes);
//            }
//
//        }
//    }
//
//    private void testReversingGraphExpressionNFTReverserLogic(boolean validateTruth) {
//        calculator.optimize();
//
//        List<VarBit> bits = calculator.getInput().getBits();
//
//        for (int i1 = 0; i1 < bits.size(); i1++) {
//            VarBit varBit = bits.get(i1);
//            SingleBitReverser singleBitReverser = GraphExpressionNFTReverserLogic.reverser();
//            SingleBitReverser bruteBitReverser = new BriteBitReverser();
//
//            int oSize = calculator.getOutput().size(); // possibility count
//
//            BooleanExpression gerlBe = singleBitReverser.reverse(calculator, varBit.getName(), validateTruth);
//            BooleanExpression bruteBe = bruteBitReverser.reverse(calculator, varBit.getName(), validateTruth);
//
//            for (long i = 0; i < 1 << oSize; i++) {
//                BinaryValue[] val = BinValueHelper.toBinValueArray(oSize, i);
//                boolean gerlBeRes = gerlBe.calculate(val);
//                boolean bruteBeRes = bruteBe.calculate(val);
//                if (gerlBeRes != bruteBeRes) {
//                    System.out.println("DEBUG! " + validateTruth);
//                }
//
//                Assert.assertEquals(varBit.getName() + " failed", bruteBeRes, gerlBeRes);
//            }
//
//        }
//    }
//}
