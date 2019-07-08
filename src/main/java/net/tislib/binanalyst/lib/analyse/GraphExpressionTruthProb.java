package net.tislib.binanalyst.lib.analyse;

import static net.tislib.binanalyst.lib.BinOps.*;

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
import net.tislib.binanalyst.lib.calc.BitOpsCalculator;
import net.tislib.binanalyst.lib.calc.SimpleBitOpsCalculator;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.Operation;

public class GraphExpressionTruthProb {
    private final BitOpsGraphCalculator calculator;

    private NamedBit truth;

    private Map<String, Bit[]> bitValues = new HashMap<>();

    private Set<Bit[]> uniqueValues = new HashSet<>();

    private static final BitOpsCalculator innerCalculator = new SimpleBitOpsCalculator();

    public GraphExpressionTruthProb(BitOpsGraphCalculator calculator) {
        this.calculator = calculator;
    }

    public void analyse() {
        this.truth = calculator.getOutput().getBits().get(0);

        traverse(truth);
    }

    private Bit[] traverse(NamedBit bit) {
        if (bitValues.containsKey(bit.getName())) {
            return bitValues.get(bit.getName());
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
            registerValue(bit, res);
            return res;
        } else {
            Bit[] number = randomNumber();
            registerValue(bit, number);
            return number;
        }
    }

    private void registerValue(NamedBit bit, Bit[] number) {
        if (uniqueValues.contains(number)) {
            System.out.println("registering duplicate" + number + bit.getName());
        }
        uniqueValues.add(number);
        bitValues.put(bit.getName(), number);
    }

    private Bit[] randomNumber() {
        return randomBits(35000);
    }


    public void show() {
        System.out.println(
                "Has solution: " + !isFalsy(bitValues.get(truth.getName()))
        );
    }
}
