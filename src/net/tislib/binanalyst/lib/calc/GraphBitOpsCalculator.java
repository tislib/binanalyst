package net.tislib.binanalyst.lib.calc;

import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.CompositeBit;
import net.tislib.binanalyst.lib.bit.OperationalBit;

import static net.tislib.binanalyst.lib.bit.ConstantBit.ONE;
import static net.tislib.binanalyst.lib.bit.ConstantBit.ZERO;

/**
 * Created by Taleh Ibrahimli on 2/5/18.
 * Email: me@talehibrahimli.com
 */
public class GraphBitOpsCalculator implements BitOpsCalculator {
    @Override
    public Bit xor(Bit... bits) {
        return OperationalBit.xor(bits);
    }

    @Override
    public Bit and(Bit... bits) {
        return OperationalBit.and(bits);
    }

    @Override
    public Bit or(Bit... bits) {
        return OperationalBit.or(bits);
    }

    @Override
    public Bit not(Bit bit) {
        return OperationalBit.not(bit);
    }

    @Override
    public Bit equal(Bit... bits) {
        return OperationalBit.equal(bits);
    }

    @Override
    public Bit wrap(Number num) {
        return num.longValue() == 0 ? ZERO : ONE;
    }
}
