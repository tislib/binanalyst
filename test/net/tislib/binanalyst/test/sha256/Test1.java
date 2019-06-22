package net.tislib.binanalyst.test.sha256;

import net.tislib.binanalyst.lib.algorithms.sha.sha256.Sha256AlgorithmImpl;
import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.GraphBitOpsCalculator;
import net.tislib.binanalyst.lib.calc.graph.decorator.BinderOptimizationDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.SimpleOptimizationDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.UnusedBitOptimizerDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.XorAndCalculatorDecorator;
import net.tislib.binanalyst.lib.calc.graph.tools.GraphCalculatorTools;

public class Test1 {

    public static void main(String... args) {
        BitOpsGraphCalculator calculator = new GraphBitOpsCalculator();
//        calculator = new BinderOptimizationDecorator(calculator);
//        calculator = new XorAndCalculatorDecorator(calculator, true);
        calculator = new SimpleOptimizationDecorator(calculator);
        calculator = new UnusedBitOptimizerDecorator(calculator);

        Sha256AlgorithmImpl sha256Algorithm = new Sha256AlgorithmImpl(calculator);

//        sha256Algorithm.setRounds(8);

        Bit[][] res = sha256Algorithm.hash("".getBytes());

        calculator.setOutputBits(res);

        calculator.calculate();

        System.out.println("MIDDLE SIZE: " + calculator.getMiddle().getBits().size());
        System.out.println("DEPTH: " + GraphCalculatorTools.getMaxDepth(calculator));

//        calculator.show();
    }

}
