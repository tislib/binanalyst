package net.tislib.binanalyst.lib.calc.graph.decorator;

import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.OperationalBit;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.Operation;

public class NotMoveUpCalculatorDecorator extends AbstractBitOpsGraphCalculatorDecorator {

    private final BitOpsGraphCalculator originalCalculator;

    public NotMoveUpCalculatorDecorator(BitOpsGraphCalculator calculator) {
        super(calculator);
        originalCalculator = this.getOriginal();
    }

    @Override
    public Bit and(Bit... bits) {
        if (bits.length == 2 && bits[0] instanceof OperationalBit && bits[1] instanceof OperationalBit) {
            OperationalBit bitL = (OperationalBit) bits[0];
            OperationalBit bitR = (OperationalBit) bits[1];

            if (bitL.getOperation() == Operation.NOT && bitR.getOperation() == Operation.NOT) {
                return originalCalculator.not(
                        originalCalculator.or(
                                bitL.getBits()[0],
                                bitR.getBits()[0]
                        )
                );
            }
        }
        return super.and(bits);
    }

    @Override
    public Bit or(Bit... bits) {
        if (bits.length == 2 && bits[0] instanceof OperationalBit && bits[1] instanceof OperationalBit) {
            OperationalBit bitL = (OperationalBit) bits[0];
            OperationalBit bitR = (OperationalBit) bits[1];

            if (bitL.getOperation() == Operation.NOT && bitR.getOperation() == Operation.NOT) {
                return originalCalculator.not(
                        originalCalculator.and(
                                bitL.getBits()[0],
                                bitR.getBits()[0]
                        )
                );
            }
        }
        return super.or(bits);
    }
}
