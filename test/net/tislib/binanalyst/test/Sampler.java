package net.tislib.binanalyst.test;

import static net.tislib.binanalyst.lib.BinValueHelper.setVal;
import static net.tislib.binanalyst.lib.bit.ConstantBit.ZERO;

import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.VarBit;
import net.tislib.binanalyst.lib.calc.graph.GraphBitOpsCalculator;
import net.tislib.binanalyst.lib.calc.graph.expression.GraphExpression;
import net.tislib.binanalyst.lib.calc.graph.optimizer.LogicalOptimizer;
import net.tislib.binanalyst.lib.calc.graph.optimizer.SimpleOptimizer;
import net.tislib.binanalyst.lib.operator.BinMul;

/**
 * Created by Taleh Ibrahimli on 2/10/18.
 * Email: me@talehibrahimli.com
 */
public class Sampler {

    public static GraphExpression graphExpressionSampler(int bitCount, int multiplicationValue) {
        GraphBitOpsCalculator calculator = new GraphBitOpsCalculator();

        VarBit[] aBits = VarBit.list("a", bitCount, ZERO);
        VarBit[] bBits = VarBit.list("b", bitCount, ZERO);
        calculator.setInputBits(aBits, bBits);

        calculator.getOptimizers().add(new SimpleOptimizer());
        calculator.getOptimizers().add(new LogicalOptimizer());

        Bit[] r = BinMul.multiply(calculator, aBits, bBits);


        VarBit[] result = VarBit.list("c", r.length, ZERO);

        setVal(result, multiplicationValue);

        System.out.println(calculator.getOperationCount());

        calculator.setOutputBits(r);

        calculator.clean();

        GraphExpression graphExpression = new GraphExpression();

        graphExpression.setCalculation(calculator, result);

        calculator.show();
        System.out.println("========");

        return graphExpression;
    }

    public static GraphExpression graphExpressionSampler(int bitCount, long a, long b) {
        GraphBitOpsCalculator calculator = new GraphBitOpsCalculator();

        VarBit[] aBits = VarBit.list("a", bitCount, ZERO);
        VarBit[] bBits = VarBit.list("b", bitCount, ZERO);

        setVal(aBits, a);
        setVal(bBits, b);

        calculator.setInputBits(aBits, bBits);

        calculator.getOptimizers().add(new LogicalOptimizer());

        Bit[] r = BinMul.multiply(calculator, aBits, bBits);

        VarBit[] result = VarBit.list("c", r.length, ZERO);

        setVal(result, a * b);

        System.out.println(calculator.getOperationCount());

        calculator.setOutputBits(r);

        GraphExpression graphExpression = new GraphExpression();

        graphExpression.setCalculation(calculator, result);
        return graphExpression;
    }
}
