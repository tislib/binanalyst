package net.tislib.binanalyst.lib.calc.graph.decorator;

import static net.tislib.binanalyst.lib.bit.ConstantBit.ONE;
import static net.tislib.binanalyst.lib.bit.ConstantBit.ZERO;

import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.ConstantBit;
import net.tislib.binanalyst.lib.bit.NamedBit;
import net.tislib.binanalyst.lib.bit.OperationalBit;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;

@Deprecated
public class ConstantOperationRemoverOptimizationDecorator extends AbstractBitOpsGraphCalculatorDecorator {
    public ConstantOperationRemoverOptimizationDecorator(BitOpsGraphCalculator calculator) {
        super(calculator);
    }

    @Override
    public Bit and(Bit... bits) {
        return optimize(super.and(bits), bits);
    }

    @Override
    public Bit or(Bit... bits) {
        return optimize(super.or(bits), bits);
    }

    @Override
    public Bit xor(Bit... bits) {
        return optimize(super.xor(bits), bits);
    }

    @Override
    public Bit not(Bit bit) {
        return optimize(super.not(bit), bit);
    }


    private Bit optimize(Bit result, Bit... bits) {
        for (Bit bit : bits) {
            if (!(bit instanceof ConstantBit)) {
                return result;
            }
        }


        if (result instanceof OperationalBit) {
            OperationalBit operationalBit = (OperationalBit) result;
            NamedBit[] namedBits = new NamedBit[bits.length];
            for (int i = 0; i < bits.length; i++) {
                namedBits[i] = (NamedBit) bits[i];
            }
            OperationalBit res = new OperationalBit(operationalBit.getOperation(), namedBits);
            res.calculate();
            result = res;
        }

        return result.getValue().isTrue() ? ONE : ZERO;
    }
}
