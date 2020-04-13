package net.tislib.binanalyst.lib.calc.graph.sat;

import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.NamedBit;
import net.tislib.binanalyst.lib.bit.VarBit;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.Layer;

import static net.tislib.binanalyst.lib.BinValueHelper.setVal;
import static net.tislib.binanalyst.lib.bit.ConstantBit.ZERO;

public class SimpleSatTester {

    public static boolean hasSolution(BitOpsGraphCalculator calc) {
        Layer<VarBit> input = calc.getInput();

        long N = 1L << input.size();

        for (int i = 0; i < N; i++) {
            setVal(input.getBits().toArray(new VarBit[0]), i);
            calc.calculate();
            if (calc.getOutput().getBitL(0).getValue().isTrue()) {
                return true;
            }
        }

        return false;
    }

    public static BitOpsGraphCalculator buildSat(BitOpsGraphCalculator calc, long r) {
        Layer<NamedBit> output = calc.getOutput();

        VarBit[] resultO = VarBit.list("r", calc.getOutput().size(), ZERO);
        Bit[] newOutput = new Bit[resultO.length];
        setVal(resultO, r);

        for (int i = 0; i < output.size(); i++) {
            if (resultO[output.size() - i - 1].getValue().isTrue()) {
                newOutput[i] = output.getBitL(i);
            } else {
                newOutput[i] = calc.not(output.getBitL(i));
            }
        }

        calc.setOutputBits(new Bit[]{calc.and(newOutput)});

        calc.optimize();

        return calc;
    }
}
