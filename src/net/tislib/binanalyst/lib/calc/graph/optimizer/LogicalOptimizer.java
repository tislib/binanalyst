package net.tislib.binanalyst.lib.calc.graph.optimizer;

import net.tislib.binanalyst.lib.bit.NamedBit;
import net.tislib.binanalyst.lib.bit.OperationalBit;
import net.tislib.binanalyst.lib.calc.graph.GraphBitOpsCalculator;
import net.tislib.binanalyst.lib.calc.graph.Operation;

/**
 * Created by Taleh Ibrahimli on 2/9/18.
 * Email: me@talehibrahimli.com
 */
public class LogicalOptimizer implements Optimizer {

    @Override
    public NamedBit optimize(GraphBitOpsCalculator graphBitOpsCalculator, Operation operation, NamedBit[] bits, NamedBit chain) {

        if (bits.length == 2) {
            if (operation == Operation.AND) {
                if (bits[0] instanceof OperationalBit && ((OperationalBit) bits[0]).getOperation() == Operation.NOT
                        && ((OperationalBit) bits[0]).getBits()[0] == bits[1]) {
                    return graphBitOpsCalculator.ZERO;
                }

                if (bits[1] instanceof OperationalBit && ((OperationalBit) bits[1]).getOperation() == Operation.NOT
                        && ((OperationalBit) bits[1]).getBits()[0] == bits[0]) {
                    return graphBitOpsCalculator.ZERO;
                }
            }
            if (operation == Operation.OR) {
                if (bits[0] instanceof OperationalBit && ((OperationalBit) bits[0]).getOperation() == Operation.NOT
                        && ((OperationalBit) bits[0]).getBits()[0] == bits[1]) {
                    return graphBitOpsCalculator.ONE;
                }

                if (bits[1] instanceof OperationalBit && ((OperationalBit) bits[1]).getOperation() == Operation.NOT
                        && ((OperationalBit) bits[1]).getBits()[0] == bits[0]) {
                    return graphBitOpsCalculator.ONE;
                }
            }
        }

        return chain;
    }
}
