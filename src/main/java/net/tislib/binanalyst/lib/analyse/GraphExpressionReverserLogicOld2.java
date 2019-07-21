package net.tislib.binanalyst.lib.analyse;

import static net.tislib.binanalyst.lib.bit.ConstantBit.ONE;
import static net.tislib.binanalyst.lib.bit.ConstantBit.ZERO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.NamedBit;
import net.tislib.binanalyst.lib.bit.OperationalBit;
import net.tislib.binanalyst.lib.bit.VarBit;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.GraphBitOpsCalculator;
import net.tislib.binanalyst.lib.calc.graph.Layer;
import net.tislib.binanalyst.lib.calc.graph.Operation;
import net.tislib.binanalyst.lib.calc.graph.decorator.SimpleOptimizationDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.UnusedBitOptimizerDecorator;

public class GraphExpressionReverserLogicOld2 {
    private final BitOpsGraphCalculator calculator;
    private NamedBit output;

    private BitOpsGraphCalculator innerCalculator = new GraphBitOpsCalculator();

    public GraphExpressionReverserLogicOld2(BitOpsGraphCalculator calculator) {
        this.calculator = calculator;
        innerCalculator.getMiddle().setLabelPrefix("I");

//        innerCalculator = new BinderOptimizationDecorator(innerCalculator);
//        innerCalculator = new TwoOpsOptimizationDecorator(innerCalculator);
//        calculator = new XorAndCalculatorDecorator(calculator, true);
        innerCalculator = new SimpleOptimizationDecorator(innerCalculator);
//        innerCalculator = new ConstantOperationRemoverOptimizationDecorator(innerCalculator);
        innerCalculator = new UnusedBitOptimizerDecorator(innerCalculator);
    }

    public void analyse() {
        this.output = calculator.getOutput().getBits().get(0);

        Layer<VarBit> inputs = this.calculator.getInput();

        NamedBit res = reverse(inputs.getBits().get(0), true);
        System.out.println(formulaToString(res));
        // ((O[3]) | (!((!(O[3])) & (!((!((!(O[2])) | (M[7]) | (M[11]))) & (!(M[13])) & (!((!(O[1])) | (M[15]) | (M[17]))) & (!(O[0])))) & (!(((!(!((!(O[2])) | (M[7]) | (M[11])))) | ((M[10]) & (M[14]) & (M[16]))) & ((!(!((!(O[1])) | (M[15]) | (M[17])))) | ((M[10]) & (M[14]) & (M[16])))))))) & ((((M[7]) | (M[11]) | (!(!((!(O[2])) | (M[7]) | (M[11]))))) & ((M[13]) | (!((!((!(O[2])) | (M[7]) | (M[11]))) & (!(M[13])) & (!((!(O[1])) | (M[15]) | (M[17]))) & (!(O[0]))))) & ((M[15]) | (M[17]) | (!((!((!(O[2])) | (M[7]) | (M[11]))) & (!(M[13])) & (!((!(O[1])) | (M[15]) | (M[17]))) & (!(O[0]))))) & ((O[0]) | (!((!((!(O[1])) | (M[15]) | (M[17]))) & (!(O[0])))))) | (!((!((!((!(O[2])) | (M[7]) | (M[11]))) & (!(M[13])) & (!((!(O[1])) | (M[15]) | (M[17]))) & (!(O[0])))) & (!((!((!(O[1])) | (M[15]) | (M[17]))) & (!(O[0])))) & (!(((!(!((!(O[2])) | (M[7]) | (M[11])))) | ((M[6]) & (M[14]))) & ((!(!((!(O[1])) | (M[15]) | (M[17])))) | ((M[10]) & (M[14]))) & ((!(!((!(O[1])) | (M[15]) | (M[17])))) | ((M[10]) & (M[14]) & (M[16])))))))) & (!(((!(!((!(O[2])) | (M[7]) | (M[11])))) | ((M[6]) & (M[14]) & (M[16]))) & ((!(!((!(O[1])) | (M[15]) | (M[17])))) | ((M[10]) & (M[14])))))
    }

    Set<String> visitedNodes = new HashSet<>();

    private NamedBit reverse(NamedBit varBit, boolean findTruth) {
        // find dependencies
        System.out.println(varBit.getName() + " Usages: ___________________");

        List<OperationalBit> usage = new ArrayList<>();
        for (OperationalBit operationalBit : calculator.getMiddle()) {
            if (operationalBit.hasBit(varBit)) {
                if (Arrays.stream(operationalBit.getBits()).noneMatch(item -> visitedNodes.contains(item.getName()))) {
                    usage.add(operationalBit);
                    System.out.println(operationalBit.toString());
                }
            }
        }
        System.out.println(varBit.getName() + " END Usages: _____________________");


        visitedNodes.add(varBit.getName());

        NamedBit res = null;

        for (OperationalBit operationalBit : usage) {
            NamedBit[] bits = operationalBit.getBits();
            NamedBit res0;
            if (operationalBit.getOperation() == Operation.AND) {
                NamedBit otherBit = bits[0] == varBit ? bits[1] : bits[0];

                if (findTruth) {
                    res0 = (NamedBit) innerCalculator.or(reverse(operationalBit, true), innerCalculator.not(reverse(otherBit, false)));
                } else {
                    res0 = (NamedBit) innerCalculator.not(reverse(operationalBit, false));
                }
            } else if (operationalBit.getOperation() == Operation.OR) {
                NamedBit otherBit = bits[0] == varBit ? bits[1] : bits[0];
                if (findTruth) {
                    res0 = reverse(operationalBit, true);
                } else {
                    res0 = (NamedBit) innerCalculator.or(innerCalculator.not(reverse(operationalBit, false)), reverse(otherBit, true));
                }
            } else {
                res0 = (NamedBit) innerCalculator.not(reverse(operationalBit, false));
            }
            if (res == null) {
                res = res0;
            } else {
                res = (NamedBit) innerCalculator.and(res, res0);
            }
        }
        if (res == null) {
            if (varBit.getName().contains("M")) {
                res = (NamedBit) (findTruth ? ONE : ZERO);
            } else {
                res = varBit;
            }
        }
        System.out.println("###################\n" + varBit.getName() + " " + findTruth + " " + formulaToString(res));
        System.out.println("xxxxxxxxxxxxxxxxxxx");
        return res;
    }

    private Set<String> addToContext(Set<String> context, String newValue) {
        Set<String> newContext = new HashSet<>(context);

        newContext.add(newValue);

        return newContext;
    }

    public void show() {
        innerCalculator.setInputBits(calculator.getOutput().getBits().toArray(new OperationalBit[0]));

//        innerCalculator.show();
    }

    public static String formulaToString(Bit bit) {
        if (bit instanceof OperationalBit) {
            if (((OperationalBit) bit).testBits(item -> !(item instanceof OperationalBit))) {
                return ((OperationalBit) bit).getName();
            }
            return ((OperationalBit) bit).showFull(false, (namedBit -> !namedBit.getName().startsWith("M") && !namedBit.getName().startsWith("O")));
        } else {
            return bit.toString();
        }
    }
}
