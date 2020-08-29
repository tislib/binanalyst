package net.tislib.binanalyst.lib.calc.graph.tools;

import net.tislib.binanalyst.lib.BinValueHelper;
import net.tislib.binanalyst.lib.analyse.lk.NormalFormStringGenerator;
import net.tislib.binanalyst.lib.bit.*;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.GraphBitOpsCalculator;
import net.tislib.binanalyst.lib.calc.graph.Operation;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.tislib.binanalyst.lib.BinValueHelper.setVal;
import static net.tislib.binanalyst.lib.bit.ConstantBit.ONE;
import static net.tislib.binanalyst.lib.bit.ConstantBit.ZERO;
import static net.tislib.binanalyst.lib.util.MapUtil.computeIfAbsent;

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
            return computeIfAbsent(cache, bit, u -> {
                int res = getMaxDepth(cache, bits);
                return 1 + res;
            });
        } else {
            return 1;
        }
    }

    public static long getMaxOperationCount(BitOpsGraphCalculator calculator) {
        return getMaxOperationCount(new HashMap<>(), calculator.getOutput().getBits());
    }

    public static long getMaxOperationCount(Map<Bit, Long> cache, Collection<? extends Bit> bits) {
        long maxContent = 0;

        for (Bit bit : bits) {
            long operationCount = getMaxOperationCount(cache, bit);
            maxContent += operationCount;
        }

        return maxContent;
    }

    private static long getMaxOperationCount(Map<Bit, Long> cache, Bit bit) {
        if (bit instanceof OperationalBit) {
            Collection<? extends Bit> bits = Arrays.asList(((OperationalBit) bit).getBits());
            return computeIfAbsent(cache, bit, u -> {
                long res = getMaxOperationCount(cache, bits);
                return bits.size() + res;
            });
        } else {
            return 0;
        }
    }

    public static GraphCalculatorSerializedData serializeCalculator(BitOpsGraphCalculator calculator, boolean withValues) {
        GraphCalculatorSerializedData data = new GraphCalculatorSerializedData();

        data.input = calculator.getInput().getBits().stream().map(item -> serializeBit(item, withValues)).collect(Collectors.toList());
        data.middle = calculator.getMiddle().getBits().stream().map(item -> serializeBit(item, withValues)).collect(Collectors.toList());
        data.output = calculator.getOutput().getBits().stream().map(item -> serializeBit(item, withValues)).collect(Collectors.toList());

        return data;
    }

    @SuppressWarnings("unchecked")
    public static <T extends NamedBit> T deserializeBit(GraphBitOpsCalculator calculator, BitData bitData) {
        switch (bitData.type) {
            case "var":
                return (T) new VarBit(bitData.name);
            case "operational":
                Operation operation = bitData.operation;
                NamedBit[] bits = (bitData.bits).stream().map(item -> locateBit(calculator, item)).toArray(NamedBit[]::new);
                return (T) calculator.operation(operation, bits);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T extends NamedBit> T locateBit(GraphBitOpsCalculator calculator, String name) {
        if (name.equals("0") || name.equals("ZERO")) {
            return (T) ConstantBit.ZERO;
        } else if (name.equals("1") || name.equals("ONE")) {
            return (T) ConstantBit.ONE;
        }
        OperationalBit middleBit = calculator.getMiddle().locate(name);
        if (middleBit != null) {
            return (T) middleBit;
        }
        VarBit inputBit = calculator.getInput().locate(name);
        return (T) inputBit;
    }

    private static BitData serializeBit(NamedBit item, boolean withValues) {
        BitData bitData = new BitData();
        bitData.type = item.getType();
        bitData.name = item.getName();
        if (withValues) {
            bitData.value = item.getValue();
        }
        if (item instanceof OperationalBit) {
            bitData.bits = Arrays.stream(((OperationalBit) item).getBits()).map(NamedBit::getName).collect(Collectors.toList());
            bitData.operation = ((OperationalBit) item).getOperation();
        }
        return bitData;
    }

    @SuppressWarnings("unchecked")
    public static BitOpsGraphCalculator deSerializeCalculator(GraphCalculatorSerializedData data) {
        GraphBitOpsCalculator calculator = new GraphBitOpsCalculator();
        calculator.setInputBits(data.input.stream().map(bitData -> deserializeBit(calculator, bitData)).toArray(VarBit[]::new));
        calculator.getMiddle().addBits(data.middle.stream().map(bitData -> deserializeBit(calculator, bitData)).toArray(OperationalBit[]::new));
        calculator.setOutputBits(data.output.stream().map(bitData -> locateBit(calculator, bitData.name)).toArray(NamedBit[]::new));
        return calculator;
    }

    public static Bit[] getTruthBit(BitOpsGraphCalculator calculator, Bit[] bits, long value) {
        VarBit[] xBits = VarBit.list("x", bits.length, ZERO);

        setVal(xBits, value);

        Bit[] truth = new Bit[bits.length];
        for (int i = 0; i < bits.length; i++) {
            if (xBits[i].getValue().isTrue()) {
                truth[i] = bits[i];
            } else {
                truth[i] = calculator.not(bits[i]);
            }
        }
        return truth;
    }

    public static Set<NamedBit> findDependencyBits(NamedBit operationalBit) {
        if (operationalBit instanceof OperationalBit && ((OperationalBit) operationalBit).getOperation() != Operation.NOT) {
            return Arrays.stream(((OperationalBit) operationalBit).getBits()).flatMap(item -> findDependencyBits(item).stream()).collect(Collectors.toSet());
        } else {
            return Collections.singleton(operationalBit);
        }
    }

    public static OperationalBit normalFormSeparatePerBit(BitOpsGraphCalculator calculator, OperationalBit operationalBit, VarBit locatedBit) {
        return (OperationalBit) calculator.or(
                calculator.and(locatedBit, normalFormSeparatePerBitReplace(calculator, operationalBit, locatedBit, (ConstantBit) ONE)),
                calculator.and(calculator.not(locatedBit), normalFormSeparatePerBitReplace(calculator, operationalBit, locatedBit, (ConstantBit) ZERO))
        );
    }

    private static NamedBit normalFormSeparatePerBitReplace(BitOpsGraphCalculator calculator, OperationalBit theBit, VarBit locatedBit, ConstantBit replacement) {
        List<NamedBit> fixedBits = new ArrayList<>();
        for (NamedBit namedBit : theBit.getBits()) {
            if (namedBit == locatedBit) {
                fixedBits.add(replacement);
            } else if (namedBit instanceof OperationalBit) {
                fixedBits.add(normalFormSeparatePerBitReplace(calculator, (OperationalBit) namedBit, locatedBit, replacement));
            } else {
                fixedBits.add(namedBit);
            }
        }

        return (NamedBit) calculator.operation(theBit.getOperation(), fixedBits.toArray(new NamedBit[0]));
    }

    public static boolean hasDependencyBit(NamedBit operationalBit, String cBit) {
        if (operationalBit instanceof OperationalBit && ((OperationalBit) operationalBit).getOperation() != Operation.NOT) {
            return Arrays.stream(((OperationalBit) operationalBit).getBits()).anyMatch(item -> hasDependencyBit(item, cBit));
        } else {
            return operationalBit.getName().equals(cBit);
        }
    }

    public static Set<NamedBit> findLeafs(OperationalBit varBit) {
        Set<NamedBit> result = new HashSet<>();
        for (NamedBit bit : varBit.getBits()) {
            if (bit instanceof OperationalBit) {
                result.addAll(findLeafs((OperationalBit) bit));
            } else {
                result.add(bit);
            }
        }
        return result;
    }

    public static Set<String> findLeafNames(OperationalBit varBit) {
        Set<String> result = new HashSet<>();
        for (NamedBit bit : varBit.getBits()) {
            if (bit instanceof OperationalBit) {
                result.addAll(findLeafNames((OperationalBit) bit));
            } else {
                result.add(bit.getName());
            }
        }
        return result;
    }

    public static NamedBit copy(BitOpsGraphCalculator calculator, NamedBit namedBit) {
        OperationalBit operationalBit = new OperationalBit(Operation.COPY, new NamedBit[]{namedBit});
        calculator.getMiddle().addBits(operationalBit);
        return operationalBit;
    }

    public static void showStates(BitOpsGraphCalculator calculator) {
        System.out.println("MIDDLE SIZE: " + calculator.getMiddle().getBits().size());
        System.out.println("DEPTH: " + GraphCalculatorTools.getMaxDepth(calculator));
    }

    public static Set<Set<String>> toNormalForm(BitOpsGraphCalculator calculator) {
        return toNormalForm(calculator.getOutput());
    }

    public static Set<Set<String>> toNormalForm(Iterable<NamedBit> it) {
        Set<Set<String>> res = new HashSet<>();
        for (NamedBit namedBit : it) {
            res.addAll(toNormalForm(namedBit));
        }
        return res;
    }

    public static Set<Set<String>> toNormalForm(NamedBit namedBit) {
        NormalFormStringGenerator normalFormStringGenerator = new NormalFormStringGenerator();
        return normalFormStringGenerator.resolve(namedBit);
    }

    public static long calculateComplexity(OperationalBit bit) {
        return calculateComplexity(bit, new HashMap<>());
    }

    public static long calculateComplexity(OperationalBit bit, Map<String, Long> cache) {
        if (cache.containsKey(bit.getName())) {
            return cache.get(bit.getName());
        }
        Stream<Long> complexityCalculator = Arrays.stream(bit.getBits()).filter(item -> item instanceof OperationalBit)
                .map(item -> GraphCalculatorTools.calculateComplexity((OperationalBit) item, cache));

        long complexity = 0L;
        switch (bit.getOperation()) {
            case OR:
            case NOT:
            case XOR:
                complexity = complexityCalculator.reduce(1L, Long::sum);
                break;
            case AND:
                complexity = complexityCalculator.reduce(1L, (a, b) -> a * b);
        }

        cache.put(bit.getName(), complexity);

        return complexity;
    }

    public static void showComplexity(BitOpsGraphCalculator calculator) {
        System.out.println("##############################################");

        System.out.println("INPUT SIZE: " + calculator.getInput().getBits().size());
        System.out.println("MIDDLE SIZE: " + calculator.getMiddle().getBits().size());
        System.out.println("OUTPUT SIZE: " + calculator.getMiddle().getBits().size());
        if (calculator.getOutput().getBitL(0) instanceof OperationalBit) {
            OperationalBit operationalBit = (OperationalBit) calculator.getOutput().getBitL(0);
            System.out.println("OUTPUT Length: " + operationalBit.getBits().length);
            int longestSubOp = 0;
            long avgSubOp = 0;
            for (NamedBit namedBit : operationalBit.getBits()) {
                if (namedBit instanceof OperationalBit) {
                    if (((OperationalBit) namedBit).getBits().length > longestSubOp) {
                        longestSubOp = ((OperationalBit) namedBit).getBits().length;
                    }
                    avgSubOp += ((OperationalBit) namedBit).getBits().length;
                }
            }
            System.out.println("Longest cnf: " + longestSubOp);
            System.out.println("Avg cnf: " + avgSubOp / operationalBit.getBits().length);
        }
//        System.out.println("DEPTH: " + GraphCalculatorTools.getMaxDepth(calculator));
        System.out.println("##############################################");
    }

    public static void showFormula(NamedBit mBit) {
        if (mBit instanceof OperationalBit) {
            System.out.println(((OperationalBit) mBit).showFull());
        } else {
            System.out.println(mBit);
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

    public static List<OperationalBit> findUsages(BitOpsGraphCalculator calculator, NamedBit varBit) {
        List<OperationalBit> usage = new ArrayList<>();
        for (OperationalBit operationalBit : calculator.getMiddle()) {
            if (operationalBit.hasBit(varBit)) {
                usage.add(operationalBit);
            }
        }

        return usage;
    }

    public static class GraphCalculatorSerializedData {
        public List<BitData> input;
        public List<BitData> middle;
        public List<BitData> output;
    }

    public static class BitData {
        public String type;
        public String name;
        public BinaryValue value;
        public Operation operation;
        public List<String> bits;
    }
}
