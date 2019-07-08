package net.tislib.binanalyst.lib.analyse;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import net.tislib.binanalyst.lib.bit.NamedBit;
import net.tislib.binanalyst.lib.bit.OperationalBit;
import net.tislib.binanalyst.lib.bit.VarBit;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.Operation;

public class GraphExpressionFindEarlyConflicts {
    private final BitOpsGraphCalculator calculator;
    private long switchCount;
    private long stepCount;
    private long variationCount = 1;
    private Map<String, Set<String>> bitUsage = new HashMap<>();
    Map<String, Integer> switchs = new HashMap<>();
    private NamedBit truth;

    public GraphExpressionFindEarlyConflicts(BitOpsGraphCalculator calculator) {
        this.calculator = calculator;
    }

    private final Map<String, Boolean> cache = new HashMap<>();

    public void analyse() {
        this.truth = calculator.getOutput().getBits().get(0);

        for (VarBit varBit : calculator.getInput()) {
            if (checkIsConflicting(varBit)) {
                System.out.println("conflicting found: " + varBit.getName());
            }
            if (checkIsConflicting((NamedBit) calculator.not(varBit))) {
                System.out.println("conflicting found: " + varBit.getName());
            }
        }
    }


    private boolean checkIsConflicting(NamedBit bit) {
        return checkIsConflicting(bit, truth);
    }

    private boolean checkIsConflicting(NamedBit bit, NamedBit truth) {
        String key = bit.getName() + "-" + truth.getName();
        if (bit instanceof OperationalBit) {
            key = ((OperationalBit) bit).showFull(false) + "-" + truth.getName();
        }
        if (cache.containsKey(key)) {
            return cache.get(key);
        }
        if (truth instanceof OperationalBit) {
            OperationalBit operationalBit = (OperationalBit) truth;
            if (operationalBit.getOperation() == Operation.AND) {
                boolean res = false;
                for (NamedBit namedBit : operationalBit.getBits()) {
                    res &= checkIsConflicting(bit, namedBit);
                }
                cache.put(key, res);
                return res;
            } else if (operationalBit.getOperation() == Operation.OR) {
                for (NamedBit namedBit : operationalBit.getBits()) {
                    if (checkIsConflicting(bit, namedBit)) {
                        cache.put(key, true);
                        return true;
                    }
                }
                cache.put(key, false);
                return false;
            } else if (operationalBit.getOperation() == Operation.NOT) {
                boolean res = AnalyserUtil.isConflicting(bit, truth);
                cache.put(key, res);
                return res;
            } else {
                return true;
            }
        } else {
            boolean res = AnalyserUtil.isConflicting(bit, truth);
            cache.put(key, res);
            return res;
        }

    }

    private boolean hasConflict(Set<String> set, String bitName) {
        boolean isNot = bitName.startsWith("!");
        String bitReverse = isNot ? bitName.substring(1) : "!" + bitName;
        return set.contains(bitReverse);
    }

    public void show() {
        System.out.println("switchCount     : " + switchCount);
        System.out.println("variationCount  : " + variationCount);
        System.out.println("stepCount       : " + stepCount);
        System.out.println("bitUsage        : " + bitUsage);
    }
}
