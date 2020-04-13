package net.tislib.binanalyst.lib.calc.graph.operations;

import net.tislib.binanalyst.lib.bit.*;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.GraphBitOpsCalculator;
import net.tislib.binanalyst.lib.calc.graph.GraphCalculatorOperation;
import net.tislib.binanalyst.lib.calc.graph.UsageFinder;
import net.tislib.binanalyst.lib.calc.graph.decorator.BinderOptimizationDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.ConstantOperationRemoverOptimizationDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.optimizer.Logical2OptimizationDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.optimizer.NewOptimizerOptimizationDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.optimizer.SimpleOptimizationDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.optimizer.UnusedBitOptimizerDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.optimizer.pattern.PatternOptimizationDecorator;

import java.util.HashMap;
import java.util.Map;

public class MutationOperation implements GraphCalculatorOperation {

    private final VarBit mutator;

    public MutationOperation(VarBit mutator) {
        this.mutator = mutator;
    }

    @Override
    public BitOpsGraphCalculator transform(final BitOpsGraphCalculator calculator) {
        BitOpsGraphCalculator newCalculator = new GraphBitOpsCalculator();
        newCalculator = new ConstantOperationRemoverOptimizationDecorator(newCalculator);
//        newCalculator = new SimpleOptimizationDecorator(newCalculator);
        newCalculator = new UnusedBitOptimizerDecorator(newCalculator);
//        newCalculator = new NewOptimizerOptimizationDecorator(newCalculator);
//        newCalculator = new PatternOptimizationDecorator(newCalculator);
        newCalculator = new BinderOptimizationDecorator(newCalculator);
        newCalculator = new Logical2OptimizationDecorator(newCalculator);

        newCalculator.setInputBits(calculator.getInput().getBits().toArray(new VarBit[0]));

        for (NamedBit namedBit : calculator.getOutput()) {
            newCalculator.getOutput().addBits((NamedBit) newCalculator.or(
                    mutate(namedBit, newCalculator, calculator, true, new HashMap<>()),
                    mutate(namedBit, newCalculator, calculator, false, new HashMap<>())
            ));
        }

//        UsageFinder usageFinder = new UsageFinder(newCalculator.getInput(), newCalculator.getMiddle(), newCalculator.getOutput());
//        usageFinder.cleanUnusedMiddleBits();

        return newCalculator;
    }

    private Bit mutate(NamedBit namedBit, BitOpsGraphCalculator newCalculator, BitOpsGraphCalculator calculator, boolean truth, Map<String, Bit> cache) {
        if (cache.containsKey(namedBit.getName())) {
            return cache.get(namedBit.getName());
        }
        if (namedBit instanceof OperationalBit) {
            OperationalBit operationalBit = (OperationalBit) namedBit;
            NamedBit[] bits = new NamedBit[operationalBit.getBits().length];
            for (int i = 0; i < bits.length; i++) {
                bits[i] = (NamedBit) mutate(operationalBit.getBits()[i], newCalculator, calculator, truth, cache);
            }
            Bit result = newCalculator.operation(operationalBit.getOperation(), bits);
            cache.put(namedBit.getName(), result);
            return result;
        } else if (namedBit instanceof VarBit) {
            if (this.mutator == namedBit) {
                return truth ? ConstantBit.ONE : ConstantBit.ZERO;
            } else {
                return namedBit;
            }
        }

        return namedBit;
    }

}
