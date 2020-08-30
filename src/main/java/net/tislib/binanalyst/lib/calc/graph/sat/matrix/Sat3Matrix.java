package net.tislib.binanalyst.lib.calc.graph.sat.matrix;

import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.NamedBit;
import net.tislib.binanalyst.lib.bit.OperationalBit;
import net.tislib.binanalyst.lib.bit.VarBit;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.Operation;
import net.tislib.binanalyst.lib.calc.graph.decorator.BinderOptimizationDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.optimizer.SimpleOptimizationDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.optimizer.UnusedBitOptimizerDecorator;
import net.tislib.binanalyst.lib.calc.graph.logic.CommonLogicalOperations;
import net.tislib.binanalyst.lib.calc.graph.sat.SimpleSatTester;
import net.tislib.binanalyst.lib.calc.graph.tools.GraphCalculatorTools;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static net.tislib.binanalyst.lib.calc.graph.logic.CommonLogicalOperations.*;

public class Sat3Matrix {

    public static byte[][] buildSat3Matrix(final BitOpsGraphCalculator bitOpsGraphCalculator, final long r) {
        BitOpsGraphCalculator calc = SimpleSatTester.buildSat(bitOpsGraphCalculator.copy(), r);

        calc = transformToLinearXorSat(calc);

        calc.show();
//        System.out.println("aa");


        calc = new SimpleOptimizationDecorator(calc);
        calc = new BinderOptimizationDecorator(calc);
//        calc = new Logical2OptimizationDecorator(calc);
//        calc = new UnusedBitOptimizerDecorator(calc);

        GraphCalculatorTools.showComplexity(calc);

        calc = new UnusedBitOptimizerDecorator(calc);

        GraphCalculatorTools.showComplexity(calc);
        calculateComplexity(calc);

        calc.setOutputBits(new Bit[]{CommonLogicalOperations.toCnf(calc.getOutput().getBitL(0), calc)});

        for (int i = 0; i < 10; i++) {
            optimizeFast(calc);
        }


        NamedBit o = calc.getOutput().getBitL(0);
        OperationalBit T = (OperationalBit) o;
        NamedBit[] ops = T.getBits();

        byte[][] res = new byte[ops.length][];

        List<VarBit> varBits = new ArrayList<>();
        varBits.addAll(calc.getInput().getBits());

        for (int i = 0; i < res.length; i++) {
            res[i] = new byte[varBits.size()];
            byte[] row = res[i];
            if (ops[i] instanceof OperationalBit) {

            } else {
                System.out.println("found");
                continue;
            }
            OperationalBit op = (OperationalBit) ops[i];
            NamedBit[] bits = op.getBits();

            for (int j = 0; j < bits.length; j++) {
                VarBit bit = (VarBit) bits[j];

                if (bit instanceof OperationalBit) {
                    bit = (VarBit) ((OperationalBit) bit).getBits()[0];

                    row[varBits.indexOf(bit)] = -1;
                } else {
                    row[varBits.indexOf(bit)] = 1;
                }


            }
        }

        return res;
    }

    public static void print(byte[][] res) {
        for (byte[] a : res) {
            System.out.print("");
            for (byte b : a) {
                System.out.print(b + "\t");
            }
            System.out.println();
        }
    }

    private static void calculateComplexity(BitOpsGraphCalculator calc) {
        BigInteger sumComplexity = BigInteger.ZERO;
        BigInteger mulComplexity = BigInteger.ONE;
        for (VarBit varBit : calc.getInput()) {
            int negativeUsage = GraphCalculatorTools.findUsages(calc, (NamedBit) calc.not(varBit)).size();
            int positiveUsage = (int) GraphCalculatorTools.findUsages(calc, varBit)
                    .stream()
                    .filter(item -> {
                        return item.getOperation() != Operation.NOT;
                    })
                    .count();
            long comp = negativeUsage * positiveUsage;
            sumComplexity = sumComplexity.add(BigInteger.valueOf(comp));
            mulComplexity = mulComplexity.multiply(BigInteger.valueOf(comp));
        }

        System.out.println("sumComplexity: " + sumComplexity);
        System.out.println("mulComplexity: " + mulComplexity);
    }

    private static void optimizeFast(BitOpsGraphCalculator calc) {
        NamedBit mainBit = calc.getOutput().getBits().get(0);
        if (!(mainBit instanceof OperationalBit)) {
            return;
        }

        OperationalBit oMainBit = (OperationalBit) mainBit;

        for (int i = 0; i < oMainBit.getBits().length; i++) {
            NamedBit namedBit = oMainBit.getBits()[i];
            if (namedBit instanceof OperationalBit) {
                if (((OperationalBit) namedBit).getOperation() == Operation.NOT) {
                    cleanBit(calc, (VarBit) namedBit, false);
                }
            } else if (namedBit instanceof VarBit) {
                cleanBit(calc, (VarBit) namedBit, true);
            }
        }

        for (VarBit varBit : calc.getInput()) {
            List<OperationalBit> usage = GraphCalculatorTools.findUsages(calc, (NamedBit) calc.not(varBit));
            if (usage.size() == 0) {
                cleanBit(calc, varBit, true);
            }
        }
    }

    private static void cleanBit(BitOpsGraphCalculator calc, VarBit theBit, boolean truth) {
        NamedBit mainBit = calc.getOutput().getBits().get(0);
        if (!(mainBit instanceof OperationalBit)) {
            return;
        }

        NamedBit[] main = ((OperationalBit) mainBit).getBits();

        List<NamedBit> newMain = new ArrayList<>();

        for (NamedBit namedBit : main) {
            if (namedBit == theBit) {
                continue;
            }

            if (hasBitDeep(namedBit, theBit)) {
                newMain.add((NamedBit) mutate(namedBit, calc, theBit, truth));
            } else {
                newMain.add(namedBit);
            }
        }

        calc.setOutputBits(new Bit[]{calc.and(newMain.toArray(new NamedBit[0]))});
//        calc.getInput().remove(theBit);
        System.out.println("removing bit: " + theBit.getName());

        calc.getInput().remove(theBit);
    }
}
