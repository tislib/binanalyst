package net.tislib.binanalyst.lib.calc.logickeeper;

import java.math.BigInteger;
import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.VarBit;
import net.tislib.binanalyst.lib.calc.BitOpsCalculator;

public class SimpleBitLogicKeeper implements BitLogicalKeeper {
    private Bit[] result = new Bit[0];
    private BigInteger variationCount;

    boolean calculateResult = true;
    boolean calculateVariationCount = true;

    public SimpleBitLogicKeeper(BitOpsCalculator graphBitOpsCalculator) {

    }

    @Override
    public void setInput(VarBit[] bits) {

    }

    @Override
    public void restrict(Bit bit) {

    }

    @Override
    public void calculate() {

    }

    @Override
    public BigInteger getVariationCount() {
        return variationCount;
    }

    @Override
    public Bit[] getResult() {
        return result;
    }

    public void setCalculateResult(boolean calculateResult) {
        this.calculateResult = calculateResult;
    }

    public void setCalculateVariationCount(boolean calculateVariationCount) {
        this.calculateVariationCount = calculateVariationCount;
    }
}
