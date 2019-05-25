package net.tislib.binanalyst.lib.calc.graph;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.NamedBit;
import net.tislib.binanalyst.lib.bit.OperationalBit;

public class OperationalBitReverser {
    public static Bit reverseInternal(GraphBitOpsCalculator calculator, OperationalBit bit) {
        if (bit.getOperation() == Operation.NOT) {
            return bit.getBits()[0];
        } else if (bit.getOperation() == Operation.XOR) {
            return calculator.operation(Operation.NOT, (NamedBit) bit);
        } else if (bit.getOperation() == Operation.AND) {
            List<Bit> revBits = Arrays.stream(bit.getBits()).map(item -> reverse(calculator, item)).collect(Collectors.toList());
            return calculator.or(revBits.toArray(new Bit[]{}));
        } else if (bit.getOperation() == Operation.OR) {
            List<Bit> revBits = Arrays.stream(bit.getBits()).map(item -> reverse(calculator, item)).collect(Collectors.toList());
            return calculator.and(revBits.toArray(new Bit[]{}));
        } else {
            throw new UnsupportedOperationException("Unk cannot be reversed");
        }
    }

    public static Bit reverse(GraphBitOpsCalculator calculator, Bit bit) {
        if (calculator.getReverseBitMap().containsKey(bit)) {
            return calculator.getReverseBitMap().get(bit);
        }
        Bit revBit;
        if (bit instanceof OperationalBit) {
            revBit = reverseInternal(calculator, (OperationalBit) bit);
        } else {
            if(bit instanceof NamedBit) {
                  revBit = calculator.getNegativeBitMap().get(bit);
            } else {
                revBit = calculator.not(bit);
            }
        }
        calculator.getReverseBitMap().put(bit, revBit);
        return revBit;
    }
}
