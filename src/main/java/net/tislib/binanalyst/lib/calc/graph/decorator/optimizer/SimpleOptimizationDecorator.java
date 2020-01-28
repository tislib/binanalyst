package net.tislib.binanalyst.lib.calc.graph.decorator.optimizer;

import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.OperationalBit;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.Operation;
import net.tislib.binanalyst.lib.calc.graph.decorator.OptimizerGraphCalculatorDecorator;

public class SimpleOptimizationDecorator extends OptimizerGraphCalculatorDecorator {

    @SuppressWarnings("unchecked")
    public SimpleOptimizationDecorator(BitOpsGraphCalculator calculator) {
        super(chain(calculator,
                DoubleNotRemovalOptimizationDecorator::new,
                LackedOperationOptimizationDecorator::new,
                ReverseOperationRemovalOptimizationDecorator::new,
                DuplicationRemovalOptimizationDecorator::new
        ));
    }

    protected Bit optimize(Operation operation, Bit... bits) {
        return delegate(operation, bits);
    }
}
