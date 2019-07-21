package net.tislib.binanalyst.test.analyse;

import static net.tislib.binanalyst.lib.bit.ConstantBit.ZERO;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.tislib.binanalyst.lib.BinValueHelper;
import net.tislib.binanalyst.lib.analyse.GraphExpressionReverserLogic;
import net.tislib.binanalyst.lib.bit.BinaryValue;
import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.NamedBit;
import net.tislib.binanalyst.lib.bit.VarBit;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.GraphBitOpsCalculator;
import net.tislib.binanalyst.lib.calc.graph.decorator.AndOrCalculatorDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.BinderOptimizationDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.TwoOpsOptimizationDecorator;

/**
 * Created by Taleh Ibrahimli on 2/9/18.
 * Email: me@talehibrahimli.com
 */
public class MultiplyGraphExpression3 {

    public static void main(String... args) throws JsonProcessingException {
        BitOpsGraphCalculator calculator = new GraphBitOpsCalculator();
        calculator = new BinderOptimizationDecorator(calculator);
        calculator = new TwoOpsOptimizationDecorator(calculator);
        calculator = new AndOrCalculatorDecorator(calculator, false);

        VarBit[] input = VarBit.list("a", 2, ZERO);

        NamedBit[] r = {
                (NamedBit) calculator.and(input[0], input[1]),
        };

        calculator.setInputBits(input);
        calculator.setOutputBits(r);

        calculator.getOutput().setLabelPrefix("O");
        calculator.getOutput().rename();

//        calculator.calculate();

//        calculator.show();

        GraphExpressionReverserLogic graphExpressionReverserLogic = new GraphExpressionReverserLogic(calculator);
        graphExpressionReverserLogic.analyse();

        graphExpressionReverserLogic.show();

        BitOpsGraphCalculator innerCalculator = graphExpressionReverserLogic.getInnerCalculator();

        innerCalculator.getInput().getBits().get(0).setValue(BinaryValue.FALSE);
//        innerCalculator.getInput().getBits().get(1).setValue(BinaryValue.FALSE);
//        innerCalculator.getInput().getBits().get(2).setValue(BinaryValue.FALSE);
//        innerCalculator.getInput().getBits().get(3).setValue(BinaryValue.TRUE);

        innerCalculator.calculate();
        innerCalculator.show(true);
//        BinValueHelper.printValues(innerCalculator.getOutput().getBits().toArray(new NamedBit[0]));

        graphExpressionReverserLogic.showNormalState();

    }

}
