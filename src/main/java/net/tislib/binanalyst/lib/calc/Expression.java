package net.tislib.binanalyst.lib.calc;

import net.tislib.binanalyst.lib.bit.Bit;

public interface Expression {

    Bit[] prepare(Bit[] input, BitOpsCalculator calculator);

}
