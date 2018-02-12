package net.tislib.binanalyst.lib.calc.graph.expression;

import net.tislib.binanalyst.lib.bit.*;
import net.tislib.binanalyst.lib.calc.graph.Layer;

import java.util.HashSet;
import java.util.Set;

import static net.tislib.binanalyst.lib.bit.AnomalBit.AnomalOperation.RAND;
import static net.tislib.binanalyst.lib.bit.AnomalBit.AnomalOperation.ROR;
import static net.tislib.binanalyst.lib.calc.graph.Operation.*;

/**
 * Created by Taleh Ibrahimli on 2/10/18.
 * Email: me@talehibrahimli.com
 */
public class AnomalGraphExpression {

    private final Layer<NamedBit> resolved = new Layer<>("Resolved");
    private final Layer<OperationalBit> middle;
    private final Layer<VarBit> root;

    private final GraphExpression expression;

    public AnomalGraphExpression(GraphExpression expression) {
        this.expression = expression;
        this.middle = this.expression.getMiddle().copy();
        this.root = this.expression.getRoot().copy();
        resolve(expression.getTruth());
    }

    Set<OperationalBit> rCache = new HashSet<>();

    private void resolve(OperationalBit operationalBit) {
        if (rCache.contains(operationalBit)) return;
        rCache.add(operationalBit);
        for (NamedBit bit : operationalBit.getBits()) {
            if (bit instanceof OperationalBit) {
                VarBit newBit = null;
                switch (operationalBit.getOperation()) {
                    case XOR:
                        newBit = new OperationalBit(XOR, swapBit(operationalBit, bit, operationalBit));
                        swap((OperationalBit) bit, newBit);
                        break;
                    case AND:
                        newBit = new AnomalBit(RAND, swapBit(operationalBit, bit, operationalBit));
                        swap((OperationalBit) bit, newBit);
                        break;
                    case OR:
                        newBit = new AnomalBit(ROR, swapBit(operationalBit, bit, operationalBit));
                        swap((OperationalBit) bit, newBit);
                        break;
                    case NOT:
                        newBit = new OperationalBit(NOT, swapBit(operationalBit, bit, operationalBit));
                        swap((OperationalBit) bit, newBit);
                        break;
                }
                resolve((OperationalBit) bit);
            }
        }
    }

    public void swap(OperationalBit oldBit, VarBit newBit) {
        resolved.register(newBit);
        middle.getBits().remove(oldBit);
        for (OperationalBit bit : middle.getBits()) {
            for (int i = 0; i < bit.getBits().length; i++) {
                if (bit.getBits()[i] == oldBit) {
                    bit.getBits()[i] = newBit;
//                    System.out.println("replaced: " + bit.getBits()[i].getName() + " => " + newBit.getName());
                }
            }
        }

        root.getBits().remove(oldBit);
    }

    private NamedBit[] swapBit(OperationalBit operationalBit, NamedBit from, VarBit to) {
        NamedBit[] bits = new NamedBit[operationalBit.getBits().length];
        for (int i = 0; i < bits.length; i++) {
            bits[i] = operationalBit.getBits()[i];
            if (bits[i] == from) {
                bits[i] = to;
            }
        }
        return bits;
    }

    public void show(boolean showValues) {
        this.root.show(showValues);
        this.middle.show(showValues);
        this.resolved.show(showValues);
    }
}
