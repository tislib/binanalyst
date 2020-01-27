package net.tislib.binanalyst.lib.analyse;

import static net.tislib.binanalyst.lib.calc.graph.tools.NormalFormulaGenerator.toNormalFormul;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.tislib.binanalyst.lib.bit.BinaryValue;
import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.NamedBit;
import net.tislib.binanalyst.lib.bit.OperationalBit;
import net.tislib.binanalyst.lib.bit.VarBit;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.GraphBitOpsCalculator;
import net.tislib.binanalyst.lib.calc.graph.Layer;
import net.tislib.binanalyst.lib.calc.graph.Operation;
import net.tislib.binanalyst.lib.calc.graph.UsageFinder;
import net.tislib.binanalyst.lib.calc.graph.decorator.ConstantOperationRemoverOptimizationDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.optimizer.SimpleOptimizationDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.optimizer.UnusedBitOptimizerDecorator;

public class GraphExpressionReverserLogicOld3 {
    private static final VarBit UNKNOWN_BIT = new VarBit();
    private static final String UNKNOWN_BIT_NAME = "UNKNOWN_BIT";
    private final BitOpsGraphCalculator calculator;

    private BitOpsGraphCalculator innerCalculator = new GraphBitOpsCalculator();

    private Map<String, NamedBit> truthExps = new HashMap<>();
    private Map<String, NamedBit> falsyExps = new HashMap<>();
    private Set<String> reversedBits = new HashSet<>();

    public GraphExpressionReverserLogicOld3(BitOpsGraphCalculator calculator) {
        this.calculator = calculator;
        innerCalculator.getMiddle().setLabelPrefix("I");

//        innerCalculator = new BinderOptimizationDecorator(innerCalculator);
//        calculator = new XorAndCalculatorDecorator(calculator, true);
        innerCalculator = new SimpleOptimizationDecorator(innerCalculator);
        innerCalculator = new ConstantOperationRemoverOptimizationDecorator(innerCalculator);
        innerCalculator = new UnusedBitOptimizerDecorator(innerCalculator);

        UNKNOWN_BIT.setValue(BinaryValue.FALSE);
        UNKNOWN_BIT.setName(UNKNOWN_BIT_NAME);
        innerCalculator.getInput().addBits(UNKNOWN_BIT);
    }

    public void analyse() {
        Layer<VarBit> inputs = this.calculator.getInput();

        for (VarBit varBit : inputs) {
            reverse(varBit);
        }

        for (VarBit varBit : calculator.getMiddle()) {
            reverse(varBit);
        }

//        for (int i = 0; i < 30; i++) {
//            for (VarBit varBit : calculator.getMiddle()) {
//                reverse(varBit);
//            }
//
//            for (VarBit varBit : inputs) {
//                reverse(varBit);
//            }
//        }
//
//        for (VarBit varBit : calculator.getMiddle()) {
//            reverse(varBit);
//        }
//
//        for (VarBit varBit : inputs) {
//            reverse(varBit);
//        }

        List<OperationalBit> bits = calculator.getMiddle().getBits();
        for (int i = 0, bitsSize = bits.size(); i < bitsSize; i++) {
            VarBit varBit = bits.get(i);
            if (falsyExps.get(varBit.getName()) != null) {
                innerCalculator.replaceBit((NamedBit) calculator.not(varBit), falsyExps.get(varBit.getName()));
            }
            if (truthExps.get(varBit.getName()) != null) {
                innerCalculator.replaceBit(varBit, truthExps.get(varBit.getName()));
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

        UsageFinder usageFinder = new UsageFinder(innerCalculator.getInput(), innerCalculator.getMiddle(), innerCalculator.getOutput());
        usageFinder.cleanUnusedMiddleBits();

        for (OperationalBit operationalBit : innerCalculator.getMiddle()) {
            for (NamedBit namedBit : operationalBit.getBits()) {
                if (namedBit.getName().equals(UNKNOWN_BIT_NAME)) {
                    throw new RuntimeException("unresolved bit found: " + namedBit.getName());
                }
            }
        }
    }

    private void reverse(NamedBit varBit) {
        if (varBit instanceof OperationalBit) {
            OperationalBit operationalBit = (OperationalBit) varBit;
            if (operationalBit.getOperation() == Operation.NOT) {
                NamedBit innerBit = operationalBit.getBits()[0];

                truthExps.put(varBit.getName(), (NamedBit) locateReverse(innerBit, false));
                falsyExps.put(varBit.getName(), (NamedBit) locateReverse(innerBit, true));
                reversedBits.add(varBit.getName());

                return;
            }
        }
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
                Bit theTruth = innerCalculator.or(locateReverse(operationalBit, true), locateReverse(otherBit, false));
                Bit theFalsy = locateReverse(operationalBit, false);
                if (truth == null) {
                    truth = (NamedBit) theTruth;
                    falsy = (NamedBit) theFalsy;
                }
                truth = (NamedBit) innerCalculator.and(truth, theTruth);
                falsy = (NamedBit) innerCalculator.and(falsy, theFalsy);
            } else if (operationalBit.getOperation() == Operation.OR) {
                NamedBit otherBit = bits[0] == varBit ? bits[1] : bits[0];
                Bit theTruth = locateReverse(operationalBit, true);
                Bit theFalsy = innerCalculator.or(locateReverse(operationalBit, false), locateReverse(otherBit, true));
                if (truth == null) {
                    truth = (NamedBit) theTruth;
                    falsy = (NamedBit) theFalsy;
                }
                truth = (NamedBit) innerCalculator.and(truth, theTruth);
                falsy = (NamedBit) innerCalculator.and(falsy, theFalsy);
            } else if (operationalBit.getOperation() == Operation.XOR) {
                NamedBit otherBit = bits[0] == varBit ? bits[1] : bits[0];
                Bit theTruth = innerCalculator.or(innerCalculator.and(locateReverse(operationalBit, false), locateReverse(otherBit, false)), innerCalculator.and(locateReverse(operationalBit, true), locateReverse(otherBit, false)));
                Bit theFalsy = innerCalculator.or(innerCalculator.and(locateReverse(operationalBit, false), locateReverse(otherBit, false)), innerCalculator.and(locateReverse(operationalBit, true), locateReverse(otherBit, true)));
                if (truth == null) {
                    truth = (NamedBit) theTruth;
                    falsy = (NamedBit) theFalsy;
                }
                truth = (NamedBit) innerCalculator.and(truth, theTruth);
                falsy = (NamedBit) innerCalculator.and(falsy, theFalsy);
            } else {
                Bit theTruth = locateReverse(operationalBit, false);
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
        if (varBit.getName().startsWith("O")) {
            return findTruth ? varBit : innerCalculator.not(varBit);
        }
        if (reversedBits.contains(varBit.getName())) {
            return findTruth ? truthExps.get(varBit.getName()) : falsyExps.get(varBit.getName());
        }
        return UNKNOWN_BIT;
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
