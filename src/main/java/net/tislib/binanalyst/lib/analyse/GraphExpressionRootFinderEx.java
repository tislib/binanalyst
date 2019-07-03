package net.tislib.binanalyst.lib.analyse;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import net.tislib.binanalyst.lib.bit.NamedBit;
import net.tislib.binanalyst.lib.bit.OperationalBit;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.Operation;

@SuppressWarnings("ALL")
public class GraphExpressionRootFinderEx {
    private final BitOpsGraphCalculator calculator;
    private long switchCount;
    private long stepCount;
    private long variationCount = 1;

    public GraphExpressionRootFinderEx(BitOpsGraphCalculator calculator) {
        this.calculator = calculator;
    }

    public void analyse() {

    }

    public void show() {
        System.out.println("switchCount     : " + switchCount);
        System.out.println("variationCount  : " + variationCount);
        System.out.println("stepCount       : " + stepCount);
    }

    public void traverse() {
        NamedBit truth = calculator.getOutput().getBits().get(0);
        Set<String> val = traverse(truth);
        System.out.println(val);
        System.out.println(val.size());
    }

    private Set<String> traverse(NamedBit bit) {
        if (bit instanceof OperationalBit) {
            OperationalBit operationalBit = (OperationalBit) bit;
            if (operationalBit.getOperation() == Operation.AND) {
                return Arrays.stream(operationalBit.getBits())
                        .flatMap(item -> this.traverse(item).stream())
                        .collect(Collectors.toSet());
            } else if (operationalBit.getOperation() == Operation.OR) {
                switchCount += operationalBit.getBits().length;
                variationCount *= operationalBit.getBits().length;
                stepCount++;
                return traverse(operationalBit.getBits()[0]);
            } else if (operationalBit.getOperation() == Operation.NOT) {
                return Collections.singleton(operationalBit.showFull(false));
            } else {
                throw new RuntimeException();
            }
        } else {
            return Collections.singleton(bit.getName());
        }
    }
}
