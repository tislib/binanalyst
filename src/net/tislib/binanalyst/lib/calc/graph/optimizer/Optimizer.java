package net.tislib.binanalyst.lib.calc.graph.optimizer;

import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.NamedBit;
import net.tislib.binanalyst.lib.bit.VarBit;
import net.tislib.binanalyst.lib.calc.graph.GraphBitOpsCalculator;
import net.tislib.binanalyst.lib.calc.graph.Operation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Taleh Ibrahimli on 2/9/18.
 * Email: me@talehibrahimli.com
 */
public interface Optimizer {
    NamedBit optimizeOperation(GraphBitOpsCalculator graphBitOpsCalculator, Operation operation, NamedBit[] bits, NamedBit chain);


    static NamedBit[] remove(NamedBit[] bits, VarBit bit) {
        List<NamedBit> namedBits = new ArrayList<>();
        for (NamedBit namedBit : bits) {
            if (!namedBit.equals(bit)) namedBits.add(namedBit);
        }
        return namedBits.toArray(new NamedBit[]{});
    }

    static boolean contains(NamedBit[] bits, Bit bit) {
        for (NamedBit namedBit : bits) {
            if (namedBit.equals(bit)) {
                return true;
            }
        }
        return false;
    }

    void optimizeCalculator(GraphBitOpsCalculator graphBitOpsCalculator);
}
