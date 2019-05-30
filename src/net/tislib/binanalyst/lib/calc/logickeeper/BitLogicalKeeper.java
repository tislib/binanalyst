package net.tislib.binanalyst.lib.calc.logickeeper;

import java.math.BigInteger;
import java.util.List;
import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.VarBit;

public interface BitLogicalKeeper {
    void setInput(VarBit[]... bits);

    void restrict(Bit bit);

    void calculate();

    BigInteger getVariationCount();

    List<Bit[]> getResult();

    void setResultingInput(VarBit[] aBits);

    long getOperationCount();
}
