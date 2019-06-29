package net.tislib.binanalyst.lib.calc.graph.decorator;

import static net.tislib.binanalyst.lib.bit.ConstantBit.ONE;
import static net.tislib.binanalyst.lib.bit.ConstantBit.ZERO;

import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.Operation;
import net.tislib.binanalyst.lib.calc.graph.optimizer.Optimizer;

public class SimpleOptimizationDecorator extends AbstractBitOpsGraphCalculatorDecorator {
    public SimpleOptimizationDecorator(BitOpsGraphCalculator calculator) {
        super(calculator);
    }

    @Override
    public Bit xor(Bit... bits) {
        return optimize(Operation.XOR, bits);
    }

    @Override
    public Bit and(Bit... bits) {
        return optimize(Operation.AND, bits);
    }

    @Override
    public Bit or(Bit... bits) {
        return optimize(Operation.OR, bits);
    }

    Bit optimize(Operation operation, Bit... bits) {
        switch (operation) {
            case AND:
                if (Optimizer.contains(bits, ZERO)) return ZERO;
                bits = Optimizer.remove(bits, ONE);
                break;
            case OR:
                if (Optimizer.contains(bits, ONE)) return ONE;
                bits = Optimizer.remove(bits, ZERO);
            case XOR:
                bits = Optimizer.remove(bits, ZERO);
                break;
        }
        if (bits.length == 0) {
            return ZERO;
        }
        if (operation != Operation.NOT && bits.length == 1) {
            return bits[0];
        }
        switch (operation) {
            case NOT:
                return super.not(bits[0]);
            case XOR:
                return super.xor(bits);
            case OR:
                return super.or(bits);
            case AND:
                return super.and(bits);
            default:
                throw new RuntimeException("unsupported operation type");
        }
    }
}
