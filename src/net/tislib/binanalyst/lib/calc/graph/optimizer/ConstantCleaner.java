package net.tislib.binanalyst.lib.calc.graph.optimizer;

import net.tislib.binanalyst.lib.bit.NamedBit;
import net.tislib.binanalyst.lib.bit.OperationalBit;
import net.tislib.binanalyst.lib.bit.VarBit;
import net.tislib.binanalyst.lib.calc.graph.GraphBitOpsCalculator;
import net.tislib.binanalyst.lib.calc.graph.Operation;

/**
 * Created by Taleh Ibrahimli on 2/11/18.
 * Email: me@talehibrahimli.com
 */
public class ConstantCleaner {
    public static void clean(GraphBitOpsCalculator calculator, OperationalBit bit) {
        throw new UnsupportedOperationException();
//        NamedBit[] bits = bit.getBits();
//        switch (bit.getOperation()) {
//            case AND:
////                bit.init(bit.getOperation(), Optimizer.remove(bits, (VarBit) calculator.not(calculator.ZERO)));
//                break;
//            case OR:
//                bit.init(bit.getOperation(), Optimizer.remove(bits, calculator.ZERO));
//                break;
//            case XOR:
//                bit.init(bit.getOperation(), Optimizer.remove(bits, calculator.ZERO));
//                break;
//        }
    }
}
