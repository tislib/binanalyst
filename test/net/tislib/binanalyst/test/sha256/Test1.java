package net.tislib.binanalyst.test.sha256;

import net.tislib.binanalyst.lib.WordOpsHelper;
import net.tislib.binanalyst.lib.algorithms.sha.sha256.Sha256AlgorithmImpl;
import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.VarBit;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.GraphBitOpsCalculator;
import net.tislib.binanalyst.lib.calc.graph.decorator.BinderOptimizationDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.ConstantOperationRemoverOptimizationDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.SimpleOptimizationDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.UnusedBitOptimizerDecorator;
import net.tislib.binanalyst.lib.calc.graph.tools.GraphCalculatorTools;
import net.tislib.binanalyst.lib.calc.graph.tools.OperationLevelMeasureAnalyser;

public class Test1 {

    public static void main(String... args) {
        BitOpsGraphCalculator calculator = new GraphBitOpsCalculator();
//        calculator = new BinderOptimizationDecorator(calculator);
//        calculator = new XorAndCalculatorDecorator(calculator, true);
        calculator = new SimpleOptimizationDecorator(calculator);
        calculator = new ConstantOperationRemoverOptimizationDecorator(calculator);
//        calculator = new UnusedBitOptimizerDecorator(calculator);
        WordOpsHelper wordOpsHelper = new WordOpsHelper(calculator);

        Sha256AlgorithmImpl sha256Algorithm = new Sha256AlgorithmImpl(calculator);

        OperationLevelMeasureAnalyser operationLevelMeasureAnalyser = new OperationLevelMeasureAnalyser(calculator);

//        sha256Algorithm.setRounds(8);

        Bit[][] constantDat = wordOpsHelper.toBitWordArray(wordOpsHelper.pad("test".getBytes()));
        Bit[][] bits = new Bit[constantDat.length][32];

        for (int i = 0; i < constantDat.length; i++) {
            for (int j = 0; j < constantDat[i].length; j++) {
                VarBit varBit = new VarBit("I[" + i + "][" + j + "]");
                varBit.setValue(constantDat[i][j].getValue());
                bits[i][j] = varBit;
            }
        }

        Bit[][] res = sha256Algorithm.hash(bits);

        calculator.setOutputBits(res);

        calculator.calculate();

        operationLevelMeasureAnalyser.reLabel();

        System.out.println("MIDDLE SIZE: " + calculator.getMiddle().getBits().size());
        System.out.println("DEPTH: " + GraphCalculatorTools.getMaxDepth(calculator));

//        calculator.show();

        operationLevelMeasureAnalyser.analyse();

//        System.out.println(operationLevelMeasureAnalyser.stats());
        System.out.println(operationLevelMeasureAnalyser.statsUpper());

        System.out.println(GraphCalculatorTools.getMaxOperationCount(calculator));
    }

}
