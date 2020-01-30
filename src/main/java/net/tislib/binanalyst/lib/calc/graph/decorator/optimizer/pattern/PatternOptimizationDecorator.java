package net.tislib.binanalyst.lib.calc.graph.decorator.optimizer.pattern;

import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.Operation;
import net.tislib.binanalyst.lib.calc.graph.decorator.OptimizerGraphCalculatorDecorator;

public class PatternOptimizationDecorator extends OptimizerGraphCalculatorDecorator {

    private static final Pattern[] DEFAULT_PATTERNS = {
            Pattern.compile("!(!a)=>a"),
    };
    private final Pattern[] patterns;

    public PatternOptimizationDecorator(BitOpsGraphCalculator calculator, Pattern... patterns) {
        super(calculator);
        this.patterns = patterns;
    }

    public PatternOptimizationDecorator(BitOpsGraphCalculator calculator) {
        this(calculator, DEFAULT_PATTERNS);
    }

    @Override
    protected Bit optimize(Operation operation, Bit... bits) {
        return delegate(operation, bits);
    }
}
