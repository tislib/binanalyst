package net.tislib.binanalyst.lib.calc.graph.expression;

import net.tislib.binanalyst.lib.bit.BinaryValue;

public interface BooleanExpression {

    boolean calculate(BinaryValue[] value);

}
