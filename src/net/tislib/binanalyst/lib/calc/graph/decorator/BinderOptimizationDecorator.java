package net.tislib.binanalyst.lib.calc.graph.decorator;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.OperationalBit;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.Operation;

public class BinderOptimizationDecorator extends AbstractBitOpsGraphCalculatorDecorator {
    private boolean ignoreHalfMiddle = true;

    public BinderOptimizationDecorator(BitOpsGraphCalculator calculator) {
        super(calculator);
    }

    @Override
    public Bit xor(Bit... bits) {
        List<Bit> newBits = new ArrayList<>();
        for (Bit bit : bits) {
            newBits.addAll(explode(bit, Operation.XOR, 1));
        }
        return super.xor(newBits.toArray(new Bit[0]));
    }

    @Override
    public Bit and(Bit... bits) {
        List<Bit> newBits = new ArrayList<>();
        for (Bit bit : bits) {
            newBits.addAll(explode(bit, Operation.AND, 1));
        }
        return super.and(newBits.toArray(new Bit[0]));
    }

    @Override
    public Bit or(Bit... bits) {
        List<Bit> newBits = new ArrayList<>();
        for (Bit bit : bits) {
            newBits.addAll(explode(bit, Operation.OR, 1));
        }
        return super.or(newBits.toArray(new Bit[0]));
    }

    public List<Bit> explode(Bit bit, Operation operation, int depth) {
        List<Bit> newBits = new ArrayList<>();
        if (bit instanceof OperationalBit
                && ((OperationalBit) bit).getOperation() == operation
                && (!ignoreHalfMiddle || !((OperationalBit) bit).testBits(a -> !(a instanceof OperationalBit)))
        ) {
            if (depth > 5) {
                newBits.addAll(Arrays.asList(((OperationalBit) bit).getBits()));
                return newBits;
            }
            for (Bit bit2 : ((OperationalBit) bit).getBits()) {
                newBits.addAll(explode(bit2, operation, depth + 1));
            }
        } else {
            newBits.add(bit);
        }
        return newBits;
    }
}
