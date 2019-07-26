package net.tislib.binanalyst.test.experiments.unknownvalue;

import static net.tislib.binanalyst.lib.BinValueHelper.setVal;
import static net.tislib.binanalyst.lib.BinValueHelper.toLong;
import static net.tislib.binanalyst.lib.bit.ConstantBit.ONE;
import static net.tislib.binanalyst.lib.bit.ConstantBit.ZERO;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.tislib.binanalyst.lib.bit.BinaryValue;
import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.NamedBit;
import net.tislib.binanalyst.lib.bit.OperationalBit;
import net.tislib.binanalyst.lib.bit.VarBit;
import net.tislib.binanalyst.lib.calc.graph.GraphBitOpsCalculator;
import net.tislib.binanalyst.lib.operator.BinMul;
import net.tislib.binanalyst.test.CalculatorHelper;

/**
 * Created by Taleh Ibrahimli on 2/9/18.
 * Email: me@talehibrahimli.com
 */
public class Test33_UnkBit_BruteDegreeChecker {

    static Map<Bit, Integer> currentDeepLengthValues = new HashMap<>();
    private static int checkCounter = 0;
    private static int checkCounter2 = 0;

    public static void main(String... args) {
        GraphBitOpsCalculator calculator = new GraphBitOpsCalculator();

        int a = 11;
        int b = 13;

//        int l = binLength(a);
        int l = 64;

        VarBit[] aBits = VarBit.list("a", l, ZERO);
        VarBit[] bBits = VarBit.list("b", l, ZERO);

//        calculator.getOptimizers().add(new SimpleOptimizer());
//        calculator.getOptimizers().add(new LogicalOptimizer());

        Bit[] expectedValues = CalculatorHelper.doMultiplication(a, b);

        Bit[] r = BinMul.multiply(calculator, aBits, bBits);

        calculator.setInputBits(aBits, bBits);
        calculator.setOutputBits(r);

        Set<String> connectedBitNames = new HashSet<>();
        Set<Bit> connectedBits = new HashSet<>();
        Map<Bit, Set<BinaryValue>> possibleValues = new HashMap<>();

        calculator.calculate();

        for (Bit bit : r) {
            if (bit instanceof OperationalBit) {
                OperationalBit operationalBit = (OperationalBit) bit;
                for (NamedBit namedBit : operationalBit.getBits()) {
                    connectedBitNames.add(namedBit.getName());
                    connectedBits.add(namedBit);
                }
                findPossibleValues(calculator, VarBit.wrap(operationalBit.getBits()), (OperationalBit) bit, possibleValues);

                for (NamedBit namedBit : operationalBit.getBits()) {
                    if (namedBit instanceof OperationalBit) {
                        OperationalBit operationalBit2 = (OperationalBit) namedBit;
                        findPossibleValues(calculator, VarBit.wrap(operationalBit2.getBits()), (OperationalBit) namedBit, possibleValues);
                    }
                }
            }
        }

        System.out.println("Done: " + checkCounter);
        System.out.println("Done: " + checkCounter2);

        for (Bit bit : r) {
            System.out.println(findDeepLength(bit));
        }
    }

    private static int findDeepLength(Bit bit) {
        if (currentDeepLengthValues.containsKey(bit)) {
            return currentDeepLengthValues.get(bit);
        }
        if (bit instanceof OperationalBit) {
            int max = 0;
            for (Bit bit1 : ((OperationalBit) bit).getBits()) {
                int value = findDeepLength(bit1);
                currentDeepLengthValues.put(bit1, value);
                if (value > max) {
                    max = value;
                }
            }
            return max + 1;
        } else {
            return 1;
        }
    }

    private static void findPossibleValues(GraphBitOpsCalculator calculator, VarBit[] bits, OperationalBit result, Map<Bit, Set<BinaryValue>> possibleValues) {
        BinaryValue correctValue = result.getValue();

        long maxValue = toLong(VarBit.list("a", bits.length, ONE)).longValue();

        for (long i = 0; i <= maxValue; i++) {
            setVal(bits, i);
            result.calculate();
            if (result.getValue() == correctValue) {
                boolean foundVariant = false;
                for (VarBit bit : bits) {
                    if (!possibleValues.containsKey(bit)) {
                        possibleValues.put(bit, new HashSet<>());
                        possibleValues.get(bit).add(bit.getValue());
                        foundVariant = true;
                    } else {
                        if (possibleValues.get(bit).contains(bit.getValue())) {
                            foundVariant = true;
                        } else {
                            foundVariant = false;
                            break;
                        }
                    }
                }
                if (foundVariant) {
                    checkCounter++;
                } else {
                    checkCounter2++;
                }
            } else {
                checkCounter2++;
            }
        }
    }


}
