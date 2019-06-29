package net.tislib.binanalyst.lib.algorithms.aes;

import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.calc.SimpleBitOpsCalculator;

public class AesAlgorithmImpl implements AesAlgorithm {
    private final SimpleBitOpsCalculator calculator;

    public AesAlgorithmImpl(SimpleBitOpsCalculator calculator) {
        this.calculator = calculator;
    }

    @Override
    public Bit[][] encrypt(byte[] data) {
        return new Bit[0][];
    }
}
