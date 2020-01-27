package net.tislib.binanalyst.lib.calc.graph.decorator.optimizer;

import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.OperationalBit;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.Operation;
import net.tislib.binanalyst.lib.calc.graph.decorator.OptimizerGraphCalculatorDecorator;

import java.util.Arrays;

import static net.tislib.binanalyst.lib.bit.ConstantBit.ONE;
import static net.tislib.binanalyst.lib.bit.ConstantBit.ZERO;

public class ReverseOperationRemovalOptimizationDecorator extends OptimizerGraphCalculatorDecorator {
    public ReverseOperationRemovalOptimizationDecorator(BitOpsGraphCalculator calculator) {
        super(calculator);
    }

    @Override
    protected Bit optimize(Operation operation, Bit... bits) {
        if (isReversedBits(bits)) {
            if (operation == Operation.AND) {
                return ZERO;
            } else if (operation == Operation.OR) {
                return ONE;
            } else if (operation == Operation.XOR) {
                return ONE;
            }
        }

        return delegate(operation, bits);
    }

    private boolean isReversedBits(Bit[] bits) {
        if (bits.length != 2) {
            return false;
        }

        if (bits[0] instanceof OperationalBit) {
            OperationalBit operationalBit = (OperationalBit) bits[0];
            if (operationalBit.getOperation() == Operation.NOT && operationalBit.getBits()[0] == bits[1]) {
                return true;
            }
        }

        if (bits[1] instanceof OperationalBit) {
            OperationalBit operationalBit = (OperationalBit) bits[1];
            if (operationalBit.getOperation() == Operation.NOT && operationalBit.getBits()[0] == bits[0]) {
                return true;
            }
        }

        return false;
    }
}
