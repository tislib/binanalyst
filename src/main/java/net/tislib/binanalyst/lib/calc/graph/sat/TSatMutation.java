package net.tislib.binanalyst.lib.calc.graph.sat;

import net.tislib.binanalyst.lib.bit.*;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.decorator.optimizer.UnusedBitOptimizerDecorator;
import net.tislib.binanalyst.lib.calc.graph.tools.GraphCalculatorTools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.tislib.binanalyst.lib.calc.graph.sat.SimpleSatTester.buildSat;

public class TSatMutation {

    public BitOpsGraphCalculator buildWithMutation(BitOpsGraphCalculator initialCalc, long r, String[] mutBits) {
        BitOpsGraphCalculator calc = buildSat(initialCalc, r);
        calc = new UnusedBitOptimizerDecorator(calc);
//        calc = new AndOrCalculatorDecorator(calc, true);
        for (String mutBit : mutBits) {
            mutateBit(calc, mutBit);
        }

        calc.optimize();

        return calc;
    }

    public BitOpsGraphCalculator buildWithMutation2(BitOpsGraphCalculator initialCalc, int r, int loop) {
        BitOpsGraphCalculator calc = buildSat(initialCalc, r);
        calc = new UnusedBitOptimizerDecorator(calc);
//        calc = new AndOrCalculatorDecorator(calc, true);
        for (int i = 0; i < loop; i++) {
            for (VarBit mutBit : new ArrayList<>(initialCalc.getInput().getBits())) {
                mutateBit(calc, mutBit.getName());
            }
        }

        calc.optimize();

        return calc;
    }

    private void mutateBit(BitOpsGraphCalculator calculator, String mutBit) {
        NamedBit mainBit = calculator.getOutput().getBits().get(0);
        List<NamedBit> main = new ArrayList<>(Arrays.asList(((OperationalBit) mainBit).getBits()));

        NamedBit bit = (NamedBit) calculator.locate(mutBit);

        List<OperationalBit> usages = GraphCalculatorTools.findUsages(calculator, bit);

        for (OperationalBit usageBit : usages) {
            VarBit replaceBit = new VarBit(usageBit.getName() + "R");
            calculator.getInput().addBits(replaceBit);
            calculator.remove(usageBit);
            calculator.replace(usageBit, replaceBit);

            Bit newOp = calculator.or(
                    mutate(usageBit, calculator, bit, false),
                    mutate(usageBit, calculator, bit, true)
            );

            NamedBit res = (NamedBit) calculator.xor(replaceBit, calculator.not(newOp));
            main.add(res);
        }

        VarBit output = (VarBit) calculator.and(main.toArray(new NamedBit[0]));
        output.setName("O");
        calculator.setOutputBits(new Bit[]{output});
        calculator.remove((OperationalBit) mainBit);
    }

    private Bit mutate(NamedBit namedBit, BitOpsGraphCalculator calculator, NamedBit mutator, boolean truth) {
        if (namedBit instanceof OperationalBit) {
            OperationalBit operationalBit = (OperationalBit) namedBit;
            NamedBit[] bits = new NamedBit[operationalBit.getBits().length];
            for (int i = 0; i < bits.length; i++) {
                bits[i] = (NamedBit) mutate(operationalBit.getBits()[i], calculator, mutator, truth);
            }
            return calculator.operation(operationalBit.getOperation(), bits);
        } else if (namedBit instanceof VarBit) {
            if (mutator == namedBit) {
                return truth ? ConstantBit.ONE : ConstantBit.ZERO;
            } else {
                return namedBit;
            }
        }

        return namedBit;
    }

    private static void print(BitOpsGraphCalculator calculator) {
        System.out.println("##############################################");

        System.out.println("MIDDLE SIZE: " + calculator.getMiddle().getBits().size());
        System.out.println("DEPTH: " + GraphCalculatorTools.getMaxDepth(calculator));
    }
}