package net.tislib.binanalyst.lib.calc.graph.decorator;

import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.ConstantBit;
import net.tislib.binanalyst.lib.bit.NamedBit;
import net.tislib.binanalyst.lib.bit.OperationalBit;
import net.tislib.binanalyst.lib.bit.VarBit;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.UsageFinder;

public class XorAndCalculatorDecorator extends AbstractBitOpsGraphCalculatorDecorator {

    private final boolean reverseInsteadOfNot;

    public XorAndCalculatorDecorator(BitOpsGraphCalculator calculator) {
        this(calculator, false);
    }

    public XorAndCalculatorDecorator(BitOpsGraphCalculator calculator, boolean reverseInsteadOfNot) {
        super(new SimpleOptimizationDecorator(calculator));
        this.reverseInsteadOfNot = reverseInsteadOfNot;
    }

    @Override
    public void setInputBits(VarBit[]... bits) {
        super.setInputBits(bits);
        if (reverseInsteadOfNot) {
            prepareNegatives();
        }
    }

    private void prepareNegatives() {
        for (VarBit varBit : this.getInput()) {
            not(varBit);
        }
    }

    @Override
    public Bit not(Bit bit) {
        if (reverseInsteadOfNot) {
            return reverse(bit);
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

                    newBits[0] = (NamedBit) not(newBits[0]);
                    return xor(newBits);
                }
                case AND:
                    if (bits.length == 0) {
                        throw new RuntimeException();
                    } else if (bits.length == 1) {
                        throw new RuntimeException();
                    } else if (bits.length == 2) {
                        return xor(bits[0], bits[1], and(not(bits[0]), not(bits[1])));
                    } else {
                        NamedBit[] newBits = new NamedBit[bits.length - 1];
                        System.arraycopy(bits, 0, newBits, 0, bits.length - 1);

                        Bit leftBit = and(newBits);

                        return xor(leftBit, bits[bits.length - 1], and(not(leftBit), not(bits[bits.length - 1])));
                    }
                default:
                    throw new RuntimeException();
            }
        } else {
            return super.not(bit);
        }
    }

    @Override
    public void calculate() {
        UsageFinder usageFinder = new UsageFinder(getInput(), getMiddle(), getOutput());
        usageFinder.cleanUnusedMiddleBits();

        super.calculate();
    }

    @Override
    public Bit or(Bit... bits) {
        Bit[] negatives = new Bit[bits.length];
        for (int i = 0; i < bits.length; i++) {
            negatives[i] = not(bits[i]);
        }
        return not(and(negatives));
    }
}
