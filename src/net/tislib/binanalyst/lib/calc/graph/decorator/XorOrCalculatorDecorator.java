package net.tislib.binanalyst.lib.calc.graph.decorator;

import java.util.HashMap;
import java.util.Map;
import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.ConstantBit;
import net.tislib.binanalyst.lib.bit.NamedBit;
import net.tislib.binanalyst.lib.bit.OperationalBit;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;

public class XorOrCalculatorDecorator extends AbstractBitOpsGraphCalculatorDecorator {

    private final boolean reverseInsteadOfNot;

    private final Map<Bit, Bit> reverseBitMap = new HashMap<>();

    public XorOrCalculatorDecorator(BitOpsGraphCalculator calculator) {
        this(calculator, false);
    }

    public XorOrCalculatorDecorator(BitOpsGraphCalculator calculator, boolean reverseInsteadOfNot) {
        super(new SimpleOptimizationDecorator(calculator));
        this.reverseInsteadOfNot = reverseInsteadOfNot;
    }

    @Override
    public Bit not(Bit bit) {
        if (reverseInsteadOfNot) {
            return reverseBitMap.computeIfAbsent(bit, this::reverse);
        } else {
            return xor(bit, ConstantBit.ONE);
        }
    }

    private Bit reverse(Bit bit) {
        if (bit instanceof NamedBit) {
//            System.out.println("reversing: " + ((NamedBit) bit).getName());
        }
        if (bit instanceof OperationalBit) {
            OperationalBit operationalBit = (OperationalBit) bit;
            NamedBit[] bits = operationalBit.getBits();
            switch (operationalBit.getOperation()) {
                case NOT:
                    return bits[0];
                case XOR: {
                    NamedBit[] newBits = new NamedBit[bits.length];
                    System.arraycopy(bits, 0, newBits, 0, bits.length);

                    newBits[newBits.length - 1] = (NamedBit) not(newBits[newBits.length - 1]);
                    return xor(newBits);
                }
                case OR:
                    if (bits.length == 0) {
                        throw new RuntimeException();
                    } else if (bits.length == 1) {
                        throw new RuntimeException();
                    } else if (bits.length == 2) {
                        return xor(or(not(bits[0]), not(bits[1])), bits[0], bits[1]);
                    } else {
                        NamedBit[] newBits = new NamedBit[bits.length - 1];
                        System.arraycopy(bits, 0, newBits, 0, bits.length - 1);

                        Bit leftBit = or(newBits);

                        Bit rightBit = bits[bits.length - 1];
                        return xor(or(not(leftBit), not(rightBit)), leftBit, rightBit);
                    }
                default:
                    throw new RuntimeException();
            }
        } else {
            return super.not(bit);
        }
    }

    @Override
    public Bit and(Bit... bits) {
        if (bits.length == 0) {
            throw new RuntimeException();
        } else if (bits.length == 1) {
            throw new RuntimeException();
        } else if (bits.length == 2) {
            return xor(or(bits[0], bits[1]), bits[0], bits[1]);
        } else {
            Bit[] newBits = new Bit[bits.length - 1];
            System.arraycopy(bits, 0, newBits, 0, bits.length - 1);

            Bit leftBit = and(newBits);
            Bit rightBit = bits[bits.length - 1];
            return xor(or(leftBit, rightBit), leftBit, rightBit);
        }
    }
}
