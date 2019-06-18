package net.tislib.binanalyst.lib.calc.graph.tools;


import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.OperationalBit;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;

public class OperationLevelMeasureAnalyser {

    private Map<OperationalBit, Integer> bitLevelMap = new HashMap<>();

    private final BitOpsGraphCalculator calculator;

    public OperationLevelMeasureAnalyser(BitOpsGraphCalculator calculator) {
        this.calculator = calculator;
    }

    public void analyse() {
        for (OperationalBit bit : calculator.getMiddle()) {
            registerInternal(bit);
        }
    }

    private OperationalBit registerInternal(OperationalBit opBit) {
        // find max internal level
        int maxInternalLevel = 0;
        int minInternalLevel = 9999;
        for (Bit bit : opBit.getBits()) {
            Integer level = bitLevelMap.get(bit);
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
//        bitLevelMap.put(opBit, minInternalLevel + 1);
        bitLevelMap.put(opBit, maxInternalLevel + 1);
        return opBit;
    }

    public void reLabel() {
        for (Bit bit : calculator.getMiddle()) {
            OperationalBit operationalBit = (OperationalBit) bit;
            operationalBit.setName("L" + bitLevelMap.get(bit) + ":" + operationalBit.getName());
        }
    }

    public void show() {
        for (Map.Entry<OperationalBit, Integer> entry : bitLevelMap.entrySet()) {
            System.out.println(entry.getKey().getName() + " => " + entry.getValue());
        }
    }

    public Map<Integer, Long> stats() {
        Map<Integer, Long> res = bitLevelMap.entrySet()
                .stream()
                .collect(Collectors.groupingBy(Map.Entry::getValue, Collectors.counting()));
        return res;
    }
}
