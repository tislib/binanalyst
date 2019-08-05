package net.tislib.binanalyst.lib.calc.graph.decorator;

import static net.tislib.binanalyst.lib.bit.ConstantBit.ONE;
import static net.tislib.binanalyst.lib.bit.ConstantBit.ZERO;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.NamedBit;
import net.tislib.binanalyst.lib.bit.OperationalBit;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.Operation;

@SuppressWarnings("ALL")
public class Logical2OptimizationDecorator extends AbstractBitOpsGraphCalculatorDecorator {
    public Logical2OptimizationDecorator(BitOpsGraphCalculator calculator) {
        super(calculator);
    }

    private static int c = 0;

    @Override
    public Bit or(Bit... bits) {
        if (bits.length != 2
                || !(bits[0] instanceof OperationalBit)
                || !(bits[1] instanceof OperationalBit)) {
            return super.or(bits);
        }

        if (bits[0] == bits[1]) {
            return bits[0];
        }

        OperationalBit operationalBit0 = (OperationalBit) bits[0];
        OperationalBit operationalBit1 = (OperationalBit) bits[1];

        if (operationalBit0.getOperation() == Operation.AND && operationalBit1.getOperation() == Operation.AND) {
            Set<NamedBit> sharedBits = new HashSet<>();
            Set<NamedBit> bits0 = new HashSet<>(Arrays.asList(operationalBit0.getBits()));
            Set<NamedBit> bits1 = new HashSet<>(Arrays.asList(operationalBit1.getBits()));

            for (NamedBit namedBit : operationalBit0.getBits()) {
                if (bits1.contains(namedBit)) {
                    sharedBits.add(namedBit);
                    bits0.remove(namedBit);
                    bits1.remove(namedBit);
                }
            }

            if (bits0.size() == 0) {
                return operationalBit1;
            }
            if (bits1.size() == 0) {
                return operationalBit0;
            }

            if (sharedBits.size() > 200) {
                NamedBit newOr = (NamedBit) optimize(Operation.OR,
                        and2(bits0.toArray(new NamedBit[0])),
                        and2(bits1.toArray(new NamedBit[0]))
                );
                sharedBits.add(newOr);

                c++;

                return and(sharedBits.toArray(new NamedBit[0]));
            }
        }

        return optimize(Operation.OR, bits);
    }

    private Bit and2(NamedBit[] bits) {
        if (bits.length == 1) {
            return bits[0];
        }
        return and(bits);
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
    public Bit not(Bit bit) {
        return optimize(Operation.NOT, bit);
    }

    Bit optimize(Operation operation, Bit... bits) {
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
