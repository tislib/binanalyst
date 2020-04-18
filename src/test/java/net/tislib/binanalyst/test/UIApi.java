package net.tislib.binanalyst.test;

import api.LightServeApi;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.sat.TSatMutation;
import net.tislib.binanalyst.lib.calc.graph.sat.TSatMutation2;
import net.tislib.binanalyst.lib.operator.BinMul;
import net.tislib.binanalyst.test.examples.SimpleTestCalculators;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static net.tislib.binanalyst.lib.calc.graph.sat.SimpleSatTester.buildSat;

public class UIApi {

    public static void main(String... args) throws IOException {
        LightServeApi.serveGraphCalculator(UIApi::generateCalculators);
    }

    private static Map<String, BitOpsGraphCalculator> generateCalculators() {
        Map<String, BitOpsGraphCalculator> calculatorMap = new HashMap<>();
//        calculatorMap.putAll(SimpleTestCalculators.simpleCalculators());
        int bitCount = 2;
//        BitOpsGraphCalculator twoBitMul = SimpleTestCalculators.nBitFunction(bitCount, BinMul::multiply, "NONE", "NONE");
//        calculatorMap.put("multiplication", twoBitMul);
//        BitOpsGraphCalculator twoBitMulMut;
//        twoBitMulMut = new MutationOperation(twoBitMul.getInput().locate("a0")).transform(twoBitMul);
//        twoBitMulMut = new MutationOperation(twoBitMul.getInput().locate("a1")).transform(twoBitMulMut);
//        twoBitMulMut = new MutationOperation(twoBitMul.getInput().locate("a2")).transform(twoBitMulMut);
//        twoBitMulMut.calculate();
//
//        calculatorMap.put("mutation", twoBitMulMut);
//
//        {
//            int bitCount2 = 3;
//            AbstractBitOpsGraphCalculatorDecorator calculator = (AbstractBitOpsGraphCalculatorDecorator) SimpleTestCalculators.nBitFunction(bitCount2, BinMul::multiply, "NMU", "TWO_BIT");
//
//            UsageFinder usageFinder = new UsageFinder(calculator.getInput(), calculator.getMiddle(), calculator.getOutput());
//            usageFinder.cleanUnusedMiddleBits();
//
//            calculator.calculate();
//            calculator.show();
//
//            calculatorMap.put("222", calculator);
//        }
//
//        {
//            AbstractBitOpsGraphCalculatorDecorator calculator = (AbstractBitOpsGraphCalculatorDecorator) SimpleTestCalculators.nBitFunction(3, BinMul::multiply, "ANDOR", "TWO_BIT");
//            calculatorMap.put("333", calculator);
//        }
//
//        {
//            AbstractBitOpsGraphCalculatorDecorator calculator = (AbstractBitOpsGraphCalculatorDecorator) SimpleTestCalculators.nBitFunction(3, BinMul::multiply, "ANDOR2", "TWO_BIT");
//            calculatorMap.put("444", calculator);
//        }

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
        {
//            BitOpsGraphCalculator calculator2Bit = SimpleTestCalculators.nBitFunction(3, BinMul::multiply, "NONE", "ANDOR");
            BitOpsGraphCalculator calculator2BitSat = buildSat(
                    SimpleTestCalculators.nBitFunction(2, BinMul::multiply, "NONE", "ANDOR")
                    , 2);
            BitOpsGraphCalculator calculator = SimpleTestCalculators.nBitFunction(3, BinMul::multiply, "NONE", "NONE");

            TSatMutation tSatMutation = new TSatMutation();

//            calculatorMap.put("2Bit", calculator2Bit);
            calculatorMap.put("calculator2BitSat", calculator2BitSat);
            calculatorMap.put("TSatBuilder3", tSatMutation.buildWithMutation(calculator, 2, new String[]{
                    "a0",
                    "a1",
                    "a2",
                    "b0",
                    "b1",
                    "b2",
                    "M1R",
                    "M2R",
                    "M3R",
                    "M4R",
                    "M5R",
                    "M6R",
                    "M7R",
                    "M8R",
                    "M9R",
                    "M10R",
                    "M11R",

                    "M38R",
                    "M36R",
                    "M40R",

                    "M1R",
                    "M2R",

                    "M38R",
                    "M36R",
                    "M40R",
            }));
        }

        {
            BitOpsGraphCalculator calculator = SimpleTestCalculators.nBitFunction(16, BinMul::multiply, "NONE", "NONE");

            TSatMutation2 tSatMutation = new TSatMutation2();
            calculatorMap.put("TSatBuilder4", tSatMutation.buildWithMutation(calculator, 2, new String[]{

            }));
        }

        {
            BitOpsGraphCalculator calculator = SimpleTestCalculators.nBitFunction(16, BinMul::multiply, "NONE", "NONE");

            TSatMutation2 tSatMutation = new TSatMutation2();
            calculatorMap.put("TSatBuilder4mut", tSatMutation.buildWithMutation(calculator, 2, new String[]{
                    "a0R",
                    "a1R",
                    "b0R",
                    "b1R",

                    "M0R",
                    "M1R",
                    "M2R",
                    "M3R",

                    "M4R",
                    "M5R",
                    "M6R",
                    "M7R",
            }));
        }

//        twoBitMulMut.show();

//        System.out.println(twoBitMul.getOperationCount());
//        System.out.println(twoBitMulMut.getOperationCount());
        return calculatorMap;
    }

}
