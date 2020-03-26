package net.tislib.binanalyst.test.examples;

import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.VarBit;
import net.tislib.binanalyst.lib.calc.BitOpsCalculator;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.GraphBitOpsCalculator;
import net.tislib.binanalyst.lib.calc.graph.decorator.*;
import net.tislib.binanalyst.lib.calc.graph.decorator.optimizer.Logical2OptimizationDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.optimizer.SimpleOptimizationDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.optimizer.TwoOpsOptimizationDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.optimizer.UnusedBitOptimizerDecorator;
import net.tislib.binanalyst.lib.operator.BinAdd;
import net.tislib.binanalyst.lib.operator.BinMul;

import java.util.HashMap;
import java.util.Map;

import static net.tislib.binanalyst.lib.BinValueHelper.setVal;
import static net.tislib.binanalyst.lib.bit.ConstantBit.ZERO;

public class SimpleTestCalculators {

    static String[] transformers = {
            "ANDOR", "ANDXOR", "ORXOR", "NONE"
    };

    static String[] deepNessOptimizer = {
            "BINDER", "TWO_BIT", "NONE"
    };

    public static Map<String, BitOpsGraphCalculator> simpleCalculators() {
        Map<String, BitOpsGraphCalculator> calculatorMap = new HashMap<>();

        for (String transformer : transformers) {
            for (String deepNessOptimize : deepNessOptimizer) {
                for (int bit = 2; bit <= 4; bit++) {
                    calculatorMap.put(bit + "BitMultiplicationT" + transformer + "D" + deepNessOptimize, nBitFunction(bit, BinMul::multiply, transformer, deepNessOptimize));
                    calculatorMap.put(bit + "BitAdditionT" + transformer + "D" + deepNessOptimize, nBitFunction(bit, BinAdd::add, transformer, deepNessOptimize));
                }
            }
        }

        return calculatorMap;
    }

    public static BitOpsGraphCalculator nBitFunction(int bitCount, nBitFunction function, String transformer, String deepNessOptimize) {
        BitOpsGraphCalculator calculator = new GraphBitOpsCalculator();

        switch (deepNessOptimize) {
            case "BINDER":
                calculator = new BinderOptimizationDecorator(calculator);
                break;
            case "TWO_BIT":
                calculator = new TwoOpsOptimizationDecorator(calculator);
                break;
            default:
                calculator = new Logical2OptimizationDecorator(calculator);
                calculator = new AndOrCalculatorDecorator(calculator, true);
        }
        switch (transformer) {
            case "ANDOR":
                calculator = new AndOrCalculatorDecorator(calculator, true);
                break;
            case "NMU":
                calculator = new NotMoveUpCalculatorDecorator(calculator);
                calculator = new AndOrCalculatorDecorator(calculator, true);
                break;
            case "ANDOR2":
                calculator = new AndOrCalculatorDecorator(calculator, false);
                break;
            case "ANDXOR":
                calculator = new XorAndCalculatorDecorator(calculator, true);
                break;
            case "ORXOR":
                calculator = new XorOrCalculatorDecorator(calculator, true);
                break;
        }
        calculator = new ConstantOperationRemoverOptimizationDecorator(calculator);
        calculator = new SimpleOptimizationDecorator(calculator);
        calculator = new UnusedBitOptimizerDecorator(calculator);

        long a = 0;
        long b = 0;

        //32532325, 23403244

        VarBit[] aBits = VarBit.list("a", bitCount, ZERO);
        VarBit[] bBits = VarBit.list("b", bitCount, ZERO);


        setVal(aBits, a);
        setVal(bBits, b);

        calculator.setInputBits(aBits, bBits);

        Bit[] r = function.calc(calculator, aBits, bBits);

        for (int i = 0; i < r.length; i++) {
            r[i] = calculator.not(r[i]);
        }

        calculator.setOutputBits(r);

        calculator.calculate();

        return calculator;
    }

    public interface nBitFunction {
        Bit[] calc(BitOpsCalculator calculator, Bit[] a, Bit[] b);
    }
}
