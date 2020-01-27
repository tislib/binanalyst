package net.tislib.binanalyst.lib.calc.graph.decorator;

import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.Operation;

public abstract class OptimizerGraphCalculatorDecorator extends AbstractBitOpsGraphCalculatorDecorator {

    protected final BitOpsGraphCalculator original;

    public OptimizerGraphCalculatorDecorator(final BitOpsGraphCalculator calculator) {
        super(calculator);
        original = calculator;
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


    protected abstract Bit optimize(Operation operation, Bit... bits);

    protected Bit delegate(Operation operation, Bit[] bits) {
        switch (operation) {
            case NOT:
                return original.not(bits[0]);
            case OR:
                return original.or(bits);
            case AND:
                return original.and(bits);
            case XOR:
                return original.xor(bits);
            default:
                throw new RuntimeException("unsupported operation type");
        }
    }
}
