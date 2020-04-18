package net.tislib.binanalyst.lib.calc.graph.sat;

import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.NamedBit;
import net.tislib.binanalyst.lib.bit.OperationalBit;
import net.tislib.binanalyst.lib.bit.VarBit;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.Operation;
import net.tislib.binanalyst.lib.calc.graph.decorator.BinderOptimizationDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.optimizer.Logical2OptimizationDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.optimizer.SimpleOptimizationDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.optimizer.UnusedBitOptimizerDecorator;
import net.tislib.binanalyst.lib.calc.graph.tools.GraphCalculatorTools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.tislib.binanalyst.lib.calc.graph.logic.CommonLogicalOperations.*;
import static net.tislib.binanalyst.lib.calc.graph.sat.SimpleSatTester.buildSat;

public class TSatMutation2 {

    public BitOpsGraphCalculator buildWithMutation(BitOpsGraphCalculator initialCalc, long r, String[] mutBits) {
        BitOpsGraphCalculator calc = buildSat(initialCalc, r);

        calc = transformToLinearXorSat(calc);

        calc = new BinderOptimizationDecorator(calc);
        calc = new SimpleOptimizationDecorator(calc);
        calc = new Logical2OptimizationDecorator(calc);
        calc = new UnusedBitOptimizerDecorator(calc);

//        NamedBit oBit = calc.getOutput().getBitL(0);
//        calc.setOutputBits(new Bit[]{CommonLogicalOperations.toCnf(oBit, calc)});

        GraphCalculatorTools.showComplexity(calc);

        if (mutBits.length == 1 && mutBits[0].equals("full")) {
            mutBits = calc.getInput().getBits().stream().map(VarBit::getName).toArray(String[]::new);
        }

        calc = new UnusedBitOptimizerDecorator(calc);
        for (String mutBit : mutBits) {
            mutateBit(calc, (NamedBit) calc.locate(mutBit));
        }

        calc.optimize();

        return calc;
    }

    private void mutateBit(BitOpsGraphCalculator calc, NamedBit mutator) {
        NamedBit mainBit = calc.getOutput().getBits().get(0);
        List<NamedBit> main = Arrays.asList(((OperationalBit) mainBit).getBits());
        List<NamedBit> newMain = new ArrayList<>();
        List<NamedBit> mutationList = new ArrayList<>();

        for (NamedBit namedBit : main) {
            if (!hasBitDeep(namedBit, mutator)) {
                newMain.add(namedBit);
            } else {
                mutationList.add(namedBit);
            }
        }

        NamedBit mBit = (NamedBit) calc.and(mutationList.toArray(new NamedBit[0]));

        Bit mut = calc.or(
                mutate(mBit, calc, mutator, true),
                mutate(mBit, calc, mutator, false)
        );

        NamedBit cnf = (NamedBit) toCnf((NamedBit) mut, calc);

        if (cnf instanceof OperationalBit && ((OperationalBit) cnf).getOperation() != Operation.NOT) {
            newMain.addAll(Arrays.asList(((OperationalBit) cnf).getBits()));
            GraphCalculatorTools.showFormula((OperationalBit) cnf);
        } else {
            newMain.add(cnf);
        }

        calc.setOutputBits(new Bit[]{calc.and(newMain.toArray(new NamedBit[0]))});

        calc.optimize();

        GraphCalculatorTools.showComplexity(calc);
    }
}