package net.tislib.binanalyst.lib.calc.graph.tools;

import static net.tislib.binanalyst.lib.util.MapUtil.computeIfAbsent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import net.tislib.binanalyst.lib.BinValueHelper;
import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.ConstantBit;
import net.tislib.binanalyst.lib.bit.NamedBit;
import net.tislib.binanalyst.lib.bit.OperationalBit;
import net.tislib.binanalyst.lib.bit.VarBit;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.GraphBitOpsCalculator;
import net.tislib.binanalyst.lib.calc.graph.Operation;

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

    public static GraphCalculatorSerializedData serializeCalculator(BitOpsGraphCalculator calculator) {
        GraphCalculatorSerializedData data = new GraphCalculatorSerializedData();

        data.input = calculator.getInput().getBits().stream().map(GraphCalculatorTools::serializeBit).collect(Collectors.toList());
        data.middle = calculator.getMiddle().getBits().stream().map(GraphCalculatorTools::serializeBit).collect(Collectors.toList());
        data.output = calculator.getOutput().getBits().stream().map(GraphCalculatorTools::serializeBit).collect(Collectors.toList());

        return data;
    }

    @SuppressWarnings("unchecked")
    public static <T extends NamedBit> T deserializeBit(GraphBitOpsCalculator calculator, BitData bitData) {
        if (VarBit.class.getTypeName().equals(bitData.type)) {
            return (T) new VarBit(bitData.name);
        } else if (OperationalBit.class.getTypeName().equals(bitData.type)) {
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

    private static BitData serializeBit(NamedBit item) {
        BitData bitData = new BitData();
        bitData.type = item.getClass().getTypeName();
        bitData.name = item.getName();
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
        calculator.setOutputBits(data.output.stream().map(bitData -> locateBit(calculator, (String) bitData.name)).toArray(NamedBit[]::new));
        return calculator;
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

    public static class GraphCalculatorSerializedData {
        public List<BitData> input;
        public List<BitData> middle;
        public List<BitData> output;
    }

    public static class BitData {
        public String type;
        public String name;
        public Operation operation;
        public List<String> bits;
    }
}
