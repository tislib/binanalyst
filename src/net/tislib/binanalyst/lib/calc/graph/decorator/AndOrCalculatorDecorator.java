package net.tislib.binanalyst.lib.calc.graph.decorator;

import static net.tislib.binanalyst.lib.bit.ConstantBit.ZERO;

import java.util.HashMap;
import java.util.Map;
import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.NamedBit;
import net.tislib.binanalyst.lib.bit.OperationalBit;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;

public class AndOrCalculatorDecorator extends AbstractBitOpsGraphCalculatorDecorator {

    private final boolean reverseInsteadOfNot;

    private final Map<Bit, Bit> reverseBitMap = new HashMap<>();

    public AndOrCalculatorDecorator(BitOpsGraphCalculator calculator) {
        this(calculator, false);
    }

    public AndOrCalculatorDecorator(BitOpsGraphCalculator calculator, boolean reverseInsteadOfNot) {
        super(new SimpleOptimizationDecorator(calculator));
        this.reverseInsteadOfNot = reverseInsteadOfNot;
    }

    @Override
    public Bit not(Bit bit) {
        if (reverseInsteadOfNot) {
            if (!reverseBitMap.containsKey(bit)) {
                reverseBitMap.put(bit, this.reverse(bit));
            }
            return reverseBitMap.get(bit);
        } else {
            return super.not(bit);
        }
    }

    @Override
    public Bit xor(Bit... bits) {
        if (bits.length == 0) {
            return ZERO;
        } else if (bits.length == 1) {
            return bits[0];
        }
        Bit[] newBits = new Bit[bits.length - 1];
        System.arraycopy(bits, 0, newBits, 0, bits.length - 1);

        Bit leftBit = xor(newBits);
        Bit rightBit = bits[bits.length - 1];

        return or(and(leftBit, not(rightBit)), and(rightBit, not(leftBit)));
    }

    private Bit reverse(Bit bit) {
        if (bit instanceof OperationalBit) {
            OperationalBit operationalBit = (OperationalBit) bit;
            NamedBit[] bits = operationalBit.getBits();
            switch (operationalBit.getOperation()) {
                case NOT:
                    return bits[0];
                case XOR: {
                    return not(xor(bits));
                }
                case AND: {
                    Bit[] negatives = new Bit[bits.length];
                    for (int i = 0; i < bits.length; i++) {
                        negatives[i] = not(bits[i]);
                    }
                    return or(negatives);
                }
                case OR: {
                    Bit[] negatives = new Bit[bits.length];
                    for (int i = 0; i < bits.length; i++) {
                        negatives[i] = not(bits[i]);
                    }
                    return and(negatives);
                }
            }
        }

        return super.not(bit);
    }
}
