package net.tislib.binanalyst.lib.calc.graph.decorator.optimizer;

import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.OperationalBit;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.Operation;
import net.tislib.binanalyst.lib.calc.graph.decorator.OptimizerGraphCalculatorDecorator;

import java.util.Arrays;

public class DoubleNotRemovalOptimizationDecorator extends OptimizerGraphCalculatorDecorator {
    public DoubleNotRemovalOptimizationDecorator(BitOpsGraphCalculator calculator) {
        super(calculator);
    }

    @Override
    protected Bit optimize(Operation operation, Bit... bits) {
        if (operation == Operation.NOT && bits[0] instanceof OperationalBit && ((OperationalBit) bits[0]).getOperation() == Operation.NOT) {
            return ((OperationalBit) bits[0]).getBits()[0];
        }

        return delegate(operation, bits);
    }

}
