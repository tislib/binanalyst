package net.tislib.binanalyst.lib.calc.graph.decorator.optimizer;

import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.Operation;
import net.tislib.binanalyst.lib.calc.graph.decorator.OptimizerGraphCalculatorDecorator;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Deprecated
public class DuplicationRemovalOptimizationDecorator extends OptimizerGraphCalculatorDecorator {
    public DuplicationRemovalOptimizationDecorator(BitOpsGraphCalculator calculator) {
        super(calculator);
    }

    @Override
    protected Bit optimize(Operation operation, Bit... bits) {
        if (operation != Operation.XOR) {
            bits = distinct(bits);
        }

//        Set<Bit> bitSet = new HashSet<>(Arrays.asList(bits));
//
//        if (bits.length != bitSet.size()) {
//            int modCount = bits.length - bitSet.size();
//            Bit[] newBits = bitSet.toArray(new Bit[0]);
//            if (operation != Operation.XOR || modCount % 2 == 0) {
//                return optimize(operation, newBits);
//            } else {
//                return not(optimize(operation, newBits));
//            }
//        }

        return delegate(operation, bits);
    }

    private Bit[] distinct(Bit[] bits) {
        return Arrays.stream(bits).distinct().toArray(Bit[]::new);
    }
}
