package net.tislib.binanalyst.lib.calc.graph.decorator;

import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.Operation;

public abstract class OptimizerGraphCalculatorDecorator extends AbstractBitOpsGraphCalculatorDecorator {
    public OptimizerGraphCalculatorDecorator(BitOpsGraphCalculator calculator) {
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


    protected abstract Bit optimize(Operation xor, Bit... bits);
}
