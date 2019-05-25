package net.tislib.binanalyst.lib.calc.graph.optimizer;

import java.util.HashMap;
import java.util.Map;
import net.tislib.binanalyst.lib.bit.NamedBit;
import net.tislib.binanalyst.lib.calc.graph.GraphBitOpsCalculator;
import net.tislib.binanalyst.lib.calc.graph.Operation;

public class CacheBasedOptimizer implements Optimizer {

    Map<String, NamedBit> bitCache = new HashMap<>();
    long oc = 0;

    @Override
    public NamedBit optimizeOperation(GraphBitOpsCalculator graphBitOpsCalculator, Operation operation, NamedBit[] bits, NamedBit chain) {
        if (chain == null) {
            return chain;
        }
        if (bitCache.containsKey(chain.toString())) {
            oc++;
            return bitCache.get(chain.toString());
        } else {
            bitCache.put(chain.toString(), chain);
        }
        return chain;
    }

    @Override
    public void optimizeCalculator(GraphBitOpsCalculator graphBitOpsCalculator) {
        System.out.println("COC: " + oc);
    }
}
