package net.tislib.binanalyst.lib.calc.graph.sat;

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
import net.tislib.binanalyst.lib.calc.graph.tools.GraphCalculatorTools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static net.tislib.binanalyst.lib.calc.graph.logic.CommonLogicalOperations.*;
import static net.tislib.binanalyst.lib.calc.graph.sat.SimpleSatTester.buildNegSat;
import static net.tislib.binanalyst.lib.calc.graph.sat.SimpleSatTester.buildSat;

public class TNegSatMutation {

    private int complexity = 100;

    public boolean hasSolution(BitOpsGraphCalculator initialCalc, long r) {
        BitOpsGraphCalculator calc = buildWithMutation(initialCalc, r, new String[]{"full"});

        NamedBit mainBit = calc.getOutput().getBits().get(0);
        GraphCalculatorTools.showFormula(mainBit);

        return SimpleSatTester.hasSolution(calc);
    }

    public void serializeSat(BitOpsGraphCalculator calc, String name) {
        for (int i = 0; i < 10; i++) {
            this.optimizeFast(calc);
        }

        File file = new File("/home/taleh/Projects/binanalyst/data/" + name + ".txt");
        try (FileWriter fw = new FileWriter(file)) {

            NamedBit mainBit = calc.getOutput().getBits().get(0);
            List<NamedBit> main = Arrays.asList(((OperationalBit) mainBit).getBits());

            for (NamedBit bit : main) {
                if (bit instanceof OperationalBit) {
                    OperationalBit operationalBit = (OperationalBit) bit;

                    String str = Arrays.stream(operationalBit.getBits())
                            .map(Object::toString)
                            .collect(Collectors.joining("\t"));
                    fw.append(
                            str
                    );
                    fw.append("\n");
                    System.out.println(str);
                } else {
                    fw.append(String.valueOf(bit)).append("\n");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
//        System.exit(0);
    }

    public BitOpsGraphCalculator buildWithMutation(BitOpsGraphCalculator initialCalc, long r, String[] mutBits) {
        BitOpsGraphCalculator calc = buildNegSat(initialCalc, r);

        calc.show();

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
        NamedBit oBit = calc.getOutput().getBitL(0);
        calc.setOutputBits(new Bit[]{CommonLogicalOperations.toCnf(oBit, calc)});
        calc.optimize();
//        calc.show();
        this.optimizeFast(calc);
        calc.optimize();
        GraphCalculatorTools.showComplexity(calc);
//        calc.show();
        System.out.println("\n\n\n\n\n");

        serializeSat(calc, "step_init");

        calculateComplexity(calc);

        this.complexity = 100;
        while (calc.getInput().size() > 100) {
            complexity += 100;

            if (mutBits.length == 1 && mutBits[0].equals("full")) {
                mutBits = calc.getInput().getBits().stream().map(VarBit::getName).toArray(String[]::new);
            }


            GraphCalculatorTools.showComplexity(calc);
            serializeSat(calc, "step_init");

            for (int i = 0; i < mutBits.length; i++) {

                String mutBit = mutBits[mutBits.length - i - 1];
                mutateBit(calc, (NamedBit) calc.locate(mutBit));

                if (i % 500 == 0) {
                    System.out.println("step end: " + i);
                    calc.optimize();
                    GraphCalculatorTools.showComplexity(calc);
                    serializeSat(calc, "step_" + i);
                }
            }

            optimizeFast(calc);
        }

        return calc;
    }

    private void calculateComplexity(BitOpsGraphCalculator calc) {
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

    private void optimizeFast(BitOpsGraphCalculator calc) {
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

    private void cleanBit(BitOpsGraphCalculator calc, VarBit theBit, boolean truth) {
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
    }

    private void mutateBit(BitOpsGraphCalculator calc, NamedBit mutator) {
        NamedBit mainBit = calc.getOutput().getBits().get(0);
        if (!(mainBit instanceof OperationalBit)) {
            return;
        }
        if (mutator == null) {
            return;
        }
        List<NamedBit> main = Arrays.asList(((OperationalBit) mainBit).getBits());
        List<NamedBit> newMain = new ArrayList<>();
        List<NamedBit> mutationList = new ArrayList<>();

        for (NamedBit namedBit : main) {
            if (!hasBitDeep(namedBit, mutator)) {
                newMain.add(namedBit);
            } else {
                mutationList.add(namedBit);
                if (mutationList.size() > complexity) {
                    return;
                }
            }
        }

        NamedBit mBit = (NamedBit) calc.and(mutationList.toArray(new NamedBit[0]));

        Bit mut = calc.or(
                mutate(mBit, calc, mutator, true),
                mutate(mBit, calc, mutator, false)
        );

//        GraphCalculatorTools.showFormula((NamedBit) mBit);
//        GraphCalculatorTools.showFormula((NamedBit) mut);

        NamedBit cnf = (NamedBit) toCnf((NamedBit) mut, calc);

//        if (mutationList.size() > 700) {
//            System.out.println("found");
//        }

//        GraphCalculatorTools.showFormula((NamedBit) cnf);

        // (M0R | !a0R | M1R) & (a0R | !M0R) & (M1R | !M0R) & (M1R | !a0R) & (M1R | a0R | !M1R)

        if (cnf instanceof OperationalBit && ((OperationalBit) cnf).getOperation() != Operation.NOT) {
            newMain.addAll(Arrays.asList(((OperationalBit) cnf).getBits()));
        } else {
            newMain.add(cnf);
        }

        calc.setOutputBits(new Bit[]{calc.and(newMain.toArray(new NamedBit[0]))});

//        calc.getInput().remove((VarBit) mutator);
        System.out.println("mutating bit: " + mutator.getName());
    }
}