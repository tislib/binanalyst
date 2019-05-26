package net.tislib.binanalyst.lib.calc.graph.decorator;

import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.ConstantBit;
import net.tislib.binanalyst.lib.bit.NamedBit;
import net.tislib.binanalyst.lib.bit.OperationalBit;
import net.tislib.binanalyst.lib.bit.VarBit;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.UsageFinder;

public class AndOrCalculatorDecorator extends AbstractBitOpsGraphCalculatorDecorator {

    private final boolean reverseInsteadOfNot;

    public AndOrCalculatorDecorator(BitOpsGraphCalculator calculator) {
        this(calculator, false);
    }

    public AndOrCalculatorDecorator(BitOpsGraphCalculator calculator, boolean reverseInsteadOfNot) {
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
            return super.not(bit);
        }
    }

    @Override
    public Bit xor(Bit... bits) {
        if (bits.length == 0) {
            throw new RuntimeException();
        } else if (bits.length == 1) {
            return bits[0];
        }
        Bit[] newBits = new Bit[bits.length - 1];
        System.arraycopy(bits, 0, newBits, 0, bits.length - 1);

        Bit leftBit = super.xor(newBits);
        Bit rightBit = bits[bits.length - 1];

        return or(and(leftBit, not(rightBit)), and(rightBit, not(leftBit)));
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
                    not(xor(bits));
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
}
