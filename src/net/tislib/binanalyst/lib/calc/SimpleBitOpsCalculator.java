package net.tislib.binanalyst.lib.calc;

import net.tislib.binanalyst.lib.bit.BinaryValue;
import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.CompositeBit;

import static net.tislib.binanalyst.lib.bit.ConstantBit.*;

/**
 * Created by Taleh Ibrahimli on 2/5/18.
 * Email: me@talehibrahimli.com
 */
public class SimpleBitOpsCalculator implements BitOpsCalculator {
    @Override
    public Bit xor(Bit... bits) {
        CompositeBit compositeBit = new CompositeBit();
        compositeBit.setValue(BinaryValue.FALSE);
        for (Bit bit : bits) {
            if (bit.getValue().isTrue()) compositeBit.setValue(compositeBit.getValue().reverse());
        }
        return compositeBit;
    }

    @Override
    public Bit and(Bit... bits) {
        for (Bit bit : bits) {
            if(bit == null){
                throw new RuntimeException();
            }
            if (bit.getValue().isFalse()) {
                return ZERO;
            }
        }
        return ONE;
    }

    @Override
    public Bit or(Bit... bits) {
        for (Bit bit : bits) {
            if (bit.getValue().isTrue()) {
                return ONE;
            }
        }
        return ZERO;
    }

    @Override
    public Bit not(Bit bit) {
        return new CompositeBit(bit.getValue().reverse());
    }


    @Override
    public Bit wrap(Number num) {
        return num.longValue() == 0 ? ZERO : ONE;
    }

    @Override
    public void calculate() {
        throw new UnsupportedOperationException();
    }
}
