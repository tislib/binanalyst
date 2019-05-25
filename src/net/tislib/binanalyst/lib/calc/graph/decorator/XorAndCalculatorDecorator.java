package net.tislib.binanalyst.lib.calc.graph.decorator;

import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.ConstantBit;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;

public class XorAndCalculatorDecorator extends AbstractBitOpsGraphCalculatorDecorator {

    public XorAndCalculatorDecorator(BitOpsGraphCalculator calculator) {
        super(new SimpleOptimizationDecorator(calculator));
    }

    @Override
    public Bit not(Bit bit) {
        return xor(bit, ConstantBit.ONE);
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
