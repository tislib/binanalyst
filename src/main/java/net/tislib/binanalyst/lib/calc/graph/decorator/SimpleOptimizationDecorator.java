package net.tislib.binanalyst.lib.calc.graph.decorator;

import static net.tislib.binanalyst.lib.bit.ConstantBit.ONE;
import static net.tislib.binanalyst.lib.bit.ConstantBit.ZERO;
import static net.tislib.binanalyst.lib.calc.graph.decorator.AbstractBitOpsGraphCalculatorDecorator.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.NamedBit;
import net.tislib.binanalyst.lib.bit.OperationalBit;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.Operation;
import net.tislib.binanalyst.lib.calc.graph.optimizer.Optimizer;

public class SimpleOptimizationDecorator extends OptimizerGraphCalculatorDecorator {
    public SimpleOptimizationDecorator(BitOpsGraphCalculator calculator) {
        super(chain(calculator));
    }

    public Bit optimize(Operation operation, Bit... bits) {
        if (operation != Operation.XOR) {
            bits = distinct(bits);
        }

        Set<Bit> bitSet = new HashSet<>(Arrays.asList(bits));

        if (bits.length != bitSet.size()) {
            int modCount = bits.length - bitSet.size();
            Bit[] newBits = bitSet.toArray(new Bit[0]);
            if (operation != Operation.XOR || modCount % 2 == 0) {
                return optimize(operation, newBits);
            } else {
                return not(optimize(operation, newBits));
            }
        }

        if (isReversedBits(bits)) {
            if (operation == Operation.AND) {
                return ZERO;
            } else if (operation == Operation.OR) {
                return ONE;
            } else if (operation == Operation.XOR) {
                return ONE;
            }
        }

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
            if (bits[0] instanceof OperationalBit) {
                OperationalBit operationalBit = (OperationalBit) bits[0];
                return operation(operationalBit.getOperation(), operationalBit.getBits());
            }
            return bits[0];
        }

        if (operation == Operation.NOT && bits[0] instanceof OperationalBit && ((OperationalBit) bits[0]).getOperation() == Operation.NOT) {
            return ((OperationalBit) bits[0]).getBits()[0];
        }

        switch (operation) {
            case NOT:
                return super.not(bits[0]);
            case OR:
                return super.or(bits);
            case AND:
                return super.and(bits);
            default:
                throw new RuntimeException("unsupported operation type");
        }
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

    private Bit[] distinct(Bit[] bits) {
        return Arrays.stream(bits).distinct().toArray(Bit[]::new);
    }
}
