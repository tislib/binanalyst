package net.tislib.binanalyst.lib.calc.graph.optimizer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.NamedBit;
import net.tislib.binanalyst.lib.bit.OperationalBit;
import net.tislib.binanalyst.lib.calc.graph.GraphBitOpsCalculator;
import net.tislib.binanalyst.lib.calc.graph.Operation;

public class NfOptimizer implements Optimizer { // Normal Form Optimizer

    private final Optimizer logicalOptimizer = new LogicalOptimizer();

    @Override
    public NamedBit optimizeOperation(GraphBitOpsCalculator calculator, Operation operation, NamedBit[] bits, NamedBit chain) {
        return chain;
    }

    @Override
    public void optimizeCalculator(GraphBitOpsCalculator calculator) {
        calculator.getOutput().setBits(
                calculator.getOutput().getBits()
                        .stream()
                        .map(oBit -> {
                            if (oBit instanceof OperationalBit) {
                                return optimize(calculator, (OperationalBit) oBit);
                            } else {
                                return oBit;
                            }
                        })
                        .collect(Collectors.toList())
        );
//        logicalOptimizer.optimizeCalculator(calculator);
    }

    public NamedBit optimize(GraphBitOpsCalculator calculator, OperationalBit bit) {
        OperationalBit res = bit;
        for (Bit inputBit : calculator.getInput().getBits()) {
            res = optimizeForBit(calculator, res, inputBit);
        }
        return res;
    }

    private OperationalBit optimizeForBit(GraphBitOpsCalculator calculator,
                                          OperationalBit oBit,
                                          Bit inputBit) {
        return (OperationalBit) calculator.or(
                calculator.and(getOPosBit(calculator, oBit, inputBit), inputBit),
                calculator.and(getONegBit(calculator, oBit, inputBit), calculator.not(inputBit))
        );
    }

    private Bit getOPosBit(GraphBitOpsCalculator calculator, OperationalBit oBit, Bit inputBit) {
        List<NamedBit> newBits = new ArrayList<>();
        for (NamedBit namedBit : oBit.getBits()) {
            NamedBit newBit = namedBit;
            if (namedBit == inputBit) {
                switch (oBit.getOperation()) {
                    case XOR:
                        newBit = calculator.ONE;
                        break;
                    case AND:
                        continue;
                    case OR:
                        return calculator.ONE;
                    case NOT:
                        return calculator.ZERO;
                }
            } else if (newBit instanceof OperationalBit) {
                newBit = (NamedBit) getOPosBit(calculator, (OperationalBit) newBit, inputBit);
            }
            newBits.add(newBit);
        }
        return calculator.operation(oBit.getOperation(), newBits.toArray(new NamedBit[]{}));
    }

    private Bit getONegBit(GraphBitOpsCalculator calculator, OperationalBit oBit, Bit inputBit) {
        List<NamedBit> newBits = new ArrayList<>();
        for (NamedBit namedBit : oBit.getBits()) {
            NamedBit newBit = namedBit;
            if (namedBit == inputBit) {
                switch (oBit.getOperation()) {
                    case XOR:
                    case OR:
                        continue;
                    case AND:
                        return calculator.ZERO;
                    case NOT:
                        return calculator.ONE;
                }
            } else if (newBit instanceof OperationalBit) {
                if (((OperationalBit) newBit).getBits().length == 0) {
                    System.out.println("ZZZ");
                }
                newBit = (NamedBit) getONegBit(calculator, (OperationalBit) newBit, inputBit);
            }
            newBits.add(newBit);
        }
        if (newBits.size() == 1 && oBit.getOperation() != Operation.NOT) {
            return newBits.get(0);
        }

        if (newBits.size() == 0) {
            System.out.println("XXX");
        }

        return calculator.operation(oBit.getOperation(), newBits.toArray(new NamedBit[]{}));
    }
}
