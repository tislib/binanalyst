package net.tislib.binanalyst.lib.calc.logickeeper;

import static net.tislib.binanalyst.lib.BinValueHelper.rasterize;
import static net.tislib.binanalyst.lib.BinValueHelper.setVal;
import static net.tislib.binanalyst.lib.bit.ConstantBit.ZERO;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.tislib.binanalyst.lib.BinValueHelper;
import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.VarBit;
import net.tislib.binanalyst.lib.calc.BitOpsCalculator;

public class SimpleBitLogicKeeper implements BitLogicalKeeper {
    private final BitOpsCalculator calculator;
    private List<Bit[]> result = new ArrayList<>();

    boolean calculateResult = true;
    boolean calculateVariationCount = true;
    private final List<Bit> restrictions = new ArrayList<>();
    private VarBit[] input;
    private long operationCount = 0;
    private VarBit[] resultingBits;

    public SimpleBitLogicKeeper(BitOpsCalculator calculator) {
        this.calculator = calculator;
    }

    @Override
    public void setResultingInput(VarBit[] aBits) {
        this.resultingBits = aBits;
    }

    @Override
    public void setInput(VarBit[]... bits) {
        List<VarBit> bits2 = new ArrayList<>();
        for (VarBit[] varBits : bits) {
            bits2.addAll(Arrays.asList(varBits));
        }
        this.input = bits2.toArray(new VarBit[]{});
    }

    @Override
    public void restrict(Bit bit) {
        this.restrictions.add(bit);
    }

    public boolean isTruth() {
        return this.restrictions.stream().allMatch(item -> item.getValue().isTrue());
    }

    @Override
    public void calculate() {
        this.result.clear();
        this.operationCount = 0;
        int l = input.length;
        long maxValue = 1 << l;
        for (long i = 0; i < maxValue; i++) {
            this.operationCount++;
            setVal(input, i);
            calculator.calculate();
            if (isTruth()) {
                if(resultingBits != null) {
                    this.result.add(rasterize(resultingBits));
                } else {
                    this.result.add(rasterize(input));
                }
            }
        }
    }

    @Override
    public BigInteger getVariationCount() {
        return BigInteger.valueOf(getResult().size());
    }

    @Override
    public List<Bit[]> getResult() {
        return result;
    }

    public void setCalculateResult(boolean calculateResult) {
        this.calculateResult = calculateResult;
    }

    public void setCalculateVariationCount(boolean calculateVariationCount) {
        this.calculateVariationCount = calculateVariationCount;
    }

    @Override
    public long getOperationCount() {
        return operationCount;
    }
}
