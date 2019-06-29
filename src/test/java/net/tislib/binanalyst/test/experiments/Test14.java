package net.tislib.binanalyst.test.experiments;

import static net.tislib.binanalyst.lib.BinValueHelper.setVal;
import static net.tislib.binanalyst.lib.bit.ConstantBit.ZERO;

import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.VarBit;
import net.tislib.binanalyst.lib.calc.graph.GraphBitOpsCalculator;
import net.tislib.binanalyst.lib.calc.graph.expression.AnomalGraphExpression;
import net.tislib.binanalyst.lib.calc.graph.expression.GraphExpression;
import net.tislib.binanalyst.lib.calc.graph.optimizer.LogicalOptimizer;
import net.tislib.binanalyst.lib.calc.graph.optimizer.SimpleOptimizer;
import net.tislib.binanalyst.lib.calc.graph.solver.AnomalitySolver;
import net.tislib.binanalyst.lib.operator.BinMul;

/**
 * Created by Taleh Ibrahimli on 2/9/18.
 * Email: me@talehibrahimli.com
 */
public class Test14 {

    public static void main(String... args) {
        GraphBitOpsCalculator calculator = new GraphBitOpsCalculator();

        VarBit[] aBits = VarBit.list("a", 2, ZERO);
        VarBit[] bBits = VarBit.list("b", 2, ZERO);

        calculator.getOptimizers().add(new SimpleOptimizer());
        calculator.getOptimizers().add(new LogicalOptimizer());

        Bit[] r = BinMul.multiply(calculator, aBits, bBits);

        setVal(aBits, 1);
        setVal(bBits, 3);


        VarBit[] result = VarBit.list("c", r.length, ZERO);

        setVal(result, 3);

        System.out.println(calculator.getOperationCount());

        calculator.setInputBits(aBits, bBits);
        calculator.setOutputBits(r);

        calculator.clean();

        GraphExpression graphExpression = new GraphExpression();

        graphExpression.setCalculation(calculator, result);

        calculator.show();
        System.out.println("========");

        System.out.println(graphExpression.check());

        AnomalitySolver simpleSolver = new AnomalitySolver();
//
        AnomalGraphExpression graphExpression2 = simpleSolver.solve(graphExpression);
//
        graphExpression2.show(false);
    }
}
