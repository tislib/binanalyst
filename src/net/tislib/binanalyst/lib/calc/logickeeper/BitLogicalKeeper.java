package net.tislib.binanalyst.lib.calc.logickeeper;

import java.math.BigInteger;
import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.VarBit;

public interface BitLogicalKeeper {
    void setInput(VarBit[] bits);

    void restrict(Bit bit);

    void calculate();

    BigInteger getVariationCount();

    Bit[] getResult();
}
