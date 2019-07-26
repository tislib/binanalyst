package net.tislib.binanalyst.lib.analyse;

import static net.tislib.binanalyst.lib.BinValueHelper.setVal;
import static net.tislib.binanalyst.lib.bit.ConstantBit.ONE;
import static net.tislib.binanalyst.lib.bit.ConstantBit.ZERO;
import static net.tislib.binanalyst.lib.calc.graph.tools.NormalFormulaGenerator.toNormalFormul;

import java.util.ArrayList;
import java.util.Collections;
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
import net.tislib.binanalyst.lib.calc.graph.decorator.ConstantOperationRemoverOptimizationDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.SimpleOptimizationDecorator;
import net.tislib.binanalyst.lib.calc.graph.decorator.UnusedBitOptimizerDecorator;
import net.tislib.binanalyst.lib.calc.graph.expression.BooleanExpression;
import net.tislib.binanalyst.lib.calc.reverse.SingleBitReverser;

public class GraphExpressionReverserLogic {
    private static final VarBit UNKNOWN_BIT = new VarBit();
    private static final String UNKNOWN_BIT_NAME = "UNKNOWN_BIT";
    private final BitOpsGraphCalculator calculator;

    private BitOpsGraphCalculator innerCalculator = new GraphBitOpsCalculator();

    private Map<String, NamedBit> truthExps = new HashMap<>();
    private Map<String, NamedBit> falsyExps = new HashMap<>();

    private Map<String, NamedBit> truthExpsResolved = new HashMap<>();
    private Map<String, NamedBit> falsyExpsResolved = new HashMap<>();

    private Set<String> reversedBits = new HashSet<>();
    private Map<String, NamedBit> inputMap = new HashMap<>();

    public GraphExpressionReverserLogic(BitOpsGraphCalculator calculator) {
        this.calculator = calculator;
        innerCalculator.getMiddle().setLabelPrefix("I");

        innerCalculator = new SimpleOptimizationDecorator(innerCalculator);
        innerCalculator = new ConstantOperationRemoverOptimizationDecorator(innerCalculator);
        innerCalculator = new UnusedBitOptimizerDecorator(innerCalculator);

//        UNKNOWN_BIT.setValue(BinaryValue.FALSE);
//        UNKNOWN_BIT.setName(UNKNOWN_BIT_NAME);
//        innerCalculator.getInput().addBits(UNKNOWN_BIT);
    }

    public static SingleBitReverser reverser() {
        return (calculator, bitName, truth) -> {
            GraphExpressionReverserLogic graphExpressionReverserLogic = new GraphExpressionReverserLogic(calculator);
            graphExpressionReverserLogic.analyse();

            Bit reversed = graphExpressionReverserLogic.getReversed(bitName, truth);

            BitOpsGraphCalculator innerCalculator = graphExpressionReverserLogic.getInnerCalculator();
            VarBit[] input = graphExpressionReverserLogic.getInnerCalculator().getInput().getBits().toArray(new VarBit[0]);

            return (BooleanExpression) value -> {
                setVal(input, value);
                innerCalculator.calculate();
                GraphExpressionReverserLogic graphExpressionReverserLogic2 = graphExpressionReverserLogic;

                try {
                    return reversed.getValue().isTrue();
                } catch (Exception e) {
                    graphExpressionReverserLogic.getInnerCalculator().show();
                    throw e;
                }
            };
        };
    }

    public void analyse() {
        rasterizeInnerCalculator();

        Layer<VarBit> inputs = this.calculator.getInput();

        for (VarBit varBit : inputs) {
            reverse(varBit);
        }

        NamedBit[] output = new NamedBit[inputs.size() * 2];
        int i = 0;

        Bit posibility = ONE;

        for (NamedBit namedBit : calculator.getInput()) {
            Bit truth = resolve(truthExps.get(namedBit.getName()), new HashSet<>(Collections.singleton(namedBit.getName())));
            Bit falsy = resolve(falsyExps.get(namedBit.getName()), new HashSet<>(Collections.singleton("!" + namedBit.getName())));

            posibility = innerCalculator.and(posibility, innerCalculator.or(truth, falsy));
        }

        for (NamedBit namedBit : calculator.getInput()) {
            Bit truth = innerCalculator.and(posibility, resolve(truthExps.get(namedBit.getName()), new HashSet<>(Collections.singleton(namedBit.getName()))));
            Bit falsy = innerCalculator.and(posibility, resolve(falsyExps.get(namedBit.getName()), new HashSet<>(Collections.singleton("!" + namedBit.getName()))));

            truthExpsResolved.put(namedBit.getName(), (NamedBit) truth);
            falsyExpsResolved.put(namedBit.getName(), (NamedBit) falsy);

            output[i++] = (NamedBit) truth;
            output[i++] = (NamedBit) falsy;
        }

        innerCalculator.setInputBits(inputMap.values().stream().map(item -> (VarBit) item).toArray(VarBit[]::new));
        innerCalculator.setOutputBits(output);

        innerCalculator.optimize();
    }

