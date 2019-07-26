package net.tislib.binanalyst.test.analyse;

import static net.tislib.binanalyst.lib.bit.ConstantBit.UNSET;
import static net.tislib.binanalyst.lib.bit.ConstantBit.ZERO;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.tislib.binanalyst.lib.analyse.GraphExpressionReverserLogic;
import net.tislib.binanalyst.lib.bit.BinaryValue;
import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.VarBit;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.GraphBitOpsCalculator;
import net.tislib.binanalyst.lib.calc.graph.UsageFinder;
import net.tislib.binanalyst.lib.calc.graph.decorator.SimpleOptimizationDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.TwoOpsOptimizationDecorator;
import net.tislib.binanalyst.lib.operator.BinMul;

/**
 * Created by Taleh Ibrahimli on 2/9/18.
 * Email: me@talehibrahimli.com
 */
public class MultiplyGraphExpression2 {

    public static void main(String... args) throws JsonProcessingException {
        BitOpsGraphCalculator calculator = new GraphBitOpsCalculator();
//        calculator = new TwoOpsOptimizationDecorator(calculator);
//        calculator = new SimpleOptimizationDecorator(calculator);
//        calculator = new AndOrCalculatorDecorator(calculator, true);

        long a = 7;
        long b = 5;

        //32532325, 23403244

        VarBit[] aBits = VarBit.list("a", 2, UNSET);
        VarBit[] bBits = VarBit.list("b", 2, UNSET);

//        setVal(aBits, a);
//        setVal(bBits, b);

        calculator.setInputBits(aBits, bBits);

        Bit[] r = BinMul.multiply(calculator, aBits, bBits);

//        Bit[] truth = GraphCalculatorTools.getTruthBit(calculator, r, 4);

//        Bit result = calculator.and(truth);

//        calculator.setOutputBits(new Bit[]{result});
        calculator.setOutputBits(r);

        calculator.getOutput().setLabelPrefix("O");
        calculator.getOutput().rename();

        UsageFinder usageFinder = new UsageFinder(calculator.getInput(), calculator.getMiddle(), calculator.getOutput());
        usageFinder.cleanUnusedMiddleBits();

        calculator.show();

        GraphExpressionReverserLogic graphExpressionReverserLogic = new GraphExpressionReverserLogic(calculator);
        graphExpressionReverserLogic.analyse();

//        graphExpressionReverserLogic.show();

        BitOpsGraphCalculator innerCalculator = graphExpressionReverserLogic.getInnerCalculator();

//        innerCalculator.getInput().getBits().get(0).setValue(BinaryValue.TRUE);
//        innerCalculator.getInput().getBits().get(1).setValue(BinaryValue.FALSE);
//        innerCalculator.getInput().getBits().get(2).setValue(BinaryValue.FALSE);
//        innerCalculator.getInput().getBits().get(3).setValue(BinaryValue.TRUE);


        innerCalculator.calculate();

        innerCalculator.show(true);
//        BinValueHelper.printValues(innerCalculator.getOutput().getBits().toArray(new NamedBit[0]));

//        graphExpressionReverserLogic.showNormalState();

    }

}
