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
        for (NamedBit bit : this.output) {
            this.markBit(bit);
        }
        List<OperationalBit> newMiddleBits = this.middle.getBits().stream().filter(item -> markedBit.contains(item.getName())).collect(Collectors.toList());
        this.middle.setBits(newMiddleBits);
    }

    private void markBit(NamedBit bit) {
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
