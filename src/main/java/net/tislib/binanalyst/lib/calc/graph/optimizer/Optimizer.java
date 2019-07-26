package net.tislib.binanalyst.lib.calc.graph.optimizer;

import java.util.ArrayList;
import java.util.List;
import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.NamedBit;
import net.tislib.binanalyst.lib.calc.graph.GraphBitOpsCalculator;
import net.tislib.binanalyst.lib.calc.graph.Operation;

/**
 * Created by Taleh Ibrahimli on 2/9/18.
 * Email: me@talehibrahimli.com
 */
public interface Optimizer {
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

    static int count(Bit[] bits, Bit bit) {
        int count = 0;

        for (Bit theBit : bits) {
            if (theBit == bit) {
                count++;
            }
        }

        return count;
    }

    NamedBit optimizeOperation(GraphBitOpsCalculator graphBitOpsCalculator, Operation operation, NamedBit[] bits, NamedBit chain);

    void optimizeCalculator(GraphBitOpsCalculator graphBitOpsCalculator);
}
