package net.tislib.binanalyst.lib.calc.graph.tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import net.tislib.binanalyst.lib.BinValueHelper;
import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.NamedBit;
import net.tislib.binanalyst.lib.bit.OperationalBit;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;

public class GraphCalculatorTools {
    public static Bit[] findReferencedBits(BitOpsGraphCalculator calculator, int depth, NamedBit... bits) {
        GraphCalculatorReferenceFinder graphCalculatorReferenceFinder = new GraphCalculatorReferenceFinder(calculator);

        return graphCalculatorReferenceFinder.findReferences(depth, bits);
    }

    public static int getMaxDepth(BitOpsGraphCalculator calculator) {
        return getMaxDepth(new HashMap<>(), calculator.getOutput().getBits());
    }

    public static int getMaxDepth(Map<Bit, Integer> cache, Collection<? extends Bit> bits) {
        int maxDepth = 0;

        for (Bit bit : bits) {
            int depth = getMaxDepth(cache, bit);
            if (depth > maxDepth) {
                maxDepth = depth;
            }
        }

        return maxDepth;
    }

    private static int getMaxDepth(Map<Bit, Integer> cache, Bit bit) {
        if (bit instanceof OperationalBit) {
            Collection<? extends Bit> bits = Arrays.asList(((OperationalBit) bit).getBits());
            return cache.computeIfAbsent(bit, u -> {
                int res = getMaxDepth(cache, bits);
                return 1 + res;
            });
        } else {
            return 1;
        }
    }


    public static class GraphCalculatorReferenceFinder {

        private final BitOpsGraphCalculator calculator;

        public GraphCalculatorReferenceFinder(BitOpsGraphCalculator calculator) {
            this.calculator = calculator;
        }

        public Bit[] findReferences(int depth, NamedBit[] bits) {
            List<Bit> result = new ArrayList<>();
            for (Bit bit : calculator.getMiddle()) {
                if (bit instanceof OperationalBit) {
                    boolean found1 = false;
                    boolean found2 = false;

                    String formula = BinValueHelper.formulaToString(bit);

                    if (Arrays.stream(bits).allMatch(sBit -> checkReference(depth, bit, sBit))) {
                        found1 = true;
                    }
                    if (formula.contains("!a[2]") && Pattern.compile("(^a\\[2\\]|[^!]a\\[2\\])").asPredicate().test(formula)) {
                        found2 = true;
                    }
                    if (found1 != found2) {
                        System.out.println(checkReference(depth, bit, bits[0]));
                        System.out.println(checkReference(depth, bit, bits[1]));
                        System.out.println("error: " + formula);
                    }
                }
            }
            return result.toArray(new Bit[0]);
        }

        private boolean checkReference(int depth, Bit bit, NamedBit searchBit) {
            if (bit == searchBit) {
                return true;
            }
            if (bit instanceof OperationalBit) {
                OperationalBit operationalBit = (OperationalBit) bit;
                if (operationalBit.isTransitive()) {
                    return false;
                }
                if (operationalBit.hasBit(searchBit)) {
                    return true;
                }

                if (depth > 0) {
                    return Arrays.stream(operationalBit.getBits())
                            .filter(item -> !(item instanceof OperationalBit) || ((OperationalBit) item).isTransitive())
                            .anyMatch(nBit -> checkReference(depth, nBit, searchBit));

                }
            }
            return false;
        }
    }
}
