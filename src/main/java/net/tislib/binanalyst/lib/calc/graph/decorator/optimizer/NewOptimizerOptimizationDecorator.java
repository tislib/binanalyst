package net.tislib.binanalyst.lib.calc.graph.decorator.optimizer;

import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.ConstantBit;
import net.tislib.binanalyst.lib.bit.NamedBit;
import net.tislib.binanalyst.lib.bit.OperationalBit;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.Operation;
import net.tislib.binanalyst.lib.calc.graph.decorator.AndOrCalculatorDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.OptimizerGraphCalculatorDecorator;
import net.tislib.binanalyst.lib.calc.graph.tools.GraphCalculatorTools;

import java.util.*;

public class NewOptimizerOptimizationDecorator extends OptimizerGraphCalculatorDecorator {
    @SuppressWarnings("unchecked")
    public NewOptimizerOptimizationDecorator(BitOpsGraphCalculator calculator) {
        super(chain(calculator, AndOrCalculatorDecorator::new));
    }

    @Override
    protected Bit optimize(Operation operation, Bit... bits) {
        Bit passedBit = delegate(operation, bits);
        if (passedBit instanceof OperationalBit) {
            Operation newOperation = ((OperationalBit) passedBit).getOperation();
            NamedBit[] newBits = ((OperationalBit) passedBit).getBits();

            if (newBits.length == 2) {
                return optimizeOperation(newOperation, newBits);
            }
        }

        return passedBit;
    }

    private Bit optimizeOperation(Operation operation, NamedBit[] bits) {
        NamedBit left = bits[0];
        NamedBit right = bits[1];

        if (containsBit(left, right) || containsBit(right, left)) {
            return optimizeInternal(operation, bits);
        }

        return delegate(operation, bits);
    }

    private Bit optimizeInternal(Operation operation, NamedBit[] bits) {
        long complexity = Arrays.stream(bits).filter(item -> item instanceof OperationalBit)
                .map(item -> GraphCalculatorTools.calculateComplexity((OperationalBit) item))
                .reduce(0L, Long::sum);

        if (complexity < 0 || complexity > 1000) {
            return delegate(operation, bits);
        }

        Set<Set<String>> nf = GraphCalculatorTools.toNormalForm((NamedBit) delegate(operation, bits));

        if (nf.size() == 0) {
            return ConstantBit.ZERO;
        }

        List<Bit> ors = new ArrayList<>();

        for (Set<String> orsNf : nf) {
            List<Bit> ands = new ArrayList<>();

            for (String nfItem : orsNf) {
                if (nfItem.startsWith("!")) {
                    ands.add(original.not(original.getInput().locate(nfItem.substring(1))));
                } else {
                    ands.add(original.getInput().locate(nfItem));
                }
            }

            Bit andBit = original.and(ands.toArray(new Bit[0]));
            ors.add(andBit);
        }

        Bit res = original.or(ors.toArray(new Bit[0]));

        long newComplexity = GraphCalculatorTools.calculateComplexity((OperationalBit) res);

        if (newComplexity < complexity) {
            return res;
        }

        return delegate(operation, bits);
    }


    private boolean containsBit(NamedBit left, NamedBit right) {
        return true;
    }

}
