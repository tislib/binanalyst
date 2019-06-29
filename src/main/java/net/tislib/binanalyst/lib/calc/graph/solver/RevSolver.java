package net.tislib.binanalyst.lib.calc.graph.solver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import net.tislib.binanalyst.lib.BinValueHelper;
import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.NamedBit;
import net.tislib.binanalyst.lib.bit.OperationalBit;
import net.tislib.binanalyst.lib.bit.VarBit;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.GraphBitOpsCalculator;
import net.tislib.binanalyst.lib.calc.graph.Operation;

public class RevSolver {
    private final BitOpsGraphCalculator calculator;
    private Bit[] symmetricInput;
    private GraphBitOpsCalculator internalCalculator;
    private Bit bit;
    private Bit truthBit;
    private Bit falsyBit;

    public RevSolver(BitOpsGraphCalculator calculator) {
        this.calculator = calculator;
    }

    public void setSymmetricInput(Bit... bits) {
        this.symmetricInput = bits;
        this.bit = bits[0];
        this.internalCalculator = new GraphBitOpsCalculator();
        this.internalCalculator.setInputBits(new VarBit[0]);
//        this.internalCalculator.getInput().setBits(
//                calculator.getOutput().getBits().stream().map(item -> (VarBit) item).collect(Collectors.toList())
//        ); // compile failure
        this.truthBit = generateTruth(bit);
//        this.falsyBit = generateFalsy(bit);

        BinValueHelper.printFormula((NamedBit) this.truthBit);
    }

    private List<Bit> findUsage(Bit bit) {
        List<Bit> bits = new ArrayList<>();
        for (OperationalBit operationalBit : this.calculator.getMiddle()) {
            if (operationalBit.hasBit((NamedBit) bit)) {
                bits.add(operationalBit);
            }
        }
        return bits;
    }


    private Bit generateFalsy(Bit bit) {
        List<Bit> usageBits = findUsage(bit);
        NamedBit[] bitsX = new NamedBit[usageBits.size()];
        for (int i = 0; i < usageBits.size(); i++) {
            Bit bit2 = usageBits.get(i);
            bitsX[i] = locateFalsy(bit2, bit);

        }
        return internalCalculator.and(bitsX);
    }

    private Bit generateTruth(Bit bit) {
        List<Bit> usageBits = findUsage(bit);
        NamedBit[] bitsX = new NamedBit[usageBits.size()];
        for (int i = 0; i < usageBits.size(); i++) {
            Bit bit2 = usageBits.get(i);
            bitsX[i] = locateTruth(bit2, bit);

        }
        return internalCalculator.and(bitsX);
    }

    private NamedBit locateTruth(Bit bit2, Bit forBit) {
        if (bit2 instanceof OperationalBit) {
            OperationalBit operationalBit = (OperationalBit) bit2;
            if (operationalBit.getOperation() == Operation.AND) {
                Bit[] theBits2 = Arrays.stream(operationalBit.getBits())
                        .filter(item -> item != forBit)
                        .map(item -> (VarBit) item)
                        .collect(Collectors.toList())
                        .toArray(new Bit[0]);

                return (NamedBit) internalCalculator.or(
                        internalCalculator.not(generateTruth(bit2)),
                        internalCalculator.and(theBits2)
                );
            }

            if (operationalBit.getOperation() == Operation.XOR) {
                Bit[] theBits2 = Arrays.stream(operationalBit.getBits())
                        .map(item -> {
                            if (item == forBit) {
                                item = (NamedBit) bit2;
                            }
                            return (VarBit) item;
                        })
                        .collect(Collectors.toList())
                        .toArray(new Bit[0]);

                return (NamedBit) internalCalculator.xor(theBits2);
            }

            if (operationalBit.getOperation() == Operation.NOT) {
                return (NamedBit) generateFalsy(operationalBit.getBits()[0]);
            }
        }
        return null;
    }

    private NamedBit locateFalsy(Bit bit2, Bit forBit) {
        if (bit2 instanceof OperationalBit) {
            OperationalBit operationalBit = (OperationalBit) bit2;
            if (operationalBit.getOperation() == Operation.AND) {
                return (NamedBit) generateFalsy(operationalBit.getBits()[0]);
            }

            if (operationalBit.getOperation() == Operation.XOR) {
                Bit[] theBits2 = Arrays.stream(operationalBit.getBits())
                        .map(item -> {
                            if (item == forBit) {
                                item = (NamedBit) bit2;
                            }
                            return (VarBit) item;
                        })
                        .collect(Collectors.toList())
                        .toArray(new Bit[0]);

                return (NamedBit) internalCalculator.not(internalCalculator.xor(theBits2));
            }

            if (operationalBit.getOperation() == Operation.NOT) {
                return (NamedBit) generateTruth(operationalBit.getBits()[0]);
            }
        }
        return null;
    }

    public void showResultFor(Bit[] crBits) {

    }
}
