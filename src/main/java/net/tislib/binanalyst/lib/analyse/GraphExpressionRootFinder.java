package net.tislib.binanalyst.lib.analyse;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import net.tislib.binanalyst.lib.bit.NamedBit;
import net.tislib.binanalyst.lib.bit.OperationalBit;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.Operation;

public class GraphExpressionRootFinder {
    private final BitOpsGraphCalculator calculator;
    private long switchCount;
    private long stepCount;
    private long variationCount = 1;
    private Map<String, Set<String>> bitUsage = new HashMap<>();
    Map<String, Integer> switchs = new HashMap<>();

    public GraphExpressionRootFinder(BitOpsGraphCalculator calculator) {
        this.calculator = calculator;
    }

    public void analyse() {
        for (OperationalBit operationalBit : calculator.getMiddle()) {
            if (operationalBit.getOperation() == Operation.OR) {
                switchs.put(operationalBit.getName(), 0);
            }
        }

        tryToSolve(calculator.getOutput().getBits().get(0), 0);
        tryToSolve(calculator.getOutput().getBits().get(0), 1);
        tryToSolve(calculator.getOutput().getBits().get(0), 2);
        tryToSolve(calculator.getOutput().getBits().get(0), 3);
        tryToSolve(calculator.getOutput().getBits().get(0), 4);
        tryToSolve(calculator.getOutput().getBits().get(0), 5);
    }

    private void tryToSolve(NamedBit namedBit, int layerNum) {
        boolean randomize = false;
        while (true) {
            System.out.println("Layer: " + layerNum);
            Set<String> conflicts = findConflicts(namedBit);
            long conflictSize = findConflicts(layerNum);
            System.out.println(conflictSize);
            System.out.println(conflicts);

            mainLoop:
            for (OperationalBit operationalBit : calculator.getMiddle()) {
                if (operationalBit.getOperation() == Operation.OR) {
                    for (int i = 0; i < operationalBit.getBits().length; i++) {
                        Integer prevSwitchValue = switchs.get(operationalBit.getName());
                        switchs.put(operationalBit.getName(), switchs.get(operationalBit.getName()) + i);
                        if (switchs.get(operationalBit.getName()) >= operationalBit.getBits().length) {
                            switchs.put(operationalBit.getName(), 0);
                        }
                        if (findConflicts(layerNum) >= conflictSize && !randomize) {
                            switchs.put(operationalBit.getName(), prevSwitchValue);
                        } else if (findConflicts(layerNum) < conflictSize) {
                            System.out.println("OPTIMIZED: " + findConflicts(layerNum));
                            break mainLoop;
                        }
                    }
                }
            }

            if (conflictSize == 0) {
                System.out.println("Layer: " + layerNum + " is done");
                return;
            }

//            randomize = findConflicts() == conflictSize;
        }

    }

    private long findConflicts(int layerNum) {
        long res = 0;
        for (OperationalBit operationalBit : calculator.getMiddle()) {
            if (Integer.parseInt(operationalBit.getName().substring(1, 2)) > layerNum || !operationalBit.getName().substring(1, 3).endsWith(":")) {
                continue;
            }
            Set<String> conflicts = findConflicts(operationalBit);
//            if (conflicts.size() > 0) {
//                System.out.println(operationalBit.getName() + ": " + conflicts);
//            }
            if (conflicts.size() > 0)
                res += 1;

        }
        return res;
    }


    private Set<String> findConflicts(NamedBit bit) {
        Map<String, Set<String>> cache = new HashMap<>();
        return AnalyserUtil.findConflicts(findDependencies(bit, cache));
    }

    @SuppressWarnings("Duplicates")
    private Set<String> findDependencies(NamedBit bit, Map<String, Set<String>> cache) {
        if (!cache.containsKey(bit.getName())) {
            if (bit instanceof OperationalBit) {
                OperationalBit operationalBit = (OperationalBit) bit;
                if (operationalBit.getOperation() == Operation.AND) {
                    Set<String> res = Arrays.stream(operationalBit.getBits())
                            .flatMap(item -> findDependencies(item, cache).stream())
                            .collect(Collectors.toSet());
                    cache.put(bit.getName(), res);
                    return res;
                } else if (operationalBit.getOperation() == Operation.OR) {
                    switchCount += operationalBit.getBits().length;
                    variationCount *= operationalBit.getBits().length;
                    stepCount++;
                    Set<String> res = findDependencies(operationalBit.getBits()[switchs.get(operationalBit.getName())], cache);
                    cache.put(bit.getName(), res);
                    return res;
                } else if (operationalBit.getOperation() == Operation.NOT) {
                    Set<String> res = Collections.singleton(operationalBit.showFull(false));
                    cache.put(bit.getName(), res);
                    return res;
                } else {
                    throw new RuntimeException();
                }
            } else {
                Set<String> res = Collections.singleton(bit.getName());
                cache.put(bit.getName(), res);
                return res;
            }
        } else {
            return cache.get(bit.getName());
        }
    }


    private long countConflicts(Set<String> set1, Set<String> set2) {
        long count = 0;
        for (String bit1 : set1) {
            if (hasConflict(set2, bit1)) count++;
        }
        return count;
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
