package net.tislib.binanalyst.lib.calc.graph.decorator;

import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.UsageFinder;

public class UnusedBitOptimizerDecorator extends AbstractBitOpsGraphCalculatorDecorator {
    public UnusedBitOptimizerDecorator(BitOpsGraphCalculator calculator) {
        super(calculator);
    }

    @Override
    public void calculate() {
        UsageFinder usageFinder = new UsageFinder(getInput(), getMiddle(), getOutput());
        usageFinder.cleanUnusedMiddleBits();
        super.calculate();
    }
}
