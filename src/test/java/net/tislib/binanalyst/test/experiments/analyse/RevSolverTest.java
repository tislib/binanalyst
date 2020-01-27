package net.tislib.binanalyst.test.experiments.analyse;

import static net.tislib.binanalyst.lib.BinValueHelper.setVal;
import static net.tislib.binanalyst.lib.bit.ConstantBit.ZERO;

import net.tislib.binanalyst.lib.BinOps;
import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.VarBit;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.GraphBitOpsCalculator;
import net.tislib.binanalyst.lib.calc.graph.decorator.BinderOptimizationDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.optimizer.SimpleOptimizationDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.optimizer.UnusedBitOptimizerDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.XorAndCalculatorDecorator;
import net.tislib.binanalyst.lib.calc.graph.solver.RevSolver;
import net.tislib.binanalyst.lib.operator.BinMul;
import org.junit.Test;

public class RevSolverTest {

    @Test
    public void test1() {
        BitOpsGraphCalculator calculator = new GraphBitOpsCalculator();

        calculator = new BinderOptimizationDecorator(calculator);
        calculator = new XorAndCalculatorDecorator(calculator);
        calculator = new SimpleOptimizationDecorator(calculator);
        calculator = new UnusedBitOptimizerDecorator(calculator);

        long a = 5;
        long b = 7;

        VarBit[] aBits = VarBit.list("a", 2, ZERO);
        VarBit[] bBits = VarBit.list("b", 2, ZERO);


        setVal(aBits, a);
        setVal(bBits, b);

        calculator.setInputBits(aBits, bBits);

        Bit[] r = BinMul.multiply(calculator, aBits, bBits);

        calculator.setOutputBits(r);

        calculator.calculate();

        calculator.show();

        RevSolver revSolver = new RevSolver(calculator);
        revSolver.setSymmetricInput(aBits[1], bBits[1]);

        BinOps.variate(r.length, crBits -> {
            revSolver.showResultFor(crBits);
        });

    }

}
