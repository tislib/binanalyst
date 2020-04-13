package net.tislib.binanalyst.lib.calc.graph;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import net.tislib.binanalyst.lib.bit.NamedBit;
import net.tislib.binanalyst.lib.bit.OperationalBit;
import net.tislib.binanalyst.lib.bit.VarBit;

public class UsageFinder {
    private final Layer<VarBit> input;
    private final Layer<OperationalBit> middle;
    private final Layer<NamedBit> output;

    Set<String> markedBit = new HashSet<>();

    public UsageFinder(Layer<VarBit> input, Layer<OperationalBit> middle, Layer<NamedBit> output) {
        this.input = input;
        this.middle = middle;
        this.output = output;
    }

    public void cleanUnusedMiddleBits() {
        int cleanCount = 0;
        for (NamedBit bit : this.output) {
            this.markBit(bit);
        }
        cleanCount +=  this.middle.getBits().stream().filter(item -> !markedBit.contains(item.getName())).count();
        List<OperationalBit> newMiddleBits = this.middle.getBits().stream().filter(item -> markedBit.contains(item.getName())).collect(Collectors.toList());
        this.middle.setBits(newMiddleBits);

        cleanCount +=  this.input.getBits().stream().filter(item -> !markedBit.contains(item.getName())).count();
        List<VarBit> newInputBits = this.input.getBits().stream().filter(item -> markedBit.contains(item.getName())).collect(Collectors.toList());
        this.input.setBits(newInputBits);
//        System.out.println(cleanCount + " bits cleaned");
    }

    private void markBit(NamedBit bit) {
        if (!this.middle.contains(bit) && !this.output.contains(bit) && !this.input.contains(bit)) {
            return;
        }
        if (this.markedBit.contains(bit.getName())) {
            // it is already marked recursively
            return;
        }
        this.markedBit.add(bit.getName());

        if (bit instanceof OperationalBit) {
            OperationalBit operationalBit = (OperationalBit) bit;
            for (NamedBit innerBit : operationalBit.getBits()) {
                markBit(innerBit);
            }
        }
    }
}
