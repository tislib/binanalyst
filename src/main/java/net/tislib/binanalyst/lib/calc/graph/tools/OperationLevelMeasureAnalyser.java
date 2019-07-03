package net.tislib.binanalyst.lib.calc.graph.tools;


import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.OperationalBit;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;

public class OperationLevelMeasureAnalyser {

    private final BitOpsGraphCalculator calculator;
    private Map<OperationalBit, Integer> bitLevelMapUpper = new HashMap<>();
    private Map<OperationalBit, Integer> bitLevelMapLower = new HashMap<>();

    public OperationLevelMeasureAnalyser(BitOpsGraphCalculator calculator) {
        this.calculator = calculator;
    }

    public void analyse() {
        for (OperationalBit bit : calculator.getMiddle()) {
            registerInternal(bit, true);
            registerInternal(bit, false);
        }
    }

    private OperationalBit registerInternal(OperationalBit opBit, boolean upper) {
        // find max internal level
        int maxInternalLevel = 0;
        int minInternalLevel = 9999;
        for (Bit bit : opBit.getBits()) {
            Integer level = upper ? bitLevelMapUpper.get(bit) : bitLevelMapLower.get(bit);
            if (level == null) {
                level = 0;
            }
            if (level > maxInternalLevel) {
                maxInternalLevel = level;
            }
            if (level < minInternalLevel) {
                minInternalLevel = level;
            }
        }
        if (upper) {
            bitLevelMapLower.put(opBit, minInternalLevel + 1);
        } else {
            bitLevelMapUpper.put(opBit, maxInternalLevel + 1);
        }
        return opBit;
    }

    public void reLabel() {
        for (Bit bit : calculator.getMiddle()) {
            OperationalBit operationalBit = (OperationalBit) bit;
            operationalBit.setName("L" + bitLevelMapUpper.get(bit) + ":" + operationalBit.getName());
        }
    }

    public void show() {
        System.out.println("LOWER STATS:");
        for (Map.Entry<OperationalBit, Integer> entry : bitLevelMapLower.entrySet()) {
            System.out.println(entry.getKey().getName() + " => " + entry.getValue());
        }
        System.out.println("UPPER STATS:");
        for (Map.Entry<OperationalBit, Integer> entry : bitLevelMapUpper.entrySet()) {
            System.out.println(entry.getKey().getName() + " => " + entry.getValue());
        }
    }

    public Map<Integer, Long> stats() {
        Map<Integer, Long> res = bitLevelMapLower.entrySet()
                .stream()
                .collect(Collectors.groupingBy(Map.Entry::getValue, Collectors.counting()));
        return res;
    }

    public Map<Integer, Long> statsUpper() {
        Map<Integer, Long> res = bitLevelMapUpper.entrySet()
                .stream()
                .collect(Collectors.groupingBy(Map.Entry::getValue, Collectors.counting()));
        return res;
    }

}
