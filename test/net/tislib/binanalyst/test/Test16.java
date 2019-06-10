package net.tislib.binanalyst.test;

import static net.tislib.binanalyst.lib.BinValueHelper.printValues;
import static net.tislib.binanalyst.lib.BinValueHelper.setVal;
import static net.tislib.binanalyst.lib.bit.ConstantBit.ZERO;

import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.VarBit;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.GraphBitOpsCalculator;
import net.tislib.binanalyst.lib.calc.graph.decorator.SimpleOptimizationDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.UnusedBitOptimizerDecorator;
import net.tislib.binanalyst.lib.operator.BinAdd;

public class Test16 {

    public static void main(String... args) {
        VarBit[] bits = VarBit.list("a", 4, ZERO);

        BitOpsGraphCalculator calculator = new GraphBitOpsCalculator();
        calculator = new UnusedBitOptimizerDecorator(calculator);
        calculator = new SimpleOptimizationDecorator(calculator);

        calculator.setInputBits(bits);

        Bit res = BinAdd.getAddStageBit(calculator, bits, 0);

        calculator.setOutputBits(new Bit[]{res});

        setVal(bits, 31);

        calculator.calculate();

        calculator.show();

        printValues(bits);
        printValues(res);

    }
}
