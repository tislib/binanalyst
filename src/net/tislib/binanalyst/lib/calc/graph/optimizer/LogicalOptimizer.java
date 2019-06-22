package net.tislib.binanalyst.lib.calc.graph.optimizer;

import static net.tislib.binanalyst.lib.bit.ConstantBit.ZERO;

import java.util.Arrays;
import java.util.stream.Collectors;
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
    public NamedBit optimizeOperation(GraphBitOpsCalculator graphBitOpsCalculator, Operation operation, NamedBit[] bits, NamedBit chain) {

//        if (bits.length == 2) {
//            if (operation == Operation.AND) {
//                if (bits[0] instanceof OperationalBit && ((OperationalBit) bits[0]).getOperation() == Operation.NOT
//                        && ((OperationalBit) bits[0]).getBits()[0] == bits[1]) {
//                    return (NamedBit) ZERO;
//                }
//
//                if (bits[1] instanceof OperationalBit && ((OperationalBit) bits[1]).getOperation() == Operation.NOT
//                        && ((OperationalBit) bits[1]).getBits()[0] == bits[0]) {
//                    return graphBitOpsCalculator.ZERO;
//                }
//            }
//            if (operation == Operation.OR) {
//                if (bits[0] instanceof OperationalBit && ((OperationalBit) bits[0]).getOperation() == Operation.NOT
//                        && ((OperationalBit) bits[0]).getBits()[0] == bits[1]) {
//                    return (NamedBit) graphBitOpsCalculator.not(graphBitOpsCalculator.ZERO);
//                }
//
//                if (bits[1] instanceof OperationalBit && ((OperationalBit) bits[1]).getOperation() == Operation.NOT
//                        && ((OperationalBit) bits[1]).getBits()[0] == bits[0]) {
//                    return (NamedBit) graphBitOpsCalculator.not(graphBitOpsCalculator.ZERO);
//                }
//            }
//        }
//
//        if (operation == Operation.XOR && chain instanceof OperationalBit) {
//            OperationalBit operationalBit = (OperationalBit) chain;
////            operationalBit.reinit(Arrays.stream(operationalBit.getBits())
////                    .filter(item -> item != graphBitOpsCalculator.ZERO)
////                    .collect(Collectors.toList())
////                    .toArray(new NamedBit[]{}));
//        }

        return chain;
    }

    @Override
    public void optimizeCalculator(GraphBitOpsCalculator calculator) {
        for (int i = 0; i < 3; i++) {
            calculator.getMiddle().setBits(
                    calculator.getMiddle()
                            .getBits()
                            .stream()
                            .filter(item -> {
                                return !calculator.isFictiveBit(item);
                            }).collect(Collectors.toList()));

            calculator.getMiddle().forEach(oBit -> {
                if (oBit.getOperation() != Operation.XOR) {
                    return;
                }
//                oBit.reinit(Arrays
//                        .stream(oBit.getBits())
//                        .filter(item -> item != calculator.ZERO)
//                        .collect(Collectors.toList())
//                        .toArray(new NamedBit[]{})
//                );
            });
        }
    }
}
