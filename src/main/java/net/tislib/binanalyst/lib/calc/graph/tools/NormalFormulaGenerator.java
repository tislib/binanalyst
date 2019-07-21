package net.tislib.binanalyst.lib.calc.graph.tools;

import static net.tislib.binanalyst.lib.bit.ConstantBit.ZERO;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import net.tislib.binanalyst.lib.BinValueHelper;
import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.NamedBit;
import net.tislib.binanalyst.lib.bit.OperationalBit;
import net.tislib.binanalyst.lib.bit.VarBit;
import net.tislib.binanalyst.lib.calc.graph.GraphBitOpsCalculator;

public class NormalFormulaGenerator {


    public static NamedBit toNormalFormul(OperationalBit varBit) {
        GraphBitOpsCalculator graphBitOpsCalculator = new GraphBitOpsCalculator();
        Set<NamedBit> deps = GraphCalculatorTools.findLeafs(varBit);
        Set<VarBit> inputBits = deps.stream().map(item -> (VarBit) item).collect(Collectors.toSet());
        graphBitOpsCalculator.getInput().setBits(inputBits);
        graphBitOpsCalculator.setOutputBits(new Bit[]{varBit});

        long valueCount = 1 << graphBitOpsCalculator.getInput().size();

        List<NamedBit> result = new ArrayList<>();


        for (int i = 0; i < valueCount; i++) {
            VarBit[] bits = inputBits.toArray(new VarBit[0]);
            BinValueHelper.setVal(bits, i);

            graphBitOpsCalculator.calculate();

            if (varBit.getValue().isTrue()) {
                List<NamedBit> inputTrueBits = new ArrayList<>();
                for (VarBit inputBit : graphBitOpsCalculator.getInput()) {
                    if (inputBit.getValue().isTrue()) {
                        inputTrueBits.add(inputBit);
                    } else {
                        inputTrueBits.add((NamedBit) graphBitOpsCalculator.not(inputBit));
                    }
                }
                result.add((NamedBit) graphBitOpsCalculator.and(inputTrueBits.toArray(new NamedBit[0])));
            }
        }
        if (result.size() == 0) {
            return (NamedBit) ZERO;
        }
        return (NamedBit) graphBitOpsCalculator.or(result.toArray(new NamedBit[0]));
    }

}
