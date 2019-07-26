package net.tislib.binanalyst.lib.calc.reverse;

import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.expression.BooleanExpression;

public interface SingleBitReverser {
    BooleanExpression reverse(BitOpsGraphCalculator calculator, String bitName, boolean truth);
}
