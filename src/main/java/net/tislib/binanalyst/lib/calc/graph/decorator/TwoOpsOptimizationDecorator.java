package net.tislib.binanalyst.lib.calc.graph.decorator;


import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;

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
}
