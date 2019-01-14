package net.tislib.binanalyst.test.rev;

import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.VarBit;
import net.tislib.binanalyst.lib.calc.RevOpsCalculator;
import net.tislib.binanalyst.lib.calc.graph.GraphBitOpsCalculator;
import net.tislib.binanalyst.lib.calc.graph.expression.AnomalGraphExpression;
import net.tislib.binanalyst.lib.calc.graph.expression.GraphExpression;
import net.tislib.binanalyst.lib.calc.graph.optimizer.LogicalOptimizer;
import net.tislib.binanalyst.lib.calc.graph.optimizer.SimpleOptimizer;
import net.tislib.binanalyst.lib.calc.graph.solver.AnomalitySolver;

public class Test16 {

    private static final RevOpsCalculator revOpsCalculator = new RevOpsCalculator();


    public static void main(String... args) {

        // x | y = 1
        // x & y | (x | (x & y)) & y = 1

        // ===>  (x | y) & (x & y | (x | (x & y)) & y) = 1
        // find values oblast

        GraphBitOpsCalculator calculator = new GraphBitOpsCalculator();

        calculator.getOptimizers().add(new SimpleOptimizer());
        calculator.getOptimizers().add(new LogicalOptimizer());

        VarBit x = new VarBit("x");
        VarBit y = new VarBit("y");

        Bit r = calculator.and(
                calculator.or(x, y),
                calculator.or(
                        calculator.and(x, y),
                        calculator.and(calculator.or(x, calculator.and(x, y)), y)
                )
        );

        VarBit rVal = VarBit.wrap("R", true);

        System.out.println(rVal.getValue());

        calculator.setInputBits(x, y);
        calculator.setOutputBits(new Bit[]{r});

        calculator.clean();

        GraphExpression graphExpression = new GraphExpression();

        graphExpression.setCalculation(calculator,
                new VarBit[]{rVal});

        calculator.show();
        System.out.println("========");

//        System.out.println(graphExpression.check());

        AnomalitySolver simpleSolver = new AnomalitySolver();
//
        AnomalGraphExpression graphExpression2 = simpleSolver.solve(graphExpression);
//
        graphExpression2.show(false);
    }

}
