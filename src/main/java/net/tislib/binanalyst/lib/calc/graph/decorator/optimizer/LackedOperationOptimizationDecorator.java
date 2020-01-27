package net.tislib.binanalyst.lib.calc.graph.decorator.optimizer;

import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.OperationalBit;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.Operation;
import net.tislib.binanalyst.lib.calc.graph.decorator.OptimizerGraphCalculatorDecorator;
import net.tislib.binanalyst.lib.calc.graph.optimizer.Optimizer;

import static net.tislib.binanalyst.lib.bit.ConstantBit.ONE;
import static net.tislib.binanalyst.lib.bit.ConstantBit.ZERO;

public class LackedOperationOptimizationDecorator extends OptimizerGraphCalculatorDecorator {
    public LackedOperationOptimizationDecorator(BitOpsGraphCalculator calculator) {
        super(calculator);
    }

    @Override
    protected Bit optimize(Operation operation, Bit... bits) {
        if (bits.length == 0) {
            return ZERO;
        }

        switch (operation) {
            case AND:
                if (Optimizer.contains(bits, ZERO)) return ZERO;
                bits = Optimizer.remove(bits, ONE);
                if (bits.length == 0) return ONE;
                break;
            case OR:
                if (Optimizer.contains(bits, ONE)) return ONE;
                bits = Optimizer.remove(bits, ZERO);
                break;
            case XOR:
                bits = Optimizer.remove(bits, ZERO);
                int oneCount = Optimizer.count(bits, ONE);
                bits = Optimizer.remove(bits, ONE);
                if (bits.length == 0) {
                    return oneCount % 2 == 0 ? ZERO : ONE;
                } else if (bits.length == 1) {
                    return bits[0];
                } else {
                    return oneCount % 2 == 0 ? original.xor(bits) : not(original.xor(bits));
                }
            case NOT:
                if (bits[0] == ZERO) {
                    return ONE;
                } else if (bits[0] == ONE) {
                    return ZERO;
                }
                break;
        }
        if (bits.length == 0) {
            return ZERO;
        }
        if (operation != Operation.NOT && bits.length == 1) {
            if (bits[0] instanceof OperationalBit) {
                OperationalBit operationalBit = (OperationalBit) bits[0];
                return operation(operationalBit.getOperation(), operationalBit.getBits());
            }
            return bits[0];
        }

        return delegate(operation, bits);
    }
}
