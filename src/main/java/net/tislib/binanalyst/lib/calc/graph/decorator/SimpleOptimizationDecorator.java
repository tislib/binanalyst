package net.tislib.binanalyst.lib.calc.graph.decorator;

import static net.tislib.binanalyst.lib.bit.ConstantBit.ONE;
import static net.tislib.binanalyst.lib.bit.ConstantBit.ZERO;

import java.util.Arrays;
import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.NamedBit;
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

    @Override
    public Bit not(Bit bit) {
        return optimize(Operation.NOT, bit);
    }

    Bit optimize(Operation operation, Bit... bits) {
        bits = distinct(bits);
        if (isReversedBits(bits)) {
            if (operation == Operation.AND) {
                return ZERO;
            } else if (operation == Operation.OR) {
                return ONE;
            } else if (operation == Operation.XOR) {
                return ONE;
            }
        }
        switch (operation) {
            case AND:
                if (Optimizer.contains(bits, ZERO)) return ZERO;
                bits = Optimizer.remove(bits, ONE);
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
                } else {
                    return oneCount % 2 == 0 ? super.xor(bits) : not(super.xor(bits));
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
            return bits[0];
        }

//        if (operation == Operation.NOT && (bits[0] instanceof OperationalBit) && ((OperationalBit) bits[0]).getOperation() == Operation.NOT) {
//            return ((OperationalBit) bits[0]).getBits()[0];
//        }
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

    private boolean isReversedBits(Bit[] bits) {
        return bits.length == 2 && (bits[0] == not(bits[1]) || bits[1] == not(bits[0]));
    }

    private Bit[] distinct(Bit[] bits) {
        return Arrays.stream(bits).distinct().toArray(Bit[]::new);
    }
}
