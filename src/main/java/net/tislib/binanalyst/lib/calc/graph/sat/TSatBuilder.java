package net.tislib.binanalyst.lib.calc.graph.sat;

import net.tislib.binanalyst.lib.bit.*;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.tools.GraphCalculatorTools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.tislib.binanalyst.lib.calc.graph.logic.CommonLogicalOperations.hasBitDeep;
import static net.tislib.binanalyst.lib.calc.graph.logic.CommonLogicalOperations.transformToLinearXorSat;

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


    public BitOpsGraphCalculator buildSat(BitOpsGraphCalculator initialCalc, long r) {
        BitOpsGraphCalculator calc = SimpleSatTester.buildSat(initialCalc, r);

        return transformToLinearXorSat(calc);
    }

    private static void print(BitOpsGraphCalculator calculator) {
        System.out.println("##############################################");

        System.out.println("MIDDLE SIZE: " + calculator.getMiddle().getBits().size());
        System.out.println("DEPTH: " + GraphCalculatorTools.getMaxDepth(calculator));
    }
}
