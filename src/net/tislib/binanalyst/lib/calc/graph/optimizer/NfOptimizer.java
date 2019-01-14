package net.tislib.binanalyst.lib.calc.graph.optimizer;

import net.tislib.binanalyst.lib.bit.NamedBit;
import net.tislib.binanalyst.lib.calc.graph.GraphBitOpsCalculator;
import net.tislib.binanalyst.lib.calc.graph.Operation;

public class NfOptimizer implements Optimizer { // Normal Form Optimizer

    private final Optimizer logicalOptimizer = new LogicalOptimizer();

    @Override
    public NamedBit optimizeOperation(GraphBitOpsCalculator graphBitOpsCalculator, Operation operation, NamedBit[] bits, NamedBit chain) {
        return null;
    }

    @Override
    public void optimizeCalculator(GraphBitOpsCalculator graphBitOpsCalculator) {

    }
}
