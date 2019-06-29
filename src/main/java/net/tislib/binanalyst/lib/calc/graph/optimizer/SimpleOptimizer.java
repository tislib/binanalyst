package net.tislib.binanalyst.lib.calc.graph.optimizer;

import net.tislib.binanalyst.lib.bit.NamedBit;
import net.tislib.binanalyst.lib.calc.graph.GraphBitOpsCalculator;
import net.tislib.binanalyst.lib.calc.graph.Operation;

/**
 * Created by Taleh Ibrahimli on 2/9/18.
 * Email: me@talehibrahimli.com
 */
public class SimpleOptimizer implements Optimizer {
    @Override
    public NamedBit optimizeOperation(GraphBitOpsCalculator graphBitOpsCalculator, Operation operation, NamedBit[] bits, NamedBit chain) {
        throw new UnsupportedOperationException("this optimizer is deprecated");
    }

    @Override
    public void optimizeCalculator(GraphBitOpsCalculator graphBitOpsCalculator) {

    }
}
