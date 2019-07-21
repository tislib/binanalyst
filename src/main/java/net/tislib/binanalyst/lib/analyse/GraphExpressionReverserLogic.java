package net.tislib.binanalyst.lib.analyse;

import static net.tislib.binanalyst.lib.calc.graph.tools.NormalFormulaGenerator.toNormalFormul;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.NamedBit;
import net.tislib.binanalyst.lib.bit.OperationalBit;
import net.tislib.binanalyst.lib.bit.VarBit;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.GraphBitOpsCalculator;
import net.tislib.binanalyst.lib.calc.graph.Layer;
import net.tislib.binanalyst.lib.calc.graph.Operation;
import net.tislib.binanalyst.lib.calc.graph.decorator.BinderOptimizationDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.ConstantOperationRemoverOptimizationDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.SimpleOptimizationDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.TwoOpsOptimizationDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.UnusedBitOptimizerDecorator;

public class GraphExpressionReverserLogic {
    private final BitOpsGraphCalculator calculator;

    private BitOpsGraphCalculator innerCalculator = new GraphBitOpsCalculator();

    private Map<String, NamedBit> truthExps = new HashMap<>();
    private Map<String, NamedBit> falsyExps = new HashMap<>();
    private Set<String> reversedBits = new HashSet<>();

    public GraphExpressionReverserLogic(BitOpsGraphCalculator calculator) {
        this.calculator = calculator;
        innerCalculator.getMiddle().setLabelPrefix("I");

        innerCalculator = new BinderOptimizationDecorator(innerCalculator);
        innerCalculator = new TwoOpsOptimizationDecorator(innerCalculator);
//        calculator = new XorAndCalculatorDecorator(calculator, true);
        innerCalculator = new SimpleOptimizationDecorator(innerCalculator);
        innerCalculator = new ConstantOperationRemoverOptimizationDecorator(innerCalculator);
        innerCalculator = new UnusedBitOptimizerDecorator(innerCalculator);
    }

    public void analyse() {
        Layer<VarBit> inputs = this.calculator.getInput();

        for (VarBit varBit : inputs) {
            reverse(varBit);
        }

        for (VarBit varBit : inputs) {
            reverse(varBit);
        }

        for (int i = 0; i < 20; i++) {
            for (VarBit varBit : calculator.getMiddle()) {
                reverse(varBit);
            }

            for (VarBit varBit : calculator.getMiddle()) {
                reverse(varBit);
            }

            for (VarBit varBit : inputs) {
                reverse(varBit);
            }
        }


        for (VarBit varBit : calculator.getInput()) {
            if (truthExps.containsKey(varBit.getName())) {
                NamedBit truthBit = truthExps.get(varBit.getName());
                NamedBit falsyFit = falsyExps.get(varBit.getName());

                truthBit.setName(varBit.getName() + "_truth");
                falsyFit.setName(varBit.getName() + "_falsy");

                innerCalculator.getOutput().addBits(truthBit);
                innerCalculator.getOutput().addBits(falsyFit);
            }
        }

        rasterizeInnerCalculator();
    }

    private void reverse(NamedBit varBit) {
        // find dependencies
        List<OperationalBit> usage = new ArrayList<>();
        for (OperationalBit operationalBit : calculator.getMiddle()) {
            if (operationalBit.hasBit(varBit)) {
                usage.add(operationalBit);
            }
        }

        NamedBit truth = null;
        NamedBit falsy = null;

        for (OperationalBit operationalBit : usage) {
            NamedBit[] bits = operationalBit.getBits();
            if (operationalBit.getOperation() == Operation.AND) {
                NamedBit otherBit = bits[0] == varBit ? bits[1] : bits[0];
                Bit theTruth = innerCalculator.or(locateReverse(operationalBit, true), innerCalculator.not(locateReverse(otherBit, false)));
                Bit theFalsy = innerCalculator.not(locateReverse(operationalBit, false));
                if (truth == null) {
                    truth = (NamedBit) theTruth;
                    falsy = (NamedBit) theFalsy;
                }
                truth = (NamedBit) innerCalculator.and(truth, theTruth);
                falsy = (NamedBit) innerCalculator.and(falsy, theFalsy);
            } else if (operationalBit.getOperation() == Operation.OR) {
                NamedBit otherBit = bits[0] == varBit ? bits[1] : bits[0];
                Bit theTruth = locateReverse(operationalBit, true);
                Bit theFalsy = innerCalculator.or(innerCalculator.not(locateReverse(operationalBit, false)), locateReverse(otherBit, true));
                if (truth == null) {
                    truth = (NamedBit) theTruth;
                    falsy = (NamedBit) theFalsy;
                }
                truth = (NamedBit) innerCalculator.and(truth, theTruth);
                falsy = (NamedBit) innerCalculator.and(falsy, theFalsy);
            } else {
                Bit theTruth = innerCalculator.not(locateReverse(operationalBit, false));
                Bit theFalsy = locateReverse(operationalBit, true);
                if (truth == null) {
                    truth = (NamedBit) theTruth;
                    falsy = (NamedBit) theFalsy;
                }
                truth = (NamedBit) innerCalculator.and(truth, theTruth);
                falsy = (NamedBit) innerCalculator.and(falsy, theFalsy);
            }
        }

        if (truth != null) {
            truthExps.put(varBit.getName(), truth);
            falsyExps.put(varBit.getName(), falsy);
            reversedBits.add(varBit.getName());
        }
    }

    private Bit locateReverse(NamedBit varBit, boolean findTruth) {
        if (reversedBits.contains(varBit.getName())) {
            return findTruth ? truthExps.get(varBit.getName()) : falsyExps.get(varBit.getName());
        }
        return varBit;
    }

    public void show() {
        showState();
    }

    public BitOpsGraphCalculator getInnerCalculator() {
        return innerCalculator;
    }

    private void rasterizeInnerCalculator() {
        List<VarBit> varBits = new ArrayList<>();
        for (NamedBit namedBit : calculator.getOutput()) {
            VarBit varBit = new VarBit();
            varBit.setName(namedBit.getName());
            varBits.add(varBit);
            innerCalculator.replaceBit(namedBit, varBit);
            System.out.println("a");
        }
        innerCalculator.getInput().addBits(varBits);
    }

    public void showState() {
        for (String bitName : reversedBits) {
            System.out.println(bitName);
            System.out.println(formulaToString(truthExps.get(bitName)));
            System.out.println(formulaToString(falsyExps.get(bitName)));
        }
    }

    public void showNormalState() {
        for (NamedBit bit : innerCalculator.getOutput()) {
            System.out.println(bit.getName() + ": " + normalFormulaToString(bit));
        }
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

    public static String normalFormulaToString(Bit bit) {
        NamedBit res = toNormalFormul((OperationalBit) bit);
        if (res instanceof OperationalBit) {
            return ((OperationalBit) res).showFull(false);
        } else {
            return String.valueOf(res.getValue());
        }
    }
}
