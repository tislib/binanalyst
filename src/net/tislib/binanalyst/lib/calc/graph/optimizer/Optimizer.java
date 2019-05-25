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


    static Bit[] remove(Bit[] bits, Bit bit) {
        List<Bit> namedBits = new ArrayList<>();
        for (Bit namedBit : bits) {
            if (!namedBit.equals(bit)) namedBits.add(namedBit);
        }
        return namedBits.toArray(new Bit[]{});
    }

    static boolean contains(Bit[] bits, Bit bit) {
        for (Bit namedBit : bits) {
            if (namedBit.equals(bit)) {
                return true;
            }
        }
        return false;
    }

    void optimizeCalculator(GraphBitOpsCalculator graphBitOpsCalculator);
}
