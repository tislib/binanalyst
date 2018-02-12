package net.tislib.binanalyst.lib.calc.graph.optimizer;

import net.tislib.binanalyst.lib.bit.NamedBit;
import net.tislib.binanalyst.lib.calc.graph.GraphBitOpsCalculator;
import net.tislib.binanalyst.lib.calc.graph.Operation;

/**
 * Created by Taleh Ibrahimli on 2/9/18.
 * Email: me@talehibrahimli.com
 */
public class SimpleOptimizer implements Optimizer {
    @Override
    public NamedBit optimize(GraphBitOpsCalculator graphBitOpsCalculator, Operation operation, NamedBit[] bits, NamedBit chain) {
        switch (operation) {
            case AND:
                if (Optimizer.contains(bits, graphBitOpsCalculator.ZERO)) return graphBitOpsCalculator.ZERO;
                break;
            case OR:
                bits = Optimizer.remove(bits, graphBitOpsCalculator.ZERO);
                break;
            case XOR:
                bits = Optimizer.remove(bits, graphBitOpsCalculator.ZERO);
                break;
        }
        if (bits.length == 0) {
            return graphBitOpsCalculator.ZERO;
        }
        if (operation != Operation.NOT && bits.length == 1) {
            return bits[0];
        }
        return chain;
    }
}
