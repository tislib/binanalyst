package net.tislib.binanalyst.lib.analyse;

import static net.tislib.binanalyst.lib.BinValueHelper.setVal;
import static net.tislib.binanalyst.lib.bit.ConstantBit.ONE;
import static net.tislib.binanalyst.lib.bit.ConstantBit.ZERO;
import static net.tislib.binanalyst.lib.calc.graph.tools.NormalFormulaGenerator.toNormalFormul;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.tislib.binanalyst.lib.analyse.reverseCalc.ReverseCalculator;
import net.tislib.binanalyst.lib.analyse.reverseCalc.ReverseOperationalBit;
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

@SuppressWarnings("Duplicates")
public class GraphExpressionReverserLogicMulti2 {
    private static final VarBit UNKNOWN_BIT = new VarBit();
    private static final String UNKNOWN_BIT_NAME = "UNKNOWN_BIT";
    private final BitOpsGraphCalculator calculator;

    private BitOpsGraphCalculator innerCalculator = new GraphBitOpsCalculator();

    private final ReverseCalculator reverseCalculator;

    Map<String, ReverseOperationalBit> reversedBitMap = new HashMap<>();

    private Map<String, NamedBit> truthExpsResolved = new HashMap<>();
    private Map<String, NamedBit> falsyExpsResolved = new HashMap<>();

    private Set<String> reversedBits = new HashSet<>();
    private Map<String, NamedBit> inputMap = new HashMap<>();

    public GraphExpressionReverserLogicMulti2(BitOpsGraphCalculator calculator) {
        this.calculator = calculator;
        innerCalculator.getMiddle().setLabelPrefix("I");

        innerCalculator = new SimpleOptimizationDecorator(innerCalculator);
        innerCalculator = new ConstantOperationRemoverOptimizationDecorator(innerCalculator);
        innerCalculator = new UnusedBitOptimizerDecorator(innerCalculator);

        reverseCalculator = new ReverseCalculator(innerCalculator);

//        UNKNOWN_BIT.setValue(BinaryValue.FALSE);
//        UNKNOWN_BIT.setName(UNKNOWN_BIT_NAME);
//        innerCalculator.getInput().addBits(UNKNOWN_BIT);
    }

    public static SingleBitReverser reverser() {
        return (calculator, bitName, truth) -> {
            GraphExpressionReverserLogicMulti2 graphExpressionReverserLogic = new GraphExpressionReverserLogicMulti2(calculator);
            graphExpressionReverserLogic.analyse();

            Bit reversed = graphExpressionReverserLogic.getReversed(bitName, truth);

            BitOpsGraphCalculator innerCalculator = graphExpressionReverserLogic.getInnerCalculator();
            VarBit[] input = graphExpressionReverserLogic.getInnerCalculator().getInput().getBits().toArray(new VarBit[0]);

            return (BooleanExpression) value -> {
                setVal(input, value);
                innerCalculator.calculate();
                GraphExpressionReverserLogicMulti2 graphExpressionReverserLogic2 = graphExpressionReverserLogic;

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

        for (VarBit varBit : calculator.getMiddle()) {
            reverse(varBit);
        }

        NamedBit[] output = new NamedBit[inputs.size() * 2];
        int i = 0;

        Bit posibility = ONE;

        for (NamedBit namedBit : calculator.getInput()) {
//            Bit truth = resolve(truthExps.get(namedBit.getName()), new HashSet<>(Collections.singleton(namedBit.getName())));
//            Bit falsy = resolve(falsyExps.get(namedBit.getName()), new HashSet<>(Collections.singleton("!" + namedBit.getName())));

//            posibility = innerCalculator.and(posibility, innerCalculator.or(truth, falsy));
        }

        for (NamedBit namedBit : calculator.getMiddle()) {
            if (!namedBit.getName().startsWith("M")) {
                continue;
            }
//            Bit truth = resolve(truthExps.get(namedBit.getName()), new HashSet<>(Collections.singleton(namedBit.getName())));
//            Bit falsy = resolve(falsyExps.get(namedBit.getName()), new HashSet<>(Collections.singleton("!" + namedBit.getName())));

//            posibility = innerCalculator.and(posibility, innerCalculator.or(truth, falsy));
        }

        for (NamedBit namedBit : calculator.getInput()) {
//            Bit truth = innerCalculator.and(posibility, resolve(truthExps.get(namedBit.getName()), new HashSet<>(Collections.singleton(namedBit.getName()))));
//            Bit falsy = innerCalculator.and(posibility, resolve(falsyExps.get(namedBit.getName()), new HashSet<>(Collections.singleton("!" + namedBit.getName()))));

//            truthExpsResolved.put(namedBit.getName(), (NamedBit) truth);
//            falsyExpsResolved.put(namedBit.getName(), (NamedBit) falsy);

//            output[i++] = (NamedBit) truth;
//            output[i++] = (NamedBit) falsy;
        }

//        innerCalculator.setInputBits(inputMap.values().stream().map(item -> (VarBit) item).toArray(VarBit[]::new));
//        innerCalculator.setOutputBits(output);

        reverseCalculator.resolve();

        innerCalculator.optimize();
    }

    private Bit getReversed(String bitName, boolean truth) {
        return truth ? truthExpsResolved.get(bitName) : falsyExpsResolved.get(bitName);
    }

    private void reverse(NamedBit varBit) {
        // find dependencies
        List<OperationalBit> usage = new ArrayList<>();
        for (OperationalBit operationalBit : calculator.getMiddle()) {
            if (operationalBit.hasBit(varBit)) {
                usage.add(operationalBit);
            }
        }

        ReverseOperationalBit reverseOperationalBit = null;

        for (OperationalBit operationalBit : usage) {
            NamedBit[] bits = operationalBit.getBits();

            if (operationalBit.getOperation() == Operation.AND) {
                NamedBit otherBit = bits[0] == varBit ? bits[1] : bits[0];

                reverseOperationalBit = reverseCalculator.and(reverseOperationalBit,
                        reverseCalculator.reverseAnd(operationalBit, otherBit)
                );
            } else if (operationalBit.getOperation() == Operation.OR) {
                NamedBit otherBit = bits[0] == varBit ? bits[1] : bits[0];

                reverseOperationalBit = reverseCalculator.and(reverseOperationalBit,
                        reverseCalculator.reverseOr(operationalBit, otherBit)
                );
            } else if (operationalBit.getOperation() == Operation.XOR) {
                if (bits.length == 1) {
                    reverseOperationalBit = reverseCalculator.and(reverseOperationalBit, reverseCalculator.wrap(operationalBit));
                } else {
                    NamedBit otherBit = bits[0] == varBit ? bits[1] : bits[0];
                    reverseOperationalBit = reverseCalculator.and(reverseOperationalBit,
                            reverseCalculator.xor(reverseCalculator.wrap(operationalBit), reverseCalculator.wrap(otherBit))
                    );
                }
            } else {
                reverseOperationalBit = reverseCalculator.and(reverseOperationalBit, reverseCalculator.not(reverseCalculator.wrap(operationalBit)));
            }
        }

        if (reverseOperationalBit != null) {
            reversedBitMap.put(varBit.getName(), reverseOperationalBit);
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