    private Bit getReversed(String bitName, boolean truth) {
        return truth ? truthExpsResolved.get(bitName) : falsyExpsResolved.get(bitName);
    }


    private Bit resolve(Bit bit, Set<String> context) {
        if (bit instanceof OperationalBit && ((NamedBit) bit).getName().startsWith("I")) {
            OperationalBit operationalBit = (OperationalBit) bit;
            NamedBit[] bits = operationalBit.getBits();
            NamedBit[] newBits = new NamedBit[bits.length];

            if (operationalBit.getOperation() == Operation.NOT && (
                    !bits[0].getName().startsWith("I") &&
                            !bits[0].getName().startsWith("O") &&
                            !bits[0].getName().startsWith("M")
            )) {
                if (context.contains(bits[0].getName())) {
                    return ZERO;
                } else if (context.contains("!" + bits[0].getName())) {
                    return ONE;
                }
                NamedBit falsyExp = falsyExps.get(bits[0].getName());
                HashSet<String> newContext = new HashSet<>(context);
                newContext.add("!" + bits[0].getName());
                return resolve(falsyExp, newContext);
            }

            for (int i = 0; i < newBits.length; i++) {
                newBits[i] = (NamedBit) resolve(bits[i], context);
            }

            return innerCalculator.operation(operationalBit.getOperation(), newBits);
        } else {
            if (bit instanceof NamedBit) {
                HashSet<String> newContext = new HashSet<>(context);
                NamedBit varBit = (NamedBit) bit;
                if (varBit.getName().startsWith("M") || varBit.getName().startsWith("O")) {
                    if (!inputMap.containsKey(varBit.getName())) {
                        return varBit;
                    }
                    return inputMap.get(varBit.getName());
                } else {
                    if (context.contains(varBit.getName())) {
                        return ONE;
                    } else if (context.contains("!" + varBit.getName())) {
                        return ZERO;
                    }
                }
                newContext.add(varBit.getName());
                return resolve(truthExps.get(varBit.getName()), newContext);
            }
        }

        return bit;
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
            } else if (operationalBit.getOperation() == Operation.XOR) {
                NamedBit otherBit = bits[0] == varBit ? bits[1] : bits[0];
                Bit theTruth = innerCalculator.or(
                        innerCalculator.and(operationalBit, innerCalculator.not(otherBit)),
                        innerCalculator.and(otherBit, innerCalculator.not(operationalBit))
                );
                Bit theFalsy = innerCalculator.or(
                        innerCalculator.and(innerCalculator.not(operationalBit), innerCalculator.not(otherBit)),
                        innerCalculator.and(otherBit, operationalBit)
                );
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
        showState();
    }

    public BitOpsGraphCalculator getInnerCalculator() {
        return innerCalculator;
    }

    private void rasterizeInnerCalculator() {
        for (NamedBit namedBit : calculator.getOutput()) {
            VarBit varBit = new VarBit();
            varBit.setName("R" + namedBit.getName());
            inputMap.put(namedBit.getName(), varBit);
        }
    }

    public void showState() {
        for (NamedBit namedBit : calculator.getInput()) {
            Bit truth = truthExpsResolved.get(namedBit.getName());
            System.out.println(namedBit.getName() + ": " + formulaToString(truth));
        }
        for (NamedBit namedBit : calculator.getInput()) {
            Bit truth = falsyExpsResolved.get(namedBit.getName());
            System.out.println("!" + namedBit.getName() + ": " + formulaToString(truth));
        }
    }

    public void showNormalState() {
        for (NamedBit bit : innerCalculator.getOutput()) {
            System.out.println(bit.getName() + ": " + normalFormulaToString(bit));
        }
    }

    public static String formulaToString(Bit bit) {
        if (bit instanceof OperationalBit) {
            return ((OperationalBit) bit)
                    .showFull(false);
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
