package net.tislib.binanalyst.lib.analyse;

import static net.tislib.binanalyst.lib.BinValueHelper.setVal;

import net.tislib.binanalyst.lib.BinValueHelper;
import net.tislib.binanalyst.lib.bit.BinaryValue;
import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.VarBit;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.expression.BooleanExpression;
import net.tislib.binanalyst.lib.calc.reverse.SingleBitReverser;

public class BriteBitReverser implements SingleBitReverser {
    @Override
    public BooleanExpression reverse(BitOpsGraphCalculator calculator, String bitName, boolean truth) {
        return value -> locateOutput(calculator, bitName, value, truth);
    }

    private boolean locateOutput(BitOpsGraphCalculator calculator, String bitName, BinaryValue[] value, boolean truth) {
        int iSize = calculator.getInput().size();
        for (long i = 0; i < 1 << iSize; i++) {
            setVal(calculator.getInput().getBits().toArray(new VarBit[0]), i);
            if (calculator.getInput().locate(bitName).getValue().isTrue() ^ truth) {
                continue;
            }
            calculator.calculate();
            if (BinValueHelper.isEqual(calculator.getOutput().getBits().toArray(new Bit[0]), value)) {
                return true;
            }
        }
        return false;
    }
}
