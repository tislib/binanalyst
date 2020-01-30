package net.tislib.binanalyst.test.experiments;

import net.tislib.binanalyst.lib.bit.BinaryValue;
import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.ConstantBit;
import net.tislib.binanalyst.lib.bit.VarBit;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.GraphBitOpsCalculator;
import net.tislib.binanalyst.lib.calc.graph.decorator.optimizer.pattern.PatternOptimizationDecorator;
import net.tislib.binanalyst.lib.calc.graph.operations.MutationOperation;
import net.tislib.binanalyst.lib.calc.graph.tools.GraphCalculatorTools;
import net.tislib.binanalyst.lib.operator.BinMul;
import net.tislib.binanalyst.test.examples.SimpleTestCalculators;

public class Test24 {

    public static void main(String... args) {
        BitOpsGraphCalculator calculator = new GraphBitOpsCalculator();
        calculator = new PatternOptimizationDecorator(calculator);

        VarBit[] bits = VarBit.list("a", 2, ConstantBit.ZERO);

        calculator.setInputBits(bits);

        calculator.setOutputBits(new Bit[]{
                calculator.and(calculator.not(calculator.not(bits[0])))
        });

        bits[0].setValue(BinaryValue.TRUE);
        bits[1].setValue(BinaryValue.TRUE);

        calculator.calculate();

        calculator.show();
    }

    private static void print(BitOpsGraphCalculator calculator) {
        System.out.println("##############################################");

        System.out.println("MIDDLE SIZE: " + calculator.getMiddle().getBits().size());
        System.out.println("DEPTH: " + GraphCalculatorTools.getMaxDepth(calculator));
    }

}
