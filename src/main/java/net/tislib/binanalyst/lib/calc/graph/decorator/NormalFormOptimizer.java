package net.tislib.binanalyst.lib.calc.graph.decorator;

import static net.tislib.binanalyst.lib.bit.ConstantBit.ONE;
import static net.tislib.binanalyst.lib.bit.ConstantBit.ZERO;

import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.Operation;
import net.tislib.binanalyst.lib.calc.graph.optimizer.Optimizer;

public class NormalFormOptimizer extends AbstractBitOpsGraphCalculatorDecorator {
    public NormalFormOptimizer(BitOpsGraphCalculator calculator) {
        super(calculator);
    }

    @Override
    public void calculate() {
        optimize();
        super.calculate();
    }

    public void optimize() {

    }
}
