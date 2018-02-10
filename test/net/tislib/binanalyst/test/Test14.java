package net.tislib.binanalyst.test;

import net.tislib.binanalyst.lib.calc.graph.expression.GraphExpression;
import net.tislib.binanalyst.lib.calc.graph.solver.SimpleSolver;

/**
 * Created by Taleh Ibrahimli on 2/9/18.
 * Email: me@talehibrahimli.com
 */
public class Test14 {

    public static void main(String... args) {
        GraphExpression graphExpression = Sampler.graphExpressionSampler(3,15);

        SimpleSolver simpleSolver = new SimpleSolver();

        graphExpression = simpleSolver.solve(graphExpression);

        graphExpression.show();
    }
}
