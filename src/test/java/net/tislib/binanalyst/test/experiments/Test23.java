package net.tislib.binanalyst.test.experiments;

import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.decorator.optimizer.NewOptimizerOptimizationDecorator;
import net.tislib.binanalyst.lib.calc.graph.operations.MutationOperation;
import net.tislib.binanalyst.lib.calc.graph.tools.GraphCalculatorTools;
import net.tislib.binanalyst.lib.operator.BinMul;
import net.tislib.binanalyst.test.examples.SimpleTestCalculators;

public class Test23 {

    public static void main(String... args) {
        int bitCount = 5;
        BitOpsGraphCalculator calculator = SimpleTestCalculators.nBitFunction(bitCount, BinMul::multiply, "NONE", "NONE");

        BitOpsGraphCalculator oldCalculator = calculator;

        calculator = new MutationOperation(calculator.getInput().locate("a0")).transform(calculator);
        calculator = new MutationOperation(calculator.getInput().locate("a1")).transform(calculator);
        calculator = new MutationOperation(calculator.getInput().locate("a2")).transform(calculator);
//        calculator = new MutationOperation(calculator.getInput().locate("a3")).transform(calculator);
//        calculator = new MutationOperation(calculator.getInput().locate("a4")).transform(calculator);
//        calculator = new MutationOperation(calculator.getInput().locate("a5")).transform(calculator);

//        calculator.show();

        print(oldCalculator);
        print(calculator);
    }

    private static void print(BitOpsGraphCalculator calculator) {
        System.out.println("##############################################");

        System.out.println("MIDDLE SIZE: " + calculator.getMiddle().getBits().size());
        System.out.println("DEPTH: " + GraphCalculatorTools.getMaxDepth(calculator));
    }

}
