package net.tislib.binanalyst.test.experiments.analyse;

import static net.tislib.binanalyst.lib.bit.ConstantBit.ZERO;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.tislib.binanalyst.lib.analyse.GraphExpressionNFTReverserLogic;
import net.tislib.binanalyst.lib.analyse.GraphExpressionReverserLogic;
import net.tislib.binanalyst.lib.analyse.GraphExpressionReverserLogicMulti;
import net.tislib.binanalyst.lib.analyse.GraphExpressionReverserLogicMulti2;
import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.NamedBit;
import net.tislib.binanalyst.lib.bit.VarBit;
import net.tislib.binanalyst.lib.calc.Expression;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.GraphBitOpsCalculator;
import net.tislib.binanalyst.lib.calc.graph.UsageFinder;
import net.tislib.binanalyst.lib.calc.graph.decorator.SimpleOptimizationDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.TwoOpsOptimizationDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.UnusedBitOptimizerDecorator;
import net.tislib.binanalyst.test.reverser.TestData;

/**
 * Created by Taleh Ibrahimli on 2/9/18.
 * Email: me@talehibrahimli.com
 */
public class MultiplyGraphExpression3 {

    public static void main(String... args) throws JsonProcessingException {
        BitOpsGraphCalculator calculator = new GraphBitOpsCalculator();
        calculator = new UnusedBitOptimizerDecorator(calculator);
        calculator = new SimpleOptimizationDecorator(calculator);
        calculator = new TwoOpsOptimizationDecorator(calculator);

//        VarBit[] input = VarBit.list("a", 4, UNSET);
        VarBit[] input = VarBit.list("a", 4, ZERO);


        calculator.setInputBits(input);

        Bit[] r = ((Expression) TestData.getMultiLineExpressions().get(3)[0])
                .prepare(input, calculator);

        calculator.setOutputBits(r);

        calculator.getOutput().setLabelPrefix("O");
        calculator.getOutput().rename();

        UsageFinder usageFinder = new UsageFinder(calculator.getInput(), calculator.getMiddle(), calculator.getOutput());
        usageFinder.cleanUnusedMiddleBits();

        calculator.calculate();

        calculator.show();
        System.out.println("___________");

        GraphExpressionNFTReverserLogic graphExpressionReverserLogic = new GraphExpressionNFTReverserLogic(calculator);
        graphExpressionReverserLogic.analyse();

        graphExpressionReverserLogic.getInnerCalculator().show();
        System.out.println("___________");

        graphExpressionReverserLogic.showState();

//        graphExpressionReverserLogic.getInnerCalculator().getOutput().setValue();
//        graphExpressionReverserLogic.showNormalState();

    }

}
