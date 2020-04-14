package net.tislib.binanalyst.lib.calc.graph.sat;

import net.tislib.binanalyst.lib.bit.*;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.GraphBitOpsCalculator;
import net.tislib.binanalyst.lib.calc.graph.decorator.optimizer.DoubleNotRemovalOptimizationDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.optimizer.UnusedBitOptimizerDecorator;
import net.tislib.binanalyst.lib.calc.graph.tools.GraphCalculatorTools;

import java.util.*;

public class TSatBuilder {

    boolean hasSolution(BitOpsGraphCalculator satCalculator) {
        return SimpleSatTester.hasSolution(satCalculator);
    }

    public boolean hasSolution(BitOpsGraphCalculator initialCalc, long r) {
        BitOpsGraphCalculator calc = buildWithMutation(initialCalc, r, new String[]{"a0R", "b0R"});

        return hasSolution(calc);
    }

    public BitOpsGraphCalculator buildWithMutation(BitOpsGraphCalculator initialCalc, long r, String[] mutBits) {
        BitOpsGraphCalculator calc = buildSat(initialCalc, r);

        print(calc);

        int l = 0;

        for (String a : mutBits) {
            mutate(calc, (NamedBit) calc.locate(a));

            if (l++ == 50) {
                break;
            }
        }

        calc.optimize();

        print(calc);
        return calc;
    }

    private void mutate(BitOpsGraphCalculator calc, NamedBit mutator) {
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

        VarBit positive = new VarBit(mutator.getName() + "_PM");
        VarBit negative = new VarBit(mutator.getName() + "_NM");

        NamedBit mBit = (NamedBit) calc.and(mutationList.toArray(new NamedBit[0]));

        calc.getInput().addBits(positive, negative);

        newMain.add((NamedBit) calc.or(positive, negative));

        newMain.add((NamedBit) calc.xor(positive, calc.not(getMutation(calc, mBit, mutator, true))));
        newMain.add((NamedBit) calc.xor(negative, calc.not(getMutation(calc, mBit, mutator, false))));

        calc.setOutputBits(new Bit[]{calc.and(newMain.toArray(new NamedBit[0]))});

        calc.optimize();
    }

    private Bit getMutation(BitOpsGraphCalculator calc, NamedBit namedBit, NamedBit mutator, boolean truth) {
        if (namedBit instanceof OperationalBit) {
            OperationalBit operationalBit = (OperationalBit) namedBit;
            NamedBit[] bits = new NamedBit[operationalBit.getBits().length];
            for (int i = 0; i < bits.length; i++) {
                bits[i] = (NamedBit) getMutation(calc, operationalBit.getBits()[i], mutator, truth);
            }
            return calc.operation(operationalBit.getOperation(), bits);
        } else if (namedBit instanceof VarBit) {
            if (mutator == namedBit) {
                return truth ? ConstantBit.ONE : ConstantBit.ZERO;
            } else {
                return namedBit;
            }
        }

        return namedBit;
    }

    private boolean hasBitDeep(NamedBit namedBit, Bit mutation) {
        if (namedBit == mutation) {
            return true;
        }
        if (namedBit instanceof OperationalBit) {
            boolean found = false;
            for (NamedBit innerBit : ((OperationalBit) namedBit).getBits()) {
                found |= hasBitDeep(innerBit, mutation);
            }
            return found;
        } else {
            return false;
        }
    }


    public BitOpsGraphCalculator buildSat(BitOpsGraphCalculator initialCalc, long r) {
        BitOpsGraphCalculator calc = SimpleSatTester.buildSat(initialCalc, r);

        return transform(calc);
    }

    private BitOpsGraphCalculator transform(BitOpsGraphCalculator calc) {
        BitOpsGraphCalculator calculator = new GraphBitOpsCalculator();

        calculator = new UnusedBitOptimizerDecorator(calculator);
        calculator = new DoubleNotRemovalOptimizationDecorator(calculator);

        // collect inputs
        Map<String, VarBit> inputMap = new HashMap<>();

        List<Bit> main = new ArrayList<>();

        for (OperationalBit operationalBit : calc.getMiddle()) {
            inputMap.put(operationalBit.getName(), fromBit(operationalBit));
            for (NamedBit namedBit : operationalBit.getBits()) {
                inputMap.put(namedBit.getName(), fromBit(namedBit));
            }
        }

        calculator.setInputBits(inputMap.values().toArray(new VarBit[0]));

        for (OperationalBit operationalBit : calc.getMiddle()) {
            NamedBit[] bits = new NamedBit[operationalBit.getBits().length];
            for (int i = 0; i < operationalBit.getBits().length; i++) {
                bits[i] = inputMap.get(operationalBit.getBits()[i].getName());
            }
            Bit newOp = calculator.operation(operationalBit.getOperation(), bits);

            main.add(calculator.xor(inputMap.get(operationalBit.getName()), calculator.not(newOp)));
        }
        main.add(inputMap.get(calc.getOutput().getBitL(0).getName()));

        calculator.setOutputBits(new Bit[]{calculator.and(main.toArray(new Bit[0]))});
        return calculator;
    }

    private static VarBit fromBit(NamedBit namedBit) {
        VarBit varBit = new VarBit();
        varBit.setName(namedBit.getName() + "R");
        return varBit;
    }

    private static void print(BitOpsGraphCalculator calculator) {
        System.out.println("##############################################");

        System.out.println("MIDDLE SIZE: " + calculator.getMiddle().getBits().size());
        System.out.println("DEPTH: " + GraphCalculatorTools.getMaxDepth(calculator));
    }
}
