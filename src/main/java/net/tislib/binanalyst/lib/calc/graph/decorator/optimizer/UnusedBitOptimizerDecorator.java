package net.tislib.binanalyst.lib.calc.graph.decorator.optimizer;

import net.tislib.binanalyst.lib.bit.NamedBit;
import net.tislib.binanalyst.lib.bit.OperationalBit;
import net.tislib.binanalyst.lib.bit.VarBit;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.decorator.AbstractBitOpsGraphCalculatorDecorator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UnusedBitOptimizerDecorator extends AbstractBitOpsGraphCalculatorDecorator {
    public UnusedBitOptimizerDecorator(BitOpsGraphCalculator calculator) {
        super(calculator);
    }

    @Override
    public void calculate() {
        optimize();
        super.calculate();
    }

    @Override
    public void optimize() {
        Set<String> markedBit = new HashSet<>();
        cleanUnusedMiddleBits(markedBit);

        super.optimize();

//        this.show();
    }

    public void cleanUnusedMiddleBits(Set<String> markedBits) {
        for (NamedBit bit : this.getOutput()) {
            this.markBit(markedBits, bit);
        }

        List<OperationalBit> bitsRemoval = this.getMiddle().getBits().stream().filter(item -> !markedBits.contains(item.getName())).collect(Collectors.toList());

        this.remove(bitsRemoval);

        List<VarBit> newInputBits = this.getInput().getBits().stream().filter(item -> markedBits.contains(item.getName())).collect(Collectors.toList());
        this.getInput().setBits(newInputBits);
    }

    private void markBit(Set<String> markedBits, NamedBit bit) {
//        if (!this.getMiddle().contains(bit) && !this.getOutput().contains(bit) && !this.getInput().contains(bit)) {
//            return;
//        }
        if (markedBits.contains(bit.getName())) {
            // it is already marked recursively
            return;
        }
        markedBits.add(bit.getName());

        if (bit instanceof OperationalBit) {
            OperationalBit operationalBit = (OperationalBit) bit;
            for (NamedBit innerBit : operationalBit.getBits()) {
                markBit(markedBits, innerBit);
            }
        }
    }
}
