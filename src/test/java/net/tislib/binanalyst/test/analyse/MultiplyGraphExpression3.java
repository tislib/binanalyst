package net.tislib.binanalyst.test.analyse;

import static net.tislib.binanalyst.lib.bit.ConstantBit.UNSET;
import static net.tislib.binanalyst.lib.bit.ConstantBit.ZERO;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.tislib.binanalyst.lib.analyse.GraphExpressionReverserLogic;
import net.tislib.binanalyst.lib.bit.BinaryValue;
import net.tislib.binanalyst.lib.bit.NamedBit;
import net.tislib.binanalyst.lib.bit.VarBit;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.GraphBitOpsCalculator;
import net.tislib.binanalyst.lib.calc.graph.UsageFinder;
import net.tislib.binanalyst.lib.calc.graph.decorator.AndOrCalculatorDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.TwoOpsOptimizationDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.UnusedBitOptimizerDecorator;

/**
 * Created by Taleh Ibrahimli on 2/9/18.
 * Email: me@talehibrahimli.com
 */
public class MultiplyGraphExpression3 {

    public static void main(String... args) throws JsonProcessingException {
        BitOpsGraphCalculator calculator = new GraphBitOpsCalculator();
        calculator = new TwoOpsOptimizationDecorator(calculator);
        calculator = new UnusedBitOptimizerDecorator(calculator);

//        VarBit[] input = VarBit.list("a", 4, UNSET);
        VarBit[] input = VarBit.list("a", 4, ZERO);

        NamedBit[] r = {
                (NamedBit) calculator.and(input[3], input[2]),
                (NamedBit) calculator.xor(input[3], input[1]),

        };

        calculator.setInputBits(input);
        calculator.setOutputBits(r);

        calculator.getOutput().setLabelPrefix("O");
        calculator.getOutput().rename();

        UsageFinder usageFinder = new UsageFinder(calculator.getInput(), calculator.getMiddle(), calculator.getOutput());
        usageFinder.cleanUnusedMiddleBits();

        calculator.calculate();

        calculator.show();
        System.out.println("___________");

        GraphExpressionReverserLogic graphExpressionReverserLogic = new GraphExpressionReverserLogic(calculator);
        graphExpressionReverserLogic.analyse();

        graphExpressionReverserLogic.show();

    }

}
