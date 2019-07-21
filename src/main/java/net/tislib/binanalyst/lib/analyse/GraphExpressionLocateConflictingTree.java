package net.tislib.binanalyst.lib.analyse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.tislib.binanalyst.lib.bit.NamedBit;
import net.tislib.binanalyst.lib.bit.OperationalBit;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.Operation;

public class GraphExpressionLocateConflictingTree {
    private final BitOpsGraphCalculator calculator;
    private Map<String, List<String>> bitConflicts = new HashMap<>();
    private Map<String, List<String>> bitDepConflicts = new HashMap<>();
    private Map<String, Set<String>> dependencies = new HashMap<>();
    private NamedBit truth;

    private Set<String> commitedPositives = new HashSet<>();

    public GraphExpressionLocateConflictingTree(BitOpsGraphCalculator calculator) {
        this.calculator = calculator;
    }

    public void analyse() {
        this.truth = calculator.getOutput().getBits().get(0);

//        commitedPositives.add("b0");
//        commitedPositives.add("b0");

        Set<String> resX = traverse(truth);
        System.out.println(resX);
        System.out.println(truth.getName());
    }

    private Set<String> traverse(NamedBit namedBit) {
        if (dependencies.containsKey(namedBit.getName())) {
            return dependencies.get(namedBit.getName());
        }
        Set<String> res = new HashSet<>();
        Set<String> depConflicts = new HashSet<>();
        if (namedBit instanceof OperationalBit) {
            OperationalBit operationalBit = (OperationalBit) namedBit;
            if (operationalBit.getOperation() == Operation.NOT) {
                res.add(operationalBit.showFull(false));
            } else {
                for (NamedBit innerBit : operationalBit.getBits()) {
                    Set<String> innerRes = traverse(innerBit);

                    res.addAll(innerRes);

                    depConflicts.addAll(AnalyserUtil.findConflicts(innerRes));
                }

                for (String commitedPositive : commitedPositives) {
                    if (res.contains(commitedPositive)) {
                        res.remove(commitedPositive);
                    } else if (res.contains(AnalyserUtil.negativate(commitedPositive))) {
                        res = Collections.emptySet();
                    }
                }
            }

            List<String> conflicts = new ArrayList<>(AnalyserUtil.findConflicts(res));
            conflicts.removeAll(depConflicts);
            if (conflicts.size() > 0 || truth == namedBit) {
                Collections.sort(conflicts);
                bitConflicts.put(namedBit.getName(), conflicts);
                bitDepConflicts.put(namedBit.getName(), new ArrayList<>(depConflicts));
            }

        } else {
            res.add(namedBit.getName());
        }

        dependencies.put(namedBit.getName(), res);

        return res;
    }

    public void show() {
//        System.out.println("Dependencies");
//        for (String key : dependencies.keySet()) {
//            System.out.println(key + ": " + dependencies.get(key));
//        }

        System.out.println("Conflicts");
        for (String key : bitConflicts.keySet()) {
            System.out.println(key + ": " + bitConflicts.get(key));
            System.out.println(key + "$: " + bitDepConflicts.get(key));
        }
        System.out.println(bitConflicts.size());
    }
}
