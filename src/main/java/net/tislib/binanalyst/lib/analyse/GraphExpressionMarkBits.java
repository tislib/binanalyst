package net.tislib.binanalyst.lib.analyse;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.tislib.binanalyst.lib.bit.NamedBit;
import net.tislib.binanalyst.lib.bit.OperationalBit;
import net.tislib.binanalyst.lib.bit.VarBit;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.Operation;
import net.tislib.binanalyst.lib.calc.graph.tools.GraphCalculatorTools;

public class GraphExpressionMarkBits {

    private final Map<String, Set<String>> marks = new HashMap<>();
    private final BitOpsGraphCalculator calculator;
    private NamedBit truth;

    public GraphExpressionMarkBits(BitOpsGraphCalculator calculator) {
        this.calculator = calculator;
    }

    public void analyse() {
        this.truth = calculator.getOutput().getBits().get(0);

        markAlso(this.truth, 0);
    }

    private void markAlso(NamedBit bit, int depth) {
        if (bit instanceof OperationalBit) {
            OperationalBit operationalBit = (OperationalBit) bit;
            if (operationalBit.getOperation() == Operation.AND) {
                for (NamedBit namedBit : ((OperationalBit) bit).getBits()) {
                    markAlso(namedBit, depth + 1);
                }
            } else if (operationalBit.getOperation() == Operation.OR) {
                for (NamedBit namedBit : ((OperationalBit) bit).getBits()) {
                    markAlso(namedBit, depth + 1);
                }
            } else if (operationalBit.getOperation() == Operation.NOT) {
                marks.putIfAbsent(getKey(bit), new HashSet<>());
                marks.get(getKey(bit)).add(String.valueOf(depth));
            }
            for (NamedBit namedBit : GraphCalculatorTools.findDependencyBits(operationalBit)) {
                marks.get(getKey(namedBit)).add(depth + ":" + bit.getName());
            }
        } else {
            marks.putIfAbsent(getKey(bit), new HashSet<>());
            marks.get(getKey(bit)).add(String.valueOf(depth));
        }
    }

    private String getKey(NamedBit bit) {
        if (bit instanceof OperationalBit && ((OperationalBit) bit).getOperation() == Operation.NOT) {
            return ((OperationalBit) bit).showFull(false);
        } else {
            return bit.getName();
        }
    }

    public void show() {
        for (VarBit varBit : calculator.getInput()) {
            System.out.println(varBit.getName() + ": " + marks.get(varBit.getName()));
            String negKey = ((OperationalBit) calculator.not(varBit)).showFull(false);
            System.out.println(negKey + ": " + marks.get(negKey));
        }
    }
}
