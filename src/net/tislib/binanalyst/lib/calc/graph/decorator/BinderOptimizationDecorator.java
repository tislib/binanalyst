package net.tislib.binanalyst.lib.calc.graph.decorator;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.OperationalBit;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.Operation;

public class BinderOptimizationDecorator extends AbstractBitOpsGraphCalculatorDecorator {
    public BinderOptimizationDecorator(BitOpsGraphCalculator calculator) {
        super(calculator);
    }

    @Override
    public Bit xor(Bit... bits) {
        List<Bit> newBits = new ArrayList<>();
        for (Bit bit : bits) {
            newBits.addAll(explode(bit, Operation.XOR));
        }
        return super.xor(newBits.toArray(new Bit[0]));
    }

    @Override
    public Bit and(Bit... bits) {
        List<Bit> newBits = new ArrayList<>();
        for (Bit bit : bits) {
            newBits.addAll(explode(bit, Operation.AND));
        }
        return super.and(newBits.toArray(new Bit[0]));
    }

    @Override
    public Bit or(Bit... bits) {
        List<Bit> newBits = new ArrayList<>();
        for (Bit bit : bits) {
            newBits.addAll(explode(bit, Operation.OR));
        }
        return super.or(newBits.toArray(new Bit[0]));
    }

    public List<Bit> explode(Bit bit, Operation operation) {
        List<Bit> newBits = new ArrayList<>();
        if (bit instanceof OperationalBit && ((OperationalBit) bit).getOperation() == operation) {
            for(Bit bit2: ((OperationalBit) bit).getBits()) {
                newBits.addAll(explode(bit2, operation));
            }
        } else {
            newBits.add(bit);
        }
        return newBits;
    }
}
