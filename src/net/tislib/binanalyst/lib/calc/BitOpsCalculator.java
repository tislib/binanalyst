package net.tislib.binanalyst.lib.calc;

import net.tislib.binanalyst.lib.bit.Bit;

/**
 * Created by Taleh Ibrahimli on 2/5/18.
 * Email: me@talehibrahimli.com
 */
public interface BitOpsCalculator {
    Bit xor(Bit[] bits);

    Bit and(Bit[] bits);

    Bit or(Bit[] bits);

    Bit not(Bit bit);

    Bit equal(Bit[] bits);

    Bit wrap(Number num);

    static BitOpsCalculator getDefault() {
        return new SimpleBitOpsCalculator();
    }
}
