package net.tislib.binanalyst.test;

import api.LightServeApi;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.UsageFinder;
import net.tislib.binanalyst.lib.calc.graph.decorator.AbstractBitOpsGraphCalculatorDecorator;
import net.tislib.binanalyst.lib.calc.graph.operations.MutationOperation;
import net.tislib.binanalyst.lib.calc.graph.sat.TSatBuilder;
import net.tislib.binanalyst.lib.operator.BinMul;
import net.tislib.binanalyst.test.examples.SimpleTestCalculators;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UIApi {

    public static void main(String... args) throws IOException {
        LightServeApi.serveGraphCalculator(UIApi::generateCalculators);
    }

    private static Map<String, BitOpsGraphCalculator> generateCalculators() {
        Map<String, BitOpsGraphCalculator> calculatorMap = new HashMap<>();
        calculatorMap.putAll(SimpleTestCalculators.simpleCalculators());
        int bitCount = 2;
        BitOpsGraphCalculator twoBitMul = SimpleTestCalculators.nBitFunction(bitCount, BinMul::multiply, "NONE", "NONE");
        calculatorMap.put("multiplication", twoBitMul);
        BitOpsGraphCalculator twoBitMulMut;
        twoBitMulMut = new MutationOperation(twoBitMul.getInput().locate("a0")).transform(twoBitMul);
        twoBitMulMut = new MutationOperation(twoBitMul.getInput().locate("a1")).transform(twoBitMulMut);
        twoBitMulMut = new MutationOperation(twoBitMul.getInput().locate("a2")).transform(twoBitMulMut);
        twoBitMulMut.calculate();

        calculatorMap.put("mutation", twoBitMulMut);

        {
            int bitCount2 = 3;
            AbstractBitOpsGraphCalculatorDecorator calculator = (AbstractBitOpsGraphCalculatorDecorator) SimpleTestCalculators.nBitFunction(bitCount2, BinMul::multiply, "NMU", "TWO_BIT");

            UsageFinder usageFinder = new UsageFinder(calculator.getInput(), calculator.getMiddle(), calculator.getOutput());
            usageFinder.cleanUnusedMiddleBits();

            calculator.calculate();
            calculator.show();

            calculatorMap.put("222", calculator);
        }

        {
            AbstractBitOpsGraphCalculatorDecorator calculator = (AbstractBitOpsGraphCalculatorDecorator) SimpleTestCalculators.nBitFunction(3, BinMul::multiply, "ANDOR", "TWO_BIT");
            calculatorMap.put("333", calculator);
        }

        {
            AbstractBitOpsGraphCalculatorDecorator calculator = (AbstractBitOpsGraphCalculatorDecorator) SimpleTestCalculators.nBitFunction(3, BinMul::multiply, "ANDOR2", "TWO_BIT");
            calculatorMap.put("444", calculator);
        }

//        {
//            BitOpsGraphCalculator calculator = SimpleTestCalculators.nBitFunction(2, BinMul::multiply, "NONE", "ANDOR");
//
//            TSatBuilder satBuilder = new TSatBuilder();
//
//            BitOpsGraphCalculator res = satBuilder.buildWithMutation(calculator, 2, new String[]{"a0R", "b0R"});
//
//            calculatorMap.put("TSatBuilder1", res);
//        }
//        {
//            BitOpsGraphCalculator calculator = SimpleTestCalculators.nBitFunction(2, BinMul::multiply, "NONE", "ANDOR");
//
//            TSatBuilder satBuilder = new TSatBuilder();
//
//            calculatorMap.put("TSatBuilder2", satBuilder.buildWithMutation(calculator, 2, new String[]{"a0R"}));
//        }
//        {
//            BitOpsGraphCalculator calculator = SimpleTestCalculators.nBitFunction(2, BinMul::multiply, "NONE", "ANDOR");
//
//            TSatBuilder satBuilder = new TSatBuilder();
//
//            calculatorMap.put("TSatBuilder3", satBuilder.buildWithMutation(calculator, 2, new String[]{"a0R", "b0R"}));
//        }

//        twoBitMulMut.show();

//        System.out.println(twoBitMul.getOperationCount());
//        System.out.println(twoBitMulMut.getOperationCount());
        return calculatorMap;
    }

}
