package net.tislib.binanalyst.test.sat;

import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.NamedBit;
import net.tislib.binanalyst.lib.bit.OperationalBit;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.decorator.BinderOptimizationDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.optimizer.SimpleOptimizationDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.optimizer.UnusedBitOptimizerDecorator;
import net.tislib.binanalyst.lib.calc.graph.logic.CommonLogicalOperations;
import net.tislib.binanalyst.lib.calc.graph.tools.GraphCalculatorTools;
import net.tislib.binanalyst.lib.operator.BinMul;
import net.tislib.binanalyst.test.examples.SimpleTestCalculators;

import java.util.*;

import static net.tislib.binanalyst.lib.calc.graph.logic.CommonLogicalOperations.transformToLinearXorSat;
import static net.tislib.binanalyst.lib.calc.graph.sat.SimpleSatTester.buildSat;

public class Test4 {

    private static BitOpsGraphCalculator calc;

    public static void main(String[] args) {
        BitOpsGraphCalculator initialCalc = SimpleTestCalculators.nBitFunction(32, BinMul::multiply, "NONE", "NONE");

        BitOpsGraphCalculator calc = buildSat(initialCalc, 0);

//        calc.show();

        calc = transformToLinearXorSat(calc);

//        System.out.println("aa");


        calc = new SimpleOptimizationDecorator(calc);
        calc = new BinderOptimizationDecorator(calc);
//        calc = new Logical2OptimizationDecorator(calc);
//        calc = new UnusedBitOptimizerDecorator(calc);

        GraphCalculatorTools.showComplexity(calc);

        calc = new UnusedBitOptimizerDecorator(calc);

        GraphCalculatorTools.showComplexity(calc);
        NamedBit oBit = calc.getOutput().getBitL(0);
        calc.setOutputBits(new Bit[]{CommonLogicalOperations.toCnf(oBit, calc)});

        calc.optimize();
        Test4.calc = calc;

        GraphCalculatorTools.showComplexity(calc);

        NamedBit mainBit = calc.getOutput().getBits().get(0);

        List<NamedBit> main = new ArrayList<>(Arrays.asList(((OperationalBit) mainBit).getBits()));

        Set<NamedBit> component1 = new HashSet<>();

        List<Integer> componentDimensions = new ArrayList<>();

        while (main.size() > 0) {
            component1.add(main.get(0));
            for (NamedBit namedBit : main) {
                if (namedBit instanceof OperationalBit) {
                    if (!component1.contains(namedBit) && !hasConflict(component1, namedBit)) {
                        component1.add(namedBit);
                    }
                }
            }

            System.out.println("component size: " + component1.size());
            componentDimensions.add(component1.size());
            main.removeAll(component1);
            component1.clear();
        }

        System.out.println(componentDimensions);
    }

    private static boolean hasConflict(Set<NamedBit> component, NamedBit namedBit) {
        return component.stream().anyMatch(item -> item instanceof OperationalBit && namedBit instanceof OperationalBit &&
                Arrays.stream(((OperationalBit) namedBit).getBits()).anyMatch(item2 -> hasConflict((OperationalBit) item, item2))
        );
    }

    private static boolean hasConflict(OperationalBit component, NamedBit namedBit) {
        boolean res = component.hasBit((NamedBit) calc.not(namedBit));
        return res;
    }
}
