package net.tislib.binanalyst.lib.calc.graph.solver;

import net.tislib.binanalyst.lib.calc.graph.expression.AnomalGraphExpression;
import net.tislib.binanalyst.lib.calc.graph.expression.GraphExpression;

/**
 * Created by Taleh Ibrahimli on 2/10/18.
 * Email: me@talehibrahimli.com
 */
public class AnomalitySolver {

    public AnomalGraphExpression solve(GraphExpression expression) {
        return new AnomalGraphExpression(expression);
    }

}
