package net.tislib.binanalyst.lib.analyse;

import static net.tislib.binanalyst.lib.bit.ConstantBit.ONE;
import static net.tislib.binanalyst.lib.bit.ConstantBit.ZERO;

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
import net.tislib.binanalyst.lib.calc.graph.decorator.optimizer.SimpleOptimizationDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.optimizer.TwoOpsOptimizationDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.optimizer.UnusedBitOptimizerDecorator;

public class GraphExpressionReverserLogicOld {
    private final BitOpsGraphCalculator calculator;
    private NamedBit output;

    private BitOpsGraphCalculator innerCalculator = new GraphBitOpsCalculator();

    private Map<String, NamedBit> truthExps = new HashMap<>();
    private Map<String, NamedBit> falsyExps = new HashMap<>();
    private Set<String> reversedBits = new HashSet<>();
    private Set<String> fullReversedBits = new HashSet<>();

    public GraphExpressionReverserLogicOld(BitOpsGraphCalculator calculator) {
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
        this.output = calculator.getOutput().getBits().get(0);

        Layer<VarBit> inputs = this.calculator.getInput();

        for (VarBit varBit : inputs) {
            reverse(varBit);
        }

        for (VarBit varBit : calculator.getMiddle()) {
            reverse(varBit);
        }
    }

    private void reverse(NamedBit varBit) {
        if (reversedBits.contains(varBit.getName())) {
            return;
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
                Bit theTruth = innerCalculator.or(operationalBit, innerCalculator.not(otherBit));
                Bit theFalsy = innerCalculator.not(operationalBit);
                if (truth == null) {
                    truth = (NamedBit) theTruth;
                    falsy = (NamedBit) theFalsy;
                }
                truth = (NamedBit) innerCalculator.and(truth, theTruth);
                falsy = (NamedBit) innerCalculator.and(falsy, theFalsy);
            } else if (operationalBit.getOperation() == Operation.OR) {
                NamedBit otherBit = bits[0] == varBit ? bits[1] : bits[0];
                Bit theTruth = operationalBit;
                Bit theFalsy = innerCalculator.or(innerCalculator.not(operationalBit), otherBit);
                if (truth == null) {
                    truth = (NamedBit) theTruth;
                    falsy = (NamedBit) theFalsy;
                }
                truth = (NamedBit) innerCalculator.and(truth, theTruth);
                falsy = (NamedBit) innerCalculator.and(falsy, theFalsy);
            } else {
                Bit theTruth = innerCalculator.not(operationalBit);
                Bit theFalsy = operationalBit;
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

    public void show() {
        for (String bitName : reversedBits) {
            System.out.println(bitName);
            System.out.println(formulaToString(truthExps.get(bitName)));
            System.out.println(formulaToString(falsyExps.get(bitName)));
        }

        NamedBit a0Truth = truthExps.get("a1");
        NamedBit a0Falsy = falsyExps.get("a1");

        Set<String> context = new HashSet<>();
        context.add("a1");

        NamedBit a0TruthResolved = operateTruth(a0Truth, context);

        System.out.println("______________");

        System.out.println(formulaToString(a0TruthResolved));
    }

    private NamedBit operateTruth(NamedBit namedBit, Set<String> context) {
        if (namedBit instanceof OperationalBit) {
            NamedBit[] bits = ((OperationalBit) namedBit).getBits();
            NamedBit[] newBits = new NamedBit[bits.length];
            Operation operation = ((OperationalBit) namedBit).getOperation();

            if (operation == Operation.NOT) {
                if (bits[0].getName().startsWith("M") || bits[0].getName().startsWith("O")) {
                    if (!falsyExps.containsKey(bits[0].getName())) {
                        return bits[0];
                    }
                    return operateFalsified(bits[0], context);
                }
            }

            for (int i = 0; i < bits.length; i++) {
                NamedBit theBit = bits[i];
                if (theBit.getName().startsWith("I")) {
                    newBits[i] = operateTruth(theBit, context);
                } else if (theBit.getName().startsWith("M") || theBit.getName().startsWith("O")) {
                    if (!truthExps.containsKey(theBit.getName())) {
                        return theBit;
                    }
                    return operateTruthed(theBit, context);
                } else {
                    throw new RuntimeException();
                }
            }
            return (NamedBit) innerCalculator.operation(operation, newBits);

        } else {
            throw new RuntimeException();
        }
    }

    private NamedBit operateX(NamedBit namedBit, Set<String> context) {
        System.out.println(context);
        if (namedBit instanceof OperationalBit) {
            NamedBit[] bits = ((OperationalBit) namedBit).getBits();
            NamedBit[] newBits = new NamedBit[bits.length];
            Operation operation = ((OperationalBit) namedBit).getOperation();

            for (int i = 0; i < bits.length; i++) {
                NamedBit theBit = bits[i];
                if (context.contains(theBit.getName())) {
                    newBits[i] = (NamedBit) ONE;
                } else if (context.contains("!" + theBit.getName())) {
                    newBits[i] = (NamedBit) ZERO;
                } else {
                    if (theBit.getName().startsWith("I")) {
                        newBits[i] = operateTruth(theBit, context);
                    } else {
                        newBits[i] = operateTruthed(theBit, context);
                    }
                }
            }

            return (NamedBit) innerCalculator.operation(operation, newBits);
        }
        if (context.contains(namedBit.getName())) {
            return (NamedBit) ONE;
        } else if (context.contains("!" + namedBit.getName())) {
            return (NamedBit) ZERO;
        }
        return namedBit;
    }

    private NamedBit operateTruthed(NamedBit namedBit, Set<String> context) {
        if (!truthExps.containsKey(namedBit.getName())) {
            return namedBit;
        }

        HashSet<String> newContext = new HashSet<>(context);
        newContext.add(namedBit.getName());

        return operateX(truthExps.get(namedBit.getName()), newContext);
    }

    private NamedBit operateFalsified(NamedBit namedBit, Set<String> context) {
        if (!falsyExps.containsKey(namedBit.getName())) {
            return namedBit;
        }

        HashSet<String> newContext = new HashSet<>(context);
        newContext.add("!" + namedBit.getName());

        return operateX(falsyExps.get(namedBit.getName()), newContext);
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
