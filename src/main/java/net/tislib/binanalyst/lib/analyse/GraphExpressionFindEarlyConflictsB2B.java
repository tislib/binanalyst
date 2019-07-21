package net.tislib.binanalyst.lib.analyse;

import static net.tislib.binanalyst.lib.BinOps.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.NamedBit;
import net.tislib.binanalyst.lib.bit.OperationalBit;
import net.tislib.binanalyst.lib.bit.VarBit;
import net.tislib.binanalyst.lib.calc.BitOpsCalculator;
import net.tislib.binanalyst.lib.calc.SimpleBitOpsCalculator;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.Operation;

public class GraphExpressionFindEarlyConflictsB2B {
    private final BitOpsGraphCalculator calculator;

    private long variationCount = 1;
    private Map<String, Set<String>> bitUsage = new HashMap<>();

    private static final BitOpsCalculator innerCalculator = new SimpleBitOpsCalculator();

    private NamedBit truth;

    public GraphExpressionFindEarlyConflictsB2B(BitOpsGraphCalculator calculator) {
        this.calculator = calculator;
    }

    private final Map<String, Boolean> cache = new HashMap<>();

    private Map<String, List<Bit>> bitValues = new HashMap<>();
    private Map<String, List<Bit>> bitPrevValues = new HashMap<>();

    public void analyse() {
        this.truth = calculator.getOutput().getBits().get(0);

        tapNext();
        long minConflictCount = findNones();
        while (true) {
            long conflictCount = findNones();
            System.out.println("minConflictCount: " + minConflictCount);
            System.out.println("conflictCount: " + conflictCount);
            tapNext();

            if (conflictCount <= minConflictCount) {
                minConflictCount = conflictCount;
            } else {
                tapBack();
            }
            if (minConflictCount == 0) {
                break;
            }
        }
    }

    private void tapNext() {
        for (String key : new HashSet<>(bitValues.keySet())) {
            if (key.startsWith("M")) {
                bitValues.remove(key);
            }
        }
        for (VarBit bit : calculator.getInput()) {
            bitPrevValues.put(bit.getName(), bitValues.get(bit.getName()));
            registerValue(bit, new ArrayList<>(Arrays.asList(randomBits(1))));
        }
    }

    private void tapBack() {
        for (VarBit varBit : calculator.getInput()) {
            registerValue(varBit, bitPrevValues.get(varBit.getName()));
        }
    }


    private long findNones() {
        traverse(truth);
        long count = 0;
        for (OperationalBit operationalBit : calculator.getMiddle()) {
            if (isFalsy(bitValues.get(operationalBit.getName()).toArray(new Bit[0]))) {
                count++;
            }
        }
        return count;
    }

    private Bit[] traverse(NamedBit bit) {
        if (bitValues.containsKey(bit.getName())) {
            return bitValues.get(bit.getName()).toArray(new Bit[0]);
        }
        if (bit instanceof OperationalBit) {
            OperationalBit operationalBit = (OperationalBit) bit;
            List<Bit[]> bits = Arrays.stream(((OperationalBit) bit).getBits())
                    .map(this::traverse)
                    .collect(Collectors.toList());

            Bit[] res = bits.get(0);
            if (operationalBit.getOperation() == Operation.AND) {
                for (int i = 1; i < bits.size(); i++) {
                    res = and(innerCalculator, res, bits.get(i));
                }
            } else if (operationalBit.getOperation() == Operation.OR) {
                for (int i = 1; i < bits.size(); i++) {
                    res = or(innerCalculator, res, bits.get(i));
                }
            } else if (operationalBit.getOperation() == Operation.XOR) {
                for (int i = 1; i < bits.size(); i++) {
                    res = xor(innerCalculator, res, bits.get(i));
                }
            } else if (operationalBit.getOperation() == Operation.NOT) {
                res = not(innerCalculator, bits.get(0));
            }
            registerValue(bit, new ArrayList<>(Arrays.asList(res)));
            return res;
        } else {
            throw new RuntimeException();
        }
    }

    private void registerValue(NamedBit bit, List<Bit> number) {
        if (bitValues.containsKey(bit.getName())) {
            bitValues.get(bit.getName()).addAll(number);
        } else {
            bitValues.put(bit.getName(), number);
        }
    }

    public void show() {

    }
}
