package net.tislib.binanalyst.lib.calc;

import net.tislib.binanalyst.lib.bit.Bit;

/**
 * Created by Taleh Ibrahimli on 2/5/18.
 * Email: me@talehibrahimli.com
 */
public interface BitOpsCalculator {
    static BitOpsCalculator getDefault() {
        return new SimpleBitOpsCalculator();
    }

    Bit xor(Bit... bits);

    Bit and(Bit... bits);

    Bit or(Bit... bits);

    Bit not(Bit bit);

    Bit wrap(Number num);

    void calculate();
}
