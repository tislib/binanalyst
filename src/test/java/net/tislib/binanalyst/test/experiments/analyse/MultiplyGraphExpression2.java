package net.tislib.binanalyst.test.experiments.analyse;

import static net.tislib.binanalyst.lib.bit.ConstantBit.UNSET;

import net.tislib.binanalyst.lib.analyse.GraphExpressionNFTReverserLogic;
import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.VarBit;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.GraphBitOpsCalculator;
import net.tislib.binanalyst.lib.calc.graph.UsageFinder;
import net.tislib.binanalyst.lib.operator.BinMul;

/**
 * Created by Taleh Ibrahimli on 2/9/18.
 * Email: me@talehibrahimli.com
 */
public class MultiplyGraphExpression2 {

    public static void main(String... args) {
        BitOpsGraphCalculator calculator = new GraphBitOpsCalculator();
//        calculator = new TwoOpsOptimizationDecorator(calculator);
//        calculator = new SimpleOptimizationDecorator(calculator);
//        calculator = new AndOrCalculatorDecorator(calculator, true);

        long a = 7;
        long b = 5;

        //32532325, 23403244

        VarBit[] aBits = VarBit.list("a", 8, UNSET);
        VarBit[] bBits = VarBit.list("b", 8, UNSET);

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

        GraphExpressionNFTReverserLogic graphExpressionReverserLogic = new GraphExpressionNFTReverserLogic(calculator);
        graphExpressionReverserLogic.analyse();

        graphExpressionReverserLogic.getInnerCalculator().show();
        System.out.println("___________");

        graphExpressionReverserLogic.showState();


    }

}
