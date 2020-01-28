package net.tislib.binanalyst.test;

import api.LightServeApi;
import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.VarBit;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.GraphBitOpsCalculator;
import net.tislib.binanalyst.lib.calc.graph.decorator.*;
import net.tislib.binanalyst.lib.calc.graph.operations.MutationOperation;
import net.tislib.binanalyst.lib.operator.BinMul;
import net.tislib.binanalyst.test.examples.SimpleTestCalculators;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static net.tislib.binanalyst.lib.BinValueHelper.setVal;
import static net.tislib.binanalyst.lib.bit.ConstantBit.ZERO;

public class UIApi {

    public static void main(String... args) throws IOException {
        LightServeApi.serveGraphCalculator(UIApi::generateCalculators);
    }

    private static Map<String, BitOpsGraphCalculator> generateCalculators() {
        Map<String, BitOpsGraphCalculator> calculatorMap = new HashMap<>();
//        calculatorMap.putAll(SimpleTestCalculators.simpleCalculators());
        int bitCount = 3;
        BitOpsGraphCalculator twoBitMul = SimpleTestCalculators.nBitFunction(bitCount, BinMul::multiply, "NONE", "NONE");
        calculatorMap.put("multiplication", twoBitMul);
        BitOpsGraphCalculator twoBitMulMut;
        twoBitMulMut = new MutationOperation(twoBitMul.getInput().locate("a0")).transform(twoBitMul);
        twoBitMulMut = new MutationOperation(twoBitMul.getInput().locate("a1")).transform(twoBitMulMut);
        twoBitMulMut = new MutationOperation(twoBitMul.getInput().locate("a2")).transform(twoBitMulMut);
        twoBitMulMut.calculate();

        calculatorMap.put("mutation", twoBitMulMut);

        twoBitMulMut.show();

        System.out.println(twoBitMul.getOperationCount());
        System.out.println(twoBitMulMut.getOperationCount());
        return calculatorMap;
    }

}
