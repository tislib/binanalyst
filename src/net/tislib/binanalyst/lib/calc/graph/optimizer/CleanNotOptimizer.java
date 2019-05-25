package net.tislib.binanalyst.lib.calc.graph.optimizer;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.NamedBit;
import net.tislib.binanalyst.lib.bit.OperationalBit;
import net.tislib.binanalyst.lib.calc.graph.GraphBitOpsCalculator;
import net.tislib.binanalyst.lib.calc.graph.Operation;

/**
 * Created by Taleh Ibrahimli on 2/9/18.
 * Email: me@talehibrahimli.com
 */
public class CleanNotOptimizer implements Optimizer {

    @Override
    public NamedBit optimizeOperation(GraphBitOpsCalculator graphBitOpsCalculator, Operation operation, NamedBit[] bits, NamedBit chain) {
        return chain;
    }

    @Override
    public void optimizeCalculator(GraphBitOpsCalculator calculator) {
        for (int i = 0; i < calculator.getOutput().getBits().size(); i++) {
            NamedBit namedBit = optimize(calculator, calculator.getOutput().getBits().get(i));
            calculator.getOutput().getBits().set(i, namedBit);
        }

        for (int i = 0; i < calculator.getMiddle().getBits().size(); i++) {
            try {
                NamedBit theBit = optimize(calculator, calculator.getMiddle().getBits().get(i));
                if (theBit instanceof OperationalBit) {
                    OperationalBit namedBit = (OperationalBit) theBit;
                    calculator.getMiddle().getBits().set(i, namedBit);
                } else {
                    calculator.getMiddle().getBits().remove(i);
                    i--;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Set<NamedBit> markedBits = new HashSet<>();
        for (NamedBit namedBit : calculator.getOutput()) {
            if (namedBit instanceof OperationalBit) {
                markBit((OperationalBit) namedBit, markedBits);
            }
        }

        calculator.getMiddle().setBits(
                calculator.getMiddle().getBits()
                        .stream().filter(markedBits::contains)
                        .collect(Collectors.toList())
        );
    }

    void markBit(OperationalBit bit, Set<NamedBit> markedBit) {
        markedBit.add(bit);
        for (NamedBit namedBit : bit.getBits()) {
            if (!markedBit.contains(namedBit) && (namedBit instanceof OperationalBit)) {
                markBit((OperationalBit) namedBit, markedBit);
            }
            markedBit.add(namedBit);
        }
    }

    private NamedBit optimize(GraphBitOpsCalculator calculator, NamedBit namedBit) {
        if (namedBit instanceof OperationalBit) {
            OperationalBit operationalBit = (OperationalBit) namedBit;
            if (operationalBit.getOperation() == Operation.XOR) {
//                return removeXor(calculator, operationalBit);
            } else if (operationalBit.getOperation() == Operation.NOT) {
                return removeNot(calculator, operationalBit);
            }
        }

        return namedBit;
    }

    private NamedBit removeNot(GraphBitOpsCalculator calculator, OperationalBit operationalBit) {
        if (operationalBit.getOperation() == Operation.NOT) {
            Bit bit = operationalBit.getBits()[0];
            if (bit instanceof OperationalBit) {
                OperationalBit nBit = (OperationalBit) bit;
                if (nBit.getOperation() == Operation.NOT) {
                    return nBit.getBits()[0];
                } else if (nBit.getOperation() == Operation.AND) {
                    Bit[] switchedBits = new Bit[nBit.getBits().length];
                    for (int i = 0; i < switchedBits.length; i++) {
                        switchedBits[i] = calculator.not(nBit.getBits()[i]);
                    }
                    return (NamedBit) calculator.or(switchedBits);
                } else if (nBit.getOperation() == Operation.OR) {
                    Bit[] switchedBits = new Bit[nBit.getBits().length];
                    for (int i = 0; i < switchedBits.length; i++) {
                        switchedBits[i] = calculator.not(nBit.getBits()[i]);
                    }
                    return (NamedBit) calculator.and(switchedBits);
                }
            }
        }
        return operationalBit;
    }
}
