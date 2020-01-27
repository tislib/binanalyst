package net.tislib.binanalyst.lib.calc.graph.decorator.optimizer;


import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.decorator.AbstractBitOpsGraphCalculatorDecorator;

public class TwoOpsOptimizationDecorator extends AbstractBitOpsGraphCalculatorDecorator {
    private boolean ignoreHalfMiddle = true;

    public TwoOpsOptimizationDecorator(BitOpsGraphCalculator calculator) {
        super(calculator);
    }

    @Override
    public Bit or(Bit... bits) {
        if (bits.length == 1) {
            return bits[0];
        }

        Bit res = bits[0];
        for (int i = 1; i < bits.length; i++) {
            res = super.or(res, bits[i]);
        }

        return res;
    }

    @Override
    public Bit and(Bit... bits) {
        if (bits.length == 1) {
            return bits[0];
        }

        Bit res = bits[0];
        for (int i = 1; i < bits.length; i++) {
            res = super.and(res, bits[i]);
        }

        return res;
    }

    @Override
    public Bit xor(Bit... bits) {
        if (bits.length == 1) {
            return bits[0];
        }

        Bit res = bits[0];
        for (int i = 1; i < bits.length; i++) {
            res = super.xor(res, bits[i]);
        }

        return res;
    }
}
